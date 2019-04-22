/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderTask.java
 * 描述： 订单相关task基类
 */
package net.chinacloud.mediator.task.order;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.OrderItem;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <订单相关task基类>
 * <订单相关task基类>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
@Component
@Scope(value="prototype")
public abstract class OrderTask extends Task {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderTask.class);
	protected static final String ORDER_TYPE = "ORDER";
	
	@Autowired
	private ServiceManager serviceManager;
	
	@Override
	protected String getType() {
		return ORDER_TYPE;
	}
	
	/**
	 * 获取渠道对应的业务接口实现
	 * @param clazz 业务接口
	 * @param channelCode 渠道标识
	 * @return
	 */
	protected <T> T getService(Class<T> clazz, String channelCode){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("get service " + clazz.getSimpleName() + " for channel " + channelCode);
		}
		return serviceManager.getService(clazz, channelCode);
	}
	
	/**
	 *  获取渠道订单信息
	 * @return
	 * @throws OrderException 
	 * @throws TranslateException 
	 * @throws ProductException 
	 */
	protected Order getOrder() throws OrderException, TranslateException, ProductException {
		Order order = null;
		OrderService orderService = null;
		
		// update 2015-06-26
		// 这里需要判断类型,扫尾的时候数据类型在这边转换
		Object data = getData();
		if(data instanceof Order){
			order = (Order)getData();
		} else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				order = JsonUtil.jsonString2Object(strData, Order.class);
			} else {
				String dataId = null;
				if ('"' == first) {
					dataId = JsonUtil.jsonString2Object(strData, String.class);
				} else {
					dataId = getDataId();
				}
				
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("get order " + getDataId() + "by service from channel " + getContext().getChannelCode());
				}
				orderService = getService(OrderService.class, getContext().getChannelCode());
				order = orderService.getOrderById(dataId);
			}
		}
		//处理是否有未填写商家编码的子订单
		if (!order.getOrderItems().isEmpty()) {
			for (OrderItem orderItem : order.getOrderItems()) {
				if (!StringUtils.hasText(orderItem.getOuterSkuId())) {	//sku外部商家编码为null
					if (StringUtils.hasText(orderItem.getChannelSkuId())) {	//sku渠道编号不为null
						ProductService productService = getService(ProductService.class, getContext().getChannelCode());
						String outerSkuId = null;
						LOGGER.info("订单 " + order.getChannelOrderId() + " 的订单项 " + orderItem.getChannelOrderItemId() + " 的商品的外部商家编码为空");
						
						outerSkuId = productService.getOuterSkuIdByChannelSkuId(orderItem.getChannelSkuId(), orderItem.getChannelProductId());
						
						if (StringUtils.hasText(outerSkuId)) {
							orderItem.setOuterSkuId(outerSkuId.trim());
						}
					} 
				}
			}
		}
		return order;
	}
}
