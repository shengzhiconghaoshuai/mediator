/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelOrderFacadeClient.java
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.client;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.jms.JMSQueueProducer;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description 
 */
public class ChannelOXOOrderFacadeClient {
	
	private static final String CLASSNAME = ChannelOXOOrderFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelOXOOrderFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	/**
	 * Constructor
	 */
	public ChannelOXOOrderFacadeClient(){
		super();
	}
	
	/**
	 * 更新订单金额
	 * @param parameters
	 * @throws Exception
	 */
	public void submitChannelUpdateOrderPrice(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "submitOXOChannelOrder(UpdateOrderFinancial)";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_UPDATEORDERFINANCIAL, order, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 更新订单物流单号
	 * @param parameters
	 * @throws Exception
	 */
	public void submitChannelLogisticsTranspostNo(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "submitOXOChannelOrder(GetLogisticsTranspostNo)";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_GETLOGISTICSTRANSPORTNO, order, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
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
	
}
