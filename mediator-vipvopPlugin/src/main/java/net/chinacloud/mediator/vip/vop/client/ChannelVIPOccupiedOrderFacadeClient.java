package net.chinacloud.mediator.vip.vop.client;

import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.jms.JMSQueueProducer;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.domain.OccupiedOrderList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelVIPOccupiedOrderFacadeClient {
	
	private static final String CLASSNAME = ChannelVIPOccupiedOrderFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelVIPOccupiedOrderFacadeClient.class);
	
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	public ChannelVIPOccupiedOrderFacadeClient(){
		super();
	}

	/**
	 * 构建消息对象
	 * @param order 数据对象
	 * @param context task上下文
	 * @return
	 */
	private MessageObject<OccupiedOrderList> buildMessageObject(String actionCode, OccupiedOrderList orders, TaskContext context, Long taskId) {
		MessageObject<OccupiedOrderList> messageObject = 
				new MessageObject<OccupiedOrderList>(actionCode, orders, context.getStoreId(), taskId);
		return messageObject;
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void send(MessageObject<OccupiedOrderList> messageObject) throws MessageSendException {
		String message = JsonUtil.object2JsonString(messageObject);
		try {
			this.queueProducer.send(message);
		} catch (Exception e) {
			LOGGER.error("订单相关jms消息发送失败", e);
			throw new MessageSendException(e);
		}
	}
	
	/**
	 * 发送实时订单
	 */
	public void postOccupiedOrder(OccupiedOrderList orders, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "GetOccupiedOrder";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		
		MessageObject<OccupiedOrderList> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_GET_INVENTORYOCCUPIEDORDERS, orders, context, taskId);
		send(messageObject);
	}
	
	
}
