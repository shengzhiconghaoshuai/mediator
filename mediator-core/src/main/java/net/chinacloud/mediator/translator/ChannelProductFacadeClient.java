/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelProductFacadeClient.java
 * 描述： 
 */
package net.chinacloud.mediator.translator;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.jms.JMSQueueProducer;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.pojo.MessageResponseObject;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 商品facadeclient
 * @author yejunwu123@gmail.com
 * @since 2015年7月4日 上午11:20:39
 */
public class ChannelProductFacadeClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelProductFacadeClient.class);
	
	private static final String CLASSNAME = "ChannelProductFacadeClient";
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}
	
	/**
	 * 渠道商品下架
	 * @param product
	 * @param context
	 */
	public void downShelf(Product product, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME = "downShelf";
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<Product> messageObject = new MessageObject<Product>(MessageActionCode.ACTION_CODE_PRODUCT_CHANNEL_DELIST, product, context.getStoreId(), taskId);

		send(messageObject);
	}
	/**
	 * 渠道商品上架架
	 * @param product
	 * @param context
	 */
	public void UpShelf(Product product, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME = "onShelf";
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<Product> messageObject = new MessageObject<Product>(MessageActionCode.ACTION_CODE_PRODUCT_CHANNEL_LIST, product, context.getStoreId(), taskId);

		send(messageObject);
	}
	/**
	 * 发送渠道商品属性
	 * @param deliveryTemplates
	 */
	public void sendChannelPropertyResponse(Category category, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		MessageResponseObject<Category> messageObject = new MessageResponseObject<Category>(
				MessageActionCode.ACTION_CODE_PRODUCT_CHANNEL_ATTRIBUTE_GET, 
				category, 
				context.getStoreId(), 
				taskId,
				success,
				errorMessage);
		
		send(messageObject);
	}
	
	/**
	 * 发送渠道类目
	 * @param deliveryTemplates
	 */
	public void sendChannelCategoryResponse(List<CategoryList> categoryList, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		MessageResponseObject<List<CategoryList>> messageObject = new MessageResponseObject<List<CategoryList>>(
				MessageActionCode.ACTION_CODE_PRODUCT_CATEGORY_GET, 
				categoryList, 
				context.getStoreId(), 
				taskId,
				success,
				errorMessage);
		
		send(messageObject);
	}
	
	/**
	 * 运费模板抓取回应
	 * @param deliveryTemplates
	 * @param context
	 * @param taskId
	 * @param success
	 * @param errorMessage
	 * @throws MessageSendException
	 */
	public void sendDeliveryTemplatesResponse(List<DeliveryTemplate> deliveryTemplates, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		MessageResponseObject<List<DeliveryTemplate>> messageObject = new MessageResponseObject<List<DeliveryTemplate>>(
				MessageActionCode.ACTION_CODE_PRODUCT_DELIVERY_TEMPLATES_GET, 
				deliveryTemplates, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		
		send(messageObject);
	}
	
	/**
	 * 商品创建失败回传
	 * @param product
	 * @param context
	 */
	public void createResponse(Map<String,Object> product, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		MessageResponseObject<Map<String,Object>> messageObject = new MessageResponseObject<Map<String,Object>>(
				MessageActionCode.ACTION_CODE_PRODUCT_CREATE, 
				product, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		
		send(messageObject);
	}
	
	/**
	 * 商品创建失败回传
	 * @param product
	 * @param context
	 */
	public void updateResponse(Map<String,Object> product, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		MessageResponseObject<Map<String,Object>> messageObject = new MessageResponseObject<Map<String,Object>>(
				MessageActionCode.ACTION_CODE_PRODUCT_UPDATE, 
				product, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		
		send(messageObject);
	}
	
	/**
	 * 商品库存更新失败回传
	 * @param product
	 * @param context
	 * @param errorMessage
	 */
	public void productInventoryResponse(Product product, TaskContext context, Long taskId, boolean success, String errorMessage)
		throws MessageSendException {
		MessageResponseObject<Product> messageObject = new MessageResponseObject<Product>(
				MessageActionCode.ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_PRODUCTS, 
				product, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		
		send(messageObject);
	}
	
	/**
	 * sku库存更新失败回传
	 * @param sku
	 * @param context
	 * @param errorMessage
	 */
	public void skuInventoryResponse(Sku sku, TaskContext context, Long taskId, boolean success, String errorMessage)
		throws MessageSendException {
		MessageResponseObject<Sku> messageObject = new MessageResponseObject<Sku>(
				MessageActionCode.ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_SKUS, 
				sku, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		
		send(messageObject);
	}
	
	/**
	 * 商品下架失败回传
	 * @param product
	 * @param context
	 * @param errorMessage
	 */
	public void offShelfResponse(Product product, TaskContext context, Long taskId, boolean success, String errorMessage)
		throws MessageSendException {
		MessageResponseObject<Product> messageObject = new MessageResponseObject<Product>(
				MessageActionCode.ACTION_CODE_PRODUCT_DELIST, 
				product, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		
		send(messageObject);
	}
	
	/**
	 * 商品上架失败回传
	 * @param product
	 * @param context
	 * @param errorMessage
	 */
	public void onShelfResponse(Product product, TaskContext context, Long taskId, boolean success, String errorMessage)
		throws MessageSendException {
		MessageResponseObject<Product> messageObject = new MessageResponseObject<Product>(
				MessageActionCode.ACTION_CODE_PRODUCT_LIST, 
				product, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		
		send(messageObject);
	}
	
	/**
	 * 价格更新失败回传
	 * @param product
	 * @param context
	 * @param errorMessage
	 */
	public void priceUpdatefResponse(Product product, TaskContext context, Long taskId, boolean success, String errorMessage)
		throws MessageSendException {
		MessageResponseObject<Product> messageObject = new MessageResponseObject<Product>(
				MessageActionCode.ACTION_CODE_PRODUCT_PRICE_UPDATE, 
				product, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		
		send(messageObject);
	}
	
	/**
	 * 商品映射消息发送
	 * @param productInfo
	 * @param context
	 * @param taskId
	 * @param success
	 * @param errorMessage
	 * @throws MessageSendException
	 */
	public void mappingResponse(Product productInfo, TaskContext context, Long taskId, boolean success, String errorMessage) 
			throws MessageSendException {
		MessageResponseObject<Product> messageObject = new MessageResponseObject<Product>(
				MessageActionCode.ACTION_CODE_PRODUCT_MAPPING,
				productInfo,
				context.getStoreId(),
				taskId,
				success,
				errorMessage);
		send(messageObject);
	}
	
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private <T> void send(MessageResponseObject<T> messageObject) throws MessageSendException {
		String message = JsonUtil.object2JsonString(messageObject);
		//LOGGER.info("send order message:" + message);
		try {
			this.queueProducer.send(message);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error("商品相关jms消息发送失败", e);
			throw new MessageSendException(e);
		}
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private <T> void send(MessageObject<T> messageObject) throws MessageSendException {
		String message = JsonUtil.object2JsonString(messageObject);
		//LOGGER.info("send order message:" + message);
		try {
			this.queueProducer.send(message);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error("商品相关jms消息发送失败", e);
			throw new MessageSendException(e);
		}
	}
}
