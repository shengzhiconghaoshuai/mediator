/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ApplicationCache.java
 * 描述： 应用缓存
 */
package net.chinacloud.mediator.system.application.cache;

import net.chinacloud.mediator.domain.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;

/**
 * <应用缓存>
 * <应用缓存>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月17日
 * @since 2014年12月17日
 */
public class ApplicationCache implements Cache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCache.class);
	
	//private LoadingCache<String, Application> applicationCache = null;
	
	private com.google.common.cache.Cache<Integer, Application> applicationCache = null;
	
	/*public ApplicationCache(final ApplicationDao applicationDao){
		applicationCache = CacheBuilder.newBuilder()
							.initialCapacity(20)
							.build(new CacheLoader<String, Application>() {

								@Override
								public Application load(String applicationCode) {
									LOGGER.debug("--load application " + applicationCode + " from db");
									return applicationDao.getApplicationByCode(applicationCode);
								}
								
							});
	}*/
	
	public ApplicationCache() {
		applicationCache = CacheBuilder.newBuilder()
							.initialCapacity(50)
							.build();
	}

	public String getName() {
		return "application";
	}

	public Object getNativeCache() {
		return applicationCache;
	}

	public ValueWrapper get(Object key) {
		LOGGER.debug("get application " + key + " from application cache");
		Object value = applicationCache.getIfPresent(key);
		return (value != null ? new SimpleValueWrapper(value) : null);
	}

	public void put(Object key, Object value) {
		applicationCache.put((Integer)key, (Application)value);
	}

	public void evict(Object key) {
		applicationCache.invalidate(key);
	}

	public void clear() {
		applicationCache.invalidateAll();
	}

	/**
	 * 根据渠道id获取渠道对应的application
	 * @param channelId 渠道id
	 * @return application列表
	 */
	/*public List<Application> getApplicationsByChannelId(Integer channelId){
		List<Application> applicationList = new ArrayList<Application>();
		ConcurrentMap<String, Application> applicationMap = applicationCache.asMap();
		for(Map.Entry<String, Application> entry : applicationMap.entrySet()){
			Application application = entry.getValue();
			if(channelId.intValue() == application.getChannelId().intValue()){
				applicationList.add(application);
			}
		}
		return applicationList;
	}*/
}
