/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderReCreateTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.order;

import java.util.List;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.MessageUtils;
import net.chinacloud.mediator.utils.SpringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 订单重新创建
 * 搜索到指定的订单创建task,将其重跑
 * @author yejunwu123@gmail.com
 * @since 2015年8月13日 下午5:52:09
 */
@Component
@Scope(value="prototype")
public class OrderReCreateTask extends OrderTask {
	
	private static final String ORDER_RE_CREATE_TYPE = "reCreate";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderReCreateTask.class);
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_RE_CREATE_TYPE, OrderReCreateTask.class);
	}
	
	@Autowired
	TaskTemplateService templateService;
	@Autowired
	TaskManager taskManager;
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;

	@Override
	protected String getSubType() {
		return ORDER_RE_CREATE_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("re create order info:" + order);
		}
		
		if (null != order) {
			String channelOrderId = order.getChannelOrderId();
			TaskService taskService = SpringUtil.getBean(TaskService.class);
			//订单创建task对应的template
			TaskTemplate orderCreateTemplate = templateService.getTaskTemplateByTypeAndSubType(OrderCreateTask.ORDER_TYPE, OrderCreateTask.ORDER_CREATE_TYPE);
			List<Task> tasks = taskService.find(channelOrderId, orderCreateTemplate, getContext().getApplicationId());
			if (CollectionUtil.isNotEmpty(tasks) && tasks.size() == 1) {
				Task task = tasks.get(0);
				try {
					taskManager.insert(task);
					taskManager.execute(task);
					orderFacadeClient.reCreateResponse(order, context, getId(), true, null);
				} catch (MessageSendException e) {
					throw e;
				} catch (TaskException e) {
					orderFacadeClient.reCreateResponse(order, context, getId(), false, e.getMessage());
					throw e;
				}
			} else {
				// 正常情况下不会执行这个分支,如果执行到这边,要么是因为订单创建的task没有创建,要么是因为task重复创建
				String errorMessage = MessageUtils.getMessage("exception.order.not.create.or.repeat");
				String subject = MessageUtils.getMessage("exception.order.recreate.failure");
				
				MailSendUtil.sendEmail(subject, errorMessage);
				
				orderFacadeClient.reCreateResponse(order, context, getId(), false, errorMessage);
			}
		}
	}

}
