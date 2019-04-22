/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductOffShelfTask.java
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
 * @description 商品下架
 * @author yejunwu123@gmail.com
 * @since 2015年7月3日 下午4:52:51
 */
@Component
@Scope(value="prototype")
public class ProductOffShelfTask extends ProductTask {
	
	private static final String PRODUCT_OFF_SHELF_TYPE = "offShelf";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductOffShelfTask.class);

	@Override
	protected String getSubType() {
		return PRODUCT_OFF_SHELF_TYPE;
	}

	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_OFF_SHELF_TYPE, ProductOffShelfTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	
	@Override
	public void doTask() throws ApplicationException {
		Product product = (Product)getData();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("off shelf product info:" + product);
		}
		
		if (null != product) {
			try {
				ProductService productService = getService(ProductService.class, getContext().getChannelCode());
				productService.offShelf(product);
				
				//success
				productFacadeClient.offShelfResponse(product, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				//e1.printStackTrace();
				// 发送失败信息
				String errorMessage = e1.getMessage();
				productFacadeClient.offShelfResponse(product, context, this.id, false, errorMessage);
				throw new ProductException("exception.product.offshelf.fail", errorMessage);
			}
		}
	}

}
