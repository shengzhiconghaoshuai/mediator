package net.chinacloud.mediator.jingdong.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.QimenEventProduce;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.jingdong.service.JingdongOrderService;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.open.api.sdk.domain.order.OrderDetailInfo;
import com.jd.open.api.sdk.domain.order.OrderInfo;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.OrderListResult;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.OrderSearchInfo;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.request.order.OrderGetRequest;
import com.jd.open.api.sdk.request.order.OrderSopOutstorageRequest;
import com.jd.open.api.sdk.request.order.OrderSopWaybillUpdateRequest;
import com.jd.open.api.sdk.request.order.PopOrderSearchRequest;
import com.jd.open.api.sdk.response.order.OrderGetResponse;
import com.jd.open.api.sdk.response.order.PopOrderSearchResponse;

@Service("jingdongOrderServiceImpl")
public class OrderServiceImpl implements JingdongOrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	ConnectorManager<JdRequest<?>> connectorManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	@Resource(name="logisticsMapforJD")
	private Map<String,String> map;
	
	
	//每次请求条数
	private Integer PAGE_COUNT = 40;
	
	@Override
	public Order getOrderById(String channelOrderId) throws OrderException,
			TranslateException {
		OrderGetRequest request = new OrderGetRequest();
		request.setOrderId(channelOrderId);
		request.setOptionalFields("vender_id,order_id,pay_type,order_total_price,order_seller_price,freight_price,seller_discount,order_payment,delivery_type,order_state,order_state_remark,invoice_info,order_remark,order_start_time,order_end_time,consignee_info,item_info_list,coupon_detail_list,return_order,pin,payment_confirm_time,logistics_id,balance_used,parent_order_id");
		//req.setOptionalFields("order_state");
		OrderGetResponse response = connectorManager.getConnector().execute(request);
		
		OrderDetailInfo orderDetail = response.getOrderDetailInfo();
		OrderInfo orderInfo = orderDetail.getOrderInfo();
		return dataTranslatorManager.getDataTranslator().transformOrder(orderInfo);
	}

	@Override
	public void deliverOrder(Order order) throws OrderException {
		OrderSopOutstorageRequest request = new OrderSopOutstorageRequest();
		request.setOrderId(String.valueOf(order.getChannelOrderId()));
		if(null != order.getShippingCompany() && null != order.getShippingId()){
			String logisticsId = map.get(order.getShippingCompany());
			request.setLogisticsId(logisticsId);
			request.setWaybill(order.getShippingId());
		}
		request.setTradeNo(Long.toString(System.nanoTime()));
		connectorManager.getConnector().execute(request);
	}

	@Override
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			Date startTime, Date endTime) throws OrderException {
		return getOrderListByStatus(status,1,startTime,endTime);
	}
	
	/**
	 * 根据状态查询订单
	 * @param status
	 * @param currPageIndex
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws OrderException
	 */
	
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			int currPageIndex, Date startTime, Date endTime)
			throws OrderException {
		List<CommonNotifyPacket<Order>> notifys = new ArrayList<CommonNotifyPacket<Order>>();
		PopOrderSearchRequest request=new PopOrderSearchRequest();
		request.setStartDate( DateUtil.format(startTime) );
		request.setEndDate(DateUtil.format(endTime));
		request.setOrderState( status );
		request.setOptionalFields( "venderId,orderId,payType,orderTotalPrice,orderSellerPrice,freightPrice,sellerDiscount,orderPayment,deliveryType,orderState,orderStateRemark,invoiceInfo,orderRemark,orderStartTime,orderEndTime,consigneeInfo,itemInfoList,couponDetailList,returnOrder,pin,paymentConfirmTime,logisticsId,modified,balanceUsed,tuiHuoWuYou,directParentOrderId");
		request.setPage( Integer.toString(currPageIndex));
		request.setPageSize( Long.toString(PAGE_COUNT));
		PopOrderSearchResponse response = connectorManager.getConnector().execute(request);
		OrderListResult orderResult =response.getSearchorderinfoResult();
		List<OrderSearchInfo> list =  orderResult.getOrderInfoList();
		if(orderResult.getOrderTotal() > 0 && CollectionUtil.isNotEmpty(list)){
			for(OrderSearchInfo osi : list){
				if ("1".equals(osi.getReturnOrder())) {
					//如果是换货单,跳过本次循环
					LOGGER.warn("continue jd exchange order:" + osi.getOrderId());
					continue;
				}
				try {
					Order order = dataTranslatorManager.getDataTranslator().transformOrder(osi);
					/* cky2Name 貌似没用 有用再开出来
					 * if(order != null){
						HashMap<String, Object> cky2Name = new HashMap<String, Object>();
						cky2Name.put("cky2Name", getCky2NameByOrderId(osi.getOrderId()));
						order.setAdditionalParams(cky2Name);
					}*/
					CommonNotifyPacket<Order> jnp = new CommonNotifyPacket<Order>(order, status);
					notifys.add(jnp);
				} catch (TranslateException e) {
					// e.printStackTrace();
					// 这边catch住异常,这个订单就会丢失,不捕获这个异常,会导致剩余的订单未处理
					// 暂时想到的方法是发邮件通知
					LOGGER.error("jd order data translate error,channel order id:" + osi.getOrderId());
					MailSendUtil.sendEmail("order data translate error", 
							"jd order data translate error,channel order id:" + osi.getOrderId());
				}
			}
			int totalResults = orderResult.getOrderTotal();
			int lastPageIndex = totalResults % PAGE_COUNT == 0 ? totalResults/PAGE_COUNT : totalResults/PAGE_COUNT + 1;
			int pageIndex = currPageIndex + 1;
			if (pageIndex <= lastPageIndex) {
				notifys.addAll(getOrderListByStatus(status, pageIndex, startTime, endTime));
			}
		}
		return notifys;
	}

	@Override
	public void reDeliverOrder(Order order) throws OrderException {
		OrderSopWaybillUpdateRequest request = new OrderSopWaybillUpdateRequest();

		request.setOrderId(String.valueOf(order.getChannelOrderId()));
		if(null != order.getShippingCompany() && null != order.getShippingId()){
			String logisticsId = map.get(order.getShippingCompany());
			request.setLogisticsId(logisticsId);
			request.setWaybill(order.getShippingId());
		}
		request.setTradeNo(Long.toString(System.nanoTime()));
		connectorManager.getConnector().execute(request);
	}

	@Override
	public void AllocationNotifiedOrder(Order order) throws OrderException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ShopHandledOrder(Order order) throws OrderException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ShopAllocationOrder(Order order) throws OrderException {
		// TODO Auto-generated method stub
		
	}


	public void deliverOrderEventToQimen(QimenEventProduce qimenEventProduce)throws OrderException {
		
	}
}
