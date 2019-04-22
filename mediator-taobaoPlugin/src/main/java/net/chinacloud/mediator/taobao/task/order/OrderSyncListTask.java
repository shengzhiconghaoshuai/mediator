/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSyncListTask.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.task.order;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.dao.OrderSyncJdpDao;
import net.chinacloud.mediator.taobao.service.TaobaoOrderService;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.order.OrderTask;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月13日 下午7:36:36
 */
@Component
@Scope(value="prototype")
public class OrderSyncListTask extends OrderTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderSyncListTask.class);
	//private static Logger STATICLOGGER = LoggerFactory.getLogger("taskStatics");
	
	private static final String ORDER_SYNC_LIST_TYPE = "syncList";
	
	private static final int TASK_INTERVAL_BUFFER = 5;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_SYNC_LIST_TYPE, OrderSyncListTask.class);
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;
	@Autowired
	TaobaoOrderService orderService;
	@Autowired
	TaskTemplateService templateService;

	@Override
	protected String getSubType() {
		return ORDER_SYNC_LIST_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Order Sync List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
		TaskTemplate template = getTemplate();
		int templateId = 0;
		if (null != template) {
			templateId = template.getId();
		}
		
		Date startModified = lasttimeService.getLastime(applicationId, templateId);
		startModified = DateUtil.modify(startModified, -TASK_INTERVAL);
		
		ApplicationService applicationService = SpringUtil.getBean(ApplicationService.class);
		Application application = applicationService.getApplicationById(applicationId);
		
		Date endModified = null;
		
		Registry registry = SpringUtil.getBean(Registry.class);
		String modify = registry.get(TaobaoConstant.KEY_REGISTRY_JDP_SCHEDULETIME_MODIFY);
		if (StringUtils.hasText(modify)) {
			if (Boolean.valueOf(modify.trim())) {
				String increment = registry.get(TaobaoConstant.KEY_REGISTRY_JDP_SCHEDULETIME_INCREMENT);
				if (StringUtils.hasText(increment)) {
					int incrementSecond = Integer.parseInt(increment.trim());
					endModified = DateUtil.modify(startModified, incrementSecond * 1000);
					LOGGER.info("jdp order sync list task use modified time " + DateUtil.format(endModified));
				}
			}
		}
		if (null == endModified) {
			endModified = new Date();
			LOGGER.info("jdp order sync list task use current time " + DateUtil.format(endModified));
		}
		LOGGER.info("jdp sync start time " + DateUtil.format(startModified) + " end time " + DateUtil.format(endModified));
		
		OrderSyncJdpDao orderSyncJdpDao = SpringUtil.getBean(OrderSyncJdpDao.class);
		// 分页倒序从后往前翻页抓取同步数据
		int totalCount = orderSyncJdpDao.count(startModified, endModified, application.getNick()).intValue();
		int totalPage = getTotalPage(totalCount);
		
		LOGGER.info("total count {},total page {}", totalCount, totalPage);
		if (totalPage > 0) {
			getSyncOrders(totalPage, startModified, endModified, application.getNick());
		}
		
		lasttimeService.insertOrUpdateLasttime(applicationId, template.getId(), endModified.getTime());
	}

	private void getSyncOrders(int currentPage, Date startModified, Date endModified, String sellerNick) throws OrderException {
		int offset = (currentPage - 1) * TaobaoConstant.ORDEY_SYNC_PAGE_SIZE;
		int rows = TaobaoConstant.ORDEY_SYNC_PAGE_SIZE;
		
		LOGGER.info("get jdp order data with currentPage {}, sellerNick {}, offset {}, rows {}", currentPage, sellerNick, offset, rows);
		//List<Order> orders = orderService.getSyncOrders(startModified, endModified, sellerNick, offset, rows);
		List<Long> orders = orderService.getSyncTradesIds(startModified, endModified, sellerNick, offset, rows);
		if (CollectionUtil.isNotEmpty(orders)) {
			LOGGER.info("get jdp orders: {}", StringUtils.join(orders, ","));
		} else {
			LOGGER.info("get empty jdp orders");
		}
		
		//genetate task
		//TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
		Task task = SpringUtil.getBean(OrderSyncTask.class);
		task.setDataId(String.valueOf(System.nanoTime()));
		task.setData(orders);
		
		TaskTemplateService taskTemplateService = SpringUtil.getBean(TaskTemplateService.class);
		TaskTemplate template = taskTemplateService.getTaskTemplateByTypeAndSubType("ORDER", "sync");
		task.setTemplate(template);
		
		task.setContext(getContext());
		
		try {
			//taskManager.executeTask(task);
			ThreadPoolExecutor executor = SpringUtil.getBean("jdpThreadPool", ThreadPoolExecutor.class);
			executor.execute(task);
		} catch (Exception e) {
			//e.printStackTrace();
			String tids = JsonUtil.object2JsonString(orders);
			LOGGER.error("jdp trade task处理失败,tids:" + tids, e);
			MailSendUtil.sendEmail("jdp trade task处理失败", tids);
		}
		
		if (currentPage <= 1) {		// 从后往前翻页,已经处理到最后一页,不再处理
			return;
		} else {
			//int newCount = orderSyncJdpDao.count(startModified, endModified, sellerNick);
			getSyncOrders(--currentPage, startModified, endModified, sellerNick);
		}
	}
	
	/**
	 * 计算总页数
	 * @param totalCount
	 * @return
	 */
	private int getTotalPage(int totalCount) {
		return ((totalCount % TaobaoConstant.ORDEY_SYNC_PAGE_SIZE == 0) ? (totalCount / TaobaoConstant.ORDEY_SYNC_PAGE_SIZE) 
				: (totalCount / TaobaoConstant.ORDEY_SYNC_PAGE_SIZE + 1));
	}

}
