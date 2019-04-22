package net.chinacloud.mediator.vip.vop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vip.osp.sdk.exception.OspException;
import com.vip.vop.omni.logistics.LogisticsTrackRequest;
import com.vip.vop.omni.logistics.LogisticsTrackResponse;

import vipapis.finance.GetOrderFinancialDataResponse;
import vipapis.finance.OrderDetail;
import vipapis.order.CancelledOrdersResponse;
import vipapis.order.InventoryCancelledOrdersRequest;
import vipapis.order.InventoryOccupiedOrdersRequest;
import vipapis.order.OccupiedOrder;
import vipapis.order.OccupiedOrderResponse;
import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.QimenEventProduce;
import net.chinacloud.mediator.domain.VopStoreMessage;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import net.chinacloud.mediator.vip.vop.service.VopOrderService;
import net.chinacloud.mediator.vip.vop.service.VopService;
import net.chinacloud.mediator.vip.vop.task.UpdateOrderPriceTask;

@Service
public class VopOrderServiceImpl extends VopService implements VopOrderService{
	
	
	@Autowired
	private ApplicationService aApplicationService;
	
	@Autowired
	DataTranslatorManager dataTranslatorManager;

	@Override
	public Order getOrderById(String channelOrderId) throws OrderException,
			TranslateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deliverOrder(Order order) throws OrderException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		createSubTypeTask(order,app,"getOXOLogisticsTransportNo");
		
	}

	@Override
	public void reDeliverOrder(Order order) throws OrderException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			Date startTime, Date endTime) throws OrderException {
		return getOrderListByStatus(status,1,startTime,endTime);
	}
	
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			int currPageIndex, Date startTime, Date endTime) throws OrderException{
		List<CommonNotifyPacket<Order>> occupiedOrders = new ArrayList<CommonNotifyPacket<Order>>();
		
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		
		if ("occupied".equals(status)) { //实时销售订单查询
			InventoryOccupiedOrdersRequest request = new InventoryOccupiedOrdersRequest();
			request.setVendor_id(Long.valueOf(venderId));
			request.setSt_query_time(startTime.getTime()/1000);
			request.setEt_query_time(endTime.getTime()/1000);
			request.setBusiness_type(3);
			request.setPage(currPageIndex);
			request.setLimit(50);
			try {
				OccupiedOrderResponse response = getVopJITConnector().getOmniOrderServiceClient().getInventoryOccupiedOrders(request);
				List<OccupiedOrder> orders = response.getOccupied_orders();
				if (CollectionUtil.isNotEmpty(orders)) {
					for (OccupiedOrder occorder : orders) {
						try {
							Order order = dataTranslatorManager.getDataTranslator().transformOrder(occorder);
							order.setStatus(Order.STATUS_WAIT_SELLER_SEND_GOODS);
							CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
							packet.setType(Order.STATUS_WAIT_SELLER_SEND_GOODS);
							occupiedOrders.add(packet);
							createSubTypeTask(order,app,"updateOXOOrderPrice");
						} catch (TranslateException e) {
							e.printStackTrace();
						}
					}
				}
				if (response.getHas_next()) {
					int pageIndex = currPageIndex + 1;
					occupiedOrders.addAll(getOrderListByStatus(status,pageIndex,startTime,endTime));
				}
			} catch (VopJitException e) {
				throw new OrderException(e.getMessage());
			} catch (OspException e) {
				throw new OrderException(e.getReturnMessage());
			}
		} else if ("cancelled".equals(status)) { //取消订单查询
			InventoryCancelledOrdersRequest request = new InventoryCancelledOrdersRequest();
			request.setVendor_id(Long.valueOf(venderId));
			request.setSt_query_time(startTime.getTime()/1000);
			request.setEt_query_time(endTime.getTime()/1000);
			request.setBusiness_type(3);
			request.setPage(currPageIndex);
			request.setLimit(50);
			try {
				CancelledOrdersResponse response = getVopJITConnector().getOmniOrderServiceClient().getInventoryCancelledOrders(request);
				List<OccupiedOrder> orders  = response.getOccupied_orders();
				if (CollectionUtil.isNotEmpty(orders)) {
					for (OccupiedOrder occorder : orders) {
						try {
							Order order = dataTranslatorManager.getDataTranslator().transformOrder(occorder);
							order.setStatus(Order.STATUS_CLOSED);
							order.setStatusDesc(Order.STATUS_CLOSED_DESC);
							CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
							packet.setType("cancelled");
							occupiedOrders.add(packet);
							createSubTypeTask(order,app,"updateOXOOrderPrice");
						} catch (TranslateException e) {
							e.printStackTrace();
						}
					}
				}
				if (response.getHas_next()) {
					int pageIndex = currPageIndex + 1;
					occupiedOrders.addAll(getOrderListByStatus(status,pageIndex,startTime,endTime));
				}
				
			} catch (VopJitException e) {
				throw new OrderException(e.getMessage());
			} catch (OspException e) {
				throw new OrderException(e.getMessage());
			}
		}
		return occupiedOrders;
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
	}

	@Override
	public void responseOrderStore(VopStoreMessage vopStoreMessage) throws OrderException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		try {
			getVopJITConnector().getOmniOrderServiceClient().responseOrderStore(Long.valueOf(venderId), vopStoreMessage.getOrder_id(), vopStoreMessage.getStore_sn());
		} catch (NumberFormatException e) {
			throw new OrderException(e.getMessage());
		} catch (VopJitException e) {
			throw new OrderException(e.getMessage());
		} catch (OspException e) {
			throw new OrderException(e.getMessage());
		}
		
	}

	@Override
	public void confirmStoreDelivery(VopStoreMessage vopStoreMessage) throws OrderException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		try {
			getVopJITConnector().getOmniOrderServiceClient().confirmStoreDelivery(Long.valueOf(venderId), vopStoreMessage.getOrder_id(), 
					vopStoreMessage.getStore_sn(), null, vopStoreMessage.getEstimate_delivery_time());
		} catch (NumberFormatException e) {
			throw new OrderException(e.getMessage());
		} catch (VopJitException e) {
			throw new OrderException(e.getMessage());
		} catch (OspException e) {
			throw new OrderException(e.getMessage());
		}
		
	}

	@Override
	public Order updateOrderPrice(Order order) throws OrderException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();//	供应商ID
		List<String> order_ids = new ArrayList<String>();
		order_ids.add(order.getChannelOrderId());//订单号列表 单个订单的查询
		Order newOrder = null;
		try {
			GetOrderFinancialDataResponse response = getVopJITConnector().getFastServiceClient().getOrderFinancialData(Integer.valueOf(venderId), order_ids);
			if(response != null && CollectionUtil.isNotEmpty(response.getOrders())){
				OrderDetail financialOrder = response.getOrders().get(0);//单个订单的查询，只获得一个订单
				newOrder = dataTranslatorManager.getDataTranslator().transformOrder(financialOrder);
			}
		} catch (NumberFormatException e) {
			throw new OrderException(e.getMessage());
		} catch (VopJitException e) {
			throw new OrderException(e.getMessage());
		} catch (OspException e) {
			throw new OrderException(e.getMessage());
		} catch (TranslateException e) {
			throw new OrderException(e.getMessage());
		}
		return newOrder;
	}
	
	/**
	 * 直接创建task status=4,让扫尾去跑
	 * @param order 提供网店单号
	 * @param application
	 * @param subType 需要收尾跑的 task_template
	 */
	private void createSubTypeTask(Order order,Application application,String subType){
		Task task = SpringUtil.getBean(UpdateOrderPriceTask.class);;
		TaskTemplateService templateService = SpringUtil.getBean(TaskTemplateService.class);
		TaskTemplate template = templateService.getTaskTemplateByTypeAndSubType("ORDER", subType);
		TaskService taskService = SpringUtil.getBean(TaskService.class);
		task.setDataId(order.getChannelOrderId());
		task.setData(order);
		task.setTemplate(template);
		task.setStartTime(new Date());
		task.setStatus(Task.TASK_STATUS_FAIL);
		task.getContext().setChannelId(application.getChannelId());
		task.getContext().setApplicationCode(application.getCode());
		task.getContext().setApplicationId(application.getId());
		task.getContext().setStoreId(application.getStoreId());
		if(!taskService.isRepeat(task)){
			taskService.createTask(task);
		}
	}

	public void deliverOrderEventToQimen(QimenEventProduce qimenEventProduce)throws OrderException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Order getOXOLogisticsTransportNo(Order order) throws OrderException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();//	供应商ID
		LogisticsTrackRequest request = new LogisticsTrackRequest();
		request.setVendor_id(Integer.valueOf(venderId));
		List<String> order_ids = new ArrayList<String>();
		order_ids.add(order.getChannelOrderId());//订单号列表 单个订单的查询
		request.setOrder_id(order_ids);
		try {
			LogisticsTrackResponse  reponse = getVopJITConnector().getOmniLogisticsServiceClient().getOrderLogisticsTrack(request);
			List<com.vip.vop.omni.logistics.Order> orders = reponse.getOrders();
			if(CollectionUtil.isNotEmpty(orders)){
				List<com.vip.vop.omni.logistics.Package> packages = orders.get(0).getPackages();
				for(com.vip.vop.omni.logistics.Package pack : packages){
					String transportNo = pack.getTransport_no();
					order.setShippingId(transportNo);
				}
			}
		} catch (VopJitException e) {
			throw new OrderException(e.getMessage());
		} catch (OspException e) {
			throw new OrderException(e.getMessage());
		}
		return order;
	}

}
