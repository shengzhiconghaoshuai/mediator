/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductOnShelfTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;

/**
 * @description 商品上架task
 * @author yejunwu123@gmail.com
 * @since 2015年7月3日 下午4:12:49
 */
@Component
@Scope(value="prototype")
public class ProductOnShelfTask extends ProductTask {
	
	private static final String PRODUCT_ON_SHELF_TYPE = "onShelf";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductOnShelfTask.class);
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_ON_SHELF_TYPE, ProductOnShelfTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	
	@Override
	protected String getSubType() {
		return PRODUCT_ON_SHELF_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Product product = (Product)getData();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("on shelf product info:" + product);
		}
		
		if (null != product) {
			try {
				ProductService productService = getService(ProductService.class, getContext().getChannelCode());
				productService.onShelf(product);
				
				//success
				productFacadeClient.onShelfResponse(product, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				//e1.printStackTrace();
				// 发送失败信息
				String errorMessage = e1.getMessage();
				productFacadeClient.onShelfResponse(product, context, this.id, false, errorMessage);
				throw new ProductException("exception.product.onshelf.fail", errorMessage);
			}
		}
	}

}
