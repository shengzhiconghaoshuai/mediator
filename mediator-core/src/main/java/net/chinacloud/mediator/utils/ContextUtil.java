/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ContextUtil.java
 * 描述： 线程上下文参数
 */
package net.chinacloud.mediator.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.NamedThreadLocal;

/**
 * <线程上下文参数>
 * <有些参数,如渠道id、应用id在task中可以获取,但在service层无法获取到,
 * 因此将必要的参数放置在ThreadLocal中,以使这些参数在线程范围内共享,
 * 没有使用jdk的ThreadLocal,而是使用spring继承的NamedThreadLocal>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月31日
 * @since 2014年12月31日
 */
public final class ContextUtil {
	
	private ContextUtil(){}
	
	/**线程上下文变量,结构为map,因此理论上可以放任何数据*/
	private static final ThreadLocal<Map<String, Object>> CONTEXT = 
			new NamedThreadLocal<Map<String,Object>>("context") {
		protected Map<String,Object> initialValue() {
			return new HashMap<String, Object>(2);
		};
	};
	
	/**
	 * 放入线程上下文范围的参数
	 * @param key
	 * @param value
	 */
	public static void set(String key, Object value) {
		CONTEXT.get().put(key, value);
	}
	
	/**
	 * 从线程范围内获取值
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String key) {
		return (T)CONTEXT.get().get(key);
	}
	
	/**
	 * 删除线程范围的参数
	 */
	public static void clear(){
		CONTEXT.remove();
	}
}
