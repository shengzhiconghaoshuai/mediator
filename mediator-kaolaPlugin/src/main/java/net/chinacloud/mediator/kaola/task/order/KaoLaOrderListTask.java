/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderListTask.java
 * 描述： 
 */
package net.chinacloud.mediator.kaola.task.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;
import net.chinacloud.mediator.task.order.OrderTask;
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
 * 
 * @description kaola抓取订单列表
 * @since 2018年3月22日 下午4:00:02
 */
@Component
@Scope(value="prototype")
public class KaoLaOrderListTask extends OrderTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaOrderListTask.class);
	
	private static final int TASK_INTERVAL_BUFFER = 48*60;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	protected static final String KAOLA_ORDER_LIST_TYPE = "kaoLaList";
	
	static {
		TaskManager.registTask(ORDER_TYPE, KAOLA_ORDER_LIST_TYPE, KaoLaOrderListTask.class);
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;

	@Override
	protected String getSubType() {
		return KAOLA_ORDER_LIST_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		OrderService orderService = getService(OrderService.class, getContext().getChannelCode());
		List<CommonNotifyPacket<Order>> orders = new ArrayList<CommonNotifyPacket<Order>>();
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("KaoLa Order List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
		TaskTemplate template = getTemplate();
		int templateId = 0;
		if (null != template) {
			templateId = template.getId();
		}
		
		//每次调度运行时,开始时间往前推 TASK_INTERVAL
    	Date lastDate = lasttimeService.getLastime(applicationId, templateId);
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("-------lastDate:" + DateUtil.format(lastDate) + "-------");
    	}
    	
    	Date startTime = DateUtil.modify(lastDate, -TASK_INTERVAL);
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("-------startTime:" + DateUtil.format(startTime) + "-------");
    	}
    	
        Date endTime = DateUtil.modify(new Date(), 0);
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("-------endTime:" + DateUtil.format(endTime) + "-------");
        }
		
		String statusStr = (String)getContext().get(Constant.SCHEDULE_PARAM_STATUS);
		if(StringUtils.hasText(statusStr)){
			for(String status : statusStr.split(",")){
				List<CommonNotifyPacket<Order>> list = orderService.getOrderListByStatus(status.trim(), startTime, endTime);
				orders.addAll(list);
			}
		}
		
		//更新时间
		lasttimeService.insertOrUpdateLasttime(applicationId, templateId, endTime.getTime());
		
		if(!orders.isEmpty()){
			for(CommonNotifyPacket<Order> order : orders){
				try {
					Task task = taskManager.generateTask(getContext().getChannelCode(), order);
					
					if (null != task) {
						//传递上下文
						task.setContext(getContext());
						
						taskManager.executeTask(task);
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
