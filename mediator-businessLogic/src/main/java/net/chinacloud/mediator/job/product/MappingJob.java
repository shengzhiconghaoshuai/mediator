package net.chinacloud.mediator.job.product;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.domain.Product;
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
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2015年12月8日 下午2:26:03
 */
public class MappingJob extends CronJob{

	private static final Logger LOGGER = LoggerFactory.getLogger(MappingJob.class);
	
	private static final long serialVersionUID = -6690929814447691456L;

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		beforeGenerateTask(context,channelId,applicationId);
		
		ChannelService channelService = SpringUtil.getBean(ChannelService.class);
		Channel channel = channelService.getChannelById(channelId);
		
		ApplicationService applicationService = SpringUtil.getBean(ApplicationService.class);
		Application application = applicationService.getApplicationById(applicationId);
		
		Product product = new Product();
		CommonNotifyPacket<Product> packet = new CommonNotifyPacket<Product>(product);
		packet.setType("mappingList");
		
		Task task = null;
		TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
		task = taskManager.generateTask(channel.getCode(),packet);
		if (null == task) {
			LOGGER.info("generate task null");
			return;
		}
		
		task.getContext().setApplicationId(applicationId);
		task.getContext().setApplicationCode(application.getCode());
		task.getContext().setChannelId(channelId);
		task.getContext().setChannelCode(channel.getCode());
		task.getContext().setStoreId(application.getStoreId());
		
		afterGenerateTask(task);
		
		try {
			taskManager.executeTask(task);
		} catch (TaskException e) {
			LOGGER.error("映射job运行失败", e);
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
