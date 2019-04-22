package net.chinacloud.mediator.kaola.init;




import java.util.List;


import net.chinacloud.mediator.domain.Address;
import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.OrderItem;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.domain.User;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.kaola.bean.KaoLaConstant;
import net.chinacloud.mediator.kaola.bean.KaoLaOrderSku;
import net.chinacloud.mediator.kaola.bean.KaoLaRefund;
import net.chinacloud.mediator.kaola.bean.KaolaOrder;
import net.chinacloud.mediator.kaola.bean.KaolaRefundSku;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.NumberUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KaoLaDatatranslator implements DataTranslator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaDatatranslator.class);
	
	private final int TEMPLATE_ID = 62 ;//整单退款创建task templateId;

	@Override
	public Order transformOrder(Object source) throws TranslateException {
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("transform " + source + " to Order");
		}
		
		Order order = null;
		if (source instanceof KaolaOrder) {
			order = new Order();
			KaolaOrder kaolaOrder = (KaolaOrder)source;
			order.setChannelOrderId(kaolaOrder.getOrder_id());
			//交易创建时间(下单时间)
			order.setCreateTime(DateUtil.parse(kaolaOrder.getOrder_time()));
			order.setModified(DateUtil.parse(kaolaOrder.getOrder_time()));
			//支付时间
			order.setPayTime(DateUtil.parse(kaolaOrder.getPay_success_time()));
			order.setModified(DateUtil.parse(kaolaOrder.getPay_success_time()));
			//修改时间
			String deliverTime = kaolaOrder.getDeliver_time();//发货时间
			String finishTime = kaolaOrder.getFinish_time();//完成时间
			
			if(StringUtils.hasText(deliverTime)){
				order.setModified(DateUtil.parse(deliverTime));
			}
			
			if(StringUtils.hasText(finishTime)){
				order.setModified(DateUtil.parse(finishTime));
				//交易成功时间
				order.setEndTime(DateUtil.parse(finishTime));
			}
			int tradeStatus = kaolaOrder.getOrder_status();
			if (tradeStatus == KaoLaConstant.ORDER_PAID || tradeStatus == 8) {
				TaskService taskService = SpringUtil.getBean(TaskService.class);
				if(taskService.isExistTask(order.getChannelOrderId(), TEMPLATE_ID) && taskService.isExistTask(order.getChannelOrderId(), 2)){
					order.setStatus(KaoLaConstant.ORDER_TO_REVOKE);
					order.setStatusDesc(KaoLaConstant.ORDER_TO_REVOKE_DESC);
				}else{
					order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
					order.setStatusDesc(Order.STATUS_WAIT_SELLER_SEND_GOODS_DESC);
				}
			} else if (tradeStatus == KaoLaConstant.ORDER_SIGNED) {
				order.setStatus(Order.STATUS_FINISHED);
				order.setStatusDesc(Order.STATUS_FINISHED_DESC);
			}else if(tradeStatus == KaoLaConstant.ORDER_PRE_CANCEL || tradeStatus == 7 ){////取消待确认
				order.setStatus(KaoLaConstant.ORDER_TO_REFUND);
				order.setStatusDesc(KaoLaConstant.ORDER_TO_REFUND_DESC);
			}else if(tradeStatus == KaoLaConstant.ORDER_CANCELED ){//已取消  未发货退款
				order.setStatus(KaoLaConstant.ORDER_TO_CANCELED);
				order.setStatusDesc(KaoLaConstant.ORDER_TO_CANCELED_DESC);
			}
			
			//-------------收货地址-------------
			Address receiverAddress = new Address();
			//国家
			receiverAddress.setCountry("");
			//省份
			receiverAddress.setState(kaolaOrder.getReceiver_province_name());
			//市
			receiverAddress.setCity(kaolaOrder.getReceiver_city_name());
			//区
			receiverAddress.setDistrict(kaolaOrder.getReceiver_district_name());
			//详细地址
			receiverAddress.setAddress(kaolaOrder.getReceiver_address_detail());
			//手机(移动电话)
			receiverAddress.setMobile(kaolaOrder.getReceiver_phone());
			//固定电话
			receiverAddress.setPhone(kaolaOrder.getBuyer_phone());
			//联系人姓名
			receiverAddress.setContactName(kaolaOrder.getReceiver_name());
			
			receiverAddress.setZipCode(kaolaOrder.getReceiver_post_code());
			order.setReceiverAddress(receiverAddress);
			
			//-------------买家信息地址-------------
			User shopper = new User();
			shopper.setChannelUserId(kaolaOrder.getBuyer_account());
			order.setShopper(shopper);
			
			Double discount = 0D;
			//-------------订单项-------------
			for (KaoLaOrderSku kaolaitem : kaolaOrder.getOrder_skus()) {
				OrderItem orderItem = new OrderItem();
				//订单行id
				orderItem.setChannelOrderItemId(kaolaOrder.getOrder_id()+"_"+kaolaitem.getSku_key());
				//渠道sku编码
				orderItem.setChannelSkuId(kaolaitem.getSku_key());
				//sku的外部商家编码,即partnumber
				orderItem.setOuterSkuId(kaolaitem.getBarcode());
				
				orderItem.setOuterProductId(kaolaitem.getGoods_no());
				//商品数量
				orderItem.setQuantity(Long.valueOf(kaolaitem.getCount()));
				//商品价格(单价)
				orderItem.setPrice(Double.valueOf(kaolaitem.getOrigin_price()));
				//商品实付金额
				orderItem.setPayment(Double.valueOf(kaolaitem.getReal_totle_price()));
				
				//折扣金额 
//				orderItem.setItemDiscount(Double.valueOf(kaolaitem.getActivity_totle_amount())+Double.valueOf(kaolaitem.getCoupon_totle_amount()));
				String activityDiscount = kaolaitem.getActivity_totle_amount();
				if(!StringUtils.isEmpty(activityDiscount)){
					orderItem.setItemDiscount(NumberUtil.add(Double.valueOf(activityDiscount),Double.valueOf(kaolaitem.getCoupon_totle_amount())));
				}
				orderItem.setTitle(kaolaitem.getProduct_name());
				
				discount = discount + orderItem.getItemDiscount();
				
				orderItem.addAdditionalParam("totalFee", Double.valueOf(kaolaitem.getReal_totle_price()));
				
				order.addOrderItem(orderItem);
						
			}
			//订单的实付金额
			order.setPayment(Double.valueOf(kaolaOrder.getOrder_real_price()));
			String orderAmount = kaolaOrder.getOrder_real_price();//
			if(!StringUtils.isEmpty(orderAmount)){
				order.setOrderAmount(Double.valueOf(orderAmount));
			}
			//订单的优惠金额
			order.setOrderDiscount(discount);
			
			order.setBuyerMessage(kaolaOrder.getOrder_buyer_remark());//买家备注
			order.setSellerMemo(kaolaOrder.getOrder_business_remark());//卖家备注
			order.setPayType(kaolaOrder.getPay_method_subname());
			order.setShippingCharge(Double.valueOf(kaolaOrder.getExpress_fee()));
			
			order.addAdditionalParam("waiting_cancel_time", kaolaOrder.getWaiting_cancel_time());//取消待商家处理时间
			//交易流水号
			order.addAdditionalParam("alipayNo", kaolaOrder.getTrade_no());
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("transformed " + order );
		}
		return order;
	}

	@Override
	public Return transformReturn(Object source) throws TranslateException {
		return null;
	}

	@Override
	public Refund transformRefund(Object source) throws TranslateException {
		Refund refund = null;
		
		if (source instanceof KaoLaRefund ) {
			refund = new Refund();
			KaoLaRefund kaolaRefund = (KaoLaRefund)source;
			//渠道退款编号
			refund.setChannelRefundId(kaolaRefund.getRefund_id() + "");
			//渠道订单号
			refund.setChannelOrderId(kaolaRefund.getOrder_id() + "");
			List<KaolaRefundSku> refundSkus = kaolaRefund.getRefund_skus();
			if(refundSkus != null && refundSkus.size()==1){
				//渠道子订单id
				refund.setChannelOrderItemId(kaolaRefund.getOrder_id()+"_"+refundSkus.get(0).getSku_key());
				//退款商品数量,这个没实际意义,不准确的
				refund.setQuantity(Double.valueOf(refundSkus.get(0).getRefund_count()));
			}
			//退款金额
			refund.setRefundFee(Double.valueOf(kaolaRefund.getRefund_fee()));
			//订单支付金额
			//退款申请时间
			refund.setCreateTime(DateUtil.parse(kaolaRefund.getRefund_create_time()));
//			//关闭时间
//			refund.setCloseTime(kaolaRefund.getModified());
//			//修改时间
//			refund.setModifyTime(kaolaRefund.getModified());
			
			//状态转换
			int kaolaStatus = kaolaRefund.getRefund_status_detail();
			if (kaolaStatus == 1) {	//买家已经申请退款,等待卖家同意
				refund.setStatus(Refund.STATUS_WAIT_SELLER_AGREE);
				refund.setStatusDesc(Refund.STATUS_WAIT_SELLER_AGREE_DESC);
			} else if (kaolaStatus==2||kaolaStatus==4||kaolaStatus==7) {	//卖家已经同意退款,等待买家退货
				refund.setStatus(Refund.STATUS_WAIT_BUYER_RETURN_GOODS);
				refund.setStatusDesc(Refund.STATUS_WAIT_BUYER_RETURN_GOODS_DESC);
			} else if (kaolaStatus==5 || kaolaStatus==6||kaolaStatus==11) {	//买家已经退货,等待卖家确认收货
				refund.setStatus(Refund.STATUS_WAIT_SELLER_CONFIRM_GOODS);
				refund.setStatusDesc(Refund.STATUS_WAIT_SELLER_CONFIRM_GOODS_DESC);
			} else if(kaolaStatus==3 || kaolaStatus==13 || kaolaStatus==12){
				refund.setStatus(Refund.STATUS_SELLER_REFUSE_BUYER);   //卖家不同意协议,等待买家修改
				refund.setStatusDesc(Refund.STATUS_SELLER_REFUSE_BUYER_DESC);
			}else if (kaolaStatus==14 || kaolaStatus==9 ||kaolaStatus==8) {	//退款关闭
				refund.setStatus(Refund.STATUS_CLOSED);
				refund.setStatusDesc(Refund.STATUS_CLOSED_DESC);
			} else if (kaolaStatus==10) {	//退款成功
				refund.setStatus(Refund.STATUS_SUCCESS);
				refund.setStatusDesc(Refund.STATUS_SUCCESS_DESC);
			}

			
			//退款原因
			refund.setReason(kaolaRefund.getRefund_reason());
			//退款描述
			refund.setDescription(kaolaRefund.getRefund_desc());
			/**买家账号*/
			refund.setBuyerNick(kaolaRefund.getUser_account());
			
			//退货物流信息
			//物流公司
			if(StringUtils.hasText(kaolaRefund.getExpress_company_name())){
				refund.setShippingCompany(kaolaRefund.getExpress_company_name());
			}
			//物流单号
			if(StringUtils.hasText(kaolaRefund.getExpress_no())){
				refund.setShippingId(String.valueOf(kaolaRefund.getExpress_no()));
			}
			/*String flag = kaolaRefund.getRefund_type();
			if(!StringUtils.isEmpty(flag)){
				if("0".equals(flag)){
					refund.setHasGoodReturn(Refund.HAS_GOOD_RETURN_NEED);
				}else if("1".equals(flag)) {
					refund.setHasGoodReturn(Refund.HAS_GOOD_RETURN_NONEED);
				}
			}*/
			//退款阶段
			refund.addAdditionalParam("refundPhase", "onsale");
			refund.addAdditionalParam("refundSkus", refundSkus);
		}
		return refund;
	}

	@Override
	public Exchange translateExchange(Object source) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
