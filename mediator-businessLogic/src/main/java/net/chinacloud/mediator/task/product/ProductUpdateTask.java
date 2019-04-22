/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductUpdateTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product;

import java.util.Map;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 商品更新task
 * @author yejunwu123@gmail.com
 * @since 2015年7月3日 上午11:01:59
 */
@Component
@Scope(value="prototype")
public class ProductUpdateTask extends ProductTask {
	
	private static final String PRODUCT_UPDATE_TYPE = "update";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductUpdateTask.class);
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_UPDATE_TYPE, ProductUpdateTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;

	@Override
	protected String getSubType() {
		return PRODUCT_UPDATE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Map<String, Object> product = getProduct();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("update product info:" + product);
		}
		
		if (null!=product) {
			try {
				ProductService productService = getService(ProductService.class, getContext().getChannelCode());
				productService.updateProduct(product);
				
				productFacadeClient.updateResponse(product, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				// 发送失败信息
				String errorMessage = e1.getMessage();
				productFacadeClient.updateResponse(product, context, this.id, false, errorMessage);
				throw new ProductException("exception.product.update.fail", errorMessage);
			}
		}
	}

}
