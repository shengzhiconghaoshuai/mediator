/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoTaskAdapter.java
 * 描述： 淘宝渠道task适配器
 */
package net.chinacloud.mediator.taobao.task;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.domain.StoreItems;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.task.order.OrderMsgSendToHoldTask;
import net.chinacloud.mediator.taobao.task.order.OrderPartlySellerShipTask;
import net.chinacloud.mediator.taobao.task.order.OrderPartlySuccess;
import net.chinacloud.mediator.taobao.task.order.OrderReceiveAddressUpdateTask;
import net.chinacloud.mediator.taobao.task.order.OrderStatusListTask;
import net.chinacloud.mediator.taobao.task.order.OrderStatusUpdateTask;
import net.chinacloud.mediator.taobao.task.order.OrderStepPayTask;
import net.chinacloud.mediator.taobao.task.order.OrderSyncListTask;
import net.chinacloud.mediator.taobao.task.order.OrderSyncTask;
import net.chinacloud.mediator.taobao.task.order.OrderUpdateResponseMsgTask;
import net.chinacloud.mediator.taobao.task.order.ZhengJiOrderListTask;
import net.chinacloud.mediator.taobao.task.order.ZhengJiOrderSupplementTask;
import net.chinacloud.mediator.taobao.task.product.ProductChannelOffShelfTask;
import net.chinacloud.mediator.taobao.task.product.ProductChannelUpShelfTask;
import net.chinacloud.mediator.taobao.task.product.ProductStoreInventoryItemInitialTask;
import net.chinacloud.mediator.taobao.task.refund.RefundSendGoodsCancelTask;
import net.chinacloud.mediator.taobao.task.refund.RefundSupplementTask;
import net.chinacloud.mediator.taobao.task.refund.RefundWarehouseUpdateTask;
import net.chinacloud.mediator.task.AbstractTaskAdapter;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.exchange.ExchangeInfoTask;
import net.chinacloud.mediator.task.exchange.ExchangeListTask;
import net.chinacloud.mediator.task.order.OrderCreateTask;
import net.chinacloud.mediator.task.order.OrderListTask;
import net.chinacloud.mediator.task.order.OrderSellerMemoModifiedTask;
import net.chinacloud.mediator.task.order.OrderSuccessTask;
import net.chinacloud.mediator.task.order.OrderSupplementTask;
import net.chinacloud.mediator.task.product.ProductMappingListTask;
import net.chinacloud.mediator.task.product.ProductMappingTask;
import net.chinacloud.mediator.task.product.ProductOffShelfTask;
import net.chinacloud.mediator.task.product.ProductOnShelfTask;
import net.chinacloud.mediator.task.product.inventory.ProductInventoryUpdateTask;
import net.chinacloud.mediator.task.product.inventory.SkuInventoryUpdateTask;
import net.chinacloud.mediator.task.refund.RefundBuyerReturnGoodsTask;
import net.chinacloud.mediator.task.refund.RefundClosedTask;
import net.chinacloud.mediator.task.refund.RefundCreateTask;
import net.chinacloud.mediator.task.refund.RefundSellerAgreeTask;
import net.chinacloud.mediator.task.refund.RefundSellerRefuseTask;
import net.chinacloud.mediator.task.refund.RefundSuccessTask;
import net.chinacloud.mediator.task.refund.RefundUpdateTask;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <淘宝渠道task适配器>
 * <淘宝渠道task适配器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
