/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SpringCacheManagerWrapper.java
 * 描述： 
 */
package org.apache.shiro.cache;

/**
 * @description spring缓存管理器包装
 * @author yejunwu123@gmail.com
 * @since 2015年8月21日 下午5:36:52
 */
public class SpringCacheManagerWrapper implements CacheManager {
	
	private org.springframework.cache.CacheManager cacheManager;
	
	public void setCacheManager(org.springframework.cache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public Cache<Object, Object> getCache(String name) throws CacheException {
		org.springframework.cache.Cache springCache = cacheManager.getCache(name);
		return new SpringCacheWrapper(springCache);
	}

}
