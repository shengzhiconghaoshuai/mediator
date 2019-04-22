/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderPartlySellerShipTask.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.task.order;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderTask;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @description 订单部分发货
 * @author yejunwu123@gmail.com
 * @since 2015年7月24日 下午3:59:19
 */
@Component
@Scope(value="prototype")
public class OrderPartlySellerShipTask extends OrderTask {
	
	private static final String ORDER_PARTLY_SELLER_SHIP_TYPE = "partlySellerShip";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderPartlySellerShipTask.class);

	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_PARTLY_SELLER_SHIP_TYPE, OrderPartlySellerShipTask.class);
	}
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;
	
	@Override
	protected String getSubType() {
		return ORDER_PARTLY_SELLER_SHIP_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("子订单发货order info:" + order);
		}
		
		if (null != order) {
			// 对于部分发货这种的,淘宝通知过来,oid与tid是一样的,没法确认是哪个子订单发货,取所有子订单
			/*OrderService orderService = getService(OrderService.class, getContext().getChannelCode());
			Order fullOrder = orderService.getOrderById(order.getChannelOrderId());
			// 子订单发货,取详情时可能会取到多个子订单,将不是成功的子订单过滤掉
			if (!CollectionUtil.isEmpty(fullOrder.getOrderItems())) {
				List<OrderItem> orderItems = new ArrayList<OrderItem>(order.getOrderItems().size());
				for (OrderItem successOrderItem : order.getOrderItems()) {
					for (OrderItem fullOrderItem : fullOrder.getOrderItems()) {
						if (successOrderItem.equals(fullOrderItem)) {
							orderItems.add(fullOrderItem);
						}
					}
				}
				fullOrder.getOrderItems().clear();
				fullOrder.getOrderItems().addAll(orderItems);
				
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("部分发货order info:" + order);
				}
			}
			orderFacadeClient.partlyShip(fullOrder, getContext(), this.id);*/
			orderFacadeClient.partlyShip(order, context, this.id);
		}
	}

}
