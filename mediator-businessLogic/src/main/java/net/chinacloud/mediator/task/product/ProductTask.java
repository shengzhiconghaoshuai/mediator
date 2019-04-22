/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductTask.java
 * 描述： 商品task
 */
package net.chinacloud.mediator.task.product;

import java.util.Map;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductInfo;
import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <商品相关task>
 * <商品相关task>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
@Component
@Scope(value="prototype")
public abstract class ProductTask extends Task {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductTask.class);
	
	protected static final String PRODUCT_TYPE = "PRODUCT";
	
	@Autowired
	private ServiceManager serviceManager;
	
	@Override
	protected String getType() {
		//商品相关task
		return PRODUCT_TYPE;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 获取商品数据
	 * 商品相关的task,往渠道更新的task没必要重跑
	 * @return
	 */
	protected <T> T getProduct() {
		Object data = getData();
		if (data instanceof Map) {
			return (T)data;
		} else if (data instanceof ProductInfo) {
			return (T)data;
		} else if (data instanceof Product) {
			return (T)data;
		} else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			
			if ('{' == first) {
				return (T)JsonUtil.jsonString2Object(strData, Product.class);
			}
		}
		return null;
	}
	
	/**
	 * 获取渠道对应的业务接口实现
	 * @param clazz 业务接口
	 * @param channelCode 渠道标识
	 * @return
	 */
	protected <T> T getService(Class<T> clazz, String channelCode){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("get service " + clazz.getSimpleName() + " for channel " + channelCode);
		}
		return serviceManager.getService(clazz, channelCode);
	}
}
