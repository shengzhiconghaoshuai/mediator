/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelService.java
 * 描述： 渠道业务类
 */
package net.chinacloud.mediator.system.channel.service;

import java.util.List;

import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.system.channel.dao.ChannelDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
/**
 * <渠道业务类>
 * <渠道业务类>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月23日
 * @since 2014年12月23日
 */
@Service
public class ChannelService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelService.class);
	
	@Autowired
	private ChannelDao channelDao;
	
	/**
	 * 获取所有渠道信息
	 * @return
	 */
	public List<Channel> getAllChannels() {
		return channelDao.getAllChannels();
	}
	
	/**
	 * 获取启用状态的渠道信息
	 * @return
	 */
	public List<Channel> getAvailableChannels() {
		return channelDao.getChannelsByStatus(1);
	}
	
	/**
	 * 保存渠道
	 * @param channel
	 */
	public void save(Channel channel) {
		channelDao.save(channel);
	}
	
	/**
	 * 根据渠道id获取渠道信息,并添加到缓存
	 * @param channelId
	 * @return
	 */
	@Caching(cacheable={
			@Cacheable(value="channel", key="#channelId", unless="#result == null")
	})
	public Channel getChannelById(Integer channelId) {
		LOGGER.info("从DB中获取channel信息,channelId=" + channelId);
		return channelDao.getChannelById(channelId);
	}
	
	/**
	 * 根据渠道标识获取渠道信息
	 * @param channelCode
	 * @return
	 */
	@Caching(cacheable={
			@Cacheable(value="channel", key="#channelCode", unless="#result == null")
	})
	public Channel getChannelByCode(String channelCode) {
		try {
			return channelDao.getChannelByCode(channelCode);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 更新渠道信息
	 * @param channel
	 */
	@Caching(evict={
			@CacheEvict(value="channel", key="#channel.id"),
			@CacheEvict(value="channel", key="#channel.code")
	})
	public void update(Channel channel) {
		channelDao.updateChannel(channel);
	}
}
