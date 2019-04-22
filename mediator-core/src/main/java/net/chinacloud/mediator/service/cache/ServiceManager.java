/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ServiceCacheManager.java
 * 描述： 
 */
package net.chinacloud.mediator.service.cache;

import java.util.Map;

import net.chinacloud.mediator.utils.SpringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
@Component
public final class ServiceManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManager.class);
	
	@Autowired
	@Qualifier(value="guavaCacheManager")
	CacheManager cacheManager;
	
	/**
	 * 根据渠道code及业务接口类型获取对应的实现类
	 * @param type
	 * @param channelCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> type, String channelCode) {
		Map<Class<?>, Class<?>> serviceMap = (Map<Class<?>, Class<?>>)(cacheManager.getCache("service").get(channelCode).get());
		Class<T> clazz = (Class<T>)serviceMap.get(type);
		return SpringUtil.getBean(clazz);
	}
	
	/**
	 * 注册业务实现类
	 * @param key
	 * @param value
	 */
	public void registService(String channelCode, Map<Class<?>, Class<?>> value) {
		LOGGER.info("向[service]缓存中注册 " + channelCode + " 渠道的Service实现");
		cacheManager.getCache("service").put(channelCode, value);
	}
}
