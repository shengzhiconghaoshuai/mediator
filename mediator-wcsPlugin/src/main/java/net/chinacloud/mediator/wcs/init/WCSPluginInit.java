/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：WCSPluginInit.java
 * 描述： 
 */
package net.chinacloud.mediator.wcs.init;

import java.util.Map;

import net.chinacloud.mediator.init.PluginInit;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.task.TaskAdapter;
import net.chinacloud.mediator.wcs.WCSConstant;
import net.chinacloud.mediator.wcs.task.WCSTaskAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class WCSPluginInit implements PluginInit {
	
	@Autowired
	WCSTaskAdapter wcsTaskAdapter;

	@Override
	public <E> Class<? extends Connector<E>> registConnector() {
		return null;
	}

	@Override
	public String registChannel() {
		return WCSConstant.CHANNEL_FLAG_WCS;
	}

	@Override
	public TaskAdapter registTaskAdapter() {
		//return SpringUtil.getBean(WCSTaskAdapter.class);
		return wcsTaskAdapter;
	}

	@Override
	public Map<Class<?>, Class<?>> registService() {
		return null;
	}

	@Override
	public DataTranslator registDataTranslator() {
		return null;
	}

}
