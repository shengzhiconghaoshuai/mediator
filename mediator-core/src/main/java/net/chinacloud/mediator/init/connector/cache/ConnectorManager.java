/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ConnectorManager.java
 * 描述： 连接管理器
 */
package net.chinacloud.mediator.init.connector.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.utils.ContextUtil;

/**
 * <连接管理器>
 * <连接管理器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月31日
 * @since 2014年12月31日
 */
@Component
public final class ConnectorManager<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorManager.class);
	
	@Autowired
	@Qualifier(value="guavaCacheManager")
	CacheManager cacheManager;
	
	@SuppressWarnings("unchecked")
	public Connector<T> getConnector() {
		//TODO 貌似只能从ThreadLocal中取得application code
		String applicationCode = ContextUtil.get(Constant.APPLICATION_CODE);
		return (Connector<T>)(cacheManager.getCache("connector").get(applicationCode).get());
	}
	
	/**
	 * 注册连接器
	 * @param key
	 * @param value
	 */
	public void registConnector(String applicationCode, Object connector) {
		LOGGER.info("向[connector]缓存中注册 " + applicationCode + " 应用的连接器");
		cacheManager.getCache("connector").put(applicationCode, connector);
	}
}
