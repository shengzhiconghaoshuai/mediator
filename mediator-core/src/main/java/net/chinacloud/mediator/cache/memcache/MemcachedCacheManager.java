/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MemcachedCacheManager.java
 * 描述： 
 */
package net.chinacloud.mediator.cache.memcache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.chinacloud.mediator.utils.CollectionUtil;
import net.spy.memcached.MemcachedClient;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

/**
 * @description memcache 管理器
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 上午10:39:30
 */
public class MemcachedCacheManager extends AbstractTransactionSupportingCacheManager {
	
	private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();
	//缓存的时间
	//private ConcurrentMap<String, Integer> expireMap = new ConcurrentHashMap<String, Integer>();
	//spy memcached的客户端
	private MemcachedClient memcachedClient;   

	@Override
	protected Collection<? extends Cache> loadCaches() {
		return cacheMap.values();
	}
	
	public void setCaches(Collection<? extends Cache> caches) {
		if (CollectionUtil.isNotEmpty(caches)) {
			for (Cache cache : caches) {
				cacheMap.put(cache.getName(), cache);
			}
		}
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = cacheMap.get(name);
	    if (cache == null) {
	    	/*Integer expire = expireMap.get(name);
	    	if (expire == null) {
	    		expire = 0;
	    		expireMap.put(name, expire);
	    	}*/
	    	cache = new MemcachedCache(name, 0, memcachedClient);
	    	cacheMap.put(name, cache);
	    }
	    return cache;
	}
}
