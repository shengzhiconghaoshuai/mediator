/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：GuavaCache.java
 * 描述： 
 */
package net.chinacloud.mediator.cache.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;

/**
 * @description spring cache 的guava cache实现
 * @author yejunwu123@gmail.com
 * @since 2015年8月21日 上午11:17:11
 */
public class GuavaCache implements Cache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCache.class);
	
	/**缓存名称*/
	private String name;
	
	private com.google.common.cache.Cache<Object, Object> cache = null;
	
	public GuavaCache(String name, int initialCapacity) {
		this.name = name;
		cache = CacheBuilder.newBuilder()
				.initialCapacity(initialCapacity > 0 ? initialCapacity : 20)
				.build();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getNativeCache() {
		return cache;
	}

	@Override
	public ValueWrapper get(Object key) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("get data from " + name + " cache with key " + key);
		}
		Object value = cache.getIfPresent(key);
		return (value != null ? new SimpleValueWrapper(value) : null);
	}

	@Override
	public void put(Object key, Object value) {
		cache.put(key, value);
	}

	@Override
	public void evict(Object key) {
		cache.invalidate(key);
	}

	@Override
	public void clear() {
		cache.invalidateAll();
	}

}
