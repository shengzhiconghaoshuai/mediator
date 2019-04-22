/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSupplementJob.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.job;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.domain.Refund;
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
 * @description 退款补单job
 */
public class RefundSupplementJob extends CronJob {

	private static final long serialVersionUID = 6273889285259997375L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundSupplementJob.class);
	/**
	 * 是否使用数据库记录调度运行时间
	 */
	public static final String USE_DB_TIME = "USE_DB_TIME";
	/**
	 * 如果不使用数据库记录调度运行时间,调度回退时间,单位:s
	 */
	public static final String FALL_BACK_INTERVAL = "FALL_BACK_INTERVAL";
	/**
	 * 渠道名称
	 */
	public static final String CHANNELNAME = "CHANNELNAME";
	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		Refund refund = new Refund();
		if(context.getMergedJobDataMap().containsKey(Constant.SCHEDULE_PARAM_STATUS)){
			String status = (String)context.getMergedJobDataMap().get(Constant.SCHEDULE_PARAM_STATUS);
			refund.setStatus(status);
		}
		
		CommonNotifyPacket<Refund> packet = new CommonNotifyPacket<Refund>(refund);
		packet.setType("refundsupplement");
		ChannelService channelService = SpringUtil.getBean(ChannelService.class);
		Channel channel = channelService.getChannelById(channelId);
		
		ApplicationService applicationService = SpringUtil.getBean(ApplicationService.class);
		Application application = applicationService.getApplicationById(applicationId);
		
		Task task = null;
		TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
		task = taskManager.generateTask(channel.getCode(), packet);
		if(null == task){
			LOGGER.info("generate task null");
			return;
		}
		//task context
		task.getContext().setApplicationId(applicationId);
		task.getContext().setApplicationCode(application.getCode());
		task.getContext().setChannelId(channelId);
		task.getContext().setChannelCode(channel.getCode());
		task.getContext().setStoreId(application.getStoreId());
		String name  = (String) context.getMergedJobDataMap().get(CHANNELNAME);
		task.getContext().put(CHANNELNAME, name);
		//cron param
		if(context.getMergedJobDataMap().containsKey(USE_DB_TIME)){
			String useDbTime = (String)context.getMergedJobDataMap().get(USE_DB_TIME);
			task.getContext().put(USE_DB_TIME, useDbTime);
		}
		if(context.getMergedJobDataMap().containsKey(FALL_BACK_INTERVAL)){
			String fallBackInterval = (String)context.getMergedJobDataMap().get(FALL_BACK_INTERVAL);
			task.getContext().put(FALL_BACK_INTERVAL, fallBackInterval);
		}
		
		try {
			taskManager.executeTask(task);
		} catch (TaskException e) {
			//e.printStackTrace();
			LOGGER.error("退款补单job运行失败", e);
			throw new JobExecutionException(e);
		}
	}

}
