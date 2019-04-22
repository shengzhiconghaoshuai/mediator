package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.client.ChannelOXOOrderFacadeClient;
import net.chinacloud.mediator.vip.vop.service.VopOrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("getLogisticsTransportNoTask")
@Scope(value="prototype")
public class GetLogisticsTransportNoTask extends VopCommonTask{

	private static final Logger LOGGER = LoggerFactory.getLogger(GetLogisticsTransportNoTask.class);
	
	private static final String ORDER_TYPE = "ORDER";
	
	private static final String ORDER_SUBTYPE = "getOXOLogisticsTransportNo";
	@Override
	protected String getType() {
		// TODO Auto-generated method stub
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		// TODO Auto-generated method stub
		return ORDER_SUBTYPE;
	}
	
	@Autowired
	ChannelOXOOrderFacadeClient oXOOrderFacadeClient;
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_SUBTYPE, GetLogisticsTransportNoTask.class);
	}

	@Override
	public void doTask() throws Exception {
		Order order = null;
		Object data = getData();
		if(data instanceof Order){
			order = (Order)data;
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
				}
				
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("get order " + getDataId() + "by service from channel " + getContext().getChannelCode());
				}
				order = new Order();
				order.setChannelOrderId(dataId);
			}
		}
		if(order != null){
			Integer appId = this.getContext().getApplicationId();
			LOGGER.info("VopOXO update LogisticsTranspostNo - appId:" + appId);
			
			VopOrderService service = (VopOrderService) getService(OrderService.class, getContext().getChannelCode());
			Order updateOrder = service.getOXOLogisticsTransportNo(order);
			oXOOrderFacadeClient.submitChannelLogisticsTranspostNo(updateOrder, getContext(), this.id);
		}
		
	}
	

}
