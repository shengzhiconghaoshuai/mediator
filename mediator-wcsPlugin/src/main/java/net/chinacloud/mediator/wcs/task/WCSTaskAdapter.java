/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：WCTaskAdapter.java
 * 描述： 
 */
package net.chinacloud.mediator.wcs.task;


import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.QimenEventProduce;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.domain.StoreItems;
import net.chinacloud.mediator.domain.VopReturnMessage;
import net.chinacloud.mediator.domain.VopStoreMessage;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskAdapter;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.exchange.ExchangeAgreeTask;
import net.chinacloud.mediator.task.exchange.ExchangeDeliverTask;
import net.chinacloud.mediator.task.exchange.ExchangeRefuseTask;
import net.chinacloud.mediator.task.exchange.ExchangeReturngoodsAgreeTask;
import net.chinacloud.mediator.task.exchange.ExchangeReturngoodsRefuseTask;
import net.chinacloud.mediator.task.order.OrderAllocationNotifiedTask;
import net.chinacloud.mediator.task.order.OrderDeliverTask;
import net.chinacloud.mediator.task.order.OrderReCreateTask;
import net.chinacloud.mediator.task.order.OrderReDeliverTask;
import net.chinacloud.mediator.task.order.OrderShopAllocationTask;
import net.chinacloud.mediator.task.order.OrderShopHandledTask;
import net.chinacloud.mediator.task.order.QimenEventProduceTask;
import net.chinacloud.mediator.task.product.ProductCategoryGetTask;
import net.chinacloud.mediator.task.product.ProductCreateTask;
import net.chinacloud.mediator.task.product.ProductDeliveryTemplatesGetTask;
import net.chinacloud.mediator.task.product.ProductItemStoreBandingTask;
import net.chinacloud.mediator.task.product.ProductOffShelfWrapperTask;
import net.chinacloud.mediator.task.product.ProductOnShelfWrapperTask;
import net.chinacloud.mediator.task.product.ProductPriceUpdateTask;
import net.chinacloud.mediator.task.product.ProductPropertyGetTask;
import net.chinacloud.mediator.task.product.ProductUpdateTask;
import net.chinacloud.mediator.task.product.inventory.ProductInventoryUpdateWrapperTask;
import net.chinacloud.mediator.task.product.inventory.SkuInventoryUpdateWrapperTask;
import net.chinacloud.mediator.task.system.PauseServiceTask;
import net.chinacloud.mediator.task.system.RecoveryServiceTask;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;
//import net.chinacloud.mediator.vip.vop.constants.JitConstants;
//import net.chinacloud.mediator.vip.vop.domain.ConfirmdeliverMsg;
//import net.chinacloud.mediator.vip.vop.domain.CreatedeliverMsg;
//import net.chinacloud.mediator.vip.vop.domain.ImportDeliverDetailMsg;
//import net.chinacloud.mediator.vip.vop.domain.PickBean;
//import net.chinacloud.mediator.vip.vop.domain.PoList;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public abstract class WCSTaskAdapter implements TaskAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(WCSTaskAdapter.class);
	/**
	 * 订单发货
	 * @return
	 */
	public abstract OrderDeliverTask generateOrderDeliverTask();
	/**
	 * 订单重新发货,更新物流信息
	 * @return
	 */
	public abstract OrderReDeliverTask generateOrderReDeliverTask();
	/**
	 * 订单重新创建
	 * @return
	 */
	public abstract OrderReCreateTask generateOrderReCreateTask();
	/**
	 * 恢复服务
	 */
	public abstract RecoveryServiceTask generateRecoveryServiceTask();
	/**
	 * 暂停服务
	 * @return
	 */
	public abstract PauseServiceTask generatePauseServiceTask();
	/**
	 * sku库存更新
	 * @return
	 */
	public abstract SkuInventoryUpdateWrapperTask generateSkuInventoryUpdateWrapperTask();
	/**
	 * product库存更新
	 * @return
	 */
	public abstract ProductInventoryUpdateWrapperTask generateProductInventoryUpdateWrapperTask();
	/**
	 * 商品创建
	 * @return
	 */
	public abstract ProductCreateTask generateProductCreateTask();
	/**
	 * 商品更新
	 * @return
	 */
	public abstract ProductUpdateTask generateProductUpdateTask();
	/**
	 * 商品上架
	 * @return
	 */
	public abstract ProductOnShelfWrapperTask generateProductOnShelfWrapperTask();
	/**
	 * 商品下架
	 * @return
	 */
	public abstract ProductOffShelfWrapperTask generateProductOffShelfWrapperTask();
	/**
	 * 价格更新
	 * @return
	 */
	public abstract ProductPriceUpdateTask generateProductPriceUpdateTask();
	/**
	 * 运费模板抓取
	 * @return
	 */
	public abstract ProductDeliveryTemplatesGetTask generateProductDeliveryTemplatesGetTask();
	/**
	 * 抓取渠道属性
	 * @return
	 */
	public abstract ProductPropertyGetTask generateProductPropertyGetTask();
	
	/**
	 * 抓取所有类目
	 */
	 public abstract ProductCategoryGetTask generateProductCategoryGetTask();
	 
	 /**
	 *订单全链路 通知门店配货
	 */
	public abstract OrderAllocationNotifiedTask generateOrderAllocationNotifiedTask();

	/**
	 *订单全链路 门店已接单
	 */
	public abstract OrderShopAllocationTask generateOrderShopAllocationTask();

	/**
	 *订单全链路 门店已发货
	 */
	public abstract OrderShopHandledTask generateOrderShopHandledTask();
	
	/**
	 * 全渠道门店商品绑定
	 * @return
	 */
	public abstract ProductItemStoreBandingTask generateProductItemStoreBandingTask();
	/**
	 * * 接入订单全链路监控
	 */
	public abstract QimenEventProduceTask generateQimenEventProduceTask();
	
	
	
	/** ========================换货start=======================*/
	/**卖家发货*/
	public abstract ExchangeDeliverTask generateExchangeDeliverTask();
	/**卖家同意换货申请 */
	public abstract ExchangeAgreeTask generateExchangeAgreeTask();
	/**卖家拒绝换货申请*/
	public abstract ExchangeRefuseTask generateExchangeRefuseTask();
	/**卖家确认收货*/
	public abstract ExchangeReturngoodsAgreeTask generateExchangeReturngoodsAgreeTask();
	/**卖家拒绝确认收货*/
	public abstract ExchangeReturngoodsRefuseTask generateExchangeReturngoodsRefuseTask();
	/** ========================换货end=======================*/

	
	@Autowired
	TaskManager taskManager;
	
	@Override
	public <T> Task generateTask(CommonNotifyPacket<T> packet) {
		T message = packet.getMessage();
		Task task = null;
		if (message instanceof MessageObject) {
			@SuppressWarnings("unchecked")
			MessageObject<String> messageObject = (MessageObject<String>)message;
			String actionCode = messageObject.getActionCode();
	        String content = messageObject.getContent();
	        
	        task = generateTask(actionCode, content);
		} else {
			return null;
		}
		return task;
	}

	public Task generateTask(String actionCode, String content) {
		Task task = null;
		if(MessageActionCode.ACTION_CODE_ORDER_DELIVER_ORDER.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order Deliver Task");
			}
			//发货
			//task = SpringUtil.getBean(OrderDeliverTask.class);
			Order order = JsonUtil.jsonString2Object(content, Order.class);
			task = generateOrderDeliverTask();
			task.setDataId(order.getChannelOrderId());
			task.setData(order);
			return task;
		} else if (MessageActionCode.ACTION_CODE_ORDER_REDELIVER_ORDER.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order ReDeliver Task");
			}
			// 重新发货,更新物流信息
			Order order = JsonUtil.jsonString2Object(content, Order.class);
			task = generateOrderReDeliverTask();
			task.setDataId(order.getChannelOrderId());
			task.setData(order);
			return task;
		} else if (MessageActionCode.ACTION_CODE_ORDER_RECREATE.equals(actionCode)) {
			// 订单重新创建
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Order ReCreate Task");
			}
			Order order = JsonUtil.jsonString2Object(content, Order.class);
			task = generateOrderReCreateTask();
			task.setDataId(order.getChannelOrderId());
			task.setData(order);
			return task;
		} else if(MessageActionCode.ACTION_CODE_UPDATE_INVENTORY_RESUME_ORDER.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Recovery Service Task");
			}
			//开启抓单
			//task = SpringUtil.getBean(RecoveryServiceTask.class);
			task = generateRecoveryServiceTask();
			task.setDataId(String.valueOf(System.nanoTime()));
			//task.getContext().setChannelCode("ALL");
			return task;
		} else if(MessageActionCode.ACTION_CODE_UPDATE_INVENTORY_STOP_ORDER.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Pause Service Task");
			}
			//停止抓单
			//task = SpringUtil.getBean(PauseServiceTask.class);
			task = generatePauseServiceTask();
			task.setDataId(String.valueOf(System.nanoTime()));
			//task.getContext().setChannelCode("ALL");
			return task;
		} else if(MessageActionCode.ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_SKUS.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Sku Inventory Update Wrapper Task");
			}
			//获取EC方的partnumber和增量的库存,更新库存
			//task = SpringUtil.getBean(InventoryTask.class);
			List<Sku> skus = JsonUtil.jsonString2List(content, Sku.class);
			
			task = generateSkuInventoryUpdateWrapperTask();
        	task.setDataId(String.valueOf(System.nanoTime()));
        	
        	task.setData(skus);
        	return task;
		} else if(MessageActionCode.ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_PRODUCTS.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Inventory Update Wrapper Task");
			}
			//批量更新库存
			//task = SpringUtil.getBean(InventoryTask.class);
			List<Product> products = JsonUtil.jsonString2List(content, Product.class);
			task = generateProductInventoryUpdateWrapperTask();
			
			task.setDataId(String.valueOf(System.nanoTime()));
			task.setData(products);
			return task;
		} else if (MessageActionCode.ACTION_CODE_PRODUCT_CREATE.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Create Task");
			}
        	// 商品创建
        	task = generateProductCreateTask();
        	Map<String, Object> product = JsonUtil.jsonString2Map(content);
        	//ProductInfo product = JsonUtil.jsonString2Object(content, ProductInfo.class);
        	task.setDataId(String.valueOf(System.nanoTime()));
        	task.setData(product);
        	return task;
        } else if (MessageActionCode.ACTION_CODE_PRODUCT_UPDATE.equals(actionCode)) {
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Update Task");
			}
        	// 商品更新
        	task = generateProductUpdateTask();
        	Map<String, Object> product = JsonUtil.jsonString2Map(content);
        	//ProductInfo product = JsonUtil.jsonString2Object(content, ProductInfo.class);
        	task.setDataId(String.valueOf(System.nanoTime()));
        	task.setData(product);
        	return task;
        } else if (MessageActionCode.ACTION_CODE_PRODUCT_LIST.equals(actionCode)) {
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product OnShelf Wrapper Task");
			}
        	// 商品上架
        	List<Product> products = JsonUtil.jsonString2List(content, Product.class);
        	task = generateProductOnShelfWrapperTask();
        	task.setDataId(String.valueOf(System.nanoTime()));
        	task.setData(products);
        	return task;
        } else if (MessageActionCode.ACTION_CODE_PRODUCT_DELIST.equals(actionCode)) {
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product OffShelf Wrapper Task");
			}
        	// 商品下架
        	List<Product> products = JsonUtil.jsonString2List(content, Product.class);
        	task = generateProductOffShelfWrapperTask();
        	task.setData(products);
        	task.setDataId(String.valueOf(System.nanoTime()));
        	return task;
        } else if (MessageActionCode.ACTION_CODE_PRODUCT_PRICE_UPDATE.equals(actionCode)) {
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Price Update Task");
			}
        	// 价格更新
        	Product product = JsonUtil.jsonString2Object(content, Product.class);
        	task = generateProductPriceUpdateTask();
        	task.setData(product);
        	if (StringUtils.hasText(product.getOuterProductId())) {
        		task.setDataId(product.getOuterProductId());
        	} else {
        		task.setDataId(String.valueOf(System.nanoTime()));
        	}
        	return task;
        } else if (MessageActionCode.ACTION_CODE_PRODUCT_DELIVERY_TEMPLATES_GET.equals(actionCode)) {
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Delivery Templates Get Task");
			}
        	task = generateProductDeliveryTemplatesGetTask();
        	task.setDataId(String.valueOf(System.nanoTime()));
        	return task;
        } else if (MessageActionCode.ACTION_CODE_PRODUCT_CHANNEL_ATTRIBUTE_GET.equals(actionCode)) {
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Product Properties Get Task");
				Category category = JsonUtil.jsonString2Object(content, Category.class);
				task = generateProductPropertyGetTask();
	        	task.setData(category);
	        	if (StringUtils.hasText(category.getCategoryId())) {
	        		task.setDataId(category.getCategoryId());
	        	} else {
	        		task.setDataId(String.valueOf(System.nanoTime()));
	        	}
	        	return task;
			}
        } else if(MessageActionCode.ACTION_CODE_UPDATE_INVENTORY_STOP_ORDER.equals(actionCode)){
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Pause Service Task");
			}
        	//单渠道停止抓单
        	//停止抓单
			//task = SpringUtil.getBean(PauseServiceTask.class);
			task = generatePauseServiceTask();
			task.setDataId(String.valueOf(System.nanoTime()));
			//task.getContext().setChannelCode("ALL");
			return task;
        } else if(MessageActionCode.ACTION_CODE_UPDATE_INVENTORY_RESUME_ORDER.equals(actionCode)){
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Recovery Service Task");
			}
        	//单渠道开启抓单
        	//task = SpringUtil.getBean(RecoveryServiceTask.class);
        	task = generateRecoveryServiceTask();
			task.setDataId(String.valueOf(System.nanoTime()));
			//task.getContext().setChannelCode("ALL");
			return task;
        } else if(MessageActionCode.ACTION_CODE_PRODUCT_CATEGORY_GET.equals(actionCode)){
        	if (LOGGER.isDebugEnabled()) {
        		LOGGER.debug("generate Product Category Get Task");
	        	task = generateProductCategoryGetTask();
	        	task.setDataId(String.valueOf(System.nanoTime()));
	        	return task;
        	}
        } else if(MessageActionCode.ACTION_CODE_REFUND_SENDGOODCANCEL.equals(actionCode)){
        	Refund refund = JsonUtil.jsonString2Object(content, Refund.class);
        	CommonNotifyPacket<Refund> packet = new CommonNotifyPacket<Refund>(refund);
        	packet.setType("SendGoodCancel");
        	task = taskManager.generateTask("TMALL", packet);
        } else if(MessageActionCode.ACTION_CODE_REFUND_WAREHOUSEUPDATE.equals(actionCode)){
        	Refund refund = JsonUtil.jsonString2Object(content, Refund.class);
        	CommonNotifyPacket<Refund> packet = new CommonNotifyPacket<Refund>(refund);
        	packet.setType("WarehouseUpdate");
        	task = taskManager.generateTask("TMALL", packet);
        }else if (MessageActionCode.ACTION_CODE_ORDER_ALLOCATION_NOTIFIED.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate order allocation norified");
			}
			task = generateOrderAllocationNotifiedTask();
			task.setData(content);
			task.setDataId(String.valueOf(System.nanoTime()));
			return task;
		} else if (MessageActionCode.ACTION_CODE_ORDER_SHOP_ALLOCATION.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate order shop allcation");
			}
			task = generateOrderShopAllocationTask();
			task.setData(content);
			task.setDataId(String.valueOf(System.nanoTime()));
			return task;
		} else if (MessageActionCode.ACTION_CODE_ORDER_SHOP_HANDLED.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate order shop handled norified");
			}
			task = generateOrderShopHandledTask();
			task.setData(content);
			task.setDataId(String.valueOf(System.nanoTime()));
			return task;
		} else if (MessageActionCode.ACTION_CODE_PRODUCT_ITEMSTORE_BANDING.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate ItemStore Binding handled norified");
			}
			Product product = JsonUtil.jsonString2Object(content, Product.class);
			task = generateProductItemStoreBandingTask();
			task.setData(product);
			task.setDataId(product.getOuterProductId());
			return task;
		}
