/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SaoweiJob.java
 * 描述： 
 */
package net.chinacloud.mediator.job.system;

import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.task.system.SaoweiTask;
import net.chinacloud.mediator.utils.SpringUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaoweiJob extends CronJob {

	private static final long serialVersionUID = 1247770495773084074L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaoweiJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		/*CommonNotifyPacket<Object> packet = new CommonNotifyPacket<Object>(null);
		packet.setType("SaoWei");*/
		
		
		TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
		
		//TODO 这里没法确定channel code,所以没法通过common notify packet方式生成task
		//不是很好
		//个人觉得没必要再单独生成一个额外的扫尾task,可以直接在扫尾的cron job中执行
		Task task = SpringUtil.getBean(SaoweiTask.class);
		//TODO 上下文的处理,系统级task上下文,这里暂时塞0吧,没啥关系
		ChannelService channelService = SpringUtil.getBean(ChannelService.class);
		ApplicationService applicationService = SpringUtil.getBean(ApplicationService.class);
		Channel channel = channelService.getChannelByCode("SYSTEM");
		List<Application> applications = applicationService.getApplicationsByChannelId(channel.getId());
		Application application = applications.get(0);
		TaskTemplateService templateService = SpringUtil.getBean(TaskTemplateService.class);
		TaskTemplate template = templateService.getTaskTemplateByTypeAndSubType("SYSTEM", "saowei");
		try {
			task.setDataId(String.valueOf(System.nanoTime()));
			task.setData("");
			task.setTemplate(template);
			
			task.getContext().setChannelCode(channel.getCode());
			task.getContext().setChannelId(channel.getId());
			task.getContext().setApplicationCode(application.getCode());
			task.getContext().setApplicationId(application.getId());
			task.getContext().setStoreId(application.getStoreId());
			
			if (context.getMergedJobDataMap().containsKey(Constant.SCHEDULE_PARAM_TASK_RERUN_DELAY)) {
				String taskRerunDelay = (String)context.getMergedJobDataMap().get(Constant.SCHEDULE_PARAM_TASK_RERUN_DELAY);
				task.getContext().put(Constant.SCHEDULE_PARAM_TASK_RERUN_DELAY, taskRerunDelay);
			}
			
			taskManager.executeTask(task);
		} catch (TaskException e) {
			//e.printStackTrace();
			LOGGER.error("扫尾job运行失败", e);
			throw new JobExecutionException(e);
		}
	}

}
