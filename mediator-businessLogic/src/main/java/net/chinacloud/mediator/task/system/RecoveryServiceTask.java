/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RecoveryServiceTask.java
 * 描述： mediator恢复服务
 */
package net.chinacloud.mediator.task.system;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.task.TaskManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <恢复服务>
 * <目前仅包括恢复抓单>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
@Component
@Scope(value="prototype")
public class RecoveryServiceTask extends SystemTask {
	
	private static final String RECOVERY_SERVICE_TYPE = "recoveryService";
	
	static {
		TaskManager.registTask(SYSTEM_TYPE, RECOVERY_SERVICE_TYPE, RecoveryServiceTask.class);
	}
	
	@Autowired
	TaskManager taskManager;

	@Override
	protected String getSubType() {
		// 恢复服务
		return RECOVERY_SERVICE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		TaskContext context = getContext();
		CommonNotifyPacket<Object> packet = new CommonNotifyPacket<Object>(null);
		packet.setType("StartGetOrder");
		
		Task task = taskManager.generateTask(context.getChannelCode(), packet);
		
		if (null != task) {
			//这里可以直接复用外层task的context
			task.setContext(context);
			
			taskManager.executeTask(task);
		}
	}

}
