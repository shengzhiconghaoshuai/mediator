/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：FailureTaskProcessJob.java
 * 描述： 
 */
package net.chinacloud.mediator.job.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.service.TaskFailureService;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.SpringUtil;

/**
 * @description 由于多线程的原因,可能已经收到失败的消息,但原来的task还没有完全跑完(状态还没更新上)
 * 有可能会造成Task.finishTask()更新的状态覆盖taskDao.updateTaskStatus(...)更新的状态
 * 导致本来task应该是失败状态,而task表中却显示成功状态
 * @author yejunwu123@gmail.com
 * @since 2015年8月15日 下午12:54:53
 */
public class FailureTaskProcessJob extends CronJob {

	private static final long serialVersionUID = 3915518389159865691L;

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		TaskFailureService taskFailureService = SpringUtil.getBean(TaskFailureService.class);
		TaskService taskService = SpringUtil.getBean(TaskService.class);
		
		List<Map<String, Object>> failureTasks = taskFailureService.getFailureMsgsByStatus(0);
		if (CollectionUtil.isNotEmpty(failureTasks)) {
			for (Map<String, Object> failureTask : failureTasks) {
				Long taskId = (Long)failureTask.get("TASK_ID");
				String errorCode = (String)failureTask.get("CODE");
				
				int result = taskService.updateTaskStatus(taskId, Task.TASK_STATUS_FAIL, Task.TASK_STATUS_FINISH, errorCode);
				if (result == 1) {
					taskFailureService.updateTaskFailureMsgStatus(taskId, 1, new Date());
				}
			}
		}
	}

}
