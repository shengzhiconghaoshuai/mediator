/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：GuavaCacheManager.java
 * 描述： Google Guava Cache 管理类
 */
package net.chinacloud.mediator.cache.guava;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

/**
 * <Google Guava Cache 管理类>
 * <使用Spring的Cache接口对外提供一致的Cache访问,底层使用Guava Cache实现
 * 提供缓存区块的管理,如获取该缓存管理器管理的所有缓存区块的名称,根据缓存区块的名称获取对应的
 * 缓存区块等>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月16日
 * @since 2014年12月16日
 */
public class GuavaCacheManager extends AbstractCacheManager {

	private Collection<? extends Cache> caches;

	/**
	 * Specify the collection of Cache instances to use for GuavaCacheManager.
	 */
	public void setCaches(Collection<? extends Cache> caches) {
		this.caches = caches;
	}
	
	@Override
	protected Collection<? extends Cache> loadCaches() {
		return this.caches;
	}
}
