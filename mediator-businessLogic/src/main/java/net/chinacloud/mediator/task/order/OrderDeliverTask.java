/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderDeliverTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.order;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * 
 * @description 发货
 * @author yejunwu123@gmail.com
 * @since 2015年7月4日 下午3:06:45
 */
@Component
@Scope(value="prototype")
public class OrderDeliverTask extends OrderTask {
	
	protected static final String ORDER_DELIVERY_TYPE = "delivery";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderDeliverTask.class);
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_DELIVERY_TYPE, OrderDeliverTask.class);
	}
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;

	@Override
	protected String getSubType() {
		return ORDER_DELIVERY_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("deliver order info:" + order);
		}
		
		if (null != order) {
			try {
				OrderService orderService = getService(OrderService.class, getContext().getChannelCode());
				orderService.deliverOrder(order);
				orderFacadeClient.deliverResponse(order, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				// 发送失败信息
				String errorMsg = e1.getMessage();
				orderFacadeClient.deliverResponse(order, context, this.id, false, errorMsg);
				throw e1;
			} 
		}
	}

}
