package net.chinacloud.mediator.translator;



import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.domain.VopOrderReturnList;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.exception.returns.ReturnException;
import net.chinacloud.mediator.jms.JMSQueueProducer;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.utils.JsonUtil;

/**
 * 
 * @description 退货facade client
 * @author yejunwu123@gmail.com
 * @since 2015年7月8日 下午6:06:34
 */
public class ChannelReturnFacadeClient {

	//jms message sender
	private JMSQueueProducer queueProducer;
	

	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	public ChannelReturnFacadeClient() {
		super();
	}

	/**
	 * Create a Return
	 * 暂时支持一号店
	 * @param parameters
	 * @return
	 * @throws ReturnException
	 */
	public void submitChannelReturn(Return returns, TaskContext context) throws MessageSendException {
		this.queueProducer.send(JsonUtil.object2JsonString(returns));
	}
	
	
	public void sendVopOrderReturn(VopOrderReturnList returns, TaskContext context, Long taskId) throws MessageSendException{
		MessageObject<VopOrderReturnList> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_GETORDERRETURNLIST, returns, context, taskId);
		send(messageObject);
	}
	
	
	/**
	 * 构建消息对象
	 * @param order 数据对象
	 * @param context task上下文
	 * @return
	 */
	private MessageObject<VopOrderReturnList> buildMessageObject(String actionCode, VopOrderReturnList returns, TaskContext context, Long taskId) {
		MessageObject<VopOrderReturnList> messageObject = 
				new MessageObject<VopOrderReturnList>(actionCode, returns, context.getStoreId(), taskId);
		return messageObject;
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 */
	private void send(MessageObject<VopOrderReturnList> messageObject) throws MessageSendException {
		String message = JsonUtil.object2JsonString(messageObject);
		try {
			this.queueProducer.send(message);
		} catch (Exception e) {
			throw new MessageSendException(e);
		}
	}
	
	
}