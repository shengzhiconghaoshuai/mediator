/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：PluginInitPostProcessor.java
 * 描述： 处理各渠道的初始化信息
 */
package net.chinacloud.mediator.init;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.exception.SystemException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.TaskAdapter;
import net.chinacloud.mediator.task.cache.TaskAdapterManager;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * <处理各渠道的初始化信息>
 * <处理各渠道的初始化信息,如注册connector,注册渠道标识,注册转换器等>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
@Component
public class PluginInitPostProcessor implements BeanPostProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginInitPostProcessor.class);
	
	private int pluginNumber = 1;
	
	@Autowired
	ApplicationService applicationService;
	@Autowired
	ChannelService channelService;
	@Autowired
	ConnectorManager<?> connectorManager;
	@Autowired
	ServiceManager serviceManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	@Autowired
	TaskAdapterManager taskAdapterManager;

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		//do nothing
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if(bean instanceof PluginInit){
			PluginInit pluginInit = (PluginInit)bean;
			String channelCode = pluginInit.registChannel();
			
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("===================插件 " + pluginNumber + " [" + channelCode + "]初始化...=======================");
			}
			
			//regist connector(eg:TaobaoConnector、JingDongConnector)
			registConnector(channelCode, pluginInit);
			//regist TaskAdapter(eg:TaobaoTaskAdapter、JingDongTaskAdapter)
			registTaskAdapter(channelCode, pluginInit);
			//regist service impl
			registService(channelCode, pluginInit);
			//regist data translator
			registDataTranslator(channelCode, pluginInit);
			
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("插件[" + pluginNumber + "]" + channelCode + "完成初始化...");
				pluginNumber++;
			}
		}
		return bean;
	}
	
	private void registDataTranslator(String channelCode, PluginInit pluginInit) {
		DataTranslator translator = pluginInit.registDataTranslator();
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("初始化[" + channelCode + "]渠道的数据转换器[" + translator + "]");
		}
		
		if(null != translator) {
			dataTranslatorManager.registDataTranslator(channelCode, translator);
		}
	}

	private void registService(String channelCode, PluginInit pluginInit) {
		Map<Class<?>, Class<?>> serviceMap = pluginInit.registService();
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("初始化[" + channelCode + "]渠道的Service实现[" + serviceMap + "]");
		}
		
		if(null != serviceMap) {
			serviceManager.registService(channelCode, serviceMap);
		}
	}

	@SuppressWarnings("rawtypes")
	private void registConnector(String channelCode, PluginInit pluginInit) {
		Class<?> clazz = pluginInit.registConnector();
		if(null != clazz){
			//向缓存中注册连接器
			//ConnectorCache connectorCache = SpringUtil.getBean(ConnectorCache.class);
			Channel channel = channelService.getChannelByCode(channelCode);
			//获取渠道对应的应用
			if (null != channel) {
				List<Application> applications = applicationService.getApplicationsByChannelId(channel.getId());
				if (CollectionUtil.isNotEmpty(applications)) {
					for(Application application : applications) {
						if(application.getStatus() == 1) {
							if(LOGGER.isDebugEnabled()) {
								LOGGER.debug("初始化[" + channelCode + "] 渠道 [" + application.getCode() + "] 应用的连接器");
							}
							
							Constructor constructor = null;
							try {
								constructor = clazz.getConstructor(new Class[]{ApplicationParam.class});
							} catch (Exception e) {
								//e.printStackTrace();
								LOGGER.error("连接器初始异常", e);
								throw new SystemException("exception.connector.initial.err", new Object[]{application.getId(),application.getChannelId()});
							}
							try {
								//connectorCache.put(application.getCode(), constructor.newInstance(application.getParam()));
								connectorManager.registConnector(application.getCode(), constructor.newInstance(application.getParam()));
							} catch (Exception e) {
								//e.printStackTrace();
								LOGGER.error("连接器注册异常", e);
								throw new SystemException("exception.connector.initial.err", new Object[]{application.getId(),application.getChannelId()});
							}
						}
					}
				}
			}
		}
	}
	
	private void registTaskAdapter(String channelCode, PluginInit pluginInit){
		TaskAdapter adapter = pluginInit.registTaskAdapter();
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("初始化[" + channelCode + "]渠道的TaskAdapter[" + adapter + "]");
		}
		
		//向缓存中注册适配器
		/*TaskAdapterCache taskAdapterCache = SpringUtil.getBean(TaskAdapterCache.class);
		taskAdapterCache.put(channelCode, adapter);*/
		
		if(null != adapter){
			taskAdapterManager.registTaskAdapter(channelCode, adapter);
		}
	}
}
