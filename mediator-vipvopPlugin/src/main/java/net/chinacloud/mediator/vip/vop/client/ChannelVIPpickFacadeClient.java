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
import net.chinacloud.mediator.vip.vop.domain.PickBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelVIPpickFacadeClient {
	
	private static final String CLASSNAME = ChannelVIPpickFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelVIPpickFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	/**
	 * Constructor
	 */
	public ChannelVIPpickFacadeClient(){
		super();
	}
	
	
	/**
	 * 构建消息对象
	 * @param order 数据对象
	 * @param context task上下文
	 * @return
	 */
	private MessageObject<PickBean> buildMessageObject(String actionCode, PickBean pickBean, TaskContext context, Long taskId) {
		MessageObject<PickBean> messageObject = 
				new MessageObject<PickBean>(actionCode, pickBean, context.getStoreId(), taskId);
		return messageObject;
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void send(MessageObject<PickBean> messageObject) throws MessageSendException {
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
	 * vip创建拣货单返回明细
	 * @param order
	 * @param context
	 * @throws Exception
	 */
	public void postPick(PickBean pickBean, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "batchCreatePick";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<PickBean> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_CREATE_PICK, pickBean, context, taskId);
		send(messageObject);
	}
	
}
