package net.chinacloud.mediator.xhs.init;

import java.util.Date;

import net.chinacloud.mediator.domain.Address;
import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.OrderItem;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.domain.User;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.xhs.bean.XhsOrder;
import net.chinacloud.mediator.xhs.bean.XhsOrderItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XhsDatatranslator implements DataTranslator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XhsDatatranslator.class);

	@Override
	public Order transformOrder(Object source) throws TranslateException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("transform " + source + " to Order");
		}
		
		Order order = null;
		if (source instanceof XhsOrder) {
			order = new Order();
			XhsOrder xhsOrder = (XhsOrder)source;
			
			order.setChannelOrderId(xhsOrder.getPackage_id());
			//交易创建时间(下单时间)
			
			order.setCreateTime(new Date(Long.valueOf(xhsOrder.getTime()*1000L)));
			//支付时间
			order.setPayTime(new Date(Long.valueOf(xhsOrder.getPay_time()*1000L)));


			String tradeStatus = xhsOrder.getStatus();
			if ("waiting".equals(tradeStatus)) {
				order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
				order.setStatusDesc(Order.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
			} else if ("received".equals(tradeStatus)) {
				order.setStatus(Order.STATUS_FINISHED);
				order.setStatusDesc(Order.STATUS_FINISHED_DESC);
			}
			
			//-------------收货地址-------------
			Address receiverAddress = new Address();
			//国家
			receiverAddress.setCountry("");
			//省份
			receiverAddress.setState(xhsOrder.getProvince());
			//市
			receiverAddress.setCity(xhsOrder.getCity());
			//区
			receiverAddress.setDistrict(xhsOrder.getDistrict());
			//详细地址
			receiverAddress.setAddress(xhsOrder.getReceiver_address());
			//手机(移动电话)
			receiverAddress.setMobile(xhsOrder.getReceiver_phone());
			//固定电话
			receiverAddress.setPhone(xhsOrder.getReceiver_phone());
			//联系人姓名
			receiverAddress.setContactName(xhsOrder.getReceiver_name());
			
			order.setReceiverAddress(receiverAddress);
			
			//-------------买家信息地址-------------
			User shopper = new User();
			shopper.setChannelUserId(xhsOrder.getReceiver_name());
			order.setShopper(shopper);
			
			Double discount = 0D;
			//-------------订单项-------------
			for (XhsOrderItem item : xhsOrder.getItem_list()) {
				OrderItem orderItem = new OrderItem();
				
				//订单行id
				orderItem.setChannelOrderItemId(xhsOrder.getPackage_id()+"_"+item.getSkucode());
				//sku的外部商家编码,即partnumber
				orderItem.setOuterSkuId(item.getBarcode());
				//渠道sku编码
				orderItem.setChannelSkuId(item.getSkucode());
				
				//商品数量
				orderItem.setQuantity(Long.valueOf(item.getQty()));
				//商品价格(单价)
				orderItem.setPrice(Double.valueOf(item.getPrice()));
				
				orderItem.setPayment(Double.valueOf(item.getPay_price()));
				
				//折扣金额 
				orderItem.setItemDiscount(Double.valueOf(item.getMerchant_discount())+Double.valueOf(item.getRed_discount()));
				
				orderItem.setTitle(item.getItem_name());
				
				discount = discount + orderItem.getQuantity() * orderItem.getItemDiscount();
				
				orderItem.addAdditionalParam("totalFee", Double.valueOf(item.getPay_price()) * Long.valueOf(item.getQty()));
				
				order.addOrderItem(orderItem);
				
			}
			//订单的实付金额
			order.setPayment(xhsOrder.getPay_amount());
			//订单的优惠金额
			order.setOrderDiscount(discount);
			
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("transform to Order result:" + order);
			}
		}
		return order;
	}

	@Override
	public Return transformReturn(Object source) throws TranslateException {
		return null;
	}

	@Override
	public Refund transformRefund(Object source) throws TranslateException {
		return null;
	}

	@Override
	public Exchange translateExchange(Object source) {
		// TODO Auto-generated method stub
		return null;
	}

}
