package net.chinacloud.mediator.taobao.client;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.jms.JMSQueueProducer;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelTaoBaoAndTmallFacadeClient {

private static final String CLASSNAME = ChannelTaoBaoAndTmallFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelTaoBaoAndTmallFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	public ChannelTaoBaoAndTmallFacadeClient() {
		super();
	}

	/**
	 * 下发预约退款消息
	 * @param parameters
	 * @throws Exception
	 */
	public void submitChannelSendToHoldOrder(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "submitTaoBaoandTmallChannelOrder(msgSendToHoldOrder)";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObjectOrder(MessageActionCode.ACTION_CODE_ORDER_SEND_HOLD_ORDER, order, context, taskId);
		sendOrder(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 下发订单状态更新，用于门店自提订单
	 * @param parameters
	 * @throws Exception
	 */
	public void submitChannelSendToStatusUpdateOrder(Order order, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "submitTaoBaoandTmallChannelOrder(SendToStatusUpdateOrder)";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Order> messageObject = buildMessageObjectOrder(MessageActionCode.ACTION_CODE_ORDER_STATUS, order, context, taskId);
		sendOrder(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void sendOrder(MessageObject<Order> messageObject) throws MessageSendException {
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
	private MessageObject<Order> buildMessageObjectOrder(String actionCode, Order order, TaskContext context, Long taskId) {
		MessageObject<Order> messageObject = 
				new MessageObject<Order>(actionCode, order, context.getStoreId(), taskId);
		return messageObject;
	}
	
}
