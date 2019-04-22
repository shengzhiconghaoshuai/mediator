package net.chinacloud.mediator.xhs.init;

import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.init.PluginInit;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskAdapter;
import net.chinacloud.mediator.xhs.bean.XhsConstant;
import net.chinacloud.mediator.xhs.serviceImpl.XhsOrderServiceImpl;
import net.chinacloud.mediator.xhs.serviceImpl.XhsProductServiceImpl;
import net.chinacloud.mediator.xhs.task.XhsTaskAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XhsPluginInit implements PluginInit{
	
	@Autowired
	XhsTaskAdapter xhsTaskAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public Class<XhsConnector> registConnector() {
		return XhsConnector.class;
	}

	@Override
	public String registChannel() {
		return XhsConstant.CHANNEL_XHS;
	}

	@Override
	public TaskAdapter registTaskAdapter() {
		return xhsTaskAdapter;
	}

	@Override
	public Map<Class<?>, Class<?>> registService() {
		Map<Class<?>, Class<?>> serviceMap = new HashMap<Class<?>, Class<?>>(4);
		serviceMap.put(OrderService.class, XhsOrderServiceImpl.class);
		serviceMap.put(ProductService.class, XhsProductServiceImpl.class);
		return serviceMap;
	}

	@Override
	public DataTranslator registDataTranslator() {
		return new XhsDatatranslator();
	}

}
