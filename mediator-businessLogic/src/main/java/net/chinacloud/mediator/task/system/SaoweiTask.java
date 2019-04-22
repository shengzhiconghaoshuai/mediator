/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SaoweiTask.java
 * 描述： 扫尾task
 */
package net.chinacloud.mediator.task.system;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <扫尾task>
 * <将对应的task template可重跑的task取出重新运行>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月20日
 * @since 2015年1月20日
 */
@Component
@Scope(value="prototype")
public class SaoweiTask extends SystemTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaoweiTask.class);
	
	private static final String SAOWEI_TYPE = "saowei";
	
	static {
		TaskManager.registTask(SYSTEM_TYPE, SAOWEI_TYPE, SaoweiTask.class);
	}
	
	@Autowired
	TaskService taskService;
	@Autowired
	TaskManager taskManager;

	@Override
	protected String getSubType() {
		// 扫尾task
		return SAOWEI_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		LOGGER.info("--------------扫尾task开始运行--------------");
		String taskRerunDelayStr = (String)getContext().get(Constant.SCHEDULE_PARAM_TASK_RERUN_DELAY);
		Date startDate = null;
		if (StringUtils.hasText(taskRerunDelayStr)) {
			startDate = DateUtil.modify(new Date(), - Integer.valueOf(taskRerunDelayStr) * 1000);
		}
		List<Task> tasks = taskService.getAllReRunTask(startDate);
		if (CollectionUtil.isNotEmpty(tasks)) {
			for (Task task : tasks) {
				taskManager.insert(task);
				taskManager.execute(task);
			}
		}
		LOGGER.info("--------------扫尾task结束运行--------------");
	}

}
