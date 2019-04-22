package net.chinacloud.mediator.kaola.task;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.kaola.bean.KaoLaConstant;
import net.chinacloud.mediator.kaola.service.KaoLaRefundService;
import net.chinacloud.mediator.kaola.task.order.KaoLaOrderListTask;
import net.chinacloud.mediator.kaola.task.refund.KaoLaRefundBuyerReturnGoodsTask;
import net.chinacloud.mediator.kaola.task.refund.KaoLaRefundClosedTask;
import net.chinacloud.mediator.kaola.task.refund.KaoLaRefundCreateTask;
import net.chinacloud.mediator.kaola.task.refund.KaoLaRefundListTask;
import net.chinacloud.mediator.kaola.task.refund.KaoLaRefundSellerAgreeTask;
import net.chinacloud.mediator.kaola.task.refund.KaoLaRefundSuccessTask;
import net.chinacloud.mediator.kaola.task.refund.KaoLaRefundSellerRefuseTask;
import net.chinacloud.mediator.kaola.task.skuBasic.SaveSkuMappingTask;
import net.chinacloud.mediator.kaola.task.skuBasic.SkuMappingListTask;
import net.chinacloud.mediator.task.AbstractTaskAdapter;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderCreateTask;
import net.chinacloud.mediator.task.order.OrderSuccessTask;
import net.chinacloud.mediator.task.product.inventory.SkuInventoryUpdateTask;
import net.chinacloud.mediator.task.refund.RefundWholeClosedTask;
import net.chinacloud.mediator.task.refund.RefundWholeCreateTask;
import net.chinacloud.mediator.task.refund.RefundWholeSuccessTask;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class KaoLaTaskAdapter extends AbstractTaskAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaTaskAdapter.class);
	
	/**
	 * 订单列表
	 * @return
	 */
	public abstract KaoLaOrderListTask generateKaoLaOrderListTask();
	
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
	 * 单sku库存更新
	 * @return
	 */
	public abstract SkuInventoryUpdateTask generateSkuInventoryUpdateTask();
	
	/**
	 * 获取sku的mappingList，考拉平台的sku的key和oms里的skuid
	 */
	public abstract SkuMappingListTask generateSkuMappingListTask();
	/**
	 * 将mappinglist分成一个一个的skumapping储存到数据库中
	 */
	public abstract SaveSkuMappingTask generateSaveSkuMappingTask();
	

	//----------------Refund----------------
	/**订单取消待处理*/
	public abstract RefundWholeCreateTask generateRefundWholeCreateTask();
	/**订单已取消*/
	public abstract RefundWholeSuccessTask generateRefundWholeSuccessTask();
	/**放弃取消订单操作*/
	public abstract RefundWholeClosedTask generateRefundWholeClosedTask();
	/**抓取退款列表*/
	public abstract KaoLaRefundListTask generateKaoLaRefundListTask();
	/**退款创建 */
	public abstract KaoLaRefundCreateTask generateKaoLaRefundCreateTask();
	/**退款更新*/
	public abstract KaoLaRefundSellerRefuseTask generateKaoLaRefundSellerRefuseTask();
	/**退款成功*/
	public abstract KaoLaRefundSuccessTask generateKaoLaRefundSuccessTask();
	/**退款关闭*/
	public abstract KaoLaRefundClosedTask generateKaoLaRefundClosedTask();
	/** 卖家同意退款*/
	public abstract KaoLaRefundSellerAgreeTask generateKaoLaRefundSellerAgreeTask();
	/** 买家退货给卖家*/
	public abstract KaoLaRefundBuyerReturnGoodsTask generateKaoLaRefundBuyerReturnGoodsTask();

	@Autowired
	TaskManager taskManager;
	@Autowired
	KaoLaRefundService kaoLaRefundService;
	
	public <T> Task generateServiceTask(CommonNotifyPacket<T> packet) {
		T message = packet.getMessage();
		Task task = null;
		if(message instanceof Order){	//订单相关task
			LOGGER.debug("generate order task, packet = {}", packet);
			task = generateOrderTask((Order)message, packet.getType());
		}else if(message instanceof Refund) {	//商品相关task
			LOGGER.debug("generate refund task, packet = {}", packet);
			task = generateRefundTask((Refund)message, packet.getType());
		}else if(message instanceof Product) {	//商品相关task
			LOGGER.debug("generate product task, packet = {}", packet);
			task = generateProductTask((Product)message, packet.getType());
		}else if(message instanceof Sku) {	//商品相关task
			LOGGER.debug("generate product task, packet = {}", packet);
			task = generateSkuTask((Sku)message, packet.getType());
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
	
	private Task generateSkuTask(Sku sku, String type) {
		Task task = null;
		String[] strs = type.split("_");
		type = strs[0];
		String status = strs[1];
		if("skuMappingList".equals(type)) {	//抓取skumappinglist
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Sku mapping list Task");
			}
			task = generateSkuMappingListTask();
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(type);
			task.setData(sku);
		}else if("skuMapping".equals(type)){//处理单个sku的mapping
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Sku mapping Task");
			}
			task = generateSaveSkuMappingTask();
			task.setData(sku);
			task.setDataId(sku.getOuterSkuId());
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
			task = generateKaoLaOrderListTask();
			//对于list类型的task,order中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		} else if (Order.STATUS_WAIT_SELLER_SEND_GOODS.equals(status)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Create Task");
			}
			task = generateOrderCreateTask();
			task.setDataId(order.getChannelOrderId());
			task.setData(order.getChannelOrderId());
		} else if (Order.STATUS_FINISHED.equals(status)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Success Task");
			}
			task = generateOrderSuccessTask();
			task.setDataId(order.getChannelOrderId());
			task.setData(order.getChannelOrderId());
		}else if (KaoLaConstant.ORDER_TO_REFUND.equals(status) || KaoLaConstant.ORDER_TO_CANCELED.equals(status)||KaoLaConstant.ORDER_TO_REVOKE.equals(status)) {
			Refund refund = kaoLaRefundService.getRefundByOrder(order);
        	CommonNotifyPacket<Refund> packet = new CommonNotifyPacket<Refund>(refund);
        	packet.setType(status);
        	task = taskManager.generateTask((String)ContextUtil.get(Constant.CHANNEL_CODE), packet);
		}
		return task;
	}
	
	private Task generateRefundTask(Refund refund, String type) {
		Task task = null;
		String status = refund.getStatus();
		 if("list".equals(type)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate list refund Task ");
			}
			task = generateKaoLaRefundListTask();
			task.setData(refund.getChannelRefundId());
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(String.valueOf(System.nanoTime())+"_"+status);
		}else if(KaoLaConstant.ORDER_TO_REFUND.equals(type)) {	
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate order cancel waiting Task = whole create refund");
			}
			
			task = generateRefundWholeCreateTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund);
		}else if(KaoLaConstant.ORDER_TO_CANCELED.equals(type)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate order canceled Task = wholeSuccess refund");
			}
			task = generateRefundWholeSuccessTask();
			task.setData(refund);
			task.setDataId(refund.getChannelRefundId());
		}else if(KaoLaConstant.ORDER_TO_REVOKE.equals(type)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate order changed origin Task = whole Closed refund");
			}
			task = generateRefundWholeClosedTask();
			task.setData(refund);
			task.setDataId(refund.getChannelRefundId());
		}else if ("WAIT_SELLER_AGREE".equals(status) && StringUtils.isEmpty(type)) {	//退款创建
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Create Task");
			}
			//task = SpringUtil.getBean(RefundCreateTask.class);
			task = generateKaoLaRefundCreateTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
			
		} else if ("SELLER_REFUSE_BUYER".equals(status) && StringUtils.isEmpty(type)) {	//买家修改退款协议
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Update Task");
			}
			//task = SpringUtil.getBean(RefundUpdateTask.class);
			task = generateKaoLaRefundSellerRefuseTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("SUCCESS".equals(status)&& StringUtils.isEmpty(type)) {	//主动通知退款成功
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Success Task");
			}
			//task = SpringUtil.getBean(RefundSuccessTask.class);
			task = generateKaoLaRefundSuccessTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("CLOSED".equals(status)&& StringUtils.isEmpty(type)) {		//退款申请未成功
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Close Task");
			}
			task = generateKaoLaRefundClosedTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("WAIT_BUYER_RETURN_GOODS".equals(status)&& StringUtils.isEmpty(type)) {		//卖家同意退货
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Seller Agree Task");
			}
			task = generateKaoLaRefundSellerAgreeTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("WAIT_SELLER_CONFIRM_GOODS".equals(status)&& StringUtils.isEmpty(type)) {		//买家退货给卖家
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Buyer Return Goods Task");
			}
			task = generateKaoLaRefundBuyerReturnGoodsTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} 
		return task;
	}

}
