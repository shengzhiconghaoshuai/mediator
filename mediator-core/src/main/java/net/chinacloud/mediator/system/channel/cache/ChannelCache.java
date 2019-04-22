/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelCache.java
 * 描述： 渠道相关的缓存
 */
package net.chinacloud.mediator.system.channel.cache;

import net.chinacloud.mediator.domain.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;

/**
 * <渠道相关缓存>
 * <缓存渠道、应用信息>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月16日
 * @since 2014年12月16日
 */
public class ChannelCache implements Cache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelCache.class);
	
	private com.google.common.cache.Cache<Integer, Channel> channelCache = null;
	
	public ChannelCache () {
		channelCache = CacheBuilder.newBuilder()
							.initialCapacity(20)
							.build();
	}
	
	//private final ChannelDao channelDao;
	
	/*public ChannelCache(final ChannelDao channelDao){
		//this.channelDao = channelDao;
		channelCache = CacheBuilder.newBuilder()
							.initialCapacity(10)
							.build(new CacheLoader<String, Channel>() {
				
								@Override
								public Channel load(String channelCode) {
									LOGGER.debug("--load channel " + channelCode + " from db");
									return channelDao.getChannelByCode(channelCode);
								}
								
							});
	}*/

	public String getName() {
		return "channel";
	}

	public Object getNativeCache() {
		return channelCache;
	}

	public ValueWrapper get(Object key) {
		LOGGER.debug("get channel " + key + " from channel cache");
		Object value = channelCache.getIfPresent(key);
		return (value != null ? new SimpleValueWrapper(value) : null);
	}

	public void put(Object key, Object value) {
		channelCache.put((Integer)key, (Channel)value);
	}

	public void evict(Object key) {
		channelCache.invalidate(key);
	}

	public void clear() {
		channelCache.invalidateAll();
	}
	
	/**
	 * 根据渠道代号获取对应的应用
	 * @param channelCode 渠道代号
	 * @return 应用列表
	 */
	/*public List<Application> getApplicationsByChannelCode(String channelCode){
		Channel channel = (Channel)get(channelCode).get();
		ApplicationCache applicationCache = SpringUtil.getBean(ApplicationCache.class);
		return applicationCache.getApplicationsByChannelId(channel.getId());
	}*/
}
