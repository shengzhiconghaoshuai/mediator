/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskAdapterManager.java
 * 描述： task适配器管理
 */
package net.chinacloud.mediator.task.cache;

import net.chinacloud.mediator.task.TaskAdapter;
import net.chinacloud.mediator.task.TaskManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * <task适配器管理>
 * <task适配器管理>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月4日
 * @since 2015年1月4日
 */
@Component
public class TaskAdapterManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);
	
	@Autowired
	@Qualifier(value="guavaCacheManager")
	CacheManager cacheManager;
	
	public void registTaskAdapter(String channelCode, TaskAdapter taskAdapter) {
		LOGGER.info("向[taskAdapter]缓存中注册 " + channelCode + "渠道的TaskAdapter");
		cacheManager.getCache("taskAdapter").put(channelCode, taskAdapter);
	}
	
	public TaskAdapter getTaskAdapter(String channelCode) {
		return (TaskAdapter)(cacheManager.getCache("taskAdapter").get(channelCode).get());
	}
}
