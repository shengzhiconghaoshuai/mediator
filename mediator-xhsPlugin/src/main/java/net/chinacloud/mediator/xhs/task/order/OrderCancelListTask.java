package net.chinacloud.mediator.xhs.task.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;
import net.chinacloud.mediator.xhs.service.XhsOrderService;
@Component("orderCancelListTask")
@Scope(value="prototype")
public class OrderCancelListTask extends Task {

private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelListTask.class);
	
	private static final int TASK_INTERVAL_BUFFER = 20;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	protected static final String ORDER_CANCEL_LIST_TYPE = "cancelList";
	
	static {
		TaskManager.registTask("ORDER", ORDER_CANCEL_LIST_TYPE, OrderCancelListTask.class);
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	private XhsOrderService xhsOrderService;
	
	@Override
	protected String getSubType() {
		return ORDER_CANCEL_LIST_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		
		List<CommonNotifyPacket<Order>> orders = new ArrayList<CommonNotifyPacket<Order>>();
		//orders.setType("statusList");
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Order List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
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
				List<CommonNotifyPacket<Order>> list = xhsOrderService.getOrderCancelListByStatus(status.trim(), startTime, endTime);
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

	@Override
	protected String getType() {
		return "ORDER";
	}


}
