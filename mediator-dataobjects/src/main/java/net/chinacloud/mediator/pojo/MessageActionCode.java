/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MessageActionCode.java
 * 描述： 
 */
package net.chinacloud.mediator.pojo;

/**
 * @description 消息代号
 * @author yejunwu123@gmail.com
 * @since 2015年6月30日 下午2:46:28
 */
public abstract class MessageActionCode {
	//===========================ORDER============================
	/**订单发货*/
	public static final String ACTION_CODE_ORDER_DELIVER_ORDER = "Order/Deliver";
	/**重新发货,即更新物流*/
	public static final String ACTION_CODE_ORDER_REDELIVER_ORDER = "Order/ReDeliver";
	/**订单创建*/
	public static final String ACTION_CODE_ORDER_CREATE = "Order/Create";
	/**订单同步方式创建*/
	public static final String ACTION_CODE_ORDER_CREATE_SYNC = "Order/SyncCreate";
	/**预售订单支付尾款*/
	public static final String ACTION_CODE_ORDER_PAY_FOR_STEPTRADE = "Order/PayForSetpTrade";
	/**征集订单*/
	public static final String ACTION_CODE_ORDER_PAY_FOR_ZHENGJI= "Order/PayForZhengji";
	/**订单成功*/
	public static final String ACTION_CODE_ORDER_SUCCESS = "Order/Success";
	/**订单部分成功(子订单成功)*/
	public static final String ACTION_CODE_ORDER_PARTLY_SUCCESS = "Order/PartlySuccess";
	/**订单部分发货*/
	public static final String ACTION_CODE_ORDER_PARTLY_SHIP = "Order/PartlyShip";
	/**订单关闭*/
	public static final String ACTION_CODE_ORDER_CLOSE = "Order/Close";
	/**订单卖家备注修改*/
	public static final String ACTION_CODE_ORDER_UPDATE_SELLER_MEMO = "Order/UpdateSellerMemo";
	/**订单更新收货地址*/
	public static final String ACTION_CODE_ORDER_UPDATE_RECEIVE_ADDRESS = "Order/UpdateReceiveAddress";
	/**订单取消*/
	public static final String ACTION_CODE_ORDER_CANCEL = "Order/Cancel";
	/**预售订单取消*/
	public static final String ACTION_CODE_ORDER_CANCEL_STEPTRADE = "Order/CancelStepTrade";
	/**订单重新创建*/
	public static final String ACTION_CODE_ORDER_RECREATE ="Order/ReCreate";
	
	/**更新订单旗帜到平台**/
	public static final String ACTION_CODE_ORDER_UPDATE_FLAG ="Order/UpdateSellerFlag";
	/**同步订单旗帜到OMS**/
	public static final String ACTION_CODE_ORDER_SYNC_FLAG ="Order/SyncSellerFlag";
	/**申请菜鸟电子面单**/
	public static final String ACTION_CODE_ORDER_APPLY_FOR_WAYBILL="Order/ApplyForWaybill";
	/**获取菜鸟电子面单**/
	public static final String ACTION_CODE_ORDER_CREATE_WAYBILL="Order/CreateWaybill";
	/**取消菜鸟电子面单**/
	public static final String ACTION_CODE_ORDER_CANCEL_WAYBILL="Order/CancelWaybill";
	
	/**订单全链路 通知门店配货**/
	public static final String ACTION_CODE_ORDER_ALLOCATION_NOTIFIED="Order/AllocationNotified";
	/**订单全链路 门店已接单**/
	public static final String ACTION_CODE_ORDER_SHOP_ALLOCATION="Order/ShopAllocation";
	/**订单全链路 门店已发货**/
	public static final String ACTION_CODE_ORDER_SHOP_HANDLED="Order/ShopHandled";
	
	/**发送订单去hold订单**/
	public static final String ACTION_CODE_ORDER_SEND_HOLD_ORDER="Order/SendHoldOrder";
	/**hold订单结果状态回传**/
	public static final String ACTION_CODE_ORDER_ALIGENIUS_MSG_UPDATE="Order/AligeniusMsg/Update";
	
	/**发出奇门事件 当订单被处理时，用于通知奇门系统**/
	public static final String ACTION_CODE_ORDER_QIMEN_EVENT_PRODUCE="Order/QimenEventProduce";
	
