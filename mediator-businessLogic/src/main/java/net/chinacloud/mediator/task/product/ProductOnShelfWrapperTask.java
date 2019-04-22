/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductOnShelfListTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product;

import java.util.List;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 商品上架包装task
 * @author yejunwu123@gmail.com
 * @since 2015年7月3日 下午4:05:32
 */
@Component
@Scope(value="prototype")
public class ProductOnShelfWrapperTask extends ProductTask {
	
	private static final String PRODUCT_ON_SHELF_WRAPPER_TYPE = "onShelfWrapper";

	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_ON_SHELF_WRAPPER_TYPE, ProductOnShelfWrapperTask.class);
	}
	
	@Autowired
	private TaskManager taskManager;
	
	@Override
	protected String getSubType() {
		return PRODUCT_ON_SHELF_WRAPPER_TYPE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doTask() throws ApplicationException {
		List<Product> products = (List<Product>)getData();
		
		if (!CollectionUtil.isEmpty(products)) {
			for (Product product : products) {
				CommonNotifyPacket<Product> packet = new CommonNotifyPacket<Product>(product);
				packet.setType("onShelf");
				
				Task task = taskManager.generateTask(getContext().getChannelCode(), packet);
				
				if (null != task) {
					task.setContext(getContext());
					taskManager.executeTask(task);
				}
			}
		}
	}

}
