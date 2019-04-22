package net.chinacloud.mediator.xhs.task;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.task.AbstractTaskAdapter;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.order.OrderCancelTask;
import net.chinacloud.mediator.task.order.OrderCreateTask;
import net.chinacloud.mediator.task.order.OrderListTask;
import net.chinacloud.mediator.task.order.OrderSuccessTask;
import net.chinacloud.mediator.task.product.inventory.SkuInventoryUpdateTask;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.StringUtils;
import net.chinacloud.mediator.xhs.task.order.OrderCancelAuditingTask;
import net.chinacloud.mediator.xhs.task.order.OrderCancelListTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class XhsTaskAdapter extends AbstractTaskAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XhsTaskAdapter.class);
	
	/**
	 * 订单列表
	 * @return
	 */
	public abstract OrderListTask generateOrderListTask();
	
	/**
	 * 订单创建
	 * @return
	 */
	public abstract OrderCreateTask generateOrderCreateTask();
	
	
	/**
	 * 订单成功
	 * @return
	 */
	public abstract OrderSuccessTask generateOrderSuccessTask();
	/**
	 * 订单取消列表
	 * @return
	 */
	public abstract OrderCancelListTask generateOrderCancelListTask();
	
	/**
	 * 订单取消
	 * @return
	 */
	public abstract OrderCancelTask generateOrderCancelTask();
	/**
	 * 订单取消
	 * @return
	 */
	public abstract OrderCancelAuditingTask generateOrderCancelAuditingTask();
	
	/**
	 * 单sku库存更新
	 * @return
	 */
	public abstract SkuInventoryUpdateTask generateSkuInventoryUpdateTask();
	
	public <T> Task generateServiceTask(CommonNotifyPacket<T> packet) {
		T message = packet.getMessage();
		Task task = null;
		if(message instanceof Order){	//订单相关task
			LOGGER.debug("generate order task, packet = {}", packet);
			task = generateOrderTask((Order)message, packet.getType());
		}else if(message instanceof Product) {	//商品相关task
			LOGGER.debug("generate product task, packet = {}", packet);
			task = generateProductTask((Product)message, packet.getType());
		}else{
			return null;
		}
		return task;
	}

	private Task generateProductTask(Product product, String type) {
		Task task = null;
		if("skuUpdateInventory".equals(type)) {	//sku 库存同步
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Sku Inventory Update Task");
			}
			//task = SpringUtil.getBean(InventorySingleUpdateWrapperTask.class);
			task = generateSkuInventoryUpdateTask();
			if (!CollectionUtil.isEmpty(product.getSkuList())) {
				task.setData(product.getSkuList().get(0));
				if (StringUtils.hasText(product.getSkuList().get(0).getOuterSkuId())) {
					task.setDataId(product.getSkuList().get(0).getOuterSkuId());
				} else {
					task.setDataId(String.valueOf(System.nanoTime()));
				}
			}
		}
		return task;
	}

	private Task generateOrderTask(Order order, String type) {
		Task task = null;
		String status = order.getStatus();
		
		if("list".equals(type)) {	//订单列表
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order List Task");
			}
			task = generateOrderListTask();
			//对于list类型的task,order中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		} else if ("waiting".equals(status)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Create Task");
			}
			task = generateOrderCreateTask();
			task.setDataId(order.getChannelOrderId());
			task.setData(order.getChannelOrderId());
		} else if ("received".equals(status)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Success Task");
			}
			task = generateOrderSuccessTask();
			task.setDataId(order.getChannelOrderId());
			task.setData(order.getChannelOrderId());
		}else if ("cancelList".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order cancelList Task");
			}
			task = generateOrderCancelListTask();
			//对于list类型的task,order中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		}else if ("CancelAuditing".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order cancelList Task");
			}
			task = generateOrderCancelAuditingTask();
			task.setData(order);
			task.setDataId(order.getChannelOrderId());
		}else if(Order.STATUS_CLOSED.equals(status)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Cancel Task" +order);
			}
			task = generateOrderCancelTask();
			task.setDataId(order.getChannelOrderId()+"_"+order.getAdditionalParam("cancel_time"));
			task.setData(order);
		}
		return task;
	}

}
