package net.chinacloud.mediator.translator;

import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.jms.JMSQueueProducer;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.pojo.MessageResponseObject;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelExchangeFacadeClient {
	
	private static final String CLASSNAME = ChannelExchangeFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelExchangeFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	/**
	 * Constructor
	 */
	public ChannelExchangeFacadeClient(){
		super();
	}
	
	/**
	 * 发送换货信息
	 * @param parameters
	 * @throws Exception
	 */
	public void submitChannelExchange(Exchange exchange, TaskContext context, Long taskId) throws MessageSendException {
		final String METHODNAME= "submitChannelExchange";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Exchange> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_EXCHANGE_INFO, exchange, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
		
	}
	
	
	/**
	 * 换货发货失败回传
	 */
	public void deliverResponse(Exchange exchange, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		final String METHODNAME= "submitOMSExchangedeliverResponse";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		MessageResponseObject<Exchange> messageObject = new MessageResponseObject<Exchange>(
						MessageActionCode.ACTION_CODE_EXCHANGE_CONSIGNGOODS, 
						exchange, 
						context.getStoreId(), 
						taskId, 
						success, 
						errorMessage);
		send(messageObject);
	}
	/**
	 * 卖家同意换货申请失败回传
	 */
	public void agreeResponse(Exchange exchange, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		final String METHODNAME= "submitOMSExchangeagreeResponse";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		MessageResponseObject<Exchange> messageObject = new MessageResponseObject<Exchange>(
						MessageActionCode.ACTION_CODE_EXCHANGE_AGREE, 
						exchange, 
						context.getStoreId(), 
						taskId, 
						success, 
						errorMessage);
		send(messageObject);
	}

	/**
	 * 卖家拒绝换货申请失败回传
	 */
	public void refuseResponse(Exchange exchange, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		final String METHODNAME= "submitOMSExchangerefuseResponse";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		MessageResponseObject<Exchange> messageObject = new MessageResponseObject<Exchange>(
						MessageActionCode.ACTION_CODE_EXCHANGE_REFUSE, 
						exchange, 
						context.getStoreId(), 
						taskId, 
						success, 
						errorMessage);
		send(messageObject);
	}
	
	
	
	/**
	 * 卖家确认收货失败回传
	 */
	public void agreeExchangeReturngoodsResponse(Exchange exchange, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		final String METHODNAME= "submitOMSagreeExchangeReturngoods";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		MessageResponseObject<Exchange> messageObject = new MessageResponseObject<Exchange>(
						MessageActionCode.ACTION_CODE_EXCHANGE_RETURNGOODS_AGREE, 
						exchange, 
						context.getStoreId(), 
						taskId, 
						success, 
						errorMessage);
		send(messageObject);
	}
	
	/**
	 * 卖家拒绝确认收货失败回传
	 */
	public void refuseExchangeReturngoodsResponse(Exchange exchange, TaskContext context, Long taskId, boolean success, String errorMessage) throws MessageSendException {
		final String METHODNAME= "submitOMSrefuseExchangeReturngoods";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		MessageResponseObject<Exchange> messageObject = new MessageResponseObject<Exchange>(
						MessageActionCode.ACTION_CODE_EXCHANGE_RETURNGOODS_REFUSE, 
						exchange, 
						context.getStoreId(), 
						taskId, 
						success, 
						errorMessage);
		send(messageObject);
	}
	/**
	 * 构建消息对象
	 * @param order 数据对象
	 * @param context task上下文
	 * @return
	 */
	private MessageObject<Exchange> buildMessageObject(String actionCode, Exchange exchange, TaskContext context, Long taskId) {
		MessageObject<Exchange> messageObject = 
				new MessageObject<Exchange>(actionCode, exchange, context.getStoreId(), taskId);
		return messageObject;
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void send(MessageObject<Exchange> messageObject) throws MessageSendException {
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
	

}
