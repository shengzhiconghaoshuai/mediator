/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoPluginInit.java
 * 描述： 天猫渠道初始化
 */
package net.chinacloud.mediator.taobao.init;

import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.init.PluginInit;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.service.ExchangeService;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.service.RefundService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.service.impl.ExchangeServiceImpl;
import net.chinacloud.mediator.taobao.service.impl.OrderServiceImpl;
import net.chinacloud.mediator.taobao.service.impl.ProductServiceImpl;
import net.chinacloud.mediator.taobao.service.impl.RefundServiceImpl;
import net.chinacloud.mediator.taobao.task.TaobaoTaskAdapter;
import net.chinacloud.mediator.task.TaskAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * <天猫渠道初始化>
 * <天猫渠道初始化>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
@Component
public class TmallPluginInit implements PluginInit {
	
	@Autowired
	TaobaoTaskAdapter taobaoTaskAdapter;

	public String registChannel() {
		return TaobaoConstant.CHANNEL_TMALL;
	}

	public TaskAdapter registTaskAdapter() {
		//return SpringUtil.getBean(TaobaoTaskAdapter.class);
		return taobaoTaskAdapter;
	}

	@SuppressWarnings("unchecked")
	public Class<TaobaoConnector> registConnector() {
		return TaobaoConnector.class;
	}

	@Override
	public Map<Class<?>, Class<?>> registService() {
		Map<Class<?>, Class<?>> serviceMap = new HashMap<Class<?>, Class<?>>(4);
		serviceMap.put(OrderService.class, OrderServiceImpl.class);
		serviceMap.put(ProductService.class, ProductServiceImpl.class);
		serviceMap.put(RefundService.class, RefundServiceImpl.class);
		serviceMap.put(ExchangeService.class, ExchangeServiceImpl.class);
		return serviceMap;
	}

	@Override
	public DataTranslator registDataTranslator() {
		return new TaobaoDataTranslator();
	}
}
