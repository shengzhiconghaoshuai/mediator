/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：JingdongDataTranslator.java
 * 描述： 京东数据类型转换器
 */
package net.chinacloud.mediator.jingdong.init;


import net.chinacloud.mediator.domain.Address;
import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.Invoice;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.OrderItem;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.domain.User;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.jingdong.constants.JingDongConstants;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.NumberUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jd.open.api.sdk.domain.order.OrderInfo;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.CouponDetail;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.ItemInfo;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.OrderSearchInfo;
import com.jd.open.api.sdk.domain.refundapply.RefundApplySoaService.RefundApplyVo;

/**
 * 
 * <京东数据类型转换器>
 * <京东数据类型转换器>
 * @author jianjunnie
 * @version 0.0.0,2015年2月11日
 * @since 2015年2月11日
 */
public class JingdongDataTranslator implements DataTranslator{
	private static final Logger LOGGER = LoggerFactory.getLogger(JingdongDataTranslator.class);
	@Override
	public Order transformOrder(Object source) throws TranslateException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("transform " + source + " to Order");
		}
		Order order = null;
		
		if(source instanceof OrderInfo){
			order = translatorOrderInfo((OrderInfo)source);
		}else if(source instanceof OrderSearchInfo){
			order = translatorOrderSearchInfo((OrderSearchInfo)source);
		}
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("transform to Order result:" + order);
		}
		
		return order;
	}
	
	/**
	 * 将京东OrderSearchInfo型订单转换为meidiator的订单Order
	 * @param OrderSearchInfo 京东OrderSearchInfo订单
	 * @return 返回meidiator系统订单Order
	 */
	private Order translatorOrderSearchInfo(OrderSearchInfo orderSearchInfo){
		Order order = new Order();
		//订单号
		order.setChannelOrderId(orderSearchInfo.getOrderId());
		//directParentOrderId	直接父订单号
		order.addAdditionalParam("directParentOrderId", orderSearchInfo.getDirectParentOrderId());

		//交易创建时间(下单时间)
		order.setCreateTime(DateUtil.parse(orderSearchInfo.getOrderStartTime()));
		//支付时间 TODO COD支付时间可能为空
		if (!"0001-01-01 00:00:00".equals(orderSearchInfo.getPaymentConfirmTime()) && !StringUtils.isEmpty(orderSearchInfo.getPaymentConfirmTime())) {
			order.setPayTime(DateUtil.parse(orderSearchInfo.getPaymentConfirmTime()));
		} else {
			LOGGER.warn("JD order {} pay time is {}", orderSearchInfo.getOrderId(), orderSearchInfo.getPaymentConfirmTime());
		}
		//修改时间
		if(!StringUtils.isEmpty(orderSearchInfo.getModified())){
			order.setModified(DateUtil.parse(orderSearchInfo.getModified()));
		}else{
			order.setModified(null);
		}
		//交易成功时间
		if (!"0001-01-01 00:00:00".equals(orderSearchInfo.getOrderEndTime()) 
				&& !"1970-01-01 00:00:00".equals(orderSearchInfo.getOrderEndTime()) && !StringUtils.isEmpty(orderSearchInfo.getOrderEndTime())) {
			order.setEndTime(DateUtil.parse(orderSearchInfo.getOrderEndTime()));
		}
		
		//买家留言
		order.setBuyerMessage(orderSearchInfo.getOrderRemark());
		//卖家备注
		order.setSellerMemo(orderSearchInfo.getVenderRemark());
		
		//订单状态,这个需要匹配
		String jingdongStatus = orderSearchInfo.getOrderState();
		if("WAIT_SELLER_STOCK_OUT".equals(jingdongStatus)) {	//等待发货
			order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
			order.setStatusDesc(Order.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
		}else if("WAIT_GOODS_RECEIVE_CONFIRM".equals(jingdongStatus)) {	//等待买家收货
			order.setStatus(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS);
			order.setStatusDesc(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS_DESC);
		}else if("FINISHED_L".equals(jingdongStatus)) {	//交易成功
			order.setStatus(Order.STATUS_FINISHED);
			order.setStatusDesc(Order.STATUS_FINISHED_DESC);
		}else if("TRADE_CANCELED".equals(jingdongStatus)) {		//取消
			order.setStatus(Order.STATUS_CLOSED);
			order.setStatusDesc(Order.STATUS_CLOSED_DESC);
		}else if("LOCKED".equals(jingdongStatus)) {	//已锁定
			order.setStatus("LOCKED");
			order.setStatusDesc("已锁定");
		}
		
		//-------------物流-------------
		if (StringUtils.hasLength(orderSearchInfo.getFreightPrice())) {
			order.setShippingCharge(Double.valueOf(orderSearchInfo.getFreightPrice()));
		}
		
		//-------------发票-------------
		String invoiceInfo = orderSearchInfo.getInvoiceInfo();
		String[] invoiceInfoArr = resolveJingdongInvoice(invoiceInfo);
		if (invoiceInfoArr != null) {
			Invoice invoice = new Invoice(invoiceInfoArr[0],invoiceInfoArr[1],invoiceInfoArr[2]);
			order.setInvoice(invoice);
		}
		
		//-------------收货地址-------------
		Address receiverAddress = new Address();
		//国家
		receiverAddress.setCountry("");
		//省份
		receiverAddress.setState(orderSearchInfo.getConsigneeInfo().getProvince());
		//市
		receiverAddress.setCity(orderSearchInfo.getConsigneeInfo().getCity());
		//区
		receiverAddress.setDistrict(orderSearchInfo.getConsigneeInfo().getCounty());
		//详细地址
		receiverAddress.setAddress(orderSearchInfo.getConsigneeInfo().getFullAddress());
		//邮政编码  gri4中邮编也是这么设置的
		receiverAddress.setZipCode("000000");
		//手机(移动电话)
		receiverAddress.setMobile(orderSearchInfo.getConsigneeInfo().getMobile());
		//固定电话
		receiverAddress.setPhone(orderSearchInfo.getConsigneeInfo().getTelephone());
		//联系人姓名
		receiverAddress.setContactName(orderSearchInfo.getConsigneeInfo().getFullname());;
		
		order.setReceiverAddress(receiverAddress);
		
		//-------------买家信息地址-------------
		User shopper = new User();
		shopper.setChannelUserId(StringUtils.hasLength(orderSearchInfo.getPin()) ? orderSearchInfo.getPin() : null);
		order.setShopper(shopper);
		
		//-------------金额折扣-------------
		//订单实付金额
		if (StringUtils.hasText(orderSearchInfo.getOrderPayment())) {
			order.setPayment(Double.valueOf(orderSearchInfo.getOrderPayment()));
		}
		//订单总金额 
		if (StringUtils.hasText(orderSearchInfo.getOrderTotalPrice())) {
			order.setOrderAmount(Double.valueOf(orderSearchInfo.getOrderTotalPrice()));
		}
		//-------------订单项-------------
		for (ItemInfo o : orderSearchInfo.getItemInfoList()) {
			OrderItem orderItem = new OrderItem();
			//渠道子订单id
			orderItem.setChannelOrderItemId(orderSearchInfo.getOrderId()+"_"+o.getSkuId());
			//sku的外部商家编码,即partnumber
			orderItem.setOuterSkuId(o.getOuterSkuId());
			//渠道sku编码
			orderItem.setChannelSkuId(o.getSkuId());
			//商品的外部商家编码
			orderItem.setOuterProductId(o.getProductNo());
			//渠道商品编码
			orderItem.setChannelProductId(o.getSkuId());
			//商品数量
			orderItem.setQuantity(Long.parseLong(o.getItemTotal()));
			//商品价格(单价)
			orderItem.setPrice(Double.valueOf(o.getJdPrice()));
			//商品标题
			orderItem.setTitle(o.getSkuName());
			
			//子订单折扣
			if (CollectionUtil.isNotEmpty(orderSearchInfo.getCouponDetailList())) {
				for (CouponDetail c : orderSearchInfo.getCouponDetailList()) {
					if (StringUtils.hasText(c.getSkuId())) {
						if (o.getSkuId().equals(c.getSkuId())) {
							if (StringUtils.hasText(c.getCouponPrice())) {
								orderItem.setItemDiscount(NumberUtil.add(orderItem.getItemDiscount(), Double.valueOf(c.getCouponPrice().trim())));
							}
						}
					} 
				}
			}
			if(order.getPayment()==0D||"0".equals(orderSearchInfo.getOrderPayment())||"0.00".equals(orderSearchInfo.getOrderPayment())){
				//实付金额为0时，将订单行折扣为吊牌价*数量，让订单行金额也为0
				orderItem.setItemDiscount(NumberUtil.mul(orderItem.getPrice(), orderItem.getQuantity().doubleValue()));;
			}
			//子订单实付金额(单价*数量)
			orderItem.setPayment(NumberUtil.mul(orderItem.getPrice(), orderItem.getQuantity().doubleValue()));
			
			order.addOrderItem(orderItem);
		}
		
	
		
		// 订单折扣位计算
		if (CollectionUtil.isNotEmpty(orderSearchInfo.getCouponDetailList())) {
			for (CouponDetail c : orderSearchInfo.getCouponDetailList()) {
				if (!StringUtils.hasText(c.getSkuId())&& !StringUtils.isEmpty(c.getCouponType())) {
					String couponType = c.getCouponType();
					if (couponType.startsWith("100-") || couponType.startsWith("20-") || couponType.startsWith("35-")) {
						if (StringUtils.hasText(c.getCouponPrice())) {
							order.setOrderDiscount(NumberUtil.sub(order.getOrderDiscount(), Double.valueOf(c.getCouponPrice())));
						}
					}
				}
			}
			order.setOrderDiscount(Math.abs(order.getOrderDiscount()));
		}
		if(order.getPayment()==0D||"0".equals(orderSearchInfo.getOrderPayment())||"0.00".equals(orderSearchInfo.getOrderPayment())){
			//实付金额为0时，将订单折扣为0
			order.setOrderDiscount(0D);
		}
		
		
		//-------------货到付款-------------
		String payType = orderSearchInfo.getPayType();
		//1-货到付款
		if ("1-\u8d27\u5230\u4ed8\u6b3e".equals(payType)) {
			order.setType(Order.ORDER_TYPE_COD);
			//货到付款还需支付的金额
			if (StringUtils.hasText(orderSearchInfo.getOrderPayment())) {
				order.setCodPrice(Double.valueOf(orderSearchInfo.getOrderPayment()));
			}
			order.setPayment(0D);
		}
		return order;
	}
	
	/**
	 * 将京东OrderInfo型订单转换为meidiator的订单Order
	 * @param orderInfo 京东OrderInfo订单
	 * @return 返回meidiator系统订单Order
	 */
	private Order translatorOrderInfo(OrderInfo orderInfo){
		Order order = new Order();
		//订单号
		order.setChannelOrderId(orderInfo.getOrderId());
		//订单父订单订单号
		order.addAdditionalParam("directParentOrderId", orderInfo.getParentOrderId());

		//交易创建时间(下单时间)
		order.setCreateTime(DateUtil.parse(orderInfo.getOrderStartTime()));
		//支付时间 TODO COD支付时间可能为空
		if (!"0001-01-01 00:00:00".equals(orderInfo.getPaymentConfirmTime())) {
			order.setPayTime(DateUtil.parse(orderInfo.getPaymentConfirmTime()));
		} else {
			LOGGER.warn("JD order {} pay time is {}", orderInfo.getOrderId(), orderInfo.getPaymentConfirmTime());
		}
		//修改时间

		if (orderInfo.getModified() != null) {
			order.setModified(DateUtil.parse(orderInfo.getModified()));
		}
		//交易成功时间
		if (!"0001-01-01 00:00:00".equals(orderInfo.getOrderEndTime()) 
				&& !"1970-01-01 00:00:00".equals(orderInfo.getOrderEndTime())) {
			order.setEndTime(DateUtil.parse(orderInfo.getOrderEndTime()));
		}
		
		//买家留言
		order.setBuyerMessage(orderInfo.getOrderRemark());
		//卖家备注
		order.setSellerMemo(orderInfo.getVenderRemark());
		
		//订单状态,这个需要匹配
		String jingdongStatus = orderInfo.getOrderState();
		if ("WAIT_SELLER_STOCK_OUT".equals(jingdongStatus)) {	//等待发货
			order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
			order.setStatusDesc(Order.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
		} else if ("WAIT_GOODS_RECEIVE_CONFIRM".equals(jingdongStatus)) {	//等待买家收货
			order.setStatus(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS);
			order.setStatusDesc(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS_DESC);
		} else if ("FINISHED_L".equals(jingdongStatus)) {	//交易成功
			order.setStatus(Order.STATUS_FINISHED);
			order.setStatusDesc(Order.STATUS_FINISHED_DESC);
		} else if ("TRADE_CANCELED".equals(jingdongStatus)) {		//取消
			order.setStatus(Order.STATUS_CLOSED);
			order.setStatusDesc(Order.STATUS_CLOSED_DESC);
		} 
		
		//-------------物流-------------
		//order.setShippingType(shippingType);
		if (StringUtils.hasLength(orderInfo.getFreightPrice())) {
			order.setShippingCharge(Double.valueOf(orderInfo.getFreightPrice()));
		}
		
		//-------------发票-------------
		String invoiceInfo = orderInfo.getInvoiceInfo();
		String[] invoiceInfoArr = resolveJingdongInvoice(invoiceInfo);
		if (invoiceInfoArr != null) {
			Invoice invoice = new Invoice(invoiceInfoArr[0], invoiceInfoArr[1], invoiceInfoArr[2]);
			order.setInvoice(invoice);
		}
		
		//-------------收货地址-------------
		Address receiverAddress = new Address();
		//国家
		receiverAddress.setCountry("");
		//省份
		receiverAddress.setState(orderInfo.getConsigneeInfo().getProvince());
		//市
		receiverAddress.setCity(orderInfo.getConsigneeInfo().getCity());
		//区
		receiverAddress.setDistrict(orderInfo.getConsigneeInfo().getCounty());
		//详细地址
		receiverAddress.setAddress(orderInfo.getConsigneeInfo().getFullAddress());
		//邮政编码
		//receiverAddress.setZipCode();
		//手机(移动电话)
		receiverAddress.setMobile(orderInfo.getConsigneeInfo().getMobile());
		//固定电话
		receiverAddress.setPhone(orderInfo.getConsigneeInfo().getTelephone());
		//联系人姓名
		receiverAddress.setContactName(orderInfo.getConsigneeInfo().getFullname());;
		
		order.setReceiverAddress(receiverAddress);
		
		//-------------买家信息地址-------------
		User shopper = new User();
		shopper.setChannelUserId(StringUtils.hasLength(orderInfo.getPin()) ? orderInfo.getPin() : null);
		order.setShopper(shopper);
		

		//-------------金额折扣-------------
		//订单实付金额
		if (StringUtils.hasText(orderInfo.getOrderPayment())) {
			order.setPayment(Double.valueOf(orderInfo.getOrderPayment()));
		}
		//订单总金额 
		if (StringUtils.hasText(orderInfo.getOrderTotalPrice())) {
			order.setOrderAmount(Double.valueOf(orderInfo.getOrderTotalPrice()));
		}
		//-------------订单项-------------
		for(com.jd.open.api.sdk.domain.order.ItemInfo o : orderInfo.getItemInfoList()) {
			OrderItem orderItem = new OrderItem();
			//渠道子订单id
			orderItem.setChannelOrderItemId(orderInfo.getOrderId()+"_"+o.getSkuId());
			//sku的外部商家编码,即partnumber
			orderItem.setOuterSkuId(o.getOuterSkuId());
			//渠道sku编码
			orderItem.setChannelSkuId(o.getSkuId());
			//商品的外部商家编码
			orderItem.setOuterProductId(o.getProductNo());
			//渠道商品编码
			orderItem.setChannelProductId(o.getSkuId());
			//商品数量
			orderItem.setQuantity(Long.parseLong(o.getItemTotal()));
			//商品价格(单价)
			orderItem.setPrice(Double.valueOf(o.getJdPrice()));
			//商品标题
			orderItem.setTitle(o.getSkuName());
			
			//子订单折扣
			if (CollectionUtil.isNotEmpty(orderInfo.getCouponDetailList())) {
				for (com.jd.open.api.sdk.domain.order.CouponDetail c : orderInfo.getCouponDetailList()) {
					if (StringUtils.hasText(c.getSkuId())) {
						if (o.getSkuId().equals(c.getSkuId())) {
							if (StringUtils.hasText(c.getCouponPrice())) {
								orderItem.setItemDiscount(NumberUtil.add(orderItem.getItemDiscount(), Double.valueOf(c.getCouponPrice().trim())));
							}
						}
					} 
				}
			}
			if(order.getPayment()==0D||"0".equals(orderInfo.getOrderPayment())||"0.00".equals(orderInfo.getOrderPayment())){
				//实付金额为0时，将订单行折扣为吊牌价*数量，让订单行金额也为0
				orderItem.setItemDiscount(NumberUtil.mul(orderItem.getPrice(), orderItem.getQuantity().doubleValue()));;
			}
			//子订单实付金额(单价*数量)
			orderItem.setPayment(NumberUtil.mul(orderItem.getPrice(), orderItem.getQuantity().doubleValue()));
			
			order.addOrderItem(orderItem);
		}
		
		// 订单折扣位计算
		//List<CouponDetail> couponDetailList = (List<CouponDetail>) orderSearchInfo.getCouponDetailList();
		if (CollectionUtil.isNotEmpty(orderInfo.getCouponDetailList())) {
			for (com.jd.open.api.sdk.domain.order.CouponDetail c : orderInfo.getCouponDetailList()) {
				if (!StringUtils.hasText(c.getSkuId())) {
					String couponType = c.getCouponType();
					if (couponType.startsWith("100-") || couponType.startsWith("20-") || couponType.startsWith("35-")) {
						if (StringUtils.hasText(c.getCouponPrice())) {
							order.setOrderDiscount(NumberUtil.sub(order.getOrderDiscount(), Double.valueOf(c.getCouponPrice())));
						}
					}
				}
			}
			order.setOrderDiscount(Math.abs(order.getOrderDiscount()));
		}
		if(order.getPayment()==0D||"0".equals(orderInfo.getOrderPayment())||"0.00".equals(orderInfo.getOrderPayment())){
			//实付金额为0时，将订单折扣为0
			order.setOrderDiscount(0D);
		}
		
		//-------------货到付款-------------
		//codpayment
		String payType = orderInfo.getPayType();
		//1-货到付款
		if ("1-\u8d27\u5230\u4ed8\u6b3e".equals(payType)) {
			order.setType(Order.ORDER_TYPE_COD);
			//货到付款还需支付的金额
			if (StringUtils.hasText(orderInfo.getOrderPayment())) {
				order.setCodPrice(Double.valueOf(orderInfo.getOrderPayment()));
			}
			order.setPayment(0D);
		}
		
		return order;
	}
	
	
	
//	/**
//	 * 将京东OrderInfo型订单转换为meidiator的订单Order
//	 * @param orderInfo 京东OrderInfo订单
//	 * @return 返回meidiator系统订单Order
//	 */
//	private Order translatorOrderInfo(OrderInfo orderInfo){
//		Order order = new Order();
//		//订单号
//		order.setChannelOrderId(orderInfo.getOrderId());
//		//交易创建时间(下单时间)
//		order.setCreateTime(DateUtil.parse(orderInfo.getOrderStartTime()));
//		//支付时间 TODO COD支付时间可能为空
//		if (!"0001-01-01 00:00:00".equals(orderInfo.getPaymentConfirmTime()) && !StringUtils.isEmpty(orderInfo.getPaymentConfirmTime())) {
//			order.setPayTime(DateUtil.parse(orderInfo.getPaymentConfirmTime()));
//		} else {
//			LOGGER.warn("JD order {} pay time is {}", orderInfo.getOrderId(), orderInfo.getPaymentConfirmTime());
//		}
//		//修改时间
//		if (!StringUtils.isEmpty(orderInfo.getModified())) {
//			order.setModified(DateUtil.parse(orderInfo.getModified()));
//		}
//		//交易成功时间
//		if (!"0001-01-01 00:00:00".equals(orderInfo.getOrderEndTime()) && !StringUtils.isEmpty(orderInfo.getOrderEndTime()) 
//				&& !"1970-01-01 00:00:00".equals(orderInfo.getOrderEndTime())) {
//			order.setEndTime(DateUtil.parse(orderInfo.getOrderEndTime()));
//		}
//		
//		//买家留言
//		order.setBuyerMessage(orderInfo.getOrderRemark());
//		//卖家备注
//		order.setSellerMemo(orderInfo.getVenderRemark());
//		
//		//订单状态,这个需要匹配
//		String jingdongStatus = orderInfo.getOrderState();
//		if ("WAIT_SELLER_STOCK_OUT".equals(jingdongStatus)) {	//等待发货
//			order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
//			order.setStatusDesc(Order.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
//		} else if ("WAIT_GOODS_RECEIVE_CONFIRM".equals(jingdongStatus)) {	//等待买家收货
//			order.setStatus(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS);
//			order.setStatusDesc(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS_DESC);
//		} else if ("FINISHED_L".equals(jingdongStatus)) {	//交易成功
//			order.setStatus(Order.STATUS_FINISHED);
//			order.setStatusDesc(Order.STATUS_FINISHED_DESC);
//		} else if ("TRADE_CANCELED".equals(jingdongStatus)) {		//取消
//			order.setStatus(Order.STATUS_CLOSED);
//			order.setStatusDesc(Order.STATUS_CLOSED_DESC);
//		} 
//		
//		//-------------物流-------------
//		//order.setShippingType(shippingType);
//		if (StringUtils.hasLength(orderInfo.getFreightPrice())) {
//			order.setShippingCharge(Double.valueOf(orderInfo.getFreightPrice()));
//		}
//		
//		//-------------发票-------------
//		String invoiceInfo = orderInfo.getInvoiceInfo();
//		String[] invoiceInfoArr = resolveJingdongInvoice(invoiceInfo);
//		if (invoiceInfoArr != null) {
//			Invoice invoice = new Invoice(invoiceInfoArr[0], invoiceInfoArr[1], invoiceInfoArr[2]);
//			order.setInvoice(invoice);
//		}
//		
//		//-------------收货地址-------------
//		Address receiverAddress = new Address();
//		//国家
//		receiverAddress.setCountry("");
//		//省份
//		receiverAddress.setState(orderInfo.getConsigneeInfo().getProvince());
//		//市
//		receiverAddress.setCity(orderInfo.getConsigneeInfo().getCity());
//		//区
//		receiverAddress.setDistrict(orderInfo.getConsigneeInfo().getCounty());
//		//详细地址
//		receiverAddress.setAddress(orderInfo.getConsigneeInfo().getFullAddress());
//		//邮政编码
//		//receiverAddress.setZipCode();
//		//手机(移动电话)
//		receiverAddress.setMobile(orderInfo.getConsigneeInfo().getMobile());
//		//固定电话
//		receiverAddress.setPhone(orderInfo.getConsigneeInfo().getTelephone());
//		//联系人姓名
//		receiverAddress.setContactName(orderInfo.getConsigneeInfo().getFullname());;
//		
//		order.setReceiverAddress(receiverAddress);
//		
//		//-------------买家信息地址-------------
//		User shopper = new User();
//		shopper.setChannelUserId(StringUtils.hasLength(orderInfo.getPin()) ? orderInfo.getPin() : null);
//		order.setShopper(shopper);
//		
//		//-------------订单项-------------
//		for(ItemInfo o : orderInfo.getItemInfoList()) {
//			OrderItem orderItem = new OrderItem();
//			//渠道子订单id
//			orderItem.setChannelOrderItemId(orderInfo.getOrderId()+"_"+o.getSkuId());
//			//sku的外部商家编码,即partnumber
//			orderItem.setOuterSkuId(o.getOuterSkuId());
//			//渠道sku编码
//			orderItem.setChannelSkuId(o.getProductNo());
//			//商品的外部商家编码
//			orderItem.setOuterProductId(o.getOuterSkuId());
//			//渠道商品编码
//			orderItem.setChannelProductId(o.getSkuId());
//			//商品数量
//			orderItem.setQuantity(Long.parseLong(o.getItemTotal()));
//			//商品价格(单价)
//			orderItem.setPrice(Double.valueOf(o.getJdPrice()));
//			//商品标题
//			orderItem.setTitle(o.getSkuName());
//			
//			//子订单折扣
//			if (CollectionUtil.isNotEmpty(orderInfo.getCouponDetailList())) {
//				for (CouponDetail c : orderInfo.getCouponDetailList()) {
//					if (StringUtils.hasText(c.getSkuId())) {
//						if (o.getSkuId().equals(c.getSkuId())) {
//							if (StringUtils.hasText(c.getCouponPrice())) {
//								orderItem.setItemDiscount(NumberUtil.add(orderItem.getItemDiscount(), Double.valueOf(c.getCouponPrice().trim())));
//							}
//						}
//					} 
//				}
//			}
//			//子订单实付金额(单价*数量)
//			orderItem.setPayment(NumberUtil.mul(orderItem.getPrice(), orderItem.getQuantity().doubleValue()));
//			
//			order.addOrderItem(orderItem);
//		}
//		if (StringUtils.hasText(orderInfo.getOrderPayment())||StringUtils.hasText(orderInfo.getBalanceUsed())) {
//			order.setPayment(Double.valueOf(orderInfo.getOrderPayment())+Double.valueOf(orderInfo.getBalanceUsed()));
//		}
//
//		if (StringUtils.hasText(orderInfo.getOrderTotalPrice())) {
//			order.setOrderAmount(Double.valueOf(orderInfo.getOrderTotalPrice()));
//		}
//		
//		// 订单折扣位计算
//		//List<CouponDetail> couponDetailList = (List<CouponDetail>) orderSearchInfo.getCouponDetailList();
//		if (CollectionUtil.isNotEmpty(orderInfo.getCouponDetailList())) {
//			for (CouponDetail c : orderInfo.getCouponDetailList()) {
//				if (!StringUtils.hasText(c.getSkuId())) {
//					String couponType = c.getCouponType();
//					if (couponType.startsWith("100-") || couponType.startsWith("20-") || couponType.startsWith("35-")) {
//						if (StringUtils.hasText(c.getCouponPrice())) {
//							order.setOrderDiscount(NumberUtil.sub(order.getOrderDiscount(), Double.valueOf(c.getCouponPrice())));
//						}
//					}
//				}
//			}
//			order.setOrderDiscount(Math.abs(order.getOrderDiscount()));
//		}
//		
//		//-------------货到付款-------------
//		//codpayment
//		String payType = orderInfo.getPayType();
//		//1-货到付款
//		if ("1-\u8d27\u5230\u4ed8\u6b3e".equals(payType)) {
//			order.setType(Order.ORDER_TYPE_COD);
//			//货到付款还需支付的金额
//			if (StringUtils.hasText(orderInfo.getOrderPayment())) {
//				order.setCodPrice(Double.valueOf(orderInfo.getOrderPayment()));
//			}
//			order.setPayment(0D);
//		}
//		
//		return order;
//	}
//	
//	/**
//	 * 将京东OrderSearchInfo型订单转换为meidiator的订单Order
//	 * @param OrderSearchInfo 京东OrderSearchInfo订单
//	 * @return 返回meidiator系统订单Order
//	 */
//	private Order translatorOrderSearchInfo(OrderSearchInfo orderSearchInfo){
//		Order order = new Order();
//		//订单号
//		order.setChannelOrderId(orderSearchInfo.getOrderId());
//		//交易创建时间(下单时间)
//		order.setCreateTime(DateUtil.parse(orderSearchInfo.getOrderStartTime()));
//		//支付时间 TODO COD支付时间可能为空
//		if (!"0001-01-01 00:00:00".equals(orderSearchInfo.getPaymentConfirmTime()) && !StringUtils.isEmpty(orderSearchInfo.getPaymentConfirmTime())) {
//			order.setPayTime(DateUtil.parse(orderSearchInfo.getPaymentConfirmTime()));
//		} else {
//			LOGGER.warn("JD order {} pay time is {}", orderSearchInfo.getOrderId(), orderSearchInfo.getPaymentConfirmTime());
//		}
//		//修改时间
//		if (!StringUtils.isEmpty(orderSearchInfo.getModified())) {
//			order.setModified(DateUtil.parse(orderSearchInfo.getModified()));
//		}
//		//交易成功时间
//		if (!"0001-01-01 00:00:00".equals(orderSearchInfo.getOrderEndTime()) && !StringUtils.isEmpty(orderSearchInfo.getOrderEndTime()) 
//				&& !"1970-01-01 00:00:00".equals(orderSearchInfo.getOrderEndTime())) {
//			order.setEndTime(DateUtil.parse(orderSearchInfo.getOrderEndTime()));
//		}
//		
//		//买家留言
//		order.setBuyerMessage(orderSearchInfo.getOrderRemark());
//		//卖家备注
//		order.setSellerMemo(orderSearchInfo.getVenderRemark());
//		
//		//订单状态,这个需要匹配
//		String jingdongStatus = orderSearchInfo.getOrderState();
//		if("WAIT_SELLER_STOCK_OUT".equals(jingdongStatus)) {	//等待发货
//			order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
//			order.setStatusDesc(Order.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
//		}else if("WAIT_GOODS_RECEIVE_CONFIRM".equals(jingdongStatus)) {	//等待买家收货
//			order.setStatus(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS);
//			order.setStatusDesc(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS_DESC);
//		}else if("FINISHED_L".equals(jingdongStatus)) {	//交易成功
//			order.setStatus(Order.STATUS_FINISHED);
//			order.setStatusDesc(Order.STATUS_FINISHED_DESC);
//		}else if("TRADE_CANCELED".equals(jingdongStatus)) {		//取消
//			order.setStatus(Order.STATUS_CLOSED);
//			order.setStatusDesc(Order.STATUS_CLOSED_DESC);
//		}else if("LOCKED".equals(jingdongStatus)) {	//已锁定
//			order.setStatus("LOCKED");
//			order.setStatusDesc("已锁定");
//		}
//		
//		//-------------物流-------------
//		if (StringUtils.hasLength(orderSearchInfo.getFreightPrice())) {
//			order.setShippingCharge(Double.valueOf(orderSearchInfo.getFreightPrice()));
//		}
//		
//		//-------------发票-------------
//		String invoiceInfo = orderSearchInfo.getInvoiceInfo();
//		String[] invoiceInfoArr = resolveJingdongInvoice(invoiceInfo);
//		if (invoiceInfoArr != null) {
//			Invoice invoice = new Invoice(invoiceInfoArr[0],invoiceInfoArr[1],invoiceInfoArr[2]);
//			order.setInvoice(invoice);
//		}
//		
//		//-------------收货地址-------------
//		Address receiverAddress = new Address();
//		//国家
//		receiverAddress.setCountry("");
//		//省份
//		receiverAddress.setState(orderSearchInfo.getConsigneeInfo().getProvince());
//		//市
//		receiverAddress.setCity(orderSearchInfo.getConsigneeInfo().getCity());
//		//区
//		receiverAddress.setDistrict(orderSearchInfo.getConsigneeInfo().getCounty());
//		//详细地址
//		receiverAddress.setAddress(orderSearchInfo.getConsigneeInfo().getFullAddress());
//		//邮政编码  gri4中邮编也是这么设置的
//		receiverAddress.setZipCode("000000");
//		//手机(移动电话)
//		receiverAddress.setMobile(orderSearchInfo.getConsigneeInfo().getMobile());
//		//固定电话
//		receiverAddress.setPhone(orderSearchInfo.getConsigneeInfo().getTelephone());
//		//联系人姓名
//		receiverAddress.setContactName(orderSearchInfo.getConsigneeInfo().getFullname());;
//		
//		order.setReceiverAddress(receiverAddress);
//		
//		//-------------买家信息地址-------------
//		User shopper = new User();
//		shopper.setChannelUserId(StringUtils.hasLength(orderSearchInfo.getPin()) ? orderSearchInfo.getPin() : null);
//		order.setShopper(shopper);
//		
//		//-------------订单项-------------
//		for (ItemInfo o : orderSearchInfo.getItemInfoList()) {
//			OrderItem orderItem = new OrderItem();
//			//渠道子订单id
//			orderItem.setChannelOrderItemId(orderSearchInfo.getOrderId()+"_"+o.getSkuId());
//			//sku的外部商家编码,即partnumber
//			orderItem.setOuterSkuId(o.getOuterSkuId());
//			//渠道sku编码
//			orderItem.setChannelSkuId(o.getProductNo());
//			//商品的外部商家编码
//			orderItem.setOuterProductId(o.getOuterSkuId());
//			//渠道商品编码
//			orderItem.setChannelProductId(o.getSkuId());
//			//商品数量
//			orderItem.setQuantity(Long.parseLong(o.getItemTotal()));
//			//商品价格(单价)
//			orderItem.setPrice(Double.valueOf(o.getJdPrice()));
//			//商品标题
//			orderItem.setTitle(o.getSkuName());
//			
//			//子订单折扣
//			if (CollectionUtil.isNotEmpty(orderSearchInfo.getCouponDetailList())) {
//				for (CouponDetail c : orderSearchInfo.getCouponDetailList()) {
//					if (StringUtils.hasText(c.getSkuId())) {
//						if (o.getSkuId().equals(c.getSkuId())) {
//							if (StringUtils.hasText(c.getCouponPrice())) {
//								orderItem.setItemDiscount(NumberUtil.add(orderItem.getItemDiscount(), Double.valueOf(c.getCouponPrice().trim())));
//							}
//						}
//					} 
//				}
//			}
//			//子订单实付金额(单价*数量)
//			orderItem.setPayment(NumberUtil.mul(orderItem.getPrice(), orderItem.getQuantity().doubleValue()));
//			
//			order.addOrderItem(orderItem);
//		}
//		
//		//-------------金额折扣-------------
//		if (StringUtils.hasText(orderSearchInfo.getOrderPayment())||StringUtils.hasText(orderSearchInfo.getBalanceUsed())) {
//			order.setPayment(Double.valueOf(orderSearchInfo.getOrderPayment())+Double.valueOf(orderSearchInfo.getBalanceUsed()));
//		}
//		if (StringUtils.hasText(orderSearchInfo.getOrderTotalPrice())) {
//			order.setOrderAmount(Double.valueOf(orderSearchInfo.getOrderTotalPrice()));
//		}
//		
//		// 订单折扣位计算
//		//Double orderDiscount = 0D;
//		
//		//Double totalAdjustment = 0D;
//		//List<CouponDetail> couponDetailList = (List<CouponDetail>) orderSearchInfo.getCouponDetailList();
//		if (CollectionUtil.isNotEmpty(orderSearchInfo.getCouponDetailList())) {
//			for (CouponDetail c : orderSearchInfo.getCouponDetailList()) {
//				if (!StringUtils.hasText(c.getSkuId())) {
//					String couponType = c.getCouponType();
//					if (couponType.startsWith("100-") || couponType.startsWith("20-") || couponType.startsWith("35-")) {
//						if (StringUtils.hasText(c.getCouponPrice())) {
//							order.setOrderDiscount(NumberUtil.sub(order.getOrderDiscount(), Double.valueOf(c.getCouponPrice())));
//						}
//					}
//				}
//			}
//			order.setOrderDiscount(Math.abs(order.getOrderDiscount()));
//		}
//		
//		//order.setTotalAdjustment(totalAdjustment);
//		
//		//-------------货到付款-------------
//		//codpayment
//		String payType = orderSearchInfo.getPayType();
//		//1-货到付款
//		if ("1-\u8d27\u5230\u4ed8\u6b3e".equals(payType)) {
//			order.setType(Order.ORDER_TYPE_COD);
//			//货到付款还需支付的金额
//			if (StringUtils.hasText(orderSearchInfo.getOrderPayment())) {
//				order.setCodPrice(Double.valueOf(orderSearchInfo.getOrderPayment()));
//			}
//			order.setPayment(0D);
//		}
//		
//		return order;
//	}

	
	@Override
	public Return transformReturn(Object source) throws TranslateException {
		return null;
	}

	@Override
	public Refund transformRefund(Object source) throws TranslateException {
		Refund refund = null;
		if(source instanceof RefundApplyVo){
			RefundApplyVo jingdongRefund = (RefundApplyVo)source;
			refund = new Refund();
			//渠道退款编号---退款单id 
			refund.setChannelRefundId(jingdongRefund.getId()+ "");
			//渠道订单号-----订单号 
			refund.setChannelOrderId(jingdongRefund.getOrderId() + "");
			//退款金额
			refund.setRefundFee(jingdongRefund.getApplyRefundSum()/100.00);
			//退款申s请时间
			refund.setCreateTime(DateUtil.parse(jingdongRefund.getApplyTime()));
			//退款审核时间---退款修改时间
			if(jingdongRefund.getCheckTime()!=null){
				refund.setModifyTime(DateUtil.parse(jingdongRefund.getCheckTime()));
			}else{
				//没有审核时jingdongRefund.getCheckTime()就会是null
				refund.setModifyTime(null);
			}
			//客户帐号---买家账号
			refund.setBuyerNick(jingdongRefund.getBuyerId());
			
			//审核状态 ----退款申请单状态 0、未审核 1、审核通过
			Long status = jingdongRefund.getStatus();
			if( status == 0L){
				//买家已经申请退款,等待卖家同意
				refund.setStatus(Refund.STATUS_WAIT_SELLER_AGREE);
				refund.setStatusDesc(Refund.STATUS_WAIT_SELLER_AGREE_DESC);
			}else if(status == 1L || status == 3L){
				//卖家已经同意退款,等待买家退货       1、审核通过 3、京东财务审核通过
				refund.setStatus(Refund.STATUS_SUCCESS);
				refund.setStatusDesc(Refund.STATUS_SUCCESS_DESC);
			}else if(status == 2L){
				//卖家拒绝退款      2、审核不通过
				refund.setStatus(Refund.STATUS_CLOSED);
				refund.setStatusDesc(Refund.STATUS_CLOSED_DESC);
			}
			//退款阶段
			refund.addAdditionalParam(JingDongConstants.KEY_REFUND_PHASE,"onsale");
		}
		return refund;
	}
	
	/**
	 * 解析发票
	 * @param invoiceInfo
	 * @return
	 */
	public String[] resolveJingdongInvoice(String invoiceInfo){
		if (StringUtils.hasLength(invoiceInfo)) {
			if(invoiceInfo.contains(";")){
				String invoiceTitle;
				String invoiceType;
				String invoiceContent;
				
				//buyer do need invoice
				//invoice structor: 发票类型:普通发票;发票抬头:个人;发票内容:明细
				String[] invoices = invoiceInfo.split(";");
				if(invoices.length == 3){
					String titleInfo = invoices[1].trim();	//(eg:)发票抬头:个人
					String[] titles = titleInfo.split(":");
					invoiceTitle = titles[1].trim();
					if("\u4e2a\u4eba".equals(invoiceTitle)){	//个人发票
						invoiceType = "1";
					}else{	//非个人(公司)发票
						invoiceType = "2";
					}
					String contentInfo = invoices[2].trim();	//(eg:)发票内容:明细
					String[] contents = contentInfo.split(":");
					invoiceContent = contents[1].trim();
					
					String[] str = {invoiceType, invoiceTitle, invoiceContent};
					return str;
				}
			}
		}
		return null;
	}

	@Override
	public Exchange translateExchange(Object source) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
