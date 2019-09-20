package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.VopReturnMessage;
import net.chinacloud.mediator.domain.VopStoreMessage;
import net.chinacloud.mediator.task.AbstractTaskAdapter;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.order.OrderCancelTask;
import net.chinacloud.mediator.task.order.OrderCreateTask;
import net.chinacloud.mediator.task.order.OrderListTask;
import net.chinacloud.mediator.task.product.inventory.SkuInventoryUpdateTask;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.StringUtils;
import net.chinacloud.mediator.vip.vop.constants.JitConstants;
import net.chinacloud.mediator.vip.vop.domain.*;
import net.chinacloud.mediator.vip.vop.task.BatchCreatePickTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class VopTaskAdapter extends AbstractTaskAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(VopTaskAdapter.class);
	
	/**
	 * 
	 * @return
	 */
	public abstract PoListTask generatePoListTask();
	/**
	 * 
	 * @return
	 */
	public abstract CreatePoTask generateCreatePoTask();
	/**
	 * 
	 * @return
	 */
	public abstract PostPickTask generatePostPickTask();
	/**
	 * 
	 * @return
	 */
	public abstract CreatedeliverTask generateCreatedeliverTask();
	/**
	 * 
	 * @return
	 */
	public abstract ImportDeliverDetailTask generateImportDeliverDetailTask();
	/**
	 * 
	 * @return
	 */
	public abstract CreatePickTask generateCreatePickTask();
	/**
	 * 
	 * @return
	 */
	public abstract ConfirmdeliverTask generateConfirmdeliverTask();
	/**
	 * 
	 */
	public abstract BatchCreatePickTask generateBatchCreatePickTask();
	/**
	 * 单sku库存更新
	 * @return
	 */
	public abstract SkuInventoryUpdateTask generateSkuInventoryUpdateTask();
	
	/**
	 * 
	 * @return
	 */
	public abstract GetInventoryDeductOrdersTask generateGetInventoryDeductOrdersTask();
	
	/**
	 * 
	 * @return
	 */
	public abstract GetInventoryOccupiedOrdersTask generateGetInventoryOccupiedOrdersTask();
	
	/**
	 * 
	 * @return
	 */
	public abstract GetPoListTask generateGetPoListTask();
	
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
	 * 订单取消 
	 * @return
	 */
	public abstract OrderCancelTask generateOrderCancelTask();
	
	/**
	 * 
	 * @return
	 */
	public abstract ResponseOrderStoreTask generateResponseOrderStoreTask();
	
	/**
	 * 
	 * @return
	 */
	public abstract ConfirmStoreDeliveryTask generateConfirmStoreDeliveryTask();
	
	/**
	 * 
	 * @return
	 */
	public abstract ConfirmRefuseResultTask generateConfirmRefuseResultTask();
	
	/**
	 * 
	 * @return
	 */
	public abstract ConfirmReturnResultTask generateConfirmReturnResultTask();
	/**获取客退列表*/
	public abstract OrderReturnListTask generateOrderReturnListTask();

	public abstract VipSyscInventoryTask  generateVipSyscInventoryTask();
	public abstract VipSyscInventoryItemTask  generateVipSyscInventoryItemTask();


	
	@Override
	public <T> Task generateServiceTask(CommonNotifyPacket<T> packet) {
		Task task = null;
		
		String status = packet.getType();
		if(JitConstants.STATUS_BATCH_CREATE_PICK.equals(status)){
			task = generateBatchCreatePickTask();
			task.setDataId( String.valueOf(System.nanoTime()));
			PoList polist = (PoList) packet.getMessage();
			task.setData(polist);
		}
		//批量获取PO
		if(JitConstants.STATUS_PO_BATCHGET.equals(status)){
			LOGGER.info("--vop批量获取PO--");
			//task = SpringUtil.getBean(PoListTask.class);
			task = generatePoListTask();
			task.setDataId( String.valueOf(System.nanoTime()));
			task.setData(String.valueOf(System.nanoTime()));
		}
		
		//发送po
		if(JitConstants.STATUS_PO_CREATE.equals(status)){
			LOGGER.info("--vop发送po--");
			//task = SpringUtil.getBean(CreatePoTask.class);
			task = generateCreatePoTask();
			PoBean po = (PoBean) packet.getMessage();
			task.setData(po);
			task.setDataId(po.po_no);
		}
		
		//发送pick
		if(JitConstants.STATUS_PICK_CREATE.equals(status)){
			LOGGER.info("--vop发送pick--");
			//task = SpringUtil.getBean(PostPickTask.class);
			task = generatePostPickTask();
			CreatePick aCreatePick = (CreatePick)packet.getMessage();
			task.setData(aCreatePick);
			task.setDataId(aCreatePick.getPick_no());
		}
		
		//批量创建pick
		if(JitConstants.STATUS_PICK_BATCHCREATE.equals(status)){
			LOGGER.info("--批量创建pick--");
			//task = SpringUtil.getBean(CreatePickTask.class);
			task = generateCreatePickTask();
			PoMessage po = (PoMessage)packet.getMessage();
			task.setData(po);
			task.setDataId( String.valueOf(System.nanoTime()));
		}
		
		//创建deliver
		if(JitConstants.STATUS_CREATE_DELIVER.equals(status)){
			LOGGER.info("--创建deliver--");
			//task = SpringUtil.getBean(CreatedeliverTask.class);
			task = generateCreatedeliverTask();
			CreatedeliverMsg msg = (CreatedeliverMsg)packet.getMessage();
			task.setData(msg);
			task.setDataId(String.valueOf(System.nanoTime()));
		}
		
		//import deliver
		if(JitConstants.STATUS_IMPORT_DELIVER_DETAIL.equals(status)){
			LOGGER.info("--import deliver--");
			task = generateImportDeliverDetailTask();
			ImportDeliverDetailMsg msg = (ImportDeliverDetailMsg)packet.getMessage();
			task.setData(msg);
			task.setDataId(String.valueOf(System.nanoTime()));
		}
		
		//CONFIRMDELIVER
		if(JitConstants.STATUS_CONFIRMDELIVER_JIT.equals(status)){
			LOGGER.info("--CONFIRMDELIVER--");
			task = generateConfirmdeliverTask();
			ConfirmdeliverMsg msg = (ConfirmdeliverMsg)packet.getMessage();
			task.setData(msg);
			task.setDataId(String.valueOf(System.nanoTime()));
		}
		/*
		* vip库存同步oms的sku
		* */
		if("skuUpdateInventory".equals(status)) {	//sku 库存同步
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Sku Inventory Update Task");
			}
			task = generateSkuInventoryUpdateTask();
			Product product = (Product) packet.getMessage();
			if (!CollectionUtil.isEmpty(product.getSkuList())) {
				task.setData(product.getSkuList().get(0));
				if (StringUtils.hasText(product.getSkuList().get(0).getOuterSkuId())) {
					task.setDataId(product.getSkuList().get(0).getOuterSkuId());
				} else {
					task.setDataId(String.valueOf(System.nanoTime()));
				}
			}
		}
		if (JitConstants.GET_INVENTORYDEDUCTORDERS.equals(status)) {
			LOGGER.info("generate getInventoryDeductOrders task");
			task = generateGetInventoryDeductOrdersTask();
			PickBean pickBean = (PickBean)packet.getMessage();
			task.setData(pickBean);
			task.setDataId(String.valueOf(System.nanoTime()));
		}
		if (JitConstants.GET_OCCUPIEDORDER.equals(status)) {
			LOGGER.info("generate getOccupiedOrder task");
			task = generateGetInventoryOccupiedOrdersTask();
			task.setDataId(String.valueOf(System.nanoTime()));
			task.setData(String.valueOf(System.nanoTime()));
		}
		if (JitConstants.GET_POLIST.equals(status)) {
			LOGGER.info("generate getPoList task");
			task = generateGetPoListTask();
			task.setDataId(String.valueOf(System.nanoTime()));
			task.setData(String.valueOf(System.nanoTime()));
		}
		
		if("list".equals(status)) {	//订单列表
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order List Task");
			}
			task = generateOrderListTask();
			Order order = (Order) packet.getMessage();
			//对于list类型的task,order中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, order.getStatus());
			task.setDataId(order.getStatus());
		}
		if ("WAIT_SELLER_SEND_GOODS".equals(status)) { //订单创建
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Create Task");
			}
			task = generateOrderCreateTask();
			Order order = (Order) packet.getMessage();
			task.setDataId(order.getChannelOrderId());
			task.setData(order);
		}
		if ("cancelled".equals(status)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order cancel Task");
			}
			task = generateOrderCancelTask();
			Order order = (Order) packet.getMessage();
			task.setDataId(order.getChannelOrderId());
			task.setData(order);
		}
		if ("ResponseOrderStore".equals(status)) {
			task = generateResponseOrderStoreTask();
			VopStoreMessage message = (VopStoreMessage)packet.getMessage();
			task.setData(message);
			task.setDataId(message.getOrder_id());
		}
		if ("ConfirmStoreDelivery".equals(status)) {
			task = generateConfirmStoreDeliveryTask();
			VopStoreMessage message = (VopStoreMessage)packet.getMessage();
			task.setData(message);
			task.setDataId(message.getOrder_id());
		}
		if ("ConfirmRefuseResult".equals(status)) {
			task = generateConfirmRefuseResultTask();
			VopReturnMessage message = (VopReturnMessage)packet.getMessage();
			task.setData(message);
			task.setDataId(message.getOrder_id());
		}
		if ("ConfirmReturnResult".equals(status)) {
			task = generateConfirmReturnResultTask();
			VopReturnMessage message = (VopReturnMessage)packet.getMessage();
			task.setData(message);
			task.setDataId(message.getOrder_id());
		}
		if("orderReturnList".equals(status)) {	//订单列表
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate orderReturn List Task");
			}
			task = generateOrderReturnListTask();
			Refund refund = (Refund) packet.getMessage();//获取客退或拒收的订单列表，属于退款的一部分，job里是用的refund封装的。
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, refund.getStatus());
			task.setDataId(refund.getStatus());
		}
		/*
		* 同步vip库存parent task
		* */
		if(JitConstants.VIP_SYNC_INVENTORY.equals(status)){
			LOGGER.info("--同步vip parent task VIP_SYNC_INVENTORY库存执行--");
			task = generateVipSyscInventoryTask();
			task.setDataId( String.valueOf(System.nanoTime()));
			VipInventoryUpList list = (VipInventoryUpList) packet.getMessage();
			task.setData(list);
		}
		/*
		* 同步vip库存 sub task
		* */
		if(JitConstants.VIP_ITEM_SYNC_INVENTORY.equals(status)){
			LOGGER.info("--同步vip item库存执行--");
			//task = SpringUtil.getBean(CreatePickTask.class);
			//task = generateCreatePickTask();
			task = generateVipSyscInventoryItemTask();
			VipInventoryMessage vipInventoryMessage = (VipInventoryMessage)packet.getMessage();
			task.setData(vipInventoryMessage);
			task.setDataId( String.valueOf(System.nanoTime()));
		}


		return task;
	}

}
