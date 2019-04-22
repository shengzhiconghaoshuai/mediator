package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.domain.VopStoreMessage;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderCreateTask;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.service.VopOrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("responseOrderStoreTask")
@Scope(value="prototype")
public class ResponseOrderStoreTask extends VopCommonTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseOrderStoreTask.class);
	
	private static final String ORDER_TYPE = "ORDER";
	
	private static final String ORDER_SUBTYPE = "RESPONSEORDERSTORE";

	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		return ORDER_SUBTYPE;
	}
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_SUBTYPE, ResponseOrderStoreTask.class);
	}

	@Override
	public void doTask() throws Exception {
		Object data = this.getData();
		VopStoreMessage vopStoreMessage = null;
		if (data instanceof VopStoreMessage) {
			vopStoreMessage = (VopStoreMessage) this.getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				vopStoreMessage = JsonUtil.jsonString2Object(strData, VopStoreMessage.class);
			} 
		}
		
		Integer appId = this.getContext().getApplicationId();
		LOGGER.info("VopOXO response OrderStore - appId:" + appId);
		
		VopOrderService service = (VopOrderService) getService(OrderService.class, getContext().getChannelCode());
		service.responseOrderStore(vopStoreMessage);
		
	}
	
	

}
