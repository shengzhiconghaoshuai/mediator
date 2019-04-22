package net.chinacloud.mediator.vip.vop.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.job.refund.RefundJob;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.utils.SpringUtil;

public class VopOrderReturnListJob extends CronJob {

	private static final long serialVersionUID = 6723054233025834334L;
	private static final Logger LOGGER = LoggerFactory.getLogger(VopOrderReturnListJob.class);

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
		packet.setType("orderReturnList");
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
			LOGGER.error("获取客退列表job运行失败", e);
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
