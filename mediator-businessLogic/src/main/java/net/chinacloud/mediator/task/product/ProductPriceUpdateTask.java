/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductPriceUpdateTask.java
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
 * @description 商品、sku价格更新task
 * @author yejunwu123@gmail.com
 * @since 2015年7月3日 下午5:15:24
 */
@Component
@Scope(value="prototype")
public class ProductPriceUpdateTask extends ProductTask {

	private static final String PRODUCT_PRICE_UPDATE_TYPE = "priceUpdate";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceUpdateTask.class);
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_PRICE_UPDATE_TYPE, ProductPriceUpdateTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	
	@Override
	protected String getSubType() {
		return PRODUCT_PRICE_UPDATE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Product product = (Product)getData();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("update price product info:" + product);
		}
		
		if (null != product) {
			try {
				ProductService productService = getService(ProductService.class, getContext().getChannelCode());
				productService.updatePrice(product);
				
				//success
				productFacadeClient.priceUpdatefResponse(product, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				//e1.printStackTrace();
				// 发送失败信息
				String errorMessage = e1.getMessage();
				productFacadeClient.priceUpdatefResponse(product, context, this.id, false, errorMessage);
				throw new ProductException("exception.product.price.update.fail", errorMessage);
			}
		}
	}

}
