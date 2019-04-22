/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSyncJob.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.job;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.taobao.task.order.OrderSyncListTask;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.utils.SpringUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 订单同步job,使用订单同步服务,聚石塔会将用户数据推送至指定的数据库表,
 * 定期从指定的数据库表中抓取数据处理,目前只处理订单付款、订单更新两种类型的trade数据,其他的还是
 * 走通知模式,数据抓取的时候sql中不根据status过滤(status没加索引,数据量大可能会影响查询性能),而是在
 * 程序中处理指定status的trade数据
 * 
 * 问题描述
 * 1、使用倒序排序+从后往前翻页,能保证从jdp_tb_trade表中取出的数据不会漏(有可能会重复)
 * 2、取出的数据量可能会比较多,翻页将每页数据直接装入List,会导致lisi很大,占内存
 * 3、对每一页数据先处理,创建task(不等到所有数据都读到List中),创建task需要时间,数据库连接打开时间较长,不知道有没有影响
 * 4、对读到的数据循环创建task时,task在持久化到数据库之前程序抛出异常,会导致数据丢失,漏单
 * 5、调度时间控制,时间间隔小,有可能上次调度还没跑完,下次调度已经运行,下次调度获取的开始时间有可能是
 * 上次调度的开始时间(上次调度在task创建完才更新结束时间)
 * 
 * 解决方案:
 * 方案1
 * 1、调度A使用倒序排序+从后往前翻页,从jdp_tb_trade表中取出的数据
 * 2、使用批处理将从jdp_tb_trade表中取出的数据存入临时表,设标记位,标记数据是否已经处理过,这样即使创建task之前对数据的处理失败了也不会丢失数据
 * 3、调度A更新结束时间,如果调度A中有数据处理失败,回滚事务,那么下次调度A运行时取到的开始时间还是上次调度运行的开始时间,数据不会丢失
 * 4、启用调度B,定时从临时表中取数据创建task,task创建成功则更新标记位
 * 
 * 方案2
 * 基本同方案1,调度A抓取数据时只抓取主键,task运行时再根据主键查询response,保证调度A运行的足够快,能在指定的调度间隔时间内处理完所有数据,不会导致
 * 下次调度的开始时间还是上次调度的开始时间
 * @author yejunwu123@gmail.com
 * @since 2015年7月11日 下午2:50:48
 */
public class OrderSyncJob extends CronJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7119927379107117796L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderSyncJob.class);

	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		
		ChannelService channelService = SpringUtil.getBean(ChannelService.class);
		Channel channel = channelService.getChannelById(channelId);
		
		ApplicationService applicationService = SpringUtil.getBean(ApplicationService.class);
		Application application = applicationService.getApplicationById(applicationId);
		
		TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
		Task task = SpringUtil.getBean(OrderSyncListTask.class);
		task.setDataId(String.valueOf(System.nanoTime()));
		//task context
		task.getContext().setApplicationId(applicationId);
		task.getContext().setApplicationCode(application.getCode());
		task.getContext().setChannelId(channelId);
		task.getContext().setChannelCode(channel.getCode());
		task.getContext().setStoreId(application.getStoreId());
		
		TaskTemplateService taskTemplateService = SpringUtil.getBean(TaskTemplateService.class);
		TaskTemplate template = taskTemplateService.getTaskTemplateByTypeAndSubType("ORDER", "syncList");
		task.setTemplate(template);
		
		try {
			taskManager.executeTask(task);
		} catch (TaskException e) {
			//e.printStackTrace();
			LOGGER.error("订单同步job运行失败", e);
			throw new JobExecutionException(e);
		}
	}
	
}
