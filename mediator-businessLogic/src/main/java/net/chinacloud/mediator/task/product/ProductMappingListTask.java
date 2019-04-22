package net.chinacloud.mediator.task.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.service.ProductPartnumberMappingService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2015年12月8日 下午2:09:21
 */
@Component
@Scope(value="prototype")
public class ProductMappingListTask extends ProductTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductMappingListTask.class);

	private static final String PRODUCT_MAPPINGLIST_TYPE = "mappingList";
	
	private static final int TASK_INTERVAL_BUFFER = 60;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER * 168;
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	ProductPartnumberMappingService productPartnumberMappingService;
	
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	Registry registry;
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_MAPPINGLIST_TYPE, ProductMappingListTask.class);
	}
	
	@Override
	protected String getSubType() {
		return PRODUCT_MAPPINGLIST_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("ProductMapping List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
		TaskTemplate template = getTemplate();
		int templateId = 0;
		if (null != template) {
			templateId = template.getId();
		}
		
		Date lastDate = lasttimeService.getLastime(applicationId, templateId);
		Date startDate = DateUtil.modify(lastDate, -TASK_INTERVAL);
		if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("-------startDate:" + DateUtil.format(startDate) + "-------");
    	}
		
		Date entDate = new Date();
		if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("-------endDate:" + DateUtil.format(entDate) + "-------");
    	}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", entDate);
		
		ProductService productService = getService(ProductService.class, getContext().getChannelCode());
		List<Product> productInfos = productService.getTotalProducts(params);
		
		List<Product> checkedProductInfos = new ArrayList<Product>(); //经过判空过后的接收集合
		
		for (Product po : productInfos) {
			String outId = po.getOuterProductId();
			String wareId = po.getChannelProductId();
			
			if (!StringUtils.hasText(outId) || !StringUtils.hasText(wareId)) { //如果有一个为空直接跳过.忽略
				continue;
			}
			checkedProductInfos.add(po);
		}
		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("partnumber mapping products: {}", checkedProductInfos);
		}
		
		if (!CollectionUtil.isEmpty(checkedProductInfos)) {
			//是否处理上下架标记
			String processShelfStatusStr = registry.get("processShelfStatus");

			for (Product p : checkedProductInfos) {
				try {
					CommonNotifyPacket<Product> productInfoPacket = new CommonNotifyPacket<Product>(p,"mapping");
					Task task = taskManager.generateTask(getContext().getChannelCode(), productInfoPacket);
					
					if (null != task) {
						//传递上下文
						task.setContext(getContext());
						if (StringUtils.hasText(processShelfStatusStr)) {
							task.getContext().put("processShelfStatus", processShelfStatusStr);
						}
						taskManager.executeTask(task);
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		
		//更新时间
		lasttimeService.insertOrUpdateLasttime(applicationId, templateId, entDate.getTime());
	}	
}
