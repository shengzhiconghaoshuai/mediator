package net.chinacloud.mediator.task.product;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.JsonUtil;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2017年9月12日 下午4:01:10
 */
@Component
@Scope(value="prototype")
public class ProductItemStoreBandingTask extends ProductTask{
	
	private static final String PRODUC_TITEMSTORE_BANDING_TYPE = "itemStoreBanding";

	@Override
	protected String getSubType() {
		return PRODUC_TITEMSTORE_BANDING_TYPE;
	}
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUC_TITEMSTORE_BANDING_TYPE, ProductItemStoreBandingTask.class);
	}

	@Override
	public void doTask() throws Exception {
		Object data = this.getData();
		Product product = null;
		
		if (data instanceof Product) {
			product = (Product)this.getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				product = JsonUtil.jsonString2Object(strData, Product.class);
			} 
		}
		ProductService productService = getService(ProductService.class, getContext().getChannelCode());
		productService.BindItemStore(product);
	}
}
