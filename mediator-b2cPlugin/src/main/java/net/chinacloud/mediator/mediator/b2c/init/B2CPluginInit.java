/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：B2CPluginInit.java
 * 描述： 
 */
package net.chinacloud.mediator.mediator.b2c.init;

import java.util.Map;

import net.chinacloud.mediator.init.PluginInit;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.task.TaskAdapter;

/**
 * @description b2c渠道初始化
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 下午6:18:25
 */
public class B2CPluginInit implements PluginInit {

	@Override
	public <E> Class<? extends Connector<E>> registConnector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String registChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskAdapter registTaskAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<?>, Class<?>> registService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataTranslator registDataTranslator() {
		// TODO Auto-generated method stub
		return null;
	}

}
