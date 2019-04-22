/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoPluginInit.java
 * 描述： 淘宝渠道初始化
 */
package net.chinacloud.mediator.taobao.init;

import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.init.PluginInit;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.service.RefundService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.service.impl.OrderServiceImpl;
import net.chinacloud.mediator.taobao.service.impl.RefundServiceImpl;
import net.chinacloud.mediator.taobao.service.impl.TaobaoProductServiceImpl;

import org.springframework.stereotype.Component;
/**
 * <淘宝渠道初始化>
 * <淘宝渠道初始化>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
@Component
public class TaobaoPluginInit extends TmallPluginInit implements PluginInit {
	
	@Override
	public String registChannel() {
		return TaobaoConstant.CHANNEL_TAOBAO;
	}

	@Override
	public Map<Class<?>, Class<?>> registService() {
		Map<Class<?>, Class<?>> serviceMap = new HashMap<Class<?>, Class<?>>(4);
		serviceMap.put(OrderService.class, OrderServiceImpl.class);
		serviceMap.put(ProductService.class, TaobaoProductServiceImpl.class);
		serviceMap.put(RefundService.class, RefundServiceImpl.class);
		return serviceMap;
	}

}
