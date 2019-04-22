package net.chinacloud.mediator.vip.vop.job;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.vip.vop.constants.JitConstants;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoListJob extends CronJob{

	private static final long serialVersionUID = -4793311881364276254L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PoListJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		Order order = new Order();
		CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
		packet.setType(JitConstants.GET_POLIST);
		
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
		try {
			taskManager.executeTask(task);
		} catch (TaskException e) {
			LOGGER.error("订单job运行失败", e);
			throw new JobExecutionException(e);
		}
		
	}

}
