/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductCreateTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product;

import java.util.Map;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.exception.product.ImageUploadException;
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
 * @description 商品创建task
 * @author yejunwu123@gmail.com
 * @since 2015年7月3日 上午10:45:23
 */
@Component
@Scope(value="prototype")
public class ProductCreateTask extends ProductTask {
	
	private static final String PRODUCT_CREATE_TYPE = "create";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductCreateTask.class);
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_CREATE_TYPE, ProductCreateTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;

	@Override
	protected String getSubType() {
		return PRODUCT_CREATE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Map<String, Object> product = getProduct();
		//ProductInfo product = getProduct();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("create product info:" + product);
		}
		
		if (null != product) {
			try {
				ProductService productService = getService(ProductService.class, getContext().getChannelCode());
				productService.createProduct(product);
				productFacadeClient.createResponse(product, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (ImageUploadException e1) {
				//e1.printStackTrace();
				LOGGER.error("image upload failure", e1);
				String errorMessage = e1.getMessage();
				productFacadeClient.createResponse(product, context, this.id, true, errorMessage);
			} catch (Exception e2) {
				//e2.printStackTrace();
				// 发送失败信息
				String errorMessage = e2.getMessage();
				productFacadeClient.createResponse(product, context, this.id, false, errorMessage);
				throw new ProductException("exception.product.create.fail", errorMessage);
			}
		}
	}

}
