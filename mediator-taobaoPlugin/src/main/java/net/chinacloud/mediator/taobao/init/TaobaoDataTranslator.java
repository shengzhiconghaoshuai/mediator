/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoDataTranslator.java
 * 描述： 淘宝数据类型转换器
 */
package net.chinacloud.mediator.taobao.init;

import java.util.Map;

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
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.NumberUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.api.domain.RefundRemindTimeout;
import com.taobao.api.domain.Trade;
import com.taobao.top.schema.Util.StringUtil;
/**
 * <淘宝数据类型转换器>
 * <淘宝数据类型转换器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月31日
 * @since 2014年12月31日
 */
public class TaobaoDataTranslator implements DataTranslator {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaobaoDataTranslator.class);

	
	@Override
	public Order transformOrder(Object source) throws TranslateException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("transform " + source + " to Order");
		}
		
		Order order = null;
		if(source instanceof Trade){
			order = new Order();
			Trade trade = (Trade)source;
			//淘宝单号
			order.setChannelOrderId(String.valueOf(trade.getTid()));
			//交易创建时间(下单时间)
			order.setCreateTime(trade.getCreated());
			//支付时间
			order.setPayTime(trade.getPayTime());
			//修改时间
			order.setModified(trade.getModified());
			//交易成功时间
			order.setEndTime(trade.getEndTime());
			//交易类型,eg:fixed-一口价,step-分期,cod-货到付款,auction-拍卖
			order.setType(trade.getType());
		
			//交易内部来源,如手机、PC、PAD等
			order.setTradeFrom(trade.getTradeFrom());
			//买家留言
			order.setBuyerMessage(trade.getBuyerMessage());
			//卖家备注
			order.setSellerMemo(trade.getSellerMemo());
			
			//订单状态,这个需要匹配
			String tradeStatus = trade.getStatus();
			if ("SELLER_CONSIGNED_PART".equals(tradeStatus)) {	//卖家部分发货
				order.setStatus(Order.STATUS_SELLER_CONSIGNED_PART);
				order.setStatusDesc(Order.STATUS_SELLER_CONSIGNED_PART_DESC);
			} else if ("WAIT_SELLER_SEND_GOODS".equals(tradeStatus)) {	//买家已付款
				order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
				order.setStatusDesc(Order.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
			} else if ("WAIT_BUYER_CONFIRM_GOODS".equals(tradeStatus)) {	//卖家已发货
				order.setStatus(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS);
				order.setStatusDesc(Order.STATUS_WAIT_BUYER_CONFIRM_GOODS_DESC);
			} else if ("TRADE_BUYER_SIGNED".equals(tradeStatus)) {		//买家已签收(货到付款)
				order.setStatus(Order.STATUS_BUYER_SIGNED);
				order.setStatusDesc(Order.STATUS_BUYER_SIGNED_DESC);
			} else if ("TRADE_FINISHED".equals(tradeStatus)) {	//交易成功
				order.setStatus(Order.STATUS_FINISHED);
				order.setStatusDesc(Order.STATUS_FINISHED_DESC);
			} else if ("TRADE_CLOSED".equals(tradeStatus)) {	//交易关闭
				order.setStatus(Order.STATUS_CLOSED);
				order.setStatusDesc(Order.STATUS_CLOSED_DESC);
			} else if ("PAID_FORBID_CONSIGN".equals(tradeStatus)) { //订单已付款但是处于禁止发货状态
				order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
				order.setStatusDesc(Order.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
				order.setSellerMemo(trade.getSellerMemo()+",订单已付款但是禁止发货");
			}else if("WAIT_BUYER_PAY".equals(tradeStatus)){
				order.setStatus("WAIT_BUYER_PAY");
				order.setStatusDesc("等待买家付款");
			}
			
			//-------------预售-------------
			//预售订单状态,系统直接使用的淘宝预售单状态,因此不用再适配,直接使用
			order.setStepStatus(trade.getStepTradeStatus());
			//预售单已付金额
			order.setStepPaidFee(StringUtils.hasText(trade.getStepPaidFee()) ? Double.valueOf(trade.getStepPaidFee()) : null);
			
			//-------------货到付款-------------
			//货到付款还需支付的金额
			order.setCodPrice(StringUtils.hasText(trade.getCodFee()) ? Double.valueOf(trade.getCodFee()) : null);
			
			//-------------物流-------------
			//order.setShippingType(shippingType);
			if(StringUtils.hasText(trade.getPostFee())) {
				order.setShippingCharge(Double.valueOf(trade.getPostFee()));
			}
			
			//-------------发票-------------
			if (StringUtils.hasText(trade.getInvoiceName())) {
				Invoice invoice = new Invoice(trade.getInvoiceName(), null);
				invoice.setType(trade.getInvoiceType());
				order.setInvoice(invoice);
			}
			
			//-------------收货地址-------------
			Address receiverAddress = new Address();
			//国家
			receiverAddress.setCountry("");
			//省份
			receiverAddress.setState(trade.getReceiverState());
			//市
			receiverAddress.setCity(trade.getReceiverCity());
			//区
			receiverAddress.setDistrict(trade.getReceiverDistrict());
			//详细地址
			receiverAddress.setAddress(trade.getReceiverAddress());
			//邮政编码
			receiverAddress.setZipCode(trade.getReceiverZip());
			//手机(移动电话)
			receiverAddress.setMobile(trade.getReceiverMobile());
			//固定电话
			receiverAddress.setPhone(trade.getReceiverPhone());
			//联系人姓名
			receiverAddress.setContactName(trade.getReceiverName());;
			
			order.setReceiverAddress(receiverAddress);
			
			//-------------买家信息地址-------------
			User shopper = new User();
			shopper.setChannelUserId(trade.getBuyerNick());
			order.setShopper(shopper);
			
			//----------------全渠道商品通相关字段-----------
			String omnichannelParam = trade.getOmnichannelParam();
			if(!StringUtil.isEmpty(omnichannelParam)){
				if(omnichannelParam.contains(Order.STORE_COLLECT)){
					order.setType(Order.STORE_COLLECT);//门店自提
				}
			}
			
			//-------------订单项-------------
			for(com.taobao.api.domain.Order o : trade.getOrders()) {
				OrderItem orderItem = new OrderItem();
				//渠道子订单id
				orderItem.setChannelOrderItemId(String.valueOf(o.getOid()));
				//sku的外部商家编码,即partnumber
				orderItem.setOuterSkuId(o.getOuterSkuId());
				//渠道sku编码
				orderItem.setChannelSkuId(o.getSkuId());
				//商品的外部商家编码
				orderItem.setOuterProductId(o.getOuterIid());
				//渠道商品编码
				orderItem.setChannelProductId(String.valueOf(o.getNumIid()));
				//商品数量
				orderItem.setQuantity(o.getNum());
				//商品价格(单价)
				orderItem.setPrice(Double.valueOf(o.getPrice()));
				
				//子订单折扣
				if (StringUtils.hasText(o.getDiscountFee())) {
					orderItem.setItemDiscount(Double.valueOf(o.getDiscountFee().trim()));
				}
				if(StringUtils.hasText(o.getAdjustFee())){
					orderItem.setItemDiscount(NumberUtil.add(orderItem.getItemDiscount(), Double.valueOf(o.getAdjustFee())));
				}
				// 子订单实付金额
				if(StringUtils.hasText(o.getPayment())) {
					orderItem.setPayment(Double.valueOf(o.getPayment()));
				}
				
				//是否超卖
				orderItem.setOverSold(o.getIsOversold());
				//物流方式
				//orderItem.setShippingType(shippingType);
				//子订单状态
				String orderStatus = o.getStatus();
				if ("WAIT_SELLER_SEND_GOODS".equals(orderStatus)) {		//买家已付款
					orderItem.setStatus(OrderItem.STATUS_WAIT_SELLER_SEND_GOODS);
					orderItem.setStatusDesc(OrderItem.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
				} else if ("WAIT_BUYER_CONFIRM_GOODS".equals(orderStatus)) {	//卖家已发货
					orderItem.setStatus(OrderItem.STATUS_WAIT_BUYER_CONFIRM_GOODS);
					orderItem.setStatusDesc(OrderItem.STATUS_WAIT_BUYER_CONFIRM_GOODS_DESC);
				} else if ("TRADE_BUYER_SIGNED".equals(orderStatus)) {	//买家已签收
					orderItem.setStatus(OrderItem.STATUS_BUYER_SIGNED);
					orderItem.setStatusDesc(OrderItem.STATUS_BUYER_SIGNED_DESC);
				} else if ("TRADE_FINISHED".equals(orderStatus)) {	//交易成功
					orderItem.setStatus(OrderItem.STATUS_FINISHED);
					orderItem.setStatusDesc(OrderItem.STATUS_FINISHED_DESC);
				} else if ("TRADE_CLOSED".equals(orderStatus)) {	//交易关闭
					orderItem.setStatus(OrderItem.STATUS_CLOSED);
					orderItem.setStatusDesc(OrderItem.STATUS_CLOSED_DESC);
				}else if("WAIT_BUYER_PAY".equals(orderStatus)){
					orderItem.setStatus("WAIT_BUYER_PAY");
					orderItem.setStatusDesc("等待买家付款");
				}
					
				//发货时间
				//orderItem.setDeliveryTime(o.get);
				//交易成功时间
				orderItem.setEndTime(o.getEndTime());
				//应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分
				orderItem.addAdditionalParam(TaobaoConstant.KEY_ORDER_ITEM_TOTALFEE, Double.valueOf(o.getTotalFee()));
				//征集预售状态
				orderItem.addAdditionalParam(TaobaoConstant.KEY_ORDER_ZHENGJI, o.getZhengjiStatus());
				//全渠道商品通相关字段
				if(Order.STORE_COLLECT.equals(order.getType())){
					String [] mapomnichannelParam = omnichannelParam.split(";");
					for(int i=0;i < mapomnichannelParam.length;i++){
						String subOrder = mapomnichannelParam[i];
						if(subOrder.contains(String.valueOf(o.getOid()))){
							String [] substr = subOrder.split(",");
							for(int j=0;j < substr.length;j++){
								if(substr[j].contains("targetCode")){
									String storeCollectTargetCode = substr[j].split(":")[1];
									orderItem.addAdditionalParam("shipNode",storeCollectTargetCode);//若派到的是门店，则此处为门店id；若派到的是电商仓，则此处为电商仓的名字
								}
								if(substr[j].contains("orderState")){
									String orderState = substr[j].split(":")[1];
									orderItem.addAdditionalParam("orderState",orderState);//星盘状态
								}
							}
						}
					}
				}
				//优惠分摊
				if (StringUtils.hasText(o.getPartMjzDiscount())) {
					//orderItem.addAdditionalParam(TaobaoConstant.KEY_ORDER_ITEM_PART_MJZ_DISCOUNT, Double.valueOf(o.getPartMjzDiscount()));
					orderItem.setSharedDiscount(Double.valueOf(o.getPartMjzDiscount()));
				}
				//商品标题
				orderItem.setTitle(o.getTitle());
				
				
				order.addOrderItem(orderItem);
			}
			
			//订单折扣
			//-------------金额折扣-------------
			//商品金额(商品价格乘以数量的总金额)
			if(StringUtils.hasText(trade.getTotalFee())) {
				order.setOrderAmount(Double.valueOf(trade.getTotalFee()));
			}
			if(StringUtils.hasText(trade.getPayment())) {
				order.setPayment(Double.valueOf(trade.getPayment()));
			}
			//订单级别的折扣
			//orderDiscount = orderAmount(商品金额[商品价格乘以数量的总金额]) + shippingCharge(邮费) + 订单项折扣  - payment(实付金额)
			//-------不计算-------
			/*Double orderDiscount = NumberUtil.add(order.getOrderAmount(), order.getShippingCharge());
			orderDiscount = NumberUtil.sub(orderDiscount,order.getPayment());
			for(OrderItem orderItem : order.getOrderItems()) {
				orderDiscount = NumberUtil.add(orderDiscount,orderItem.getItemDiscount());
			}
			orderDiscount = -orderDiscount;
			order.setOrderDiscount(orderDiscount);*/
			
			/*Double totalAdjustment = NumberUtil.sub(order.getPayment(), order.getShippingCharge());
			totalAdjustment = NumberUtil.sub(totalAdjustment, order.getOrderAmount());
			order.setTotalAdjustment(totalAdjustment);*/
			if (StringUtils.hasText(trade.getDiscountFee())) {
				order.setOrderDiscount(Double.valueOf(trade.getDiscountFee()));
			}
			
			//-------------处理渠道特有的信息-------------
			//品牌特卖
			//order.addAdditionalParam(TaobaoConstant.KEY_ORDER_BRANDSALE, trade.getIsBrandSale() ? 1 : 0);
			//买家支付宝账号
			order.addAdditionalParam(TaobaoConstant.KEY_ORDER_BUYER_ALIPAY_NO, trade.getBuyerAlipayNo());
			//支付宝交易流水号
			order.addAdditionalParam(TaobaoConstant.KEY_ORDER_ALIPAY_NO, trade.getAlipayNo());
			//卖家备注旗帜
			if (null != trade.getSellerFlag()) {
				Long sellerFlag = trade.getSellerFlag();
				@SuppressWarnings("unchecked")
				Map<Long, String> sellerFlagMap = SpringUtil.getBean("sellerFlag", Map.class);
				order.addAdditionalParam(TaobaoConstant.KEY_ORDER_SELLER_FLAG, sellerFlagMap.get(sellerFlag));
			}
		}
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("transform to Order result:" + order);
		}
		
		return order;
	}

	@Override
	public Return transformReturn(Object source) throws TranslateException {
		Return returns = new Return();
		return returns;
	}

	@Override
	public Refund transformRefund(Object source) throws TranslateException {
		Refund refund = null;
		if (source instanceof com.taobao.api.domain.Refund) {
			com.taobao.api.domain.Refund taobaoRefund = (com.taobao.api.domain.Refund)source;
			refund = new Refund();
			//渠道退款编号
			refund.setChannelRefundId(taobaoRefund.getRefundId() + "");
			//渠道订单号
			refund.setChannelOrderId(taobaoRefund.getTid() + "");
			//渠道子订单id
			refund.setChannelOrderItemId(taobaoRefund.getOid() + "");
			//退款商品数量,这个没实际意义,不准确的
			refund.setQuantity(taobaoRefund.getNum().doubleValue());
			//退款金额
			if (StringUtils.hasText(taobaoRefund.getRefundFee())) {
				refund.setRefundFee(Double.valueOf(taobaoRefund.getRefundFee()));
			}
			//订单支付金额
			if (StringUtils.hasText(taobaoRefund.getPayment())) {
				refund.setPayment(Double.valueOf(taobaoRefund.getPayment()));
			}
			//退款申请时间
			refund.setCreateTime(taobaoRefund.getCreated());
			//关闭时间
			refund.setCloseTime(taobaoRefund.getModified());
			//修改时间
			refund.setModifyTime(taobaoRefund.getModified());
			//物流公司
			refund.setShippingCompany(taobaoRefund.getCompanyName());
			//物流单号
			refund.setShippingId(taobaoRefund.getSid());
			//状态转换
			String taobaoStatus = taobaoRefund.getStatus();
			if (Refund.STATUS_WAIT_SELLER_AGREE.equals(taobaoStatus)) {	//买家已经申请退款,等待卖家同意
				refund.setStatus(Refund.STATUS_WAIT_SELLER_AGREE);
				refund.setStatusDesc(Refund.STATUS_WAIT_SELLER_AGREE_DESC);
			} else if (Refund.STATUS_WAIT_BUYER_RETURN_GOODS.equals(taobaoStatus)) {	//卖家已经同意退款,等待买家退货
				refund.setStatus(Refund.STATUS_WAIT_BUYER_RETURN_GOODS);
				refund.setStatusDesc(Refund.STATUS_WAIT_BUYER_RETURN_GOODS_DESC);
			} else if (Refund.STATUS_WAIT_SELLER_CONFIRM_GOODS.equals(taobaoStatus)) {	//买家已经退货,等待卖家确认收货
				refund.setStatus(Refund.STATUS_WAIT_SELLER_CONFIRM_GOODS);
				refund.setStatusDesc(Refund.STATUS_WAIT_SELLER_CONFIRM_GOODS_DESC);
			} else if (Refund.STATUS_SELLER_REFUSE_BUYER.equals(taobaoStatus)) {	//卖家拒绝退款
				refund.setStatus(Refund.STATUS_SELLER_REFUSE_BUYER);
				refund.setStatusDesc(Refund.STATUS_SELLER_REFUSE_BUYER_DESC);
			} else if (Refund.STATUS_CLOSED.equals(taobaoStatus)) {	//退款关闭
				refund.setStatus(Refund.STATUS_CLOSED);
				refund.setStatusDesc(Refund.STATUS_CLOSED_DESC);
			} else if (Refund.STATUS_SUCCESS.equals(taobaoStatus)) {	//退款成功
				refund.setStatus(Refund.STATUS_SUCCESS);
				refund.setStatusDesc(Refund.STATUS_SUCCESS_DESC);
			}
			
			//退款原因
			refund.setReason(taobaoRefund.getReason());
			//退款描述(TODO 测试的时候发现desc为空)
			refund.setDescription(taobaoRefund.getDesc());
			/**买家账号*/
			refund.setBuyerNick(taobaoRefund.getBuyerNick());
			
			//退货物流信息
			refund.setShippingType(taobaoRefund.getShippingType());
			refund.setShippingCompany(taobaoRefund.getCompanyName());
			refund.setShippingId(taobaoRefund.getSid());
			
			//超时时间
			RefundRemindTimeout timeout = taobaoRefund.getRefundRemindTimeout();
			if (null != timeout) {
				if (null!=timeout.getExistTimeout()&&timeout.getExistTimeout()) {
					refund.setExpireTime(timeout.getTimeout());
				}
			}
			 /**买家是否需要退货 00表示未知，01表示需要 02表示不需要*/
			Boolean flag = taobaoRefund.getHasGoodReturn();
			if(!StringUtils.isEmpty(flag)){
				if(flag){
					refund.setHasGoodReturn(Refund.HAS_GOOD_RETURN_NEED);
				}else {
					refund.setHasGoodReturn(Refund.HAS_GOOD_RETURN_NONEED);
				}
			}
			//退款阶段
			refund.addAdditionalParam(TaobaoConstant.KEY_REFUND_PHASE, taobaoRefund.getRefundPhase());
		}
		return refund;
	}

	@Override
	public Exchange translateExchange(Object source) throws TranslateException{
		Exchange exchange = null;
		if (source instanceof com.taobao.api.response.TmallExchangeReceiveGetResponse.Exchange) {
			com.taobao.api.response.TmallExchangeReceiveGetResponse.Exchange tmallExchange = (com.taobao.api.response.TmallExchangeReceiveGetResponse.Exchange)source;
			exchange = new Exchange();
			/**换货单号ID 目前支持*/
			exchange.setDisputeId(tmallExchange.getDisputeId());
			/**正向单号ID,网店主单号或子单号  目前支持*/
			exchange.setBizOrderId(tmallExchange.getBizOrderId());
			/**购买数  目前支持*/
			exchange.setNum(tmallExchange.getNum());
			/**当前换货单状态*/
			exchange.setStatus(tmallExchange.getStatus());
			/**换货单创建时间*/
			exchange.setCreated(tmallExchange.getCreated());
			/**换货单最新修改时间*/
			exchange.setModified(tmallExchange.getModified());
			/**申请换货原因*/
			exchange.setReason(tmallExchange.getReason());
			/**商品名称*/
			exchange.setTitle(tmallExchange.getTitle());
			/**买家发货物流公司*/
			exchange.setBuyerLogisticName(tmallExchange.getBuyerLogisticName());
			/**买家发货快递单号*/
			exchange.setBuyerLogisticNo(tmallExchange.getBuyerLogisticNo());
			/**买家换货地址 信息*/
			String tmallbuyeraddress = tmallExchange.getBuyerAddress();
			String [] strs = tmallbuyeraddress.split("\\^\\^\\^");
			
			Address buyerAddress = new Address();
			buyerAddress.setState(strs[0]);
			buyerAddress.setCity(strs[1]);
			buyerAddress.setDistrict(strs[2]);
			buyerAddress.setAddress(strs[3]);
			buyerAddress.setContactName(tmallExchange.getBuyerName());
			buyerAddress.setMobile(tmallExchange.getBuyerPhone());
			exchange.setBuyerAddress(buyerAddress);
			/**买家昵称*/
			exchange.setBuyerNick(tmallExchange.getBuyerNick());
			/**卖家换货地址(待定感觉没有用)*/
			exchange.setSellerAddress(tmallExchange.getAddress());
			/**卖家发货快递公司*/
			exchange.setSellerLogisticName(tmallExchange.getSellerLogisticName());
			/**卖家发货快递单号*/
			exchange.setSellerLogisticNo(tmallExchange.getSellerLogisticNo());
			/**卖家昵称*/
			exchange.setSellerNick(tmallExchange.getSellerNick());
			/**购买的商品sku*/
//			exchange.setBoughtSku(tmallExchange.getBoughtSku());不需要传给oms
			/**买家申请换货的商品sku*/
			exchange.setExchangeSku(tmallExchange.getExchangeSku());
			/**支付宝ID*/
			exchange.setAlipayNo(tmallExchange.getAlipayNo());
			/**超时时间*/
			if(!StringUtil.isEmpty(tmallExchange.getTimeOut())){
				exchange.setTimeOut(DateUtil.parse(tmallExchange.getTimeOut()));
			}
			/**当前商品状态*/
			exchange.setGoodStatus(tmallExchange.getGoodStatus());
		} else if(source instanceof com.taobao.api.response.TmallExchangeGetResponse.Exchange){
			com.taobao.api.response.TmallExchangeGetResponse.Exchange tmallExchange = (com.taobao.api.response.TmallExchangeGetResponse.Exchange)source;
			exchange = new Exchange();
			/**换货单号ID 目前支持*/
			exchange.setDisputeId(tmallExchange.getDisputeId());
			/**正向单号ID,网店主单号或子单号  目前支持*/
			exchange.setBizOrderId(tmallExchange.getBizOrderId());
			/**购买数  目前支持*/
			exchange.setNum(tmallExchange.getNum());
			/**当前换货单状态*/
			exchange.setStatus(tmallExchange.getStatus());
			/**换货单创建时间*/
			exchange.setCreated(tmallExchange.getCreated());
			/**换货单最新修改时间*/
			exchange.setModified(tmallExchange.getModified());
			/**申请换货原因*/
			exchange.setReason(tmallExchange.getReason());
			/**商品名称*/
			exchange.setTitle(tmallExchange.getTitle());
			/**买家发货物流公司*/
			exchange.setBuyerLogisticName(tmallExchange.getBuyerLogisticName());
			/**买家发货快递单号*/
			exchange.setBuyerLogisticNo(tmallExchange.getBuyerLogisticNo());
			/**买家换货地址 信息*/
			String tmallbuyeraddress = tmallExchange.getBuyerAddress();
			String [] strs = tmallbuyeraddress.split("\\^\\^\\^");
			
			Address buyerAddress = new Address();
			buyerAddress.setState(strs[0]);
			buyerAddress.setCity(strs[1]);
			buyerAddress.setDistrict(strs[2]);
			buyerAddress.setAddress(strs[3]);
			buyerAddress.setContactName(tmallExchange.getBuyerName());
			buyerAddress.setMobile(tmallExchange.getBuyerPhone());
			exchange.setBuyerAddress(buyerAddress);
			/**买家昵称*/
			exchange.setBuyerNick(tmallExchange.getBuyerNick());
			/**卖家换货地址(待定感觉没有用)*/
			exchange.setSellerAddress(tmallExchange.getAddress());
			/**卖家发货快递公司*/
			exchange.setSellerLogisticName(tmallExchange.getSellerLogisticName());
			/**卖家发货快递单号*/
			exchange.setSellerLogisticNo(tmallExchange.getSellerLogisticNo());
			/**卖家昵称*/
			exchange.setSellerNick(tmallExchange.getSellerNick());
			/**购买的商品sku*/
//			exchange.setBoughtSku(tmallExchange.getBoughtSku());不需要传给oms
			/**买家申请换货的商品sku*/
			exchange.setExchangeSku(tmallExchange.getExchangeSku());
			/**支付宝ID*/
			exchange.setAlipayNo(tmallExchange.getAlipayNo());
			/**超时时间*/
			if(!StringUtil.isEmpty(tmallExchange.getTimeOut())){
				exchange.setTimeOut(DateUtil.parse(tmallExchange.getTimeOut()));
			}
			/**当前商品状态*/
			exchange.setGoodStatus(tmallExchange.getGoodStatus());
		}
		return exchange;
	}

	

}
