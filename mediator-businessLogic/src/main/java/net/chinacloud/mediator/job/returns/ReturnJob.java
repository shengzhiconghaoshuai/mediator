/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ReturnJob.java
 * 描述： 退货调度
 */
package net.chinacloud.mediator.job.returns;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.utils.SpringUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <退货调度>
 * <退货调度,子类可继承>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年3月12日
 * @since 2015年3月12日
 */
public class ReturnJob extends CronJob {

	private static final long serialVersionUID = 474519512272660664L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		beforeGenerateTask(context, channelId, applicationId);
		
		Return returns = new Return();
		if(context.getMergedJobDataMap().containsKey(Constant.SCHEDULE_PARAM_STATUS)){
			String status = (String)context.getMergedJobDataMap().get(Constant.SCHEDULE_PARAM_STATUS);
			returns.setStatus(status);
		}
		
		CommonNotifyPacket<Return> packet = new CommonNotifyPacket<Return>(returns);
		packet.setType("list");
		ChannelService channelService = SpringUtil.getBean(ChannelService.class);
		Channel channel = channelService.getChannelById(channelId);
		
		ApplicationService applicationService = SpringUtil.getBean(ApplicationService.class);
		Application application = applicationService.getApplicationById(applicationId);
		
		Task task = null;
		TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
		task = taskManager.generateTask(channel.getCode(), packet);
		if(null == task){
			return;
		}
		//task context
		task.getContext().setApplicationId(applicationId);
		task.getContext().setApplicationCode(application.getCode());
		task.getContext().setChannelId(channelId);
		task.getContext().setChannelCode(channel.getCode());
		task.getContext().setStoreId(application.getStoreId());
		
		afterGenerateTask(task);
		
		try {
			taskManager.executeTask(task);
		} catch (TaskException e) {
			//e.printStackTrace();
			LOGGER.error("退货job运行失败", e);
			throw new JobExecutionException(e);
		}
	}

	/**
	 * task生成之前
	 * @param context
	 * @param applicationId
	 */
	protected void beforeGenerateTask(JobExecutionContext context, Integer channelId, Integer applicationId) {
		//do nothing by default
	}
	
	/**
	 * task生成之后
	 * @param task
	 */
	protected void afterGenerateTask(Task task){
		//do nothing by default
	}
}
