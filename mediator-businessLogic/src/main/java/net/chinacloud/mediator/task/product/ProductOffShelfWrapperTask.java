/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductOffShelfWrapperTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.CollectionUtil;

/**
 * @description 商品下架包装task
 * @author yejunwu123@gmail.com
 * @since 2015年7月3日 下午4:47:43
 */
@Component
@Scope(value="prototype")
public class ProductOffShelfWrapperTask extends ProductTask {

	private static final String PRODUCT_OFF_SHELF_WRAPPER_TYPE = "offShelfWrapper";
	
	@Autowired
	private TaskManager taskManager;
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_OFF_SHELF_WRAPPER_TYPE, ProductOffShelfWrapperTask.class);
	}
	
	@Override
	protected String getSubType() {
		return PRODUCT_OFF_SHELF_WRAPPER_TYPE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doTask() throws ApplicationException {
		List<Product> products = (List<Product>)getData();
		
		if (!CollectionUtil.isEmpty(products)) {
			for (Product product : products) {
				CommonNotifyPacket<Product> packet = new CommonNotifyPacket<Product>(product);
				packet.setType("offShelf");
				
				Task task = taskManager.generateTask(getContext().getChannelCode(), packet);
				
				if (null != task) {
					task.setContext(getContext());
					taskManager.executeTask(task);
				}
			}
		}
	}

}
