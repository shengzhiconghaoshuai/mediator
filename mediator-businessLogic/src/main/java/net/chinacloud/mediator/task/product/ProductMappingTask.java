package net.chinacloud.mediator.task.product;

import java.util.Map;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.service.ProductPartnumberMappingService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;
import net.chinacloud.mediator.utils.ContextUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2015年12月14日 下午3:37:46
 */
@Component
@Scope(value="prototype")
public class ProductMappingTask extends ProductTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductMappingTask.class);

	private static final String PRODUCT_MAPPING_TYPE = "mapping";

	@Autowired
	ProductPartnumberMappingService productPartnumberMappingService;
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	
	@Override
	protected String getSubType() {
		return PRODUCT_MAPPING_TYPE;
	}

	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_MAPPING_TYPE, ProductMappingTask.class);
	}
	
	@Override
	public void doTask() throws Exception {
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("ProductMapping Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
		
		Product productInfo = getProduct();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Product Mapping info:" + productInfo);
		}
		
		if (productInfo != null) {
			ProductService productService = getService(ProductService.class, getContext().getChannelCode());
			Map<String,String> code = productService.isRepeatOuterId(productInfo);
 			
 			if (code.isEmpty()) {
 				LOGGER.info("外部商家编码在渠道中不存在, outerId:"+productInfo.getOuterProductId()+", channelProductId:"+productInfo.getChannelProductId());
 			} else {
 				Product p = new Product();
 				p.setChannelProductId(code.get("ChannelProductId"));
 				p.setOuterProductId(code.get("OuterProductId"));
 				
 				productPartnumberMappingService.saveOrUpdateProductPartnumberMapping(p,applicationId,0);
				productFacadeClient.mappingResponse(productInfo, context, this.id, true, null);
 			}
		}
	}
}
