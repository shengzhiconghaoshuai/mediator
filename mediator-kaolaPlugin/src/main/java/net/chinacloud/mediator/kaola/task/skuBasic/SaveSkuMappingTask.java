package net.chinacloud.mediator.kaola.task.skuBasic;


import java.util.Date;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.kaola.service.KaoLaSkuMappingSevice;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class SaveSkuMappingTask extends SkuTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(SaveSkuMappingTask.class);

	private static final String SAVE_SKU_MAPPING_TYPE = "saveSkuMapping";
	@Autowired
	private KaoLaSkuMappingSevice kaoLaSkuMappingSevice;
	
	static {
		TaskManager.registTask(SKU_TYPE, SAVE_SKU_MAPPING_TYPE, SaveSkuMappingTask.class);
	}

	@Override
	protected String getSubType() {
		return SAVE_SKU_MAPPING_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Sku skuInfo = getSku();
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("sku Mapping info:" + skuInfo);
		}
		String modifiedTime = DateUtil.format(new Date());
		if (skuInfo != null) {
			kaoLaSkuMappingSevice.saveOrupdateSkuPartnumberMapping(skuInfo,applicationId,modifiedTime);
		}
		
	}

}
