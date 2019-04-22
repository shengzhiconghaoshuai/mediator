/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：JingdongPluginInit.java
 * 描述： 京东渠道初始化
 */
package net.chinacloud.mediator.jingdong.init;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.init.PluginInit;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.jingdong.JingDongConstant;
import net.chinacloud.mediator.jingdong.service.impl.OrderServiceImpl;
import net.chinacloud.mediator.jingdong.service.impl.ProductServiceImpl;
import net.chinacloud.mediator.jingdong.service.impl.RefundServiceImpl;
import net.chinacloud.mediator.jingdong.service.impl.ReturnServiceImpl;
import net.chinacloud.mediator.jingdong.task.JingdongTaskAdapter;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.service.RefundService;
import net.chinacloud.mediator.service.ReturnService;
import net.chinacloud.mediator.task.TaskAdapter;

/**
 * <京东渠道初始化>
 * <京东渠道初始化>
 * @author mwu
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
@Component
public class JingdongPluginInit implements PluginInit{
	@Autowired
	JingdongTaskAdapter jingdongTaskAdapter;
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<JingdongConnector> registConnector() {
		return JingdongConnector.class;
	}

	@Override
	public String registChannel() {
		return JingDongConstant.CHANNEL_JINGDONG;
	}

	@Override
	public TaskAdapter registTaskAdapter() {
		return jingdongTaskAdapter;
	}

	@Override
	public Map<Class<?>, Class<?>> registService() {
		Map<Class<?>, Class<?>> serviceMap = new HashMap<Class<?>, Class<?>>(4);
		serviceMap.put(OrderService.class, OrderServiceImpl.class);
		serviceMap.put(ProductService.class, ProductServiceImpl.class);
		serviceMap.put(RefundService.class, RefundServiceImpl.class);
		serviceMap.put(ReturnService.class, ReturnServiceImpl.class);
		return serviceMap;
	}

	@Override
	public DataTranslator registDataTranslator() {
		return new JingdongDataTranslator();
	}

}
