/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MemCache.java
 * 描述： 
 */
package net.chinacloud.mediator.cache.memcache;

import java.util.HashSet;
import java.util.Set;

import net.spy.memcached.MemcachedClient;

/**
 * @description memcache的缓存区块
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 上午10:48:48
 */
public class MemCache {

	private Set<String> keySet = new HashSet<String>();
	/**缓存区块名称*/
	private final String name;
	/**过期时间*/
	private final int expire;
	/**memcached客户端*/
	private final MemcachedClient memcachedClient;

	public MemCache(String name, int expire, MemcachedClient memcachedClient) {
	    this.name = name;
	    this.expire = expire;
	    this.memcachedClient = memcachedClient;
	}
	
	public String getName() {
		return this.name;
	}

	public Object get(String key) {
	    Object value = null;
	    
    	key = this.getKey(key);
    	value = memcachedClient.get(key);
	   
	    return value;
	}

	public void put(String key, Object value) {
	    if (value == null) {
	    	return;
	    }

    	key = this.getKey(key);
    	memcachedClient.set(key, expire, value);
    	keySet.add(key);
	}

	public void clear() {
		for (String key : keySet) {
			memcachedClient.delete(this.getKey(key));
	    }
		keySet.clear();
	}

	public void delete(String key) {
	    key = this.getKey(key);
	    memcachedClient.delete(key);
	    keySet.remove(key);
	}

	private String getKey(String key) {
	    return name + "_" + key;
	}
}