	/**订单状态更新**/
	public static final String ACTION_CODE_ORDER_STATUS="Order/statusUpdate";
	
	//===========================END ORDER============================
	
	//===========================PRODUCT============================
	/**商品上传*/
	public static final String ACTION_CODE_PRODUCT_CREATE = "Product/Create";
	/**商品更新*/
	public static final String ACTION_CODE_PRODUCT_UPDATE = "Product/Update";
	/**商品上架*/
	public static final String ACTION_CODE_PRODUCT_LIST = "Product/List";
	/**商品下架*/
	public static final String ACTION_CODE_PRODUCT_DELIST = "Product/Delist";
	/**渠道商品下架*/
	public static final String ACTION_CODE_PRODUCT_CHANNEL_DELIST = "Product/ChannelDelist";
	/**渠道商品上架*/
	public static final String ACTION_CODE_PRODUCT_CHANNEL_LIST = "Product/Channellist";
	/**价格更新*/
	public static final String ACTION_CODE_PRODUCT_PRICE_UPDATE = "Product/Price/Update";
	/**根据款更新库存*/
	public static final String ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_PRODUCTS = "Product/Inventory/Update/Products";
	/**根据sku更新库存*/
	public static final String ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_SKUS = "Product/Inventory/Update/Skus";
	/**获取所有运费模板*/
	public static final String ACTION_CODE_PRODUCT_DELIVERY_TEMPLATES_GET = "Product/DeliveryTemplates/Get";
	/**渠道商品属性获取*/
	public static final String ACTION_CODE_PRODUCT_CHANNEL_ATTRIBUTE_GET = "Product/ChannelAttribute/Get";
	/**渠道商品映射*/
	public static final String ACTION_CODE_PRODUCT_MAPPING = "Product/Mapping";
	/**获取所有类目*/
	public static final String ACTION_CODE_PRODUCT_CATEGORY_GET= "Product/Category/Get";
	/**全渠道商品关联绑定*/
	public static final String ACTION_CODE_PRODUCT_ITEMSTORE_BANDING= "Product/ItemStore/Banding";
	/**调用奇门的接口，对门店的库存进行初始化*/
	public static final String ACTION_CODE_PRODUCT_STOREINVENTORY_ITEMINITIAL= "Product/StoreInventory/ItemInitial";
	
	
	//===========================END PRODUCT============================
	
	//===========================RETURN============================
	/**退货创建*/
	public static final String ACTION_CODE_RETURN_CREATE = "Return/Create";
	//===========================END RETURN============================
	
	//===========================REFUND============================
	/**退款创建*/
	public static final String ACTION_CODE_REFUND_CREATE = "Refund/Create";
	/**退款成功*/
	public static final String ACTION_CODE_REFUND_SUCCESS = "Refund/Success";
	/**退款关闭*/
	public static final String ACTION_CODE_REFUND_CLOSE = "Refund/Close";
	/**退款更新*/
	public static final String ACTION_CODE_REFUND_UPDATE = "Refund/Update";
	/**退款卖家拒绝*/
	public static final String ACTION_CODE_REFUND_SELLER_REFUSE = "Refund/SellerRefuse";
	/**退款卖家同意*/
	public static final String ACTION_CODE_REFUND_SELLER_AGREE = "Refund/SellerAgree";
	/**买家退货给卖家*/
	public static final String ACTION_CODE_REFUND_BUYER_RETURN_GOODS = "Refund/BuyerReturnGoods";
	
	/**整单退款创建*/
	public static final String ACTION_CODE_REFUND_WHOLE_CREATE = "Refund/WholeCreate";
	/**整单退款成功*/
	public static final String ACTION_CODE_REFUND_WHOLE_SUCCESS = "Refund/WholeSuccess";
	/**整单退款关闭*/
	public static final String ACTION_CODE_REFUND_WHOLE_CLOSE = "Refund/WholeClose";
	/**取消发货*/
	public static final String ACTION_CODE_REFUND_SENDGOODCANCEL = "Refund/SendGoodCancel";
	/**AG退货入仓状态写接口*/
	public static final String ACTION_CODE_REFUND_WAREHOUSEUPDATE = "Refund/WarehouseUpdate";
	
	//===========================END REFUND============================
	
