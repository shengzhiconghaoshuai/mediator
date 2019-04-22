/**
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundJob.java
 * 描述： 退款调度
 */
package net.chinacloud.mediator.job.refund;

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
 * <退款调度>
 * <退款调度,子类可继承>
 */
//@Component
//该类是quartz job的实现,quartz通过class.newInstance的方式创建,获取的并不是
//spring容器管理的实例,因此没法享受依赖注入的功能,也就无需再交给spring容器管理
public class RefundJob extends CronJob {
	private static final long serialVersionUID = -8528800232362353614L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context,Integer channelId,
			Integer applicationId) throws JobExecutionException{
		
		beforeGenerateTask(context, channelId, applicationId);
		
		Refund refund = new Refund();
		if(context.getMergedJobDataMap().containsKey(Constant.SCHEDULE_PARAM_STATUS)){
			String status = (String)context.getMergedJobDataMap().get(Constant.SCHEDULE_PARAM_STATUS);
			refund.setStatus(status);
		}
		
		CommonNotifyPacket<Refund> packet = new CommonNotifyPacket<Refund>(refund);
		packet.setType("list");
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
		
		afterGenerateTask(task);
		
		try {
			taskManager.executeTask(task);
		} catch (TaskException e) {
			//e.printStackTrace();
			LOGGER.error("退款job运行失败", e);
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
