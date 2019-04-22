/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：StopGetOrderTask.java
 * 描述： 停止抓单
 */
package net.chinacloud.mediator.task.system;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <停止抓单,主要是停止task最终发送消息,task还是会创建,只是状态为TASK_STATUS_HANG>
 * <库存同步时停止新订单的创建,防止库存不准>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
@Component
@Scope(value="prototype")
public class StopGetOrderTask extends SystemTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StopGetOrderTask.class);
	
	private static final String STOP_GET_ORDER_TYPE = "stopGetOrder";
	
	static {
		TaskManager.registTask(SYSTEM_TYPE, STOP_GET_ORDER_TYPE, StopGetOrderTask.class);
	}

	@Autowired
	private TaskManager taskManager;
	
	@Override
	protected String getSubType() {
		//停止抓单
		return STOP_GET_ORDER_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		//只是简单的disable task,阻塞新订单的创建,并不保证正在运行的task已经运行完毕,库存还是可能会有问题
		
		TaskManager.disableTask(context.getApplicationCode(), 
				"ORDER", 
				"create");
		LOGGER.info(context.getChannelCode() + "渠道" + context.getApplicationCode() + "应用暂停抓单");
	}

}
