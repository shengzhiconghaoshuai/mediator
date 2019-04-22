/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MessageResponseObject.java
 * 描述： 
 */
package net.chinacloud.mediator.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 响应消息结构
 * @author yejunwu123@gmail.com
 * @since 2015年7月11日 上午9:47:48
 */
public class MessageResponseObject<T> extends MessageObject<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3970348757412328948L;
	/**是否成功*/
	private boolean success = true;
	/**错误提示*/
	private List<Errors> errors = new ArrayList<Errors>(0);
	
	public MessageResponseObject() {
		
	}
	
	public MessageResponseObject(String actionCode, T content, Integer storeId, Long taskId, boolean success) {
		this(actionCode, content, storeId, taskId, success, null);
	}
	
	public MessageResponseObject(String actionCode, T content, Integer storeId, Long taskId, boolean success, String errorMessage) {
		this(actionCode, content, new MessageContext(storeId, taskId), success, errorMessage);
	}
	
	public MessageResponseObject(String actionCode, T content, MessageContext context, boolean success, String errorMessage) {
		this.actionCode = actionCode;
		this.content = content;
		this.context = context;
		this.success = success;
		if (null != errorMessage && !"".equals(errorMessage.trim())) {
			addErrors(errorMessage);
		}
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<Errors> getErrors() {
		return errors;
	}

	protected void setErrors(List<Errors> errors) {
		this.errors = errors;
	}
	
	/**
	 * 添加错误消息
	 * @param code
	 * @param errorMessage
	 */
	public void addErrors(String code, String errorMessage) {
		Errors error = new Errors(code, errorMessage);
		this.errors.add(error);
	}
	/**
	 * 添加错误消息
	 * @param errorMessage
	 */
	public void addErrors(String errorMessage) {
		Errors error = new Errors(errorMessage);
		this.errors.add(error);
	}
}
