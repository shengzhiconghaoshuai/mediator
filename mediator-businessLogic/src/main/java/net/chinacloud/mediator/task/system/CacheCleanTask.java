/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CacheCleanTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.system;

import java.util.Collection;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.cache.guava.GuavaCacheManager;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component
@Scope(value="prototype")
/**
 * <清理缓存>
 * <需要使用@see net.chinacloud.mediator.Constant.CACHE_CACHE_NAME
 * 和 @see net.chinacloud.mediator.Constant.CACHE_CACHE_KEY 两个参数
 * 指定缓存区块的名称和缓存条目的key;如果没有指定缓存区块的名称,则会刷新所有缓存,这点需慎重考虑>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年3月4日
 * @since 2015年3月4日
 */
public class CacheCleanTask extends SystemTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheCleanTask.class);
	
	private static final String CACHE_CLEAN_TYPE = "cacheClean";
	
	static {
		TaskManager.registTask(SYSTEM_TYPE, CACHE_CLEAN_TYPE, CacheCleanTask.class);
	}
	
	@Autowired
	GuavaCacheManager cacheManager;

	@Override
	protected String getSubType() {
		//缓存失效
		return CACHE_CLEAN_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		//清理缓存,这个可以借助quartz的集群功能,生成一个task,在集群的每个节点上跑一个调度,查看调度的状态
		String cacheName = (String)getContext().get(Constant.CACHE_CACHE_NAME);
		
		if (cacheName != null && !"".equals(cacheName.trim())) {
			Object cacheKey = getContext().get(Constant.CACHE_CACHE_KEY);
			
			LOGGER.info("clean specific cache:" + cacheName + " with key:" + cacheKey);
			
			Cache cache = cacheManager.getCache(cacheName);
			cache.evict(cacheKey);
		} else {
			LOGGER.info("clean all configued cache");
			Collection<String> cacheNames = cacheManager.getCacheNames();
			if (null != cacheNames && !cacheNames.isEmpty()) {
				for (String cn : cacheNames) {
					//清空缓存中的条目
					cacheManager.getCache(cn).clear();
				}
			}
		}
	}

}
