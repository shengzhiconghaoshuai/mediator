/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MessageObject.java
 * 描述： 
 */
package net.chinacloud.mediator.pojo;

import java.io.Serializable;

/**
 * @description 消息
 * @author yejunwu123@gmail.com
 * @since 2015年6月30日 下午2:24:25
 */
public class MessageObject<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -181143542601194784L;
	/**动作指令*/
	protected String actionCode;
	/**消息内容*/
	protected T content;
	/**上下文*/
	protected MessageContext context;
	
	public MessageObject() {
		
	}
	
	public MessageObject(String actionCode, Integer storeId) {
		this(actionCode, null, storeId);
	}
	
	public MessageObject(String actionCode, MessageContext context) {
		this(actionCode, null , context);
	}
	
	public MessageObject(String actionCode, T content, Integer storeId) {
		this(actionCode, content, storeId, null);
	}
	
	/*public MessageObject(String actionCode, Integer storeId, Integer taskId) {
		this(actionCode, null, storeId, taskId);
	}*/
	
	public MessageObject(String actionCode, T content, Integer storeId, Long taskId) {
		this.actionCode = actionCode;
		this.content = content;
		this.context = new MessageContext(storeId, taskId);
	}
	
	public MessageObject(String actionCode, T content, MessageContext context) {
		this.actionCode = actionCode;
		this.content = content;
		this.context = context;
	}
	
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
	public MessageContext getContext() {
		return context;
	}
	public void setContext(MessageContext context) {
		this.context = context;
	}
	public void setContext(Integer storeId) {
		this.context = new MessageContext(storeId);
	}
	public void setContext(Integer storeId, Long taskId) {
		this.context = new MessageContext(storeId, taskId);
	}

}