//		else if(MessageActionCode.ACTION_CODE_CREATE_PICK.equals(actionCode)){
//        	//wcs通知mediator创建Pick
//        	PoList list = JsonUtil.jsonString2Object(content, PoList.class);
//        	CommonNotifyPacket<PoList> packet = new CommonNotifyPacket<PoList>(list);
//        	packet.setType(JitConstants.STATUS_BATCH_CREATE_PICK);
//        	task = taskManager.generateTask(JitConstants.CHANNEL_VIP, packet);
//        	return task;
//        }else if(MessageActionCode.ACTION_CODE_JIT_CREATEDELIVER.equals(actionCode)){
//        	//wc通知jit，create_deliver
//        	CreatedeliverMsg msg = JsonUtil.jsonString2Object(content, CreatedeliverMsg.class);
//        	CommonNotifyPacket<CreatedeliverMsg> packet = new CommonNotifyPacket<CreatedeliverMsg>(msg);
//    		packet.setType("STATUS_CREATE_DELIVER");
//        	task = taskManager.generateTask(JitConstants.CHANNEL_VIP, packet);
//        	return task;
//        }else if(MessageActionCode.ACTION_CODE_IMPORTDELIVER_DETAIL.equals(actionCode)){
//        	//wms实际发货import_deliver_detail
//        	ImportDeliverDetailMsg msg  = JsonUtil.jsonString2Object(content, ImportDeliverDetailMsg.class);
//        	CommonNotifyPacket<ImportDeliverDetailMsg> packet = new CommonNotifyPacket<ImportDeliverDetailMsg>(msg);
//    		packet.setType("STATUS_IMPORT_DELIVER_DETAIL");
//        	task = taskManager.generateTask(JitConstants.CHANNEL_VIP, packet);
//        	return task;
//        }else if(MessageActionCode.ACTION_CODE_CONFIRMDELIVER_JIT.equals(actionCode)){
//        	//确认出仓
//        	ConfirmdeliverMsg msg = JsonUtil.jsonString2Object(content, ConfirmdeliverMsg.class);
//        	CommonNotifyPacket<ConfirmdeliverMsg> packet = new CommonNotifyPacket<ConfirmdeliverMsg>(msg);
//    		packet.setType("STATUS_CONFIRMDELIVER_JIT");
//        	task = taskManager.generateTask(JitConstants.CHANNEL_VIP, packet);
//        	return task;
//        }else if (MessageActionCode.ACTION_CODE_GET_INVENTORYDEDUCTORDERS.equals(actionCode)) {
//			LOGGER.info("generate getInventoryDeductOrders task");
//			PickBean pickBean = JsonUtil.jsonString2Object(content,PickBean.class);
//			CommonNotifyPacket<PickBean> packet = new CommonNotifyPacket<PickBean>(pickBean);
//			packet.setType(JitConstants.GET_INVENTORYDEDUCTORDERS);
//			task = taskManager.generateTask(JitConstants.CHANNEL_VIP, packet);
//			return task;
//		}
		else if (MessageActionCode.ACTION_CODE_EXCHANGE_CONSIGNGOODS.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Exchange Deliver Task" );
			}
			//确认收货之后，卖家发货
			Exchange exchange = JsonUtil.jsonString2Object(content, Exchange.class);
			task = generateExchangeDeliverTask();
			task.setDataId(exchange.getDisputeId());
			task.setData(exchange);
			return task;
		}else if (MessageActionCode.ACTION_CODE_EXCHANGE_AGREE.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Exchange Agree Task" );
			}
			//买家申请换货，卖家同意申请换货
			Exchange exchange = JsonUtil.jsonString2Object(content,Exchange.class);
			task = generateExchangeAgreeTask();
			task.setDataId(exchange.getDisputeId());
			task.setData(exchange);
			return task;
		}else if (MessageActionCode.ACTION_CODE_EXCHANGE_REFUSE.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Exchange refuse Task" );
			}
			//买家申请换货，卖家拒绝申请换货
			Exchange refuseExchange = JsonUtil.jsonString2Object(content, Exchange.class);
			task = generateExchangeRefuseTask();
			task.setDataId(refuseExchange.getDisputeId());
			task.setData(refuseExchange);
			return task;
		}else if (MessageActionCode.ACTION_CODE_EXCHANGE_RETURNGOODS_AGREE.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Exchange agree returngoods Task" );
			}
			//卖家确认收货
			Exchange exchange = JsonUtil.jsonString2Object(content, Exchange.class);
			task = generateExchangeReturngoodsAgreeTask();
			task.setDataId(exchange.getDisputeId());
			task.setData(exchange);
			return task;
		}else if (MessageActionCode.ACTION_CODE_EXCHANGE_RETURNGOODS_REFUSE.equals(actionCode)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("generate Exchange refuse returngoods Task" );
			}
			//卖家拒绝确认收货
			Exchange refuseExchange = JsonUtil.jsonString2Object(content, Exchange.class);
			task = generateExchangeReturngoodsRefuseTask();
			task.setDataId(refuseExchange.getDisputeId());
			task.setData(refuseExchange);
			return task;
		}else if (MessageActionCode.ACTION_CODE_RESPONSEORDERSTORE.equals(actionCode)) {
			VopStoreMessage msg  = JsonUtil.jsonString2Object(content, VopStoreMessage.class);
        	CommonNotifyPacket<VopStoreMessage> packet = new CommonNotifyPacket<VopStoreMessage>(msg);
    		packet.setType("ResponseOrderStore");
        	task = taskManager.generateTask("VIP", packet);
        	return task;
		}else if (MessageActionCode.ACTION_CODE_CONFIRMSTOREDELIVERY.equals(actionCode)) {
			VopStoreMessage msg  = JsonUtil.jsonString2Object(content, VopStoreMessage.class);
        	CommonNotifyPacket<VopStoreMessage> packet = new CommonNotifyPacket<VopStoreMessage>(msg);
    		packet.setType("ConfirmStoreDelivery");
        	task = taskManager.generateTask("VIP", packet);
        	return task;
		}else if (MessageActionCode.ACTION_CODE_CONFIRMREFUSERESULT.equals(actionCode)) {
			VopReturnMessage msg  = JsonUtil.jsonString2Object(content, VopReturnMessage.class);
        	CommonNotifyPacket<VopReturnMessage> packet = new CommonNotifyPacket<VopReturnMessage>(msg);
    		packet.setType("ConfirmRefuseResult");
        	task = taskManager.generateTask("VIP", packet);
        	return task;
		}else if (MessageActionCode.ACTION_CODE_CONFIRMRETURNRESULT.equals(actionCode)) {
			VopReturnMessage msg  = JsonUtil.jsonString2Object(content, VopReturnMessage.class);
        	CommonNotifyPacket<VopReturnMessage> packet = new CommonNotifyPacket<VopReturnMessage>(msg);
    		packet.setType("ConfirmReturnResult");
        	task = taskManager.generateTask("VIP", packet);
        	return task;
		}else if(MessageActionCode.ACTION_CODE_PRODUCT_STOREINVENTORY_ITEMINITIAL.equals(actionCode)){
			StoreItems storeItems = JsonUtil.jsonString2Object(content, StoreItems.class);
        	CommonNotifyPacket<StoreItems> packet = new CommonNotifyPacket<StoreItems>(storeItems);
        	packet.setType("productStoreInventoryIteminitial");
        	task = taskManager.generateTask("TMALL", packet);
        }else if(MessageActionCode.ACTION_CODE_ORDER_QIMEN_EVENT_PRODUCE.equals(actionCode)){
        	QimenEventProduce qimenEventProduce = JsonUtil.jsonString2Object(content, QimenEventProduce.class);
        	task = generateQimenEventProduceTask();
			task.setDataId(qimenEventProduce.getChannelOrderId());
			task.setData(qimenEventProduce);
        }else if(MessageActionCode.ACTION_CODE_ORDER_ALIGENIUS_MSG_UPDATE.equals(actionCode)){
			Order order = JsonUtil.jsonString2Object(content, Order.class);
			CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
        	packet.setType("responseMsgOrderUpdate");
        	task = taskManager.generateTask("TMALL", packet);
        }
		return task;
	}
	
}
