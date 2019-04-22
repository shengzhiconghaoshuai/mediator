/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderReDeliverTask.java
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
 * @description 订单重新发货,更新物流信息
 * @author yejunwu123@gmail.com
 * @since 2015年7月4日 下午3:05:36
 */
@Component
@Scope(value="prototype")
public class OrderReDeliverTask extends OrderTask {

	private static final String ORDER_RE_DELIVERY_TYPE = "reDelivery";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderReDeliverTask.class);
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_RE_DELIVERY_TYPE, OrderReDeliverTask.class);
	}
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;
	
	@Override
	protected String getSubType() {
		return ORDER_RE_DELIVERY_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = (Order)getData();
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("redeliver order info:" + order);
		}
		
		if (null != order) {
			try {
				OrderService orderService = getService(OrderService.class, getContext().getChannelCode());
				orderService.reDeliverOrder(order);
				orderFacadeClient.reDeliverResponse(order, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				// 发送失败信息
				String errorMsg = e1.getMessage();
				orderFacadeClient.reDeliverResponse(order, context, this.id, false, errorMsg);
				throw e1;
			}
		}
	}

}
