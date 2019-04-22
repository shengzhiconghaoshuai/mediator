package net.chinacloud.mediator.kaola.init;

import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.init.PluginInit;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.kaola.bean.KaoLaConstant;
import net.chinacloud.mediator.kaola.serviceImpl.KaoLaOrderServiceImpl;
import net.chinacloud.mediator.kaola.serviceImpl.KaoLaProductServiceImpl;
import net.chinacloud.mediator.kaola.serviceImpl.KaoLaRefundServiceImpl;
import net.chinacloud.mediator.kaola.task.KaoLaTaskAdapter;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.service.RefundService;
import net.chinacloud.mediator.task.TaskAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KaoLaPluginInit implements PluginInit{
	
	@Autowired
	KaoLaTaskAdapter kaolaTaskAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public Class<KaoLaConnector> registConnector() {
		return KaoLaConnector.class;
	}

	@Override
	public String registChannel() {
		return KaoLaConstant.CHANNEL_KAOLA;
	}

	@Override
	public TaskAdapter registTaskAdapter() {
		return kaolaTaskAdapter;
	}

	@Override
	public Map<Class<?>, Class<?>> registService() {
		Map<Class<?>, Class<?>> serviceMap = new HashMap<Class<?>, Class<?>>(4);
		serviceMap.put(OrderService.class, KaoLaOrderServiceImpl.class);
		serviceMap.put(ProductService.class, KaoLaProductServiceImpl.class);
		serviceMap.put(RefundService.class, KaoLaRefundServiceImpl.class);
		return serviceMap;
	}

	@Override
	public DataTranslator registDataTranslator() {
		return new KaoLaDatatranslator();
	}

}
