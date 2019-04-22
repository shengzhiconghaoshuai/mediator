/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CacheCleanJob.java
 * 描述： 
 */
package net.chinacloud.mediator.job.system;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.task.system.CacheCleanTask;
import net.chinacloud.mediator.utils.SpringUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <缓存清理调度>
 * <需要结合调度功能,应该是运行一次这种调度,而不是定时的cron调度,
 * 这部分封装不是非常好>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年3月4日
 * @since 2015年3月4日
 */
public class CacheCleanJob extends CronJob {

	private static final long serialVersionUID = 6293801786687900894L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheCleanJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		/*CommonNotifyPacket<Object> packet = new CommonNotifyPacket<Object>(null);
		packet.setType("CleanCache");*/
		
		String cacheName = context.getMergedJobDataMap().getString(Constant.CACHE_CACHE_NAME);
		String cacheKey = context.getMergedJobDataMap().getString(Constant.CACHE_CACHE_KEY);
		
		TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
		
		//TODO 这里没法确定channel code,所以没法通过common notify packet方式生成task
		//不是很好
		//个人觉得没必要再单独生成一个额外的扫尾task,可以直接在扫尾的cron job中执行
		Task task = SpringUtil.getBean(CacheCleanTask.class);
		//TODO 上下文的处理,系统级task上下文,这里暂时塞0吧,没啥关系
		try {
			task.setDataId(System.nanoTime() + "");
			
			task.getContext().setChannelCode("");
			task.getContext().setChannelId(0);
			task.getContext().setApplicationCode("");
			task.getContext().setApplicationId(0);
			task.getContext().setStoreId(0);
			
			task.getContext().put(Constant.CACHE_CACHE_NAME, cacheName);
			task.getContext().put(Constant.CACHE_CACHE_KEY, cacheKey);
			
			taskManager.executeTask(task);
		} catch (TaskException e) {
			//e.printStackTrace();
			LOGGER.error("缓存清理job运行失败", e);
			throw new JobExecutionException(e);
		}
	}

}
