/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：InventoryBatchUpdateTask.java
 * 描述： product更新库存
 */
package net.chinacloud.mediator.task.product.inventory;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.exception.product.InventoryUpdateException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.product.ProductTask;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <product更新库存>
 * <product更新库存,以款为单位,每个product生成一个task>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
@Component
@Scope(value="prototype")
public class ProductInventoryUpdateTask extends ProductTask {
	
	private static final String PRODUCT_INVENTORY_UPDATE_TYPE = "productInventoryUpdate";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductInventoryUpdateTask.class);

	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_INVENTORY_UPDATE_TYPE, ProductInventoryUpdateTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	
	@Override
	protected String getSubType() {
		//product更新库存
		return PRODUCT_INVENTORY_UPDATE_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Product product = (Product)getData();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("update inventory product info:" + product);
		}

		if (null != product) {
			try {
				ProductService productService = getService(ProductService.class, getContext().getChannelCode());
				productService.updateSkuInventoryBatch(product);
				
				productFacadeClient.productInventoryResponse(product, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				// 发送失败信息
				//e1.printStackTrace();
				String errorMessage = e1.getMessage();
				productFacadeClient.productInventoryResponse(product, context, this.id, false, errorMessage);
				throw new InventoryUpdateException(errorMessage);
			}
		}
	}

}
