package net.chinacloud.mediator.vip.vop.client;

import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.jms.JMSQueueProducer;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.pojo.MessageResponseObject;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.domain.ImportDeliverDetailMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelVIPimportdeliverFacadeClient {
	
	private static final String CLASSNAME = ChannelVIPimportdeliverFacadeClient.class.getName();
	
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelVIPimportdeliverFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	/**
	 * Constructor
	 */
	public ChannelVIPimportdeliverFacadeClient(){
		super();
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void send(MessageObject<ImportDeliverDetailMsg> messageObject) throws MessageSendException {
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
	public void postImportDeliver(ImportDeliverDetailMsg importDeliverDetailMsg, TaskContext context, Long taskId , boolean success, String errorMessage) throws MessageSendException {
		final String METHODNAME= "importDeliverDetailMsg";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).toString());
		}
		MessageResponseObject<ImportDeliverDetailMsg> messageObject = new MessageResponseObject<ImportDeliverDetailMsg>(
				MessageActionCode.ACTION_CODE_IMPORTDELIVER_SUCCESS, 
				importDeliverDetailMsg, 
				context.getStoreId(), 
				taskId, 
				success, 
				errorMessage);
		send(messageObject);
	}
	
}
