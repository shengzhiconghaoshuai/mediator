package net.chinacloud.mediator.xhs.serviceImpl;

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
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.xhs.bean.XhsOrder;
import net.chinacloud.mediator.xhs.bean.XhsOrderCancel;
import net.chinacloud.mediator.xhs.bean.XhsOrderCancelList;
import net.chinacloud.mediator.xhs.bean.XhsOrderList;
import net.chinacloud.mediator.xhs.request.AuditCancelOrderRequest;
import net.chinacloud.mediator.xhs.request.DeliverOrderRequest;
import net.chinacloud.mediator.xhs.request.OrderCancelListRequest;
import net.chinacloud.mediator.xhs.request.OrderDetailRequest;
import net.chinacloud.mediator.xhs.request.OrderListRequest;
import net.chinacloud.mediator.xhs.response.AuditCancelOrderResponse;
import net.chinacloud.mediator.xhs.response.DeliverOrderResponse;
import net.chinacloud.mediator.xhs.response.OrderCancelListResponse;
import net.chinacloud.mediator.xhs.response.OrderDetailResponse;
import net.chinacloud.mediator.xhs.response.OrderListResponse;
import net.chinacloud.mediator.xhs.service.XhsOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XhsOrderServiceImpl implements XhsOrderService{
	
	@Autowired
	ConnectorManager<Object> connectorManager;
	
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	
	@Resource(name="logisticsMapforXhs")
	private Map<String,String> map;
	
	private static Integer PAGESIZE = 50;
	

	@Override
	public Order getOrderById(String channelOrderId) throws OrderException,
			TranslateException {
		OrderDetailRequest request = new OrderDetailRequest();
		request.setUrlKey(channelOrderId);
		OrderDetailResponse detailResponse = connectorManager.getConnector().execute(request);
		Order order = dataTranslatorManager.getDataTranslator().transformOrder(detailResponse.getData());
		return order;
	}

	@Override
	public void deliverOrder(Order order) throws OrderException {
		DeliverOrderRequest request =  new DeliverOrderRequest();
		request.setUrlKey(order.getChannelOrderId());
		System.out.println(map.get(order.getShippingCompany().toUpperCase()));
		request.setExpress_company_code(map.get(order.getShippingCompany().toUpperCase()));
		request.setExpress_no(order.getShippingId());
		request.setStatus("shipped");
		DeliverOrderResponse response = connectorManager.getConnector().execute(request);
		if(!"0".equals(response.getError_code())||!response.getSuccess()){
			throw new OrderException("exception.api.response.invalid",response.getError_msg());
		}
	}

	@Override
	public void reDeliverOrder(Order order) throws OrderException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			Date startTime, Date endTime) throws OrderException {
		return getOrderListByStatus(status,startTime,endTime,1);
	}
	
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			Date startTime, Date endTime, Integer currentPage) throws OrderException {
		List<CommonNotifyPacket<Order>> packets = new ArrayList<CommonNotifyPacket<Order>>();
		
		OrderListRequest request = new OrderListRequest();
		request.setStart_time(new Long(startTime.getTime()/1000).intValue());
		request.setEnd_time(new Long(endTime.getTime()/1000).intValue());
		request.setPage_no(currentPage);
		request.setPage_size(PAGESIZE);
		request.setStatus(status);
		OrderListResponse response = connectorManager.getConnector().execute(request);
		if (response != null) {
			XhsOrderList xhsOrderList = response.getData();
			if (CollectionUtil.isNotEmpty(xhsOrderList.getPackage_list())) {
				for (XhsOrder xhsOrder : xhsOrderList.getPackage_list()) {
					Order order = new Order();
					order.setChannelOrderId(xhsOrder.getPackage_id());
					order.setStatus(xhsOrder.getStatus());
					CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
					packets.add(packet);
				}
			}
			Integer totalResults = response.getData().getTotal_number();
			int lastPageIndex = (totalResults % PAGESIZE == 0) ? (totalResults / PAGESIZE) : (totalResults / PAGESIZE + 1);
			int pageIndex = currentPage + 1;
			if (pageIndex <= lastPageIndex) {
				packets.addAll(getOrderListByStatus(status, startTime, endTime, pageIndex));
			}
		}
		return packets;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CommonNotifyPacket<Order>> getOrderCancelListByStatus(String status, Date startTime, Date endTime) throws OrderException {
		// TODO Auto-generated method stub
		return getOrderCancelListByStatus(status,startTime,endTime,1);
	}
		
	public List<CommonNotifyPacket<Order>> getOrderCancelListByStatus(String status,
			Date startTime, Date endTime, Integer currentPage) throws OrderException {
		List<CommonNotifyPacket<Order>> packets = new ArrayList<CommonNotifyPacket<Order>>();
		
		OrderCancelListRequest request = new OrderCancelListRequest();
		request.setStart_time(new Long(startTime.getTime()/1000).intValue());
		request.setEnd_time(new Long(endTime.getTime()/1000).intValue());
		request.setPage_no(currentPage);
		request.setPage_size(PAGESIZE);
		request.setStatus(status);
		OrderCancelListResponse response = connectorManager.getConnector().execute(request);
		if (response != null && "0".equals(response.getError_code())) {
			XhsOrderCancelList xhsOrderCancelList = response.getData();
			if (CollectionUtil.isNotEmpty(xhsOrderCancelList.getPackage_list())) {
				for (XhsOrderCancel xhsOrderCancel : xhsOrderCancelList.getPackage_list()) {
					Order order = new Order();
					order.setChannelOrderId(xhsOrderCancel.getPackage_id());
					order.setStatus(Order.STATUS_CLOSED);
					order.setStatusDesc(Order.STATUS_CLOSED_DESC);
					order.addAdditionalParam("status", xhsOrderCancel.getStatus());
					order.addAdditionalParam("cancel_time", new Date(Long.valueOf(xhsOrderCancel.getCancel_time()*1000L)));
					order.addAdditionalParam("cancel_reason",xhsOrderCancel.getCancel_reason());
					order.addAdditionalParam("max_audit_time", xhsOrderCancel.getMax_audit_time());
					order.addAdditionalParam("audit_result", xhsOrderCancel.getAudit_result());
					order.addAdditionalParam("audit_reason", xhsOrderCancel.getAudit_reason());
					CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
					packets.add(packet);
				}
			}
			Integer totalResults = response.getData().getTotal_number();
			int lastPageIndex = (totalResults % PAGESIZE == 0) ? (totalResults / PAGESIZE) : (totalResults / PAGESIZE + 1);
			int pageIndex = currentPage + 1;
			if (pageIndex <= lastPageIndex) {
				packets.addAll(getOrderListByStatus(status, startTime, endTime, pageIndex));
			}
		}
		return packets;
	}

	@Override
	public void auditingOrderCancel(Order order) throws OrderException {
		AuditCancelOrderRequest request =  new AuditCancelOrderRequest();
		request.setPackage_id(order.getChannelOrderId());
		request.setAudit_result(String.valueOf(order.getAdditionalParam("audit_result")));
		request.setAudit_reason(String.valueOf(order.getAdditionalParam("audit_reason")));
		
		AuditCancelOrderResponse response= connectorManager.getConnector().execute(request);
		if(!"0".equals(response.getError_code())||!response.getSuccess()){
			throw new OrderException("exception.api.response.invalid",response.getError_code()+response.getError_msg());
		}
	}

}
