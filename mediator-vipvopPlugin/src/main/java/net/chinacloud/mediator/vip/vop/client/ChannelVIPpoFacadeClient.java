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
import net.chinacloud.mediator.vip.vop.domain.PoBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelVIPpoFacadeClient {
	
	private static final String CLASSNAME = ChannelVIPpoFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelVIPpoFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	/**
	 * Constructor
	 */
	public ChannelVIPpoFacadeClient(){
		super();
	}
	
	
	/**
	 * 构建消息对象
	 * @param order 数据对象
	 * @param context task上下文
	 * @return
	 */
	private MessageObject<PoBean> buildMessageObject(String actionCode, PoBean poBean, TaskContext context, Long taskId) {
		MessageObject<PoBean> messageObject = 
				new MessageObject<PoBean>(actionCode, poBean, context.getStoreId(), taskId);
		return messageObject;
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void send(MessageObject<PoBean> messageObject) throws MessageSendException {
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
	 * po单
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void postPo(PoBean poBean, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "CreatePo";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<PoBean> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_CREATE_PO, poBean, context, taskId);
		send(messageObject);
	}
	
}
