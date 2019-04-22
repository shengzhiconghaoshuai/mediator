/**
 * 文件名：PoCronJob.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.vip.vop.constants.JitConstants;
import net.chinacloud.mediator.vip.vop.domain.PoBean;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <TODO simple description>
 * <TODO detail description>
 * @author mwu@wuxicloud.com
 * @version 0.0.0,2015年2月13日
 * @since 2015年2月13日
 */
public class PoCronJob extends CronJob{

	private static final long serialVersionUID = -6531764166417823762L;

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		beforeGenerateTask(context, channelId, applicationId);
		
		PoBean poBean = new PoBean();
		
		CommonNotifyPacket<PoBean> packet = new CommonNotifyPacket<PoBean>(poBean);
		packet.setType(JitConstants.STATUS_PO_BATCHGET);
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
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
		
		
		
	}
	
	protected void beforeGenerateTask(JobExecutionContext context, Integer channelId, Integer applicationId) {
		//do nothing by default
	}
	
	protected void afterGenerateTask(Task task){
		//do nothing by default
	}

}
