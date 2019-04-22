/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：StartGetOrderTask.java
 * 描述： 恢复抓单
 */
package net.chinacloud.mediator.task.system;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.task.TaskManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <恢复抓单>
 * <恢复抓单>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
@Component
@Scope(value="prototype")
public class StartGetOrderTask extends SystemTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StartGetOrderTask.class);
	
	private static final String START_GET_ORDER = "startGetOrder";
	
	static {
		TaskManager.registTask(SYSTEM_TYPE, START_GET_ORDER, StartGetOrderTask.class);
	}
	
	@Override
	protected String getSubType() {
		//恢复抓单
		return START_GET_ORDER;
	}

	@Override
	public void doTask() throws ApplicationException {
		TaskContext context = getContext();
		//貌似硬编码了
		TaskManager.enableTask(context.getApplicationCode(), 
								"ORDER", 
								"create");
		LOGGER.info(context.getChannelCode() + "渠道" + context.getApplicationCode() + "应用恢复抓单");
	}

}