	//===========================SYSTEM============================
	/**库存同步前停止抓单*/
	public static final String ACTION_CODE_UPDATE_INVENTORY_STOP_ORDER = "Task/CreateOrder/Stop";
	/**库存同步完开启抓单*/
	public static final String ACTION_CODE_UPDATE_INVENTORY_RESUME_ORDER = "Task/CreateOrder/Resume";
	/**渠道业务处理失败*/
	public static final String ACTION_CODE_BUSINESS_PROCESS_ERROR = "Task/Business/Error";
	//===========================END SYSTEM============================
	
	//=============================VIP==============================
	public static final String ACTION_CODE_CREATE_PICK = "Vip/BatchCreatePick";
	public static final String ACTION_CODE_IMPORTDELIVER_DETAIL = "Vip/ImportDeliver";
	public static final String ACTION_CODE_CONFIRMDELIVER_JIT = "Vip/ConfirmDeliver";
	public static final String ACTION_CODE_JIT_CREATEDELIVER = "Vip/CreateDeliver";
	public static final String ACTION_CODE_CREATE_PO = "Vip/CreatePo";
	public static final String ACTION_CODE_IMPORTDELIVER_SUCCESS = "Vip/ImportDeliver/Success";
	public static final String ACTION_CODE_GET_POLIST = "Vip/GetPoList";
	public static final String ACTION_CODE_GET_INVENTORYOCCUPIEDORDERS = "Vip/GetInventoryOccupiedOrders";
	public static final String ACTION_CODE_GET_INVENTORYDEDUCTORDERS = "Vip/GetInventoryDeductOrders";
	public static final String ACTION_CODE_VIP_SYNINVENTORY = "VIP/SynInventory";

	
	//=============================VIPOXO==============================
	/**门店接单消息推送*/
	public static final String ACTION_CODE_RESPONSEORDERSTORE = "Vip/ResponseOrderStore";
	/**门店发货消息推送*/
	public static final String ACTION_CODE_CONFIRMSTOREDELIVERY = "Vip/ConfirmStoreDelivery";
	/**确认拒收结果*/
	public static final String ACTION_CODE_CONFIRMREFUSERESULT = "Vip/ConfirmRefuseResult";
	/**确认客退结果*/
	public static final String ACTION_CODE_CONFIRMRETURNRESULT = "Vip/ConfirmReturnResult";
	/**获取拒收订单列表*/
	public static final String ACTION_CODE_GETORDERRETURNLIST = "Vip/GetOrderReturnList";
	/**更新订单金额*/
	public static final String ACTION_CODE_UPDATEORDERFINANCIAL = "VipOXO/UpdateOrderFinancial";
	/**获取订单物流单号*/
	public static final String ACTION_CODE_GETLOGISTICSTRANSPORTNO = "VipOXO/GetLogisticsTranspostNo";
	
	//=============================VIPOXO END============================
	
	//===========================EXCHANGE============================
	/**换货信息抓取*/
	public static final String ACTION_CODE_EXCHANGE_INFO = "Exchange/Info";
	/**卖家创建换货留言  暂时不用*/
	public static final String ACTION_CODE_EXCHANGE_MESSAGE_ADD = "Exchange/Message/Add";
	/**查询换货订单留言 暂时不用*/
	public static final String ACTION_CODE_EXCHANGE_MESSAGE = "Exchange/Message";
	/**获取拒绝换货原因列表  暂时不用*/
	public static final String ACTION_CODE_EXCHANGE_REFUSEREASON_GET = "Exchange/refusereason/Get";
	/**卖家同意换货申请 */
	public static final String ACTION_CODE_EXCHANGE_AGREE = "Exchange/Agree";
	/**卖家拒绝换货申请*/
	public static final String ACTION_CODE_EXCHANGE_REFUSE = "Exchange/Refuse";
	/**卖家确认收货*/
	public static final String ACTION_CODE_EXCHANGE_RETURNGOODS_AGREE = "Exchange/Returngoods/Agree";
	/**卖家拒绝确认收货*/
	public static final String ACTION_CODE_EXCHANGE_RETURNGOODS_REFUSE = "Exchange/Returngoods/Refuse";
	/**通知平台换货发货*/
	public static final String ACTION_CODE_EXCHANGE_CONSIGNGOODS = "Exchange/Consigngoods";
	//===========================END EXCHANGE============================
}
