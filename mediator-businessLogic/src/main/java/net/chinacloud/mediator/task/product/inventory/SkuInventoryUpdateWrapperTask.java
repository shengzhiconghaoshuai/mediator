/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：InventoryTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product.inventory;

import java.util.List;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.product.ProductTask;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * 
 * @description sku库存同步包装
 * @author yejunwu123@gmail.com
 * @since 2015年7月3日 下午7:27:56
 */
@Component
@Scope(value="prototype")
public class SkuInventoryUpdateWrapperTask extends ProductTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SkuInventoryUpdateWrapperTask.class);
	
	private static final String SKU_INVENTORY_UPDATE_WRAPPER_TYPE = "skuInventoryUpdateWrapper";

	static {
		TaskManager.registTask(PRODUCT_TYPE, SKU_INVENTORY_UPDATE_WRAPPER_TYPE, SkuInventoryUpdateWrapperTask.class);
	}
	
	@Autowired
	private TaskManager taskManager;
	
	@Override
	protected String getSubType() {
		// 库存
		return SKU_INVENTORY_UPDATE_WRAPPER_TYPE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doTask() throws ApplicationException {
		List<Sku> skus = (List<Sku>)getData();
		
		if (!CollectionUtil.isEmpty(skus)) {
			for (Sku sku : skus) {
					try {
					Product product = new Product();
					product.addSku(sku);
					CommonNotifyPacket<Product> packet = new CommonNotifyPacket<Product>(product);
					packet.setType("skuUpdateInventory");
					
					Task task = taskManager.generateTask(getContext().getChannelCode(), packet);
					
					if (null != task) {
						task.setContext(getContext());
						taskManager.executeTask(task);
					}
				} catch (Exception e) {
					LOGGER.error("update sku "+sku.getOuterSkuId()+"failed"+e.getMessage());
				}
			}
		}
	}

}
