/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：PluginInit.java
 * 描述： 插件初始化接口定义
 */
package net.chinacloud.mediator.init;

import java.util.Map;

import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.task.TaskAdapter;

/**
 * <插件初始化接口定义>
 * <插件初始化接口定义>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
public interface PluginInit {
	/**
	 * 注册连接器
	 * @return
	 */
	public <E> Class<? extends Connector<E>> registConnector();
	/**
	 * 注册渠道
	 * @return 渠道标识,eg:TAOBAO、JINGDONG
	 */
	public String registChannel();
	/**
	 * 注册task adapter
	 * @return
	 */
	public TaskAdapter registTaskAdapter();
	/**
	 * 注册业务实现类
	 * key:接口clazz
	 * value:实现类clazz
	 * @return
	 */
	public Map<Class<?>, Class<?>> registService();
	/**
	 * 注册数据类型转换器
	 * @return
	 */
	public DataTranslator registDataTranslator();
}
