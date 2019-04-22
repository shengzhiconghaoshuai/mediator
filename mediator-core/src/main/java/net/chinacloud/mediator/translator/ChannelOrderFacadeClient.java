/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelOrderFacadeClient.java
 * 描述： 
 */
package net.chinacloud.mediator.translator;

import net.chinacloud.mediator.domain.Order;
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
 * 
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月2日 下午6:07:00
 */
public class ChannelOrderFacadeClient {
	
	private static final String CLASSNAME = ChannelOrderFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelOrderFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	/**
	 * Constructor
	 */
	public ChannelOrderFacadeClient(){
		super();
	}
	
	/**
	 * 订单创建
	 * @param parameters
	 * @throws Exception
	 */
	public void submitChannelOrder(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "submitChannelOrder";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_CREATE, order, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	public void submitChannelSyncOrder(Order order, TaskContext context, Long taskId) throws Exception {
		final String METHODNAME= "submitChannelSyncOrder";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_CREATE, order, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 构建消息对象
	 * @param order 数据对象
	 * @param context task上下文
	 * @return
	 */
	private MessageObject<Order> buildMessageObject(String actionCode, Order order, TaskContext context, Long taskId) {
		MessageObject<Order> messageObject = 
				new MessageObject<Order>(actionCode, order, context.getStoreId(), taskId);
		return messageObject;
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void send(MessageObject<Order> messageObject) throws MessageSendException {
		String message = JsonUtil.object2JsonString(messageObject);
		//LOGGER.info("send order message:" + message);
		try {
			this.queueProducer.send(message);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error("订单相关jms消息发送失败", e);
			throw new MessageSendException(e);
		}
	}
	
	/**
	 * 征集预售
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void payForZhengji(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "payForZhengji";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_PAY_FOR_ZHENGJI, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 预售订单支付尾款
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void payForStepTrade(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "payForStepTrade";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_PAY_FOR_STEPTRADE, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 取消预售订单
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void cancelStepTrade(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "cancelStepTrade";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_CANCEL_STEPTRADE, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 订单成功
	 * @param order
	 * @throws Exception
	 */
	public void receive(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "receive";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_SUCCESS, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 订单部分成功(子订单成功)
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void partlyReceive(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "partlyReceive";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_PARTLY_SUCCESS, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 部分发货
	 * @param order
	 * @param context
	 * @param taskId
	 * @throws MessageSendException
	 */
	public void partlyShip(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "partlyShip";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_PARTLY_SHIP, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 卖家备注修改
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void saveSellerMemo(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "saveSellerMemo";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuffer().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_UPDATE_SELLER_MEMO, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 订单取消
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void cancelOrder(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME = "cancelOrder(Order, TaskContext)";
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_CANCEL, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 修改收货地址
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void updateOrderAddress(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME = "updateOrder(Order, TaskContext)";
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_ORDER_UPDATE_RECEIVE_ADDRESS, order, context, taskId);
		send(messageObject);
	}
	
	/**
	 * 订单发货失败回传
	 */
	public void deliverResponse(Order order, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		MessageResponseObject<Order> messageObject = new MessageResponseObject<Order>(
						MessageActionCode.ACTION_CODE_ORDER_DELIVER_ORDER, 
						order, 
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
	 * 订单重抓回传
	 */
	public void reCreateResponse(Order order, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		MessageResponseObject<Order> messageObject = new MessageResponseObject<Order>(
						MessageActionCode.ACTION_CODE_ORDER_RECREATE, 
						order, 
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
	 * 订单物流更新失败回传
	 */
	public void reDeliverResponse(Order order, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		MessageResponseObject<Order> messageObject = new MessageResponseObject<Order>(
				MessageActionCode.ACTION_CODE_ORDER_REDELIVER_ORDER, 
				order, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		/*if (StringUtils.hasLength(errorMessage)) {
			messageObject.addErrors(errorMessage);
		}*/
		send(messageObject);
	}
}
