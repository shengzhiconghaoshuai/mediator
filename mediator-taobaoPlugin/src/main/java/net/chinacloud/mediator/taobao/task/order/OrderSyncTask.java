/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSyncTask.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.task.order;

import java.util.List;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.taobao.service.TaobaoOrderService;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;
import net.chinacloud.mediator.task.order.OrderCreateTask;
import net.chinacloud.mediator.task.order.OrderSellerMemoModifiedTask;
import net.chinacloud.mediator.task.order.OrderTask;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.SpringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 订单数据同步task
 * @author yejunwu123@gmail.com
 * @since 2015年7月14日 上午10:27:20
 */
@Component
@Scope(value="prototype")
public class OrderSyncTask extends OrderTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderSyncTask.class);
	//private static Logger STATICLOGGER = LoggerFactory.getLogger("taskStatics");
	
	private static final String ORDER_SYNC_TYPE = "sync";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_SYNC_TYPE, OrderSyncTask.class);
	}
	
	@Autowired
	TaobaoOrderService orderService;

	@Override
	protected String getSubType() {
		return ORDER_SYNC_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		@SuppressWarnings("unchecked")
		List<Long> orders = (List<Long>)getData();
		if (CollectionUtil.isNotEmpty(orders)) {
			TaskTemplateService templateService = SpringUtil.getBean(TaskTemplateService.class);
			//generate task
			TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
			for (Long tid : orders) {
				LOGGER.info("process jdp order: {}", tid);
				Order order = orderService.getJdpOrderById(tid);
				try {
					Task task = null;
					TaskTemplate template = null;
					if (null != order) {
						String type = order.getType();
						String stepStatus = order.getStepStatus();
						if (Order.ORDER_TYPE_STEP.equals(type)) {
							//预售订单
							if (Order.STEP_STATUS_FRONT_PAID_FINAL_NOPAID.equals(stepStatus)) {
								//付定金
								task = SpringUtil.getBean(OrderStepPayTask.class);
								template = templateService.getTaskTemplateByTypeAndSubType(OrderTask.ORDER_TYPE, "stepPay");
							} else if (Order.STEP_STATUS_FRONT_PAID_FINAL_PAID.equals(stepStatus)) {
								//付尾款
								task = SpringUtil.getBean(OrderCreateTask.class);
								template = templateService.getTaskTemplateByTypeAndSubType(OrderTask.ORDER_TYPE, "create");
							}
						} else {
							//普通订单
							String status = order.getStatus();
							if (Order.STATUS_WAIT_SELLER_SEND_GOODS.equals(status)) {
								task = SpringUtil.getBean(OrderCreateTask.class);
								template = templateService.getTaskTemplateByTypeAndSubType(OrderTask.ORDER_TYPE, "create");
							}
						}
					} 
					
					if (null != task) {
						task.setDataId(order.getChannelOrderId());
						task.setData(order);
						task.setTemplate(template);
						task.setContext(getContext());
						
						taskManager.executeTask(task);
					}
				} catch (DuplicateTaskException e) {
					//卖家备注修改
					TaskTemplate orderSellerMemoModifiedTemplate = templateService.getTaskTemplateByTypeAndSubType(OrderTask.ORDER_TYPE, "sellerMemoModify");
					Task task = SpringUtil.getBean(OrderSellerMemoModifiedTask.class);
					task.setDataId(order.getChannelOrderId());
					task.setData(order);
					task.setTemplate(orderSellerMemoModifiedTemplate);
					
					task.setContext(getContext());
					
					try {
						taskManager.executeTask(task);
					} catch (Exception e1) {
						//e1.printStackTrace();
						LOGGER.error("jdp task创建失败[" + order.getChannelOrderId() + "]", e);
						MailSendUtil.sendEmail("jdp task create error", JsonUtil.object2JsonString(order));
					}
				} catch (Exception e) {
					//e.printStackTrace();
					LOGGER.error("jdp order create task创建失败[" + order.getChannelOrderId() + "]", e);
					MailSendUtil.sendEmail("jdp order create task create error", JsonUtil.object2JsonString(order));
				}
			}
		}
	}

}
