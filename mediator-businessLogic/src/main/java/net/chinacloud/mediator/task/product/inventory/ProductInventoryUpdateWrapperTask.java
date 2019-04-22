/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：InventorySingleUpdateWrapperTask.java
 * 描述：product库存同步包装
 */
package net.chinacloud.mediator.task.product.inventory;

import java.util.List;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.product.ProductTask;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <product库存同步包装>
 * <以款作单位,针对每一个product创建一个task>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月19日
 * @since 2015年1月19日
 */
@Component
@Scope(value="prototype")
public class ProductInventoryUpdateWrapperTask extends ProductTask {
	
	private static final String PRODUCT_INVENTORY_UPDATE_WRAPPER_TYPE = "productInventoryUpdateWrapper";
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_INVENTORY_UPDATE_WRAPPER_TYPE, ProductInventoryUpdateWrapperTask.class);
	}
	
	@Autowired
	private TaskManager taskManager;

	@Override
	protected String getSubType() {
		return PRODUCT_INVENTORY_UPDATE_WRAPPER_TYPE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doTask() throws ApplicationException {
		List<Product> products = (List<Product>)getData();
		
		if (!CollectionUtil.isEmpty(products)) {
			for (Product product : products) {
				CommonNotifyPacket<Product> packet = new CommonNotifyPacket<Product>(product);
				packet.setType("productUpdateInventory");
				
				Task task = taskManager.generateTask(getContext().getChannelCode(), packet);
				
				if (null != task) {
					task.setContext(getContext());
					taskManager.executeTask(task);
				}
			}
		}
	}
}
