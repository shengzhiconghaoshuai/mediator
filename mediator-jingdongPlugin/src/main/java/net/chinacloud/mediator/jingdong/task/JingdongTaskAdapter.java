package net.chinacloud.mediator.jingdong.task;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.jingdong.constants.JingDongConstants;
import net.chinacloud.mediator.task.AbstractTaskAdapter;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.order.OrderCancelTask;
import net.chinacloud.mediator.task.order.OrderCreateTask;
import net.chinacloud.mediator.task.order.OrderListTask;
import net.chinacloud.mediator.task.order.OrderSuccessTask;
import net.chinacloud.mediator.task.order.OrderSupplementTask;
import net.chinacloud.mediator.task.product.ProductMappingListTask;
import net.chinacloud.mediator.task.product.ProductMappingTask;
import net.chinacloud.mediator.task.product.ProductOffShelfTask;
import net.chinacloud.mediator.task.product.ProductOnShelfTask;
import net.chinacloud.mediator.task.product.inventory.ProductInventoryUpdateTask;
import net.chinacloud.mediator.task.product.inventory.SkuInventoryUpdateTask;
import net.chinacloud.mediator.task.refund.RefundListTask;
import net.chinacloud.mediator.task.refund.RefundWholeClosedTask;
import net.chinacloud.mediator.task.refund.RefundWholeCreateTask;
import net.chinacloud.mediator.task.refund.RefundWholeSuccessTask;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JingdongTaskAdapter extends AbstractTaskAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(JingdongTaskAdapter.class);
	//----------------Order----------------
	/**
	 * 订单列表
	 * @return
	 */
	public abstract OrderListTask generateOrderListTask();
	/**
	 * 订单补单
	 * @return
	 */
	public abstract OrderSupplementTask generateOrderSupplementTask();
	/**
	 * 订单创建(买家付款)
	 * @return
	 */
	public abstract OrderCreateTask generateOrderCreateTask();
	/**
	 * 订单取消
	 * @return
	 */
	public abstract OrderCancelTask generateOrderCancelTask();
	/**
	 * 订单成功
	 * @return
	 */
	public abstract OrderSuccessTask generateTradeSuccessTask();
	
	//----------------Product----------------
	/**
	 * 单sku更新包装task
	 * @return
	 */
	public abstract SkuInventoryUpdateTask generateSkuInventoryUpdateTask();
	/**
	 * 单库存更新
	 * @return
	 */
	public abstract ProductInventoryUpdateTask generateProductInventoryUpdateTask();
	/**
	 * 设置渠道商品上架
	 * @return
	 */
	public abstract ProductOnShelfTask generateProductOnShelfTask();
	/**
	 * 设置渠道商品下架
	 * @return
	 */
	public abstract ProductOffShelfTask generateProductOffShelfTask();
	/**
	 * 抓取渠道商品映射数据
	 * @return
	 */
	public abstract ProductMappingListTask generateProductMappingListTask();
	/**
	 * 处理渠道商品映射数据
	 * @return
	 */
	public abstract ProductMappingTask generateProductMappingTask();
	
    /**
     * 整单退款创建
     * @return
     */
    public abstract RefundWholeCreateTask generateRefundWhloleCreateTask();
     /**
     * 整单退款成功
     * @return
     */
    public abstract RefundWholeSuccessTask generateRefundWholeSuccessTask();
    /**
     * 整单退款关闭
     * @return
     */
    public abstract RefundWholeClosedTask generateRefundWholeClosedTask();
    /**
     * 退款列表
     */
    public abstract RefundListTask generateRefundListTask();
	
	@Override
	public <T> Task generateServiceTask(CommonNotifyPacket<T> packet) {
		T message = packet.getMessage();
		Task task = null;
		if (message instanceof Order) {	//订单相关task
			LOGGER.debug("generate JD order task, packet = {}", packet);
			task = generateOrderTask((Order)message, packet.getType());
		} else if(message instanceof Return) {	//退货相关task
			LOGGER.debug("generate JD return task, packet = {}", packet);
			task = generateReturnTask((Return)message, packet.getType());
		} else if(message instanceof Refund) {	//退款相关task
			LOGGER.debug("generate JD refund task, packet = {}", packet);
			task = generateRefundTask((Refund)message, packet.getType());
		} else if(message instanceof Product) {	//商品相关task
			LOGGER.debug("generate JD product task, packet = {}", packet);
			task = generateProductTask((Product)message, packet.getType());
		} else{
			return null;
		}
		return task;
	}
	
	private Task generateOrderTask(Order order,String type){
		Task task = null;
		String status = order.getStatus();
		if("list".equals(type)) {	//订单列表
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate JD Order List Task");
			}
			task = generateOrderListTask();
			//对于list类型的task,order中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		} else if ("supplement".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate JD Order Supplement Task");
			}
			task = generateOrderSupplementTask();
			//对于supplement类型的task,order中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		} else{
			if(JingDongConstants.WAIT_SELLER_STOCK_OUT.equals(type)){
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate JD Order Create Task");
				}
				task = generateOrderCreateTask();
				task.setDataId(order.getChannelOrderId());
				task.setData(order);
			} else if(JingDongConstants.TRADE_CANCELED.equals(type)){
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate JD Order Cancel Task");
				}
				task = generateOrderCancelTask();
				task.setDataId(order.getChannelOrderId());
				task.setData(order);
			} else if(JingDongConstants.FINISHED_L.equals(type)){
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate JD Order Success Task");
				}
				task = generateTradeSuccessTask();
				task.setDataId(order.getChannelOrderId());
				task.setData(order);
			}
		}
		
		return task;
	}
	
	/**
	 * 商品相关task
	 * @param product
	 * @param type
	 * @return
	 */
	private Task generateProductTask(Product product, String type) {
		Task task = null;
		if ("onShelf".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate JD Product OnShelf Task");
			}
			// 商品上架(设置渠道上架状态)
			task = generateProductOnShelfTask();
			task.setData(product);
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
		} else if ("offShelf".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate JD Product OffShelf Task");
			}
			task = generateProductOffShelfTask();
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
			task.setData(product);
		} else if("skuUpdateInventory".equals(type)) {	//sku 库存同步
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate JD Sku Inventory Update Task");
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
		} else if ("productUpdateInventory".equals(type)) {	//product库存更新
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate JD Product Inventory Update Task");
			}
			//task = SpringUtil.getBean(InventorySingleUpdateTask.class);
			task = generateProductInventoryUpdateTask();
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
			task.setData(product);
		} else if ("mappingList".equals(type)) { //product映射
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate JD Product MappingList Task");
			}
			task = generateProductMappingListTask();
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
		} else if ("mapping".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate JD Product Mapping Task");
			}
			task = generateProductMappingTask();
			task.setData(product);
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
		} else {
			return null;
		}
		return task;
	}
	
	/**
	 * 退款相关task
	 * @param refund
	 * @param type
	 * @return
	 */
	private Task generateRefundTask(Refund refund, String type) {
		Task task = null;
        String status = refund.getStatus();
        if ("list".equals(type)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("generate Refund List Task");
            }
            task = generateRefundListTask();
            //将refund的状态放到task上下文中参数中
            task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
            task.setDataId(String.valueOf(System.nanoTime()));
        } else if ("WAIT_SELLER_AGREE".equals(status)) {	//未审核 ,退款创建,没有主动通知
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("generate jingdong Refund apply Task");
            }
            task = generateRefundWhloleCreateTask();
            task.setDataId(refund.getChannelRefundId());
            task.setData(refund);
        } else if ("SUCCESS".equals(status)) {	//审核通过,退款成功
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" generate jingdong Refund Success Task");
            }
            task = generateRefundWholeSuccessTask();
            task.setDataId(refund.getChannelRefundId());
            task.setData(refund);
        } else if ("CLOSED".equals(status)) {		//审核不通过 ,退款申请未成功
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("generate jingdong Refund Close Task");
            }
            task = generateRefundWholeClosedTask();
            task.setDataId(refund.getChannelRefundId());
            task.setData(refund);
        }
        return task;
	}
	/**
	 * 退货相关task
	 * @param returns
	 * @param type
	 * @return
	 */
	private Task generateReturnTask(Return returns ,String type){
		return null;
	}

}
