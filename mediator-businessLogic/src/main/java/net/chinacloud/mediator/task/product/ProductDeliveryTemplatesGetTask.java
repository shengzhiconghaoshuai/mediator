/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderDeliveryTemplatesGetTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product;

import java.util.List;

import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 获取用户下的所有运费模板
 * @author yejunwu123@gmail.com
 * @since 2015年7月17日 下午7:47:26
 */
@Component
@Scope(value="prototype")
public class ProductDeliveryTemplatesGetTask extends ProductTask {
	
	private static final String PRODUCT_DELIVERY_TEMPLATES_GET_TYPE = "deliveryTemplatesGet";

	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_DELIVERY_TEMPLATES_GET_TYPE, ProductDeliveryTemplatesGetTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	
	@Override
	protected String getSubType() {
		return PRODUCT_DELIVERY_TEMPLATES_GET_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		ProductService productService = getService(ProductService.class, getContext().getChannelCode());
		List<DeliveryTemplate> deliveryTemplates = null;
		try {
			deliveryTemplates = productService.getDeliveryTemplates();
			productFacadeClient.sendDeliveryTemplatesResponse(deliveryTemplates, context, getId(), true, null);
		} catch (MessageSendException e) {
			throw e;
		} catch (Exception e1) {
			//e1.printStackTrace();
			productFacadeClient.sendDeliveryTemplatesResponse(deliveryTemplates, context, getId(), false, e1.getMessage());
			throw e1;
		}
	}

}
