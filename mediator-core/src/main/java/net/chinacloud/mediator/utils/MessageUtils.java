/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MessageUtils.java
 * 描述： 消息工具类
 */
package net.chinacloud.mediator.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;

/**
 * <消息工具类>
 * <消息工具类,提供国际化消息服务>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public final class MessageUtils {
	
	private static MessageSource messageSource;
	
	/**
	 * 根据指定的消息代号和local生成消息
	 * @param code 代号
	 * @param locale local信息
	 * @param defaultMessage 默认消息
	 * @param args 消息中的参数列表
	 * @return
	 */
	public static String getMessage(String code, Locale locale,String defaultMessage, Object... args){
		if(null == messageSource){
			messageSource = SpringUtil.getBean(MessageSource.class);
		}
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}
	/**
	 * 根据指定的消息代号生成消息,使用默认local
	 * @param code 消息代号
	 * @param defaultMessage 默认消息
	 * @param args 消息中的参数
	 * @return
	 */
	public static String getMessage(String code, String defaultMessage,Object... args){
		return getMessage(code, null, defaultMessage, args);
	}
	/**
	 * 根据指定消息代号生成消息,使用默认local
	 * @param code 消息代号
	 * @param args 消息中的参数
	 * @return
	 */
	public static String getMessage(String code, Object... args){
		return getMessage(code, null, args);
	}
}
