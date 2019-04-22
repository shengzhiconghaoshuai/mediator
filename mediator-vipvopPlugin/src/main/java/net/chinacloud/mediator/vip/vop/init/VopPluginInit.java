package net.chinacloud.mediator.vip.vop.init;

import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.init.PluginInit;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskAdapter;
import net.chinacloud.mediator.vip.vop.constants.JitConstants;
import net.chinacloud.mediator.vip.vop.service.VopPickService;
import net.chinacloud.mediator.vip.vop.service.VopPoService;
import net.chinacloud.mediator.vip.vop.service.VopProductService;
import net.chinacloud.mediator.vip.vop.service.VopReturnService;
import net.chinacloud.mediator.vip.vop.service.impl.PickServiceImpl;
import net.chinacloud.mediator.vip.vop.service.impl.PoServiceImpl;
import net.chinacloud.mediator.vip.vop.service.impl.VopOrderServiceImpl;
import net.chinacloud.mediator.vip.vop.service.impl.VopProductServiceImpl;
import net.chinacloud.mediator.vip.vop.service.impl.VopReturnServiceImpl;
import net.chinacloud.mediator.vip.vop.task.VopTaskAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VopPluginInit implements PluginInit{
	
	@Autowired
	VopTaskAdapter vopTaskAdapter;
	
	public String registChannel() {
		return JitConstants.CHANNEL_VIP;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<VopConnector> registConnector() {
		return VopConnector.class;
	}

	@Override
	public TaskAdapter registTaskAdapter() {
		return vopTaskAdapter;
	}

	@Override
	public Map<Class<?>, Class<?>> registService() {
		Map<Class<?>, Class<?>> serviceMap = new HashMap<Class<?>, Class<?>>();
		serviceMap.put(VopPoService.class, PoServiceImpl.class);
		serviceMap.put(VopPickService.class, PickServiceImpl.class);
		serviceMap.put(ProductService.class, VopProductServiceImpl.class);
		serviceMap.put(VopProductService.class, VopProductServiceImpl.class);
		serviceMap.put(OrderService.class, VopOrderServiceImpl.class);
		serviceMap.put(VopReturnService.class, VopReturnServiceImpl.class);
		return serviceMap;
	}

	@Override
	public DataTranslator registDataTranslator() {
		return new VopDataTranslator();
	}
}
