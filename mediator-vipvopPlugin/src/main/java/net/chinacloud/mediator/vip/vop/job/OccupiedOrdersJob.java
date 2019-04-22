package net.chinacloud.mediator.vip.vop.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class OccupiedOrdersJob extends CronJob{

	private static final long serialVersionUID = -2663430110614345154L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OccupiedOrdersJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		
		Order order = new Order();
		CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
		packet.setType(JitConstants.GET_OCCUPIEDORDER);
		
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
