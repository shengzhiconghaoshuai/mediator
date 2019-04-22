package net.chinacloud.mediator.taobao.task.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.StoreItems;
import net.chinacloud.mediator.taobao.service.TaobaoProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.product.ProductTask;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;
import net.chinacloud.mediator.utils.JsonUtil;
/**
 * @description 调用奇门的接口，对门店的库存进行初始化
 * @author 
 * @since 
 */
@Component
@Scope(value="prototype")
public class ProductStoreInventoryItemInitialTask extends ProductTask {

	private static final String PRODUCT_STORE_INVENTORY_ITEM_INITIAL = "storeInventoryItemInitial";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductStoreInventoryItemInitialTask.class);

	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_STORE_INVENTORY_ITEM_INITIAL, ProductStoreInventoryItemInitialTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	@Autowired
	TaobaoProductService productService;
	
	
	
	@Override
	protected String getSubType() {
		return PRODUCT_STORE_INVENTORY_ITEM_INITIAL;
	}

	@Override
	public void doTask() throws Exception {
		Object data = this.getData();
		StoreItems storeItems = null;
		
		if (data instanceof StoreItems) {
			storeItems = (StoreItems)this.getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				storeItems = JsonUtil.jsonString2Object(strData, StoreItems.class);
			} 
		}
		
		if(storeItems!=null){
			String result = productService.qimenStoreInventoryItemInitial(storeItems);
			LOGGER.info("对门店的库存进行初始化:"+result);;
		}

	}

}
