/**
 * 文件名：VopCommonTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.task.Task;

/**
 * <TODO simple description>
 * <TODO detail description>
 * @author mwu@wuxicloud.com
 * @version 0.0.0,2015年2月26日
 * @since 2015年2月26日
 */
@Component
@Scope(value="prototype")
public abstract class VopCommonTask extends Task{
	private static final Logger LOGGER = LoggerFactory.getLogger(VopCommonTask.class);
	
	@Autowired
	private ServiceManager serviceManager;
	
	/**
	 * 获取渠道对应的业务接口实现
	 * @param clazz 业务接口
	 * @param channelCode 渠道标识
	 * @return
	 */
	protected <T> T getService(Class<T> clazz, String channelCode){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("get service " + clazz.getSimpleName() + " for channel " + channelCode);
		}
		return serviceManager.getService(clazz, channelCode);
	}
}
