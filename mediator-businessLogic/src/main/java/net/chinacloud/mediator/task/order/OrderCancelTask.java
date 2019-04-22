/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderCancelTask.java
 * 描述： 订单取消task
 */
package net.chinacloud.mediator.task.order;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <订单取消task>
 * <订单取消task>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年3月13日
 * @since 2015年3月13日
 */
@Component
@Scope(value="prototype")
public class OrderCancelTask extends OrderTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelTask.class);

	protected static final String ORDER_CANCEL_TYPE = "cancel";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_CANCEL_TYPE, OrderCancelTask.class);
	}
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;
	
	@Override
	protected String getSubType() {
		return ORDER_CANCEL_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("cancel order info:" + order);
		}
		
		orderFacadeClient.cancelOrder(order, getContext(), this.id);
	}

}
