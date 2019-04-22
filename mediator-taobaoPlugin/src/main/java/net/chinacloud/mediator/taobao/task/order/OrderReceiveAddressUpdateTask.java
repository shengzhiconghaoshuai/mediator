/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderReceiveAddressUpdateTask.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.task.order;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderTask;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 订单收货地址修改
 * @author yejunwu123@gmail.com
 * @since 2015年7月4日 下午4:54:26
 */
@Component
@Scope(value="prototype")
public class OrderReceiveAddressUpdateTask extends OrderTask {
	
	private static final String ORDER_RECEIVE_ADDRESS_UPDATE_TYPE = "receiveAddressUpdate";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderReceiveAddressUpdateTask.class);

	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_RECEIVE_ADDRESS_UPDATE_TYPE, OrderReceiveAddressUpdateTask.class);
	}
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;
	
	@Override
	protected String getSubType() {
		return ORDER_RECEIVE_ADDRESS_UPDATE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Order order = getOrder();
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("order info:" + order);
		}
		
		if (null != order) {
			orderFacadeClient.updateOrderAddress(order, getContext(), this.id);
		}
	}

}
