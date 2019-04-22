package net.chinacloud.mediator.vip.vop.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import vipapis.finance.Promotion;
import vipapis.order.OccupiedOrder;
import vipapis.order.OccupiedOrderDetail;
import net.chinacloud.mediator.domain.Address;
import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.OrderItem;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.domain.User;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.NumberUtil;
import net.chinacloud.mediator.utils.StringUtils;

public class VopDataTranslator implements DataTranslator{

	@Override
	public Order transformOrder(Object source) throws TranslateException {
		Order order = null;
		if (source instanceof OccupiedOrder) {
			order = new Order();
			OccupiedOrder occOrder = (OccupiedOrder) source;
			
			order.setChannelOrderId(occOrder.getOrder_id());

			order.setCreateTime(DateUtil.parse(occOrder.getCreate_time()));
			
			order.setPayTime(DateUtil.parse(occOrder.getCreate_time()));
			
			order.setType("fixed");
			
			User user = new User();
			user.setChannelUserId("VIPOXO");
			
			order.setShopper(user);
			
			//-------------收货地址-------------
			Address receiverAddress = new Address();
			//国家
			receiverAddress.setCountry("");
			
			//省份
			String province = occOrder.getProvince();
			if(StringUtils.isEmpty(province)){
				province = "VIPOXO";
			}
			receiverAddress.setState(province);
			//市
			String city = occOrder.getCity();
			if(StringUtils.isEmpty(city)){
				city = "VIPOXO";
			}
			receiverAddress.setCity(city);
			//区
			String district = occOrder.getDistrict();
			if(StringUtils.isEmpty(district)){
				district = "VIPOXO";
			}
			receiverAddress.setDistrict(district);
			//详细地址
			receiverAddress.setAddress("VIPOXO");
			
			order.setReceiverAddress(receiverAddress);
			
			List<OccupiedOrderDetail> oodetail = occOrder.getBarcodes();
			
			if (CollectionUtil.isNotEmpty(oodetail)) {
				for (OccupiedOrderDetail detail : oodetail) {
					OrderItem orderItem = new OrderItem();
					orderItem.setChannelOrderItemId(occOrder.getOrder_id()+"_"+detail.getBarcode());
					orderItem.setChannelSkuId(detail.getBarcode());
					orderItem.setOuterSkuId(detail.getBarcode());
					orderItem.setPrice(0D);
					orderItem.setPayment(0D);
					orderItem.setTitle(detail.getBarcode());
					orderItem.setQuantity((long)detail.getAmount());
					Map<String,Object> additionalParams = new HashMap<String, Object>();
					additionalParams.put("warehouse",detail.getWarehouse());
					additionalParams.put("sales_source_indicator", detail.getSales_source_indicator());
					additionalParams.put("sales_no", detail.getSales_no());
					additionalParams.put("cooperation_no", detail.getCooperation_no());
					additionalParams.put("totalFee", 0);
					orderItem.setAdditionalParams(additionalParams);
					
					order.addOrderItem(orderItem);
				}
			}
		}else if(source instanceof vipapis.finance.OrderDetail){
			order = new Order();
			vipapis.finance.OrderDetail orderDetail = (vipapis.finance.OrderDetail) source;
			order.setChannelOrderId(orderDetail.getOrder_id());
			
			//-------------物流-------------
			//order.setShippingType(shippingType);
			if(orderDetail.getCarriage() != null) {
				order.setShippingCharge(orderDetail.getCarriage());//运费
			}
			
			//-------------订单项-------------
			OrderItem orderItem = new OrderItem();
			//渠道子订单id
			orderItem.setChannelOrderItemId(orderDetail.getOrder_id()+"_"+orderDetail.getBarcode());
			//sku的外部商家编码,即partnumber
			orderItem.setOuterSkuId(orderDetail.getBarcode());
			//商品数量
			orderItem.setQuantity(Long.valueOf(orderDetail.getAmount()));
			//商品价格(单价)
			orderItem.setItemDiscount(0D);
			orderItem.setPrice(orderDetail.getPrice());
			List<Promotion> promotions = orderDetail.getPromotions();
			if(CollectionUtil.isNotEmpty(orderDetail.getPromotions())){
				for(Promotion promot : promotions){
					orderItem.setItemDiscount(NumberUtil.add(orderItem.getItemDiscount(), promot.getDiscount_vendor_amount()));
				}
			}
			//优惠分摊
			orderItem.setSharedDiscount(orderItem.getItemDiscount());
			// 子订单实付金额
			orderItem.setPayment(NumberUtil.sub(orderDetail.getPrice(),orderItem.getItemDiscount()));
			
			//商品标题
			orderItem.setTitle(orderDetail.getProduct_name());
			
			order.addOrderItem(orderItem);
			
			//-------------订单头金额-------------
			//商品金额(商品价格乘以数量的总金额)
			order.setOrderAmount(orderDetail.getPrice());
			order.setPayment(orderItem.getPayment());
			order.setOrderDiscount(orderItem.getItemDiscount());
			
		}
		return order;
		
	}

	@Override
	public Return transformReturn(Object source) throws TranslateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Refund transformRefund(Object source) throws TranslateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Exchange translateExchange(Object source) throws TranslateException {
		// TODO Auto-generated method stub
		return null;
	}

}
