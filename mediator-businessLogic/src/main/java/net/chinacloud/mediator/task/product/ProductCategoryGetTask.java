/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductPropertyGetTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product;

import java.util.List;

import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class ProductCategoryGetTask extends ProductTask {
	
	private static final String PRODUCT_CATEGORY_GET_TYPE = "categoryGet";
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_CATEGORY_GET_TYPE, ProductCategoryGetTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;

	@Override
	protected String getSubType() {
		return PRODUCT_CATEGORY_GET_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		ProductService productService = getService(ProductService.class, getContext().getChannelCode());
		List<CategoryList> categoryList = null;
		try {
			categoryList=productService.getAllCategory();
			productFacadeClient.sendChannelCategoryResponse(categoryList, context, getId(), true, null);
		} catch (MessageSendException e) {
			throw e;
		} catch (Exception e1) {
			productFacadeClient.sendChannelCategoryResponse(categoryList, context, getId(), false, e1.getMessage());
			throw e1;
		}
	}
	
}
