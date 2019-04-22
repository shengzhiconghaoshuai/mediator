/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSupplementTask.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.task.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.job.order.OrderSupplementJob;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;
import net.chinacloud.mediator.task.order.OrderTask;
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
public class ZhengJiOrderListTask extends OrderTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZhengJiOrderListTask.class);
	
	private static final int TASK_INTERVAL_BUFFER = 30;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	protected static final String ZHENGJI_ORDER_LIST_TYPE = "zhengjilist";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ZHENGJI_ORDER_LIST_TYPE, ZhengJiOrderListTask.class);
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	TaskService taskService;

	@Override
	protected String getSubType() {
		return ZHENGJI_ORDER_LIST_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		OrderService orderService = getService(OrderService.class, getContext().getChannelCode());
		List<CommonNotifyPacket<Order>> orders = new ArrayList<CommonNotifyPacket<Order>>();
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Order Supplement Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
		TaskTemplate template = getTemplate();
		int templateId = 0;
		if (null != template) {
			templateId = template.getId();
		}
		
		Object useDbTimeObj = getContext().get(OrderSupplementJob.USE_DB_TIME);
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
			Object fallBackIntervalObj = getContext().get(OrderSupplementJob.FALL_BACK_INTERVAL);
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
		if(StringUtils.hasText(statusStr)){
			for(String status : statusStr.split(",")){
				List<CommonNotifyPacket<Order>> list = orderService.getOrderListByStatus(status.trim(), orderStartTime, orderEndTime);
				for(CommonNotifyPacket<Order> orderList : list){
					if("2".equals(orderList.getMessage().getAdditionalParam(TaobaoConstant.KEY_ORDER_ZHENGJI))){
						orders.add(orderList);
					}
				}
			}
		}
		
		if (useDbTime) {
			//更新时间
			lasttimeService.insertOrUpdateLasttime(applicationId, templateId, orderEndTime.getTime());
		}
		
		if(!orders.isEmpty()){
			for(CommonNotifyPacket<Order> order : orders){
				try {
					Task task = taskManager.generateTask(getContext().getChannelCode(), order);
					
					if (null != task) {
						//传递上下文
						task.setContext(getContext());
						boolean repeat = taskManager.checkRepeat(task);
						if (!repeat) {
							String channelOrderId = ((null == order.getMessage()) ? "" : order.getMessage().getChannelOrderId());
							LOGGER.info("supply order:{},type:{}", channelOrderId, (null == task.getTemplate() ? "" : task.getTemplate().getSubType()));
							
							taskManager.insert(task);
							taskManager.execute(task);
						}
					}
				} catch (Exception e) {
					if(!(e instanceof DuplicateTaskException)){
						String channelOrderId = ((null == order.getMessage()) ? "" : order.getMessage().getChannelOrderId());
						MailSendUtil.sendEmail("order job process order failure, channelOrderId:" + channelOrderId, 
								JsonUtil.object2JsonString(order.getMessage()));
						LOGGER.error("order job process order failure, channelOrderId:" + channelOrderId, e);
					}
				}
			}
		}
	}

}
