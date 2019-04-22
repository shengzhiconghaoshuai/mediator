/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SpringCacheWrapper.java
 * 描述： 
 */
package org.apache.shiro.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.support.SimpleValueWrapper;

/**
 * @description spring缓存包装
 * @author yejunwu123@gmail.com
 * @since 2015年8月21日 下午5:38:13
 */
public class SpringCacheWrapper implements Cache<Object, Object> {
	
	private org.springframework.cache.Cache springCache;
	
	public SpringCacheWrapper(org.springframework.cache.Cache cache) {
		this.springCache = cache;
	}

	@Override
	public Object get(Object key) throws CacheException {
		Object value = springCache.get(key);
		if (value instanceof SimpleValueWrapper) {
			return ((SimpleValueWrapper)value).get();
		}
		return value;
	}

	@Override
	public Object put(Object key, Object value) throws CacheException {
		springCache.put(key, value);
		return value;
	}

	@Override
	public Object remove(Object key) throws CacheException {
		springCache.evict(key);
		return null;
	}

	@Override
	public void clear() throws CacheException {
		springCache.clear();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int size() {
		if (springCache.getNativeCache() instanceof com.google.common.cache.Cache) {
			return (int)((com.google.common.cache.Cache)springCache).size();
		}
		throw new UnsupportedOperationException("invoke spring cache abstract size method not support");
	}

	@Override
	public Set<Object> keys() {
		if (springCache.getNativeCache() instanceof com.google.common.cache.Cache) {
			@SuppressWarnings("unchecked")
			ConcurrentMap<Object, Object> map = ((com.google.common.cache.Cache<Object, Object>)springCache).asMap();
			return map.keySet();
		}
		throw new UnsupportedOperationException("invoke spring cache abstract keys method not support");
	}

	@Override
	public Collection<Object> values() {
		if (springCache.getNativeCache() instanceof com.google.common.cache.Cache) {
			@SuppressWarnings("unchecked")
			ConcurrentMap<Object, Object> map = ((com.google.common.cache.Cache<Object, Object>)springCache).asMap();
			return map.values();
		}
		throw new UnsupportedOperationException("invoke spring cache abstract values method not support");
	}

}
