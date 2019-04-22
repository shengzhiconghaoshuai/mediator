package net.chinacloud.mediator.kaola.task.skuBasic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class SkuMappingListTask extends SkuTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(SkuMappingListTask.class);

	private static final String SKU_MAPPINGLIST_TYPE = "skuMappingList";
	
	private static final int TASK_INTERVAL_BUFFER = 60;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER*168 ;
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;
	
	
	static {
		TaskManager.registTask(SKU_TYPE, SKU_MAPPINGLIST_TYPE, SkuMappingListTask.class);
	}
	
	protected String getSubType() {
		return SKU_MAPPINGLIST_TYPE;
	}

	public void doTask() throws Exception {
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("SkuMapping List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
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
		
		List<Product> products =new ArrayList<Product>();
		List<Sku> skuInfos = new ArrayList<Sku>(); //经过判空过后的接收集合
		ProductService productService = getService(ProductService.class, getContext().getChannelCode());
		String statusStr = (String)getContext().get(Constant.SCHEDULE_PARAM_STATUS);
		if(StringUtils.hasLength(statusStr)){
			for(String status : statusStr.split(",")){
				params.put("status", status);
				List<Product> productInfos = productService.getOnSaleProducts(params);
				products.addAll(productInfos);
			}
		}else{
			params.put("status", "5");
			products = productService.getOnSaleProducts(params);
		}
		
		for (Product po : products) {
			skuInfos.addAll(po.getSkuList());
		}
		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("partnumber mapping skus: {}", skuInfos);//在售商品的sku
		}
		for (Sku p : skuInfos) {
			try {
				CommonNotifyPacket<Sku> skuInfoPacket = new CommonNotifyPacket<Sku>(p,"skuMapping_5");
				Task task = taskManager.generateTask(getContext().getChannelCode(), skuInfoPacket);
				if (null != task) {
					//传递上下文
					task.setContext(getContext());
					taskManager.executeTask(task);
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		
		//更新时间
		lasttimeService.insertOrUpdateLasttime(applicationId, templateId, entDate.getTime());
	}	
}


