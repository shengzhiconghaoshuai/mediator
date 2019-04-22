package net.chinacloud.mediator.translator;

import net.chinacloud.mediator.domain.Refund;
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
 * @description 退款facade client
 * @author yejunwu123@gmail.com
 * @since 2015年7月7日 下午7:37:49
 */
public class ChannelRefundFacadeClient {
	
	private static final String CLASSNAME = "ChannelRefundFacadeClient";
	protected static Logger LOGGER = LoggerFactory.getLogger(ChannelRefundFacadeClient.class);
	
	//jms message sender
	private JMSQueueProducer queueProducer;
	
	public void setQueueProducer(JMSQueueProducer queueProducer) {
		this.queueProducer = queueProducer;
	}

	/**
	 * 退款创建
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void submitChannelRefund(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		
		final String METHODNAME= "submitChannelRefund";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_CREATE, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 退款关闭
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void closeRefund(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		final String METHODNAME= "closeRefund";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_CLOSE, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 退款成功
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void successRefund(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		final String METHODNAME= "successRefund";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_SUCCESS, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 退款更新
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void updateRefund(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		final String METHODNAME= "updateRefund";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_UPDATE, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 卖家拒绝退款
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void sellerRefuse(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		final String METHODNAME= "sellerRefuse";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_SELLER_REFUSE, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 卖家拒绝退款
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void sellerAgree(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		final String METHODNAME= "sellerAgree";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_SELLER_AGREE, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	/**
	 * 买家退货给卖家
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void buyerReturnGoods(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		final String METHODNAME= "buyerReturnGoods";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_BUYER_RETURN_GOODS, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	
	
	/**
	 * 整单退款创建
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void createWholeRefund(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		
		final String METHODNAME= "createWholeRefund";
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_WHOLE_CREATE, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	/**
	 * 退款成功
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void successWholeRefund(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		final String METHODNAME= "successWholeRefund";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_WHOLE_SUCCESS, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	/**
	 * 整单退款关闭
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	public void closeWholeRefund(Refund refund, TaskContext context, Long taskId) throws MessageSendException{
		final String METHODNAME= "closeWholeRefund";
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" start").toString());
		}
		
		MessageObject<Refund> messageObject = buildMessageObject(MessageActionCode.ACTION_CODE_REFUND_WHOLE_CLOSE, refund, context, taskId);
		send(messageObject);
		
		if(LOGGER.isTraceEnabled()){
			LOGGER.trace(new StringBuilder().append(CLASSNAME).append(".").append(METHODNAME).append(" end").toString());
		}
	}
	/**
	 * 退款更新
	 * @param refund
	 * @param context
	 * @throws Exception
	 */
	/*public void reOpenRefund(Refund refund, TaskContext context) throws Exception{
		//fchen added 2012-08-21
		String b2cRefundId = "";
		String adjustment = "";
		
		String refundId = refund.getChannelRefundId();
		String refundFee = String.valueOf(refund.getRefundFee());
		String reason = refund.getReason();
		String desc = refund.getDescription();
		
		ProcessRefundType processRefund = new ProcessRefundType();
		processRefund.setDataArea(new ProcessRefundDataAreaType());
		ProcessType processVerb = new ProcessType(ChannelRefundFacadeConstants.PROCESS_VERB_ACTION_REOPEN_REFUND, STR_XPATH_LANG, ChannelRefundFacadeConstants.XPATH_REFUND);
		processRefund.getDataArea().setProcess(processVerb);
		List<RefundType> refundList = processRefund.getDataArea().getRefund();
		RefundType refundType = new RefundType();
		
		RefundExternalIdentifierType refundExternalIdentifierType = new RefundExternalIdentifierType();
		refundExternalIdentifierType.setChannelRefundID(refundId);
		RefundIdentifierType refundIdentifierType = new RefundIdentifierType();
		refundIdentifierType.setExternalIdentifier(refundExternalIdentifierType);
		refundType.setRefundIdentifier(refundIdentifierType);
		
		
		MonetaryAmountType monetaryAmountType = new MonetaryAmountType();
		monetaryAmountType.setCurrency("CNY");
		monetaryAmountType.setValue(new BigDecimal(refundFee));
		refundType.setRefundAmount(monetaryAmountType);
		
		refundType.setRefundReason(reason);
		refundType.setRefundComments(desc);
		
		//fchen added 2012-08-21
		UserDataType userData = refundType.getUserData();
		userData.put(ChannelRefundFacadeConstants.B2CREFUNDID, b2cRefundId);
		userData.put(ChannelRefundFacadeConstants.ADJUSTMENT, adjustment);
		
		//store id
		String storeId = String.valueOf(context.getStoreId());
		userData.put(ChannelRefundFacadeConstants.STOREID, storeId);
		
		//add task id
		//userData.put(ChannelRefundFacadeConstants.TASK_ID, String.valueOf(ContextUtil.getTaskId()));
		
		refundList.add(refundType);
		
		//对processRefund处理，发送消息
		String message = convertToXml(processRefund);
		
		this.queueProducer.send(message);
	}*/
	
	/**
	 * 构建消息对象
	 * @param order 数据对象
	 * @param context task上下文
	 * @return
	 */
	private MessageObject<Refund> buildMessageObject(String actionCode, Refund refund, TaskContext context, Long taskId) {
		MessageObject<Refund> messageObject = 
				new MessageObject<Refund>(actionCode, refund, context.getStoreId(), taskId);
		return messageObject;
	}
	
	/**
	 * 发送消息
	 * @param messageObject
	 * @throws MessageSendException 
	 */
	private void send(MessageObject<Refund> messageObject) throws MessageSendException {
		String message = JsonUtil.object2JsonString(messageObject);
		//LOGGER.info("send order message:" + message);
		try {
			this.queueProducer.send(message);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error("退款相关jms消息发送失败", e);
			throw new MessageSendException(e);
		}
	}
}