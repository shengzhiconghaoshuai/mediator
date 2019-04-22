package net.chinacloud.mediator.kaola.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;








import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.QimenEventProduce;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.kaola.bean.KaolaOrder;
import net.chinacloud.mediator.kaola.request.DeliverOrderRequest;
import net.chinacloud.mediator.kaola.request.KaoLaRequest;
import net.chinacloud.mediator.kaola.request.OrderDetailRequest;
import net.chinacloud.mediator.kaola.request.OrderListRequest;
import net.chinacloud.mediator.kaola.response.DeliverOrderResponse;
import net.chinacloud.mediator.kaola.response.OrderDetailResponse;
import net.chinacloud.mediator.kaola.response.OrderListResponse;
import net.chinacloud.mediator.kaola.service.KaoLaOrderService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KaoLaOrderServiceImpl implements KaoLaOrderService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaOrderServiceImpl.class);
	@Autowired
	ConnectorManager<KaoLaRequest> connectorManager;
	
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	
	
	
	private static Integer PAGESIZE = 50;


	@Override
	public Order getOrderById(String channelOrderId) throws OrderException,
			TranslateException {
		OrderDetailRequest request = new OrderDetailRequest();
		request.setOrder_id(channelOrderId);
		
		Order order = null;
		OrderDetailResponse detailResponse = null;
		detailResponse = connectorManager.getConnector().execute(request);
		if(detailResponse != null && detailResponse.getKaola_order_get_response() != null){
			order = dataTranslatorManager.getDataTranslator().transformOrder(detailResponse.getKaola_order_get_response().getOrder());
		}
		return order;
	}

	@Override
	public void deliverOrder(Order order) throws OrderException {
		DeliverOrderRequest request =  new DeliverOrderRequest();
		request.setOrder_id(order.getChannelOrderId());
		request.setExpress_company_code(order.getShippingCompany());//快递公司代码,用,分割
		request.setExpress_no(order.getShippingId());//快递单号,用,分割
		DeliverOrderResponse response = connectorManager.getConnector().execute(request);
		if(response.getError_response() != null){
			throw new OrderException(response.getError_response().getMsg());
		}
	}

	@Override
	public void reDeliverOrder(Order order) throws OrderException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			Date startTime, Date endTime) throws OrderException {
		String [] strs = status.split("_");
		if(strs.length==1){
			return getOrderListByStatus(status,startTime,endTime,1,1001);
		}else{
			status = strs[0];
			int dateType = Integer.parseInt(strs[1]);
			return getOrderListByStatus(status,startTime,endTime,1,dateType);
		}
	}
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			Date startTime, Date endTime, Integer currentPage,int dateType) throws OrderException {
		List<CommonNotifyPacket<Order>> packets = new ArrayList<CommonNotifyPacket<Order>>();
		OrderListRequest request = new OrderListRequest();
		request.setStart_time(DateUtil.format(startTime));
		request.setEnd_time(DateUtil.format(endTime));
//		request.setStart_time("2018-03-21 17:46:00");
//		request.setEnd_time("2018-03-21 17:54:00");
		request.setPage_no(currentPage);
		request.setPage_size(PAGESIZE);
		request.setOrder_status(Integer.valueOf(status));
		request.setData_type(dateType);
		OrderListResponse response = connectorManager.getConnector().execute(request);
		if (response != null && response.getKaola_order_search_response() != null) {
			List<KaolaOrder> orders = response.getKaola_order_search_response().getOrders();
			if (CollectionUtil.isNotEmpty(orders)&& response.getKaola_order_search_response().getTotal_count() > 0) {
				for (KaolaOrder kaolaorder : orders) {
					try {
						Order order = dataTranslatorManager.getDataTranslator().transformOrder(kaolaorder);
						CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
						packets.add(packet);
					} catch (TranslateException e) {
						LOGGER.error("order:"+kaolaorder.getOrder_id()+",errorMng:"+e.getMessage());
					}
				}
			}
			Integer totalResults = response.getKaola_order_search_response().getTotal_count();
			int lastPageIndex = (totalResults % PAGESIZE == 0) ? (totalResults / PAGESIZE) : (totalResults / PAGESIZE + 1);
			int pageIndex = currentPage + 1;
			if (pageIndex <= lastPageIndex) {
				packets.addAll(getOrderListByStatus(status, startTime, endTime, pageIndex,dateType));
			}
			
		}
		return packets;
	}
	
	

	@Override
	public void AllocationNotifiedOrder(Order order) throws OrderException {
		
	}

	@Override
	public void ShopHandledOrder(Order order) throws OrderException {
		
	}

	@Override
	public void ShopAllocationOrder(Order order) throws OrderException {
		
	}

	public void deliverOrderEventToQimen(QimenEventProduce qimenEventProduce) throws OrderException{
		// TODO Auto-generated method stub
		
	}

}
