package net.chinacloud.mediator.taobao.task.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.taobao.client.ChannelTaoBaoAndTmallFacadeClient;
import net.chinacloud.mediator.taobao.service.TaobaoOrderService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderTask;
import net.chinacloud.mediator.utils.JsonUtil;
@Component
@Scope(value="prototype")
public class OrderStatusUpdateTask extends OrderTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusUpdateTask.class);
	private static final String ORDER_STATUS_UPDATE_TYPE = "orderStatusUpdate";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_STATUS_UPDATE_TYPE, OrderStatusUpdateTask.class);
	}
	
	@Autowired
	ChannelTaoBaoAndTmallFacadeClient taoBaoAndTmallFacadeClient;
	@Autowired
	TaobaoOrderService orderService;

	@Override
	protected String getSubType() {
		return ORDER_STATUS_UPDATE_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = null;
		
		Object data = getData();
		if(data instanceof Order){
			order = (Order)getData();
		} else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				order = JsonUtil.jsonString2Object(strData, Order.class);
			} else {
				String dataId = null;
				if ('"' == first) {
					dataId = JsonUtil.jsonString2Object(strData, String.class);
				} else {
					dataId = getDataId();
					String [] str = dataId.split("_");
					dataId = str[0];
				}
				
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("get order " + getDataId() + "by service from channel " + getContext().getChannelCode());
				}
				order = orderService.getJdpOrderById(Long.valueOf(dataId));
			}
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("send to order status update  info:" + order);
		}
		if (null != order) {
			taoBaoAndTmallFacadeClient.submitChannelSendToStatusUpdateOrder(order, getContext(), this.id);
		}
	}
}
