/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ServiceCache.java
 * 描述： 缓存各渠道业务实现类
 */
package net.chinacloud.mediator.service.cache;

import java.util.Map;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;

/**
 * <缓存各渠道业务实现类>
 * <如各渠道的OrderServiceImpl、ProductServiceImpl等>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月30日
 * @since 2014年12月30日
 */
public class ServiceCache implements Cache {
	
	private com.google.common.cache.Cache<String, Map<Class<?>, Class<?>>> serviceCache = null;

	public ServiceCache(){
		serviceCache = CacheBuilder.newBuilder()
							.initialCapacity(20)
							.build();
	}
	
	@Override
	public String getName() {
		return "service";
	}

	@Override
	public Object getNativeCache() {
		return serviceCache;
	}

	@Override
	public ValueWrapper get(Object key) {
		return new SimpleValueWrapper(serviceCache.getIfPresent(key));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void put(Object key, Object value) {
		serviceCache.put((String)key, (Map<Class<?>, Class<?>>)value);
	}

	@Override
	public void evict(Object key) {
		//do nothing
	}

	@Override
	public void clear() {
		//do nothing
	}

}
