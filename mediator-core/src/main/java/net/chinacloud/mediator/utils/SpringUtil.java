/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SpringUtil.java
 * 描述： Spring相关工具类
 */
package net.chinacloud.mediator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <Spring相关工具类>
 * <用于从spring容器中获取配置的bean>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public final class SpringUtil implements ApplicationContextAware{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringUtil.class);
	
	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		LOGGER.info("spring container start,set appliationContext for Spring util...");
		context = applicationContext;
	}
	
	/**
	 * 根据bean的id获取bean
	 * @param beanId
	 * @return
	 */
	public static Object getBean(String beanId){
		return context.getBean(beanId);
	}
	
	/**
	 * 根据指定类型获取bean
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz){
		return context.getBean(clazz);
	}
	
	/**
	 * 根据bean的id获取bean,并转换成clazz对应的类型
	 * @param beanId
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(String beanId, Class<T> clazz){
		return context.getBean(beanId, clazz);
	}
}
