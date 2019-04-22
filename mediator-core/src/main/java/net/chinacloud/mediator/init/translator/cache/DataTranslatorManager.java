/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DataTranslatorManager.java
 * 描述： 
 */
package net.chinacloud.mediator.init.translator.cache;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.utils.ContextUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class DataTranslatorManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataTranslatorManager.class);
	
	@Autowired
	@Qualifier(value="guavaCacheManager")
	CacheManager cacheManager;
	
	/**
	 * 注册数据类型转换器
	 * @param channelCode 渠道标识
	 * @param translator 数据类型转换器
	 */
	public void registDataTranslator(String channelCode, DataTranslator translator) {
		LOGGER.info("向[dataTranslator]缓存注册 " + channelCode + " 渠道的数据转换器");
		cacheManager.getCache("dataTranslator").put(channelCode, translator);
	}
	
	/**
	 * 获取数据类型转换器
	 * @return
	 */
	public DataTranslator getDataTranslator(){
		String channelCode = ContextUtil.get(Constant.CHANNEL_CODE);
		return (DataTranslator)(cacheManager.getCache("dataTranslator").get(channelCode).get());
	}
}
