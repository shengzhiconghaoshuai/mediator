package net.chinacloud.mediator.taobao.task.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.taobao.client.ChannelTaoBaoAndTmallFacadeClient;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderTask;

@Component
@Scope(value="prototype")
public class OrderMsgSendToHoldTask extends OrderTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderMsgSendToHoldTask.class);
	private static final String ORDER_MSG_SEND_HOLD_ORDER_TYPE = "msgSendToHoldOrder";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_MSG_SEND_HOLD_ORDER_TYPE, OrderMsgSendToHoldTask.class);
	}
	
	@Autowired
	ChannelTaoBaoAndTmallFacadeClient taoBaoAndTmallFacadeClient;

	@Override
	protected String getSubType() {
		return ORDER_MSG_SEND_HOLD_ORDER_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("send to hold order info:" + order);
		}
		
		if (null != order) {
			taoBaoAndTmallFacadeClient.submitChannelSendToHoldOrder(order, getContext(), this.id);
		}
	}

}
