/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductChannelOffShelfTask.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.task.product;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.exception.order.VendorPartnumberNotExistException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.product.ProductTask;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 淘宝/天猫渠道商品下架
 * @author yejunwu123@gmail.com
 * @since 2015年7月4日 上午11:02:35
 */
@Component
@Scope(value="prototype")
public class ProductChannelUpShelfTask extends ProductTask {
	
	private static final String PRODUCT_CHANNEL_OFF_SHELF_TYPE = "productChannelUpShelf";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductChannelUpShelfTask.class);

	static {
		TaskManager.registTask(PRODUCT_TYPE, PRODUCT_CHANNEL_OFF_SHELF_TYPE, ProductChannelUpShelfTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	
	@Override
	protected String getSubType() {
		return PRODUCT_CHANNEL_OFF_SHELF_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Object productObj = getData();
		Product product = null;
		if (productObj instanceof Product) {
			product = (Product)productObj;
		} else if (productObj instanceof String) {
			String strData = (String)productObj;
			char first = strData.charAt(0);
			if ('{' == first) {
				product = JsonUtil.jsonString2Object(strData, Product.class);
			}
		}
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("up shelf product info:" + product);
		}
		
		if (null != product) {
			if (!StringUtils.hasText(product.getOuterProductId())) {
				// 如果款的外部商家编码为空,需要调用接口获取
				ProductService productService = getService(ProductService.class, getContext().getChannelCode());
				String outerId = productService.getOuterProductIdByChannelProductId(product.getChannelProductId());
				if (!StringUtils.hasText(outerId)) {
					throw new VendorPartnumberNotExistException("product", product.getChannelProductId());
				}
				product.setOuterProductId(outerId.trim());
			}
			productFacadeClient.UpShelf(product, context, this.id);
		}
	}

}
