/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductPropertyGetTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product;

import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月5日 下午4:15:39
 */
@Component
@Scope(value="prototype")
public class ProductPropertyGetTask extends ProductTask {
	
	private static final String PRODUCT_PROPERTY_GET_TYPE = "propertyGet";
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_PROPERTY_GET_TYPE, ProductPropertyGetTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;

	@Override
	protected String getSubType() {
		return PRODUCT_PROPERTY_GET_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		ProductService productService = getService(ProductService.class, getContext().getChannelCode());
		Category category = (Category)getData();
		
		try {
			productService.getChannelProperty(category);
			productFacadeClient.sendChannelPropertyResponse(category, context, getId(), true, null);
		} catch (MessageSendException e) {
			throw e;
		} catch (Exception e1) {
			//e1.printStackTrace();
			productFacadeClient.sendChannelPropertyResponse(category, context, getId(), false, e1.getMessage());
			throw e1;
		}
	}
	
}
