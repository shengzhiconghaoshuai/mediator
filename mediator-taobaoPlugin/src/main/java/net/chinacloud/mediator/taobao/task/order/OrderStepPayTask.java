/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderStepPay.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.task.order;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderTask;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 预售订单付定金
 * @author yejunwu123@gmail.com
 * @since 2015年7月4日 下午4:40:53
 */
@Component
@Scope(value="prototype")
public class OrderStepPayTask extends OrderTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderStepPayTask.class);
	private static final String ORDER_STEP_PAY_TYPE = "stepPay";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_STEP_PAY_TYPE, OrderStepPayTask.class);
	}
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;

	@Override
	protected String getSubType() {
		return ORDER_STEP_PAY_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		// 业务处理与
		Order order = getOrder();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("create order info:" + order);
		}
		
		//预售订单付定金,与普通订单一样
		if (null != order) {
			orderFacadeClient.submitChannelOrder(order, getContext(), this.id);
		}
	}

}