public abstract class TaobaoTaskAdapter extends AbstractTaskAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaobaoTaskAdapter.class);
	
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
	 * 订单数据同步列表
	 * @return
	 */
	public abstract OrderSyncListTask generateOrderSyncListTask();
	/**
	 * 订单数据同步
	 * @return
	 */
	public abstract OrderSyncTask generateOrderSyncTask();
	/**
	 * 订单创建
	 * @return
	 */
	public abstract OrderCreateTask generateOrderCreateTask();
	/**
	 * 预售付定金
	 * @return
	 */
	public abstract OrderStepPayTask generateOrderStepPayTask();
	/**
	 * 卖家修改备注
	 * @return
	 */
	public abstract OrderSellerMemoModifiedTask generateOrderSellerMemoModifiedTask();
	/**
	 * 订单成功
	 * @return
	 */
	public abstract OrderSuccessTask generateOrderSuccessTask();
	/**
	 * 订单部分成功(子订单成功)
	 * @return
	 */
	public abstract OrderPartlySuccess generateOrderPartlySuccess();
	/**
	 * 订单部分发货
	 * @return
	 */
	public abstract OrderPartlySellerShipTask generateOrderPartlySellerShipTask();
	/**
	 * 收货地址修改
	 * @return
	 */
	public abstract OrderReceiveAddressUpdateTask generateOrderReceiveAddressUpdateTask();
	
	/**
	 * 双十一的预约退款，用于hold订单
	 */
	public abstract OrderMsgSendToHoldTask generateOrderMsgSendToHoldTask();
	/**
	 * 回传hold订单的结果
	 */
	public abstract OrderUpdateResponseMsgTask generateOrderUpdateResponseMsgTask();
	/**
	 * 拉取订单状态列表
	 */
	public abstract OrderStatusListTask generateOrderStatusListTask();
	/**
	 * 下发订单状态，用于门店自提订单
	 */
	public abstract OrderStatusUpdateTask generateOrderStatusUpdateTask();
	
	//----------------Product----------------
	/**
	 * 单sku库存更新
	 * @return
	 */
	public abstract SkuInventoryUpdateTask generateSkuInventoryUpdateTask();
	/**
	 * 单product库存更新
	 * @return
	 */
	public abstract ProductInventoryUpdateTask generateProductInventoryUpdateTask();
	/**
	 * 渠道商品下架
	 * @return
	 */
	public abstract ProductChannelOffShelfTask generateProductChannelOffShelfTask();
	
	/**
	 * 渠道商品上架
	 * 
	 */
	public abstract ProductChannelUpShelfTask generateProductChannelUpShelfTask();
	
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
	 * 调用奇门的接口，对门店的库存进行初始化
	 * @return
	 */
	public abstract ProductStoreInventoryItemInitialTask generateProductStoreInventoryItemInitialTask();
	
	//----------------Refund----------------
	/**
	 * 退款创建
	 * @return
	 */
	public abstract RefundCreateTask generateRefundCreateTask();
	/**
	 * 退款更新
	 * @return
	 */
	public abstract RefundUpdateTask generateRefundUpdateTask();
	/**
	 * 退款成功
	 * @return
	 */
	public abstract RefundSuccessTask generateRefundSuccessTask();
	/**
	 * 退款关闭
	 * @return
	 */
	public abstract RefundClosedTask generateRefundClosedTask();
	/**
	 * 卖家拒绝退款
	 * @return
	 */
	public abstract RefundSellerRefuseTask generateRefundSellerRefuseTask();
	/**
	 * 卖家同意退款
	 * @return
	 */
	public abstract RefundSellerAgreeTask generateRefundSellerAgreeTask();
	/**
	 * 买家退货给卖家
	 * @return
	 */
	public abstract RefundBuyerReturnGoodsTask generateRefundBuyerReturnGoodsTask();
	/**
	 * 退款补单
	 */
	public abstract RefundSupplementTask generateRefundSupplementTask();
	
	/**
	 * AG退货入仓状态
	 * @return
	 */
	public abstract RefundWarehouseUpdateTask generateRefundWarehouseUpdateTask();
	 
	/**
	 * AG取消发货
	 * @return
	 */
	public abstract RefundSendGoodsCancelTask generateRefundSendGoodsCancelTask();
	
	/**
	 * 征集列表
	 */
	public abstract ZhengJiOrderListTask generateZhengJiOrderListTask();
	
	/**
	 * 成功的征集预售订单补单
	 */
	public abstract ZhengJiOrderSupplementTask generateZhengJiOrderSupplementTask();
	
	
	//--------------------------exchange-----------------------
	/**获取换货列表*/
	public abstract ExchangeListTask generateExchangeListTask();
	/**获取换货详细信息*/
	public abstract ExchangeInfoTask generateExchangeInfoTask();
	
	
	
	public <T> Task generateServiceTask(CommonNotifyPacket<T> packet) {
		T message = packet.getMessage();
		Task task = null;
		if(message instanceof Order){	//订单相关task
			LOGGER.debug("generate order task, packet = {}", packet);
			task = generateOrderTask((Order)message, packet.getType());
		}else if(message instanceof Return){	//退货相关task
			//淘宝渠道没有退货
			LOGGER.debug("generate return task, packet = {}", packet);
		}else if(message instanceof Refund){	//退款相关task
			LOGGER.debug("generate refund task, packet = {}", packet);
			task = generateRefundTask((Refund)message, packet.getType());
		}else if(message instanceof Product) {	//商品相关task
			LOGGER.debug("generate product task, packet = {}", packet);
			task = generateProductTask((Product)message, packet.getType());
		}else if(message instanceof Exchange) {	//换货相关task
			LOGGER.debug("generate exchange task, packet = {}", packet);
			task = generateExchangeTask((Exchange)message, packet.getType());
		}else if(message instanceof StoreItems){
			LOGGER.debug("generate  task, packet = {}", packet);
			task = generateStoreItemTask((StoreItems)message, packet.getType());
		}else{
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
		if ("RefundCreated".equals(status) || "WAIT_SELLER_AGREE".equals(status)) {	//主动通知退款创建
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Create Task");
			}
			//task = SpringUtil.getBean(RefundCreateTask.class);
			task = generateRefundCreateTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("refundsupplement".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Supplement Task");
			}
			task = generateRefundSupplementTask();
			//对于supplement类型的task,refund中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		}else if ("RefundBuyerModifyAgreement".equals(status)) {	//主动通知买家修改退款协议
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Update Task");
			}
			//task = SpringUtil.getBean(RefundUpdateTask.class);
			task = generateRefundUpdateTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("RefundSuccess".equals(status) || "SUCCESS".equals(status)) {	//主动通知退款成功
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Success Task");
			}
			//task = SpringUtil.getBean(RefundSuccessTask.class);
			task = generateRefundSuccessTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("RefundClosed".equals(status) || "CLOSED".equals(status)) {		//主动通知退款关闭,退款申请未成功
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Close Task");
			}
			//task = SpringUtil.getBean(RefundClosedTask.class);
			task = generateRefundClosedTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("RefundSellerRefuseAgreement".equals(status)) {	//卖家拒绝退货
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Seller Refuse Agreement Task");
			}
			task = generateRefundSellerRefuseTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("RefundSellerAgreeAgreement".equals(status)) {		//卖家同意退货
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Seller Agree Task");
			}
			task = generateRefundSellerAgreeTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("RefundBuyerReturnGoods".equals(status)) {		//买家退货给卖家
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund Buyer Return Goods Task");
			}
			task = generateRefundBuyerReturnGoodsTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund.getChannelRefundId());
		} else if ("SendGoodCancel".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund SendGoodCancel Task");
			}
			task = generateRefundSendGoodsCancelTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund);
		} else if ("WarehouseUpdate".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Refund WarehouseUpdate Task");
			}
			task = generateRefundWarehouseUpdateTask();
			task.setDataId(refund.getChannelRefundId());
			task.setData(refund);
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
		/*if ("ItemDownshelf".equals(type)) {		//主动通知商品下架
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Channel OffShelf Task");
			}
			task = generateProductChannelOffShelfTask();
			if (StringUtils.hasText(product.getChannelProductId())) {
				task.setDataId(product.getChannelProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
			task.setData(product);
		} else if("ItemUpshelf".equals(type)) { 	//主动通知商品上架
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Channel UpShelf Task");
			}
			task = generateProductChannelUpShelfTask();
			if (StringUtils.hasText(product.getChannelProductId())) {
				task.setDataId(product.getChannelProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
			task.setData(product);
		}  else if("ItemZeroStock".equals(type)) { 	//主动通知商品库存降为0
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Channel OffShelf Task");
			}
			task = generateProductChannelOffShelfTask();
			if (StringUtils.hasText(product.getChannelProductId())) {
				task.setDataId(product.getChannelProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
			task.setData(product);
		} else if ("onShelf".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product OnShelf Task");
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
				LOGGER.debug("generate Product OffShelf Task");
			}
			task = generateProductOffShelfTask();
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
			task.setData(product);
		} else*/
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
		} else if ("productUpdateInventory".equals(type)) {	//product库存更新
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Inventory Update Task");
			}
			//task = SpringUtil.getBean(InventorySingleUpdateTask.class);
			task = generateProductInventoryUpdateTask();
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
			task.setData(product);
		} else if ("mappingList".equals(type)) { //抓取product映射数据
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate  Product MappingList Task");
			}
			task = generateProductMappingListTask();
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
		} else if ("mapping".equals(type)) { //处理product映射数据
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Mapping Task");
			}
			task = generateProductMappingTask();
			task.setData(product);
			if (StringUtils.hasText(product.getOuterProductId())) {
				task.setDataId(product.getOuterProductId());
			} else {
				task.setDataId(String.valueOf(System.nanoTime()));
			}
		}else {
			return null;
		}
		return task;
	}

	/**
	 * 订单相关task
	 * @param order
	 * @param type
	 * @return
	 */
	private Task generateOrderTask(Order order,String type) {
		Task task = null;
		String status = order.getStatus();
		if("list".equals(type)) {	//订单列表
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order List Task");
			}
			//task = generateOrderListTask();
			//task = SpringUtil.getBean(OrderListTask.class);
			task = generateOrderListTask();
			//对于list类型的task,order中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		} else if ("syncList".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Sync List Task");
			}
			task = generateOrderSyncListTask();
			task.setDataId(String.valueOf(System.nanoTime()));
		} else if ("orderSync".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Sync Task");
			}
			task = generateOrderSyncTask();
			task.setDataId(order.getChannelOrderId());
		} else if ("supplement".equals(type)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Supplement Task");
			}
			task = generateOrderSupplementTask();
			//对于supplement类型的task,order中的status是一个列表
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		} else if("zhengjilist".equals(type)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate ZhengJiOrder List Task");
			}
			task = generateZhengJiOrderListTask();
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
		} else if("responseMsgOrderUpdate".equals(type)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate responseMsgOrderUpdate Task");
			}
			task = generateOrderUpdateResponseMsgTask();
			task.setData(order);
			task.setDataId(order.getChannelOrderId());
			return task;
		}else if("statusList".equals(type)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate OrderStatusList Task");
			}
			task = generateOrderStatusListTask();
			task.getContext().put(Constant.SCHEDULE_PARAM_STATUS, status);
			task.setDataId(status);
			return task;
		}else if("saveOrderStatus".equals(type)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate saveOrderStatus Task");
			}
			task = generateOrderStatusUpdateTask();
			task.setData(order.getChannelOrderId());
			task.setDataId(order.getChannelOrderId()+"_"+status);
			return task;
	   }else {
			if(status.equals("TradeBuyerPay") || status.equals("WAIT_SELLER_SEND_GOODS") || status.equals("PAID_FORBID_CONSIGN")) {	//主动通知买家付款或支付尾款 | 订单列表等待发货
				if("2".equals(order.getAdditionalParam(TaobaoConstant.KEY_ORDER_ZHENGJI))){
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("generate zhengjiOrder Create Task");
					}
					task=generateZhengJiOrderSupplementTask();
					task.setDataId(order.getChannelOrderId());
				}else{
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("generate Order Create Task");
					}
					task = generateOrderCreateTask();
					task.setDataId(order.getChannelOrderId());
				}
			} else if (status.equals("TradeBuyerStepPay")) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Order Create Task");
				}
				// 预售订单付定金
				task = generateOrderStepPayTask();
				task.setDataId(order.getChannelOrderId());
			} else if(status.equals("TradeMemoModified")) {	//主动通知卖家修改交易备注
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Order Seller Memo Modified Task");
				}
				//task = SpringUtil.getBean(OrderSellerMemoModifiedTask.class);
				task = generateOrderSellerMemoModifiedTask();
				task.setDataId(order.getChannelOrderId());
			} else if(status.equals("TradeSuccess")) {	//主动通知交易成功
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Order Success Task");
				}
				//task = SpringUtil.getBean(OrderSuccessTask.class);
				task = generateOrderSuccessTask();
				task.setDataId(order.getChannelOrderId());
			} else if(status.equals("TradeClose")) {		//主动通知交易关闭
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Order Success Task");
				}
				//这边交易关闭也交由OrderSuccessTask处理,OrderSuccessTask中会根据订单状态处理
				//task = SpringUtil.getBean(OrderSuccessTask.class);
				task = generateOrderSuccessTask();
				task.setDataId(order.getChannelOrderId());
			} else if(status.equals("TradeCreate")) {	//主动通知交易创建
				LOGGER.warn("订单[" + order.getChannelOrderId() + "]创建,未付款,系统不处理");
				return null;
			} else if (status.equals("TradePartlyConfirmPay")) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Order Partly Success Task");
				}
				// 子订单部分成功
				task = generateOrderPartlySuccess();
				task.setDataId(order.getChannelOrderId());
				/*task.setData(order);
				return task;*/
			} else if (status.equals("TradeLogisticsAddressChanged")) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Order Receive Address Update Task");
				}
				// 修改订单收货地址
				task = generateOrderReceiveAddressUpdateTask();
				task.setDataId(order.getChannelOrderId());
			} else if (status.equals("TradePartlySellerShip")) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Order Partly Seller Ship Task");
				}
				// 订单部分发货
				task = generateOrderPartlySellerShipTask();
				task.setDataId(order.getChannelOrderId());
				/*task.setData(order);
				return task;*/
			}else if (status.equals("OrderMsgSend")) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Order Partly Seller Ship Task");
				}
				//  双十一的预约退款，用于hold订单
				task = generateOrderMsgSendToHoldTask();
				task.setDataId(order.getChannelOrderId());
				task.setData(order);
				return task;
			} else {
				return null;
			}
		}
		//set data
		task.setData(order.getChannelOrderId());
		return task;
	}
	
	
	public Task generateExchangeTask(Exchange exchange,String type){
		Task task = null;
		if("list".equals(type)) {	//所有状态的换货列表
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Exchange List Task");
			}
			task = generateExchangeListTask();
			task.setDataId("exchangelist");
		} else if("info".equals(type)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Exchange info Task");
			}
			task = generateExchangeInfoTask();
			task.setDataId(exchange.getDisputeId()+"_"+exchange.getModified().getTime());
			task.setData(exchange.getDisputeId());
		}
		
		return task;
		
	}
	
	private Task generateStoreItemTask(StoreItems storeItems, String type) {
		Task task = null;
		if ("productStoreInventoryIteminitial".equals(type)) { //处理product映射数据
			if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("generate Product Mapping Task");
			}
			task = generateProductStoreInventoryItemInitialTask();
			task.setData(storeItems);
			task.setDataId(String.valueOf(storeItems.getOperationTime().getTime()));
				
		}
		return task;
	}
}
