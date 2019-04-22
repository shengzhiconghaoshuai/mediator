/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSupplementTask.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.task.refund;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.service.RefundService;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.taobao.job.RefundSupplementJob;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;
import net.chinacloud.mediator.task.refund.RefundTask;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 补单task
 * @author yejunwu123@gmail.com
 * @since 2015年11月16日 下午3:29:12
 */
@Component
@Scope(value="prototype")
public class RefundSupplementTask extends RefundTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundSupplementTask.class);
	
	private static final int TASK_INTERVAL_BUFFER = 30;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	protected static final String REFUND_SUPPLEMENT_TYPE = "refundsupplement";
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_SUPPLEMENT_TYPE, RefundSupplementTask.class);
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	TaskService taskService;

	@Override
	protected String getSubType() {
		return REFUND_SUPPLEMENT_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		RefundService refundService = getService(RefundService.class, getContext().getChannelCode());
		List<CommonNotifyPacket<Refund>> refunds = new ArrayList<CommonNotifyPacket<Refund>>();
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Order Supplement Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
		TaskTemplate template = getTemplate();
		int templateId = 0;
		if (null != template) {
			templateId = template.getId();
		}
		
		Object useDbTimeObj = getContext().get(RefundSupplementJob.USE_DB_TIME);
		boolean useDbTime = true;
		if (null != useDbTimeObj) {
			useDbTime = Boolean.valueOf((String)useDbTimeObj);
		}
		
		Date orderStartTime = null;
		Date orderEndTime = new Date();
		if (useDbTime) {
			//每次调度运行时,开始时间往前推 TASK_INTERVAL
	    	Date lastDate = lasttimeService.getLastime(applicationId, templateId);
	    	LOGGER.info("-------use db time, lastDate:" + DateUtil.format(lastDate) + "-------");
	    	
	    	orderStartTime = DateUtil.modify(lastDate, -TASK_INTERVAL);
	    	LOGGER.info("-------use db time, startTime:" + DateUtil.format(orderStartTime) + "-------");
	    	
	        LOGGER.info("-------use db time, endTime:" + DateUtil.format(orderEndTime) + "-------");
		} else {
			Object fallBackIntervalObj = getContext().get(RefundSupplementJob.FALL_BACK_INTERVAL);
			if (null != fallBackIntervalObj) {
				String fallBackIntervalStr = (String)fallBackIntervalObj;
				int fallBackInterval = 21600;
				if (StringUtils.hasText(fallBackIntervalStr)) {
					fallBackInterval = Integer.parseInt(fallBackIntervalStr);
				}
				orderStartTime = DateUtil.modify(orderEndTime, -fallBackInterval * 1000);
				
				LOGGER.info("-------use fallback time, startTime:" + DateUtil.format(orderStartTime) + "-------");
		        LOGGER.info("-------use fallback time, endTime:" + DateUtil.format(orderEndTime) + "-------");
			} else {
				Date yesterday = DateUtil.modify(orderEndTime, -86400000);
				orderStartTime = DateUtil.getFirstTimeByDate(yesterday);
				orderEndTime = DateUtil.getLastTimeByDate(yesterday);
				
				LOGGER.info("-------use yesterday time, startTime:" + DateUtil.format(orderStartTime) + "-------");
		        LOGGER.info("-------use yesterday time, endTime:" + DateUtil.format(orderEndTime) + "-------");
			}
		}
		
		String statusStr = (String)getContext().get(Constant.SCHEDULE_PARAM_STATUS);
		String name = (String)getContext().get(RefundSupplementJob.CHANNELNAME);
		if(StringUtils.hasText(statusStr)){
			for(String status : statusStr.split(",")){
				List<CommonNotifyPacket<Refund>> list = refundService.getRefundListByStatus(status.trim(), orderStartTime, orderEndTime , name);
				refunds.addAll(list);
			}
		}
		
		if (useDbTime) {
			//更新时间
			lasttimeService.insertOrUpdateLasttime(applicationId, templateId, orderEndTime.getTime());
		}
		
		if(!refunds.isEmpty()){
			for(CommonNotifyPacket<Refund> refund : refunds){
				try {
					Task task = taskManager.generateTask(getContext().getChannelCode(), refund);
					
					if (null != task) {
						//传递上下文
						task.setContext(getContext());
						boolean repeat = taskManager.checkRepeat(task);
						if (!repeat) {
							String channelRefundId = ((null == refund.getMessage()) ? "" : refund.getMessage().getChannelRefundId());
							LOGGER.info("supply order:{},type:{}", channelRefundId, (null == task.getTemplate() ? "" : task.getTemplate().getSubType()));
							
							taskManager.insert(task);
							taskManager.execute(task);
						}
					}
				} catch (Exception e) {
					if(!(e instanceof DuplicateTaskException)){
						String channelOrderId = ((null == refund.getMessage()) ? "" : refund.getMessage().getChannelRefundId());
						MailSendUtil.sendEmail("order job process order failure, channelRefundId:" + channelOrderId, 
								JsonUtil.object2JsonString(refund.getMessage()));
						LOGGER.error("order job process order failure, channelRefundId:" + channelOrderId, e);
					}
				}
			}
		}
	}

}
