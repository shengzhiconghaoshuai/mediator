/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MemcachedCache.java
 * 描述： 
 */
package net.chinacloud.mediator.cache.memcache;

import net.chinacloud.mediator.utils.ObjectUtil;
import net.spy.memcached.MemcachedClient;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * @description spring 包装过的memcache
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 上午10:42:51
 */
public class MemcachedCache implements Cache {
	
	private MemCache memCache;
	
	public MemcachedCache(String name, int expire, MemcachedClient memcachedClient) {
	    this.memCache = new MemCache(name, expire, memcachedClient);
	}
	
	@Override
	public String getName() {
		return memCache.getName();
	}
	
	@Override
	public Object getNativeCache() {
		return this.memCache;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object value = memCache.get(ObjectUtil.toString(key));
		return (value != null ? new SimpleValueWrapper(value) : null);
	}

	@Override
	public void put(Object key, Object value) {
		memCache.put(ObjectUtil.toString(key), value);
	}

	@Override
	public void evict(Object key) {
		memCache.delete(ObjectUtil.toString(key));
	}

	@Override
	public void clear() {
		memCache.clear();
	}

}
