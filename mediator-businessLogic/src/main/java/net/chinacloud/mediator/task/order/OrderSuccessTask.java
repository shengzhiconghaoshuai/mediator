/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSuccessTask.java
 * 描述： 订单成功
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
 * <订单成功>
 * <订单成功>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月13日
 * @since 2015年1月13日
 */
@Component
@Scope(value="prototype")
public class OrderSuccessTask extends OrderTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderSuccessTask.class);
	
	protected static final String ORDER_SUCCESS_TYPE = "success";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_SUCCESS_TYPE, OrderSuccessTask.class);
	}
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;

	@Override
	protected String getSubType() {
		return ORDER_SUCCESS_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("success order info:" + order);
		}
		
		//预售状态
		String stepTradeStatus = order.getStepStatus();
		if(stepTradeStatus != null && Order.STEP_STATUS_FRONT_PAID_FINAL_NOPAID.equals(stepTradeStatus)) {
			//预售&未付尾款
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("预售订单取消:" + order);
			}
			orderFacadeClient.cancelStepTrade(order, getContext(), this.id);
		} else if (Order.STATUS_CLOSED_BY_CHANNEL.equals(order.getStatus())) {
			//如果未付款就被关闭,那么不处理
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("渠道关闭订单:" + order);
			}
			return;
		} else {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("订单成功:" + order);
			}
			orderFacadeClient.receive(order, getContext(), this.id);
		}
	}

}
