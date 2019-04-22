/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelOrderFacadeClient.java
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.client;

import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.jms.JMSQueueProducer;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.domain.JITDeliveryBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelVIPdeliverFacadeClient {
	
	private static final String CLASSNAME = ChannelVIPdeliverFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelVIPdeliverFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	/**
	 * Constructor
	 */
	public ChannelVIPdeliverFacadeClient(){
		super();
	}
	
	
	/**
	 * 构建消息对象
	 * @param order 数据对象
	 * @param context task上下文
	 * @return
	 */
	private MessageObject<JITDeliveryBean> buildMessageObject(String actionCode, JITDeliveryBean jitDeliveryBean, TaskContext context, Long taskId) {
		MessageObject<JITDeliveryBean> messageObject = 
				new MessageObject<JITDeliveryBean>(actionCode, jitDeliveryBean, context.getStoreId(), taskId);
		return messageObject;
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void send(MessageObject<JITDeliveryBean> messageObject) throws MessageSendException {
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
	 *vip创建发货单
	 */
	public void postCreateDeliver(JITDeliveryBean jitDeliveryBean, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "CreateDeliver";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<JITDeliveryBean> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_JIT_CREATEDELIVER, jitDeliveryBean, context, taskId);
		send(messageObject);
	}
	
}
