/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ApplicationService.java
 * 描述： 应用管理
 */
package net.chinacloud.mediator.system.application.service;

import java.util.List;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.system.application.dao.ApplicationDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * <应用管理>
 * <应用管理>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月26日
 * @since 2014年12月26日
 */
@Service
public class ApplicationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);
	
	@Autowired
	private ApplicationDao applicationDao;
	
	/**
	 * 根据应用id获取应用信息
	 * @param applicationId 应用id
	 * @return
	 */
	@Caching(cacheable={
			@Cacheable(value="application", key="#applicationId", unless="#result == null")
	})
	public Application getApplicationById(Integer applicationId){
		LOGGER.info("从DB中获取application信息,applicationId=" + applicationId);
		return applicationDao.getApplicationById(applicationId);
	}
	
	/**
	 * 保存渠道信息
	 * @param application
	 */
	public void save(Application application) {
		applicationDao.save(application);
	}
	
	/**
	 * 获取所有应用列表
	 * @return
	 */
	public List<Application> getAllApplications(){
		return applicationDao.getAllApplications();
	}
	
	/**
	 * 更新应用信息
	 * @param application
	 */
	@Caching(evict={
			@CacheEvict(value="application", key="#application.id"),
			@CacheEvict(value="application", key="'store_'.concat(#application.storeId)")
	})
	public void update(Application application) {
		applicationDao.updateApplication(application);
	}
	
	/**
	 * 根据渠道id获取对应的应用列表
	 * @param channelId 应用对应的渠道id
	 * @return
	 */
	public List<Application> getApplicationsByChannelId(Integer channelId) {
		return applicationDao.getApplicationsByChannelId(channelId);
	}
	
	/**
	 * 根据渠道id获取开启状态的应用列表
	 * @param channelId 应用对应的渠道id
	 * @return
	 */
	public List<Application> getAvailableApplicationsByChannelId(Integer channelId) {
		return applicationDao.getApplicationsByChannelId(channelId, 1);
	}
	
	/**
	 * 根据store id获取对应的应用信息
	 * @param storeId
	 * @return
	 */
	@Caching(cacheable={
			@Cacheable(value="application", key="'store_'.concat(#storeId)", unless="#result == null")
	})
	public Application getApplicationByStoreId(Integer storeId) {
		Application application = null;
		try {
			application = applicationDao.getApplicationByStoreId(storeId);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error(e.getMessage());
			application = null;
		}
		return application;
	}
}
