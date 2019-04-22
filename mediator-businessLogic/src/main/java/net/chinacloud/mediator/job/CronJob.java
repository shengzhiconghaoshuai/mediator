/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CronJob.java
 * 描述： 调度抽象
 */
package net.chinacloud.mediator.job;

import java.io.Serializable;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.utils.SpringUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * <调度抽象>
 * <调度抽象>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月30日
 * @since 2014年12月30日
 */
//@Component
public abstract class CronJob extends QuartzJobBean implements Serializable {

	private static final long serialVersionUID = -9180970495923037873L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CronJob.class);
	
	/*@Autowired
	ApplicationService applicationService;*/
	
	/**对于一个渠道多个应用,后续应用task执行是否延迟*/
	protected boolean delayInterval = false;
	/**延迟时间*/
	protected long interval = 1000;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		//获取cron config中配置的参数
		Object channelObjectStr = context.getJobDetail().getJobDataMap().get(Constant.CHANNEL_ID);
		Object applicationObjectStr = (String)context.getJobDetail().getJobDataMap().get(Constant.APPLICATION_ID);
		
		ApplicationService applicationService = SpringUtil.getBean(ApplicationService.class);
		if(null == applicationObjectStr || "".equals(((String)applicationObjectStr).trim())){
			Integer channelId = Integer.valueOf(((String)channelObjectStr).trim());
			
			// update 2015-06-26 对于有些系统task,跟渠道、店无关,只运行一次,无需每个渠道、店铺都运行
			// 这种情况下,上下文对于task并没有什么卵用
			if (channelId < 0) {
				executeDelay(context, channelId, 0);
			} else {
				//get store id by channel id from db
				List<Application> applications = applicationService.getApplicationsByChannelId(Integer.valueOf(channelId));
				for(Application application : applications) {
					executeDelay(context, application.getChannelId(), application.getId());
				}
			}
		}else{
			String applicationIds = ((String)applicationObjectStr).trim();
			for(String applicationIdStr : applicationIds.split(",")){
				Integer applicationId = Integer.parseInt(applicationIdStr);
				Application application = applicationService.getApplicationById(applicationId);
				executeDelay(context, application.getChannelId(), application.getId());
			}
		}
	}
	
	private void executeDelay(JobExecutionContext context, Integer channelId, Integer applicationId)
			throws JobExecutionException{
		executeInternal(context, channelId, applicationId);
		if(delayInterval){
			try {
				//Thread.currentThread();
				//两个应用的task延迟一段时间执行
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				LOGGER.error("job运行失败", e);
			};
		}
	}
	
	/**
	 * 子类实现
	 * @param context 上下文,里面有一些配置的参数
	 * @param storeId
	 */
	protected abstract void executeInternal(JobExecutionContext context, Integer channelId, Integer applicationId)
			throws JobExecutionException;
}
