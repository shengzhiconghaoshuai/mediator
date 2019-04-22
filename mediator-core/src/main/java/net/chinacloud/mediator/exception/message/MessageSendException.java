/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MessageSendException.java
 * 描述： 
 */
package net.chinacloud.mediator.exception.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chinacloud.mediator.exception.ApplicationException;

/**
 * @description 消息发送失败异常
 * @author yejunwu123@gmail.com
 * @since 2015年7月18日 上午9:59:09
 */
public class MessageSendException extends ApplicationException {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendException.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 776611380776772638L;
	
	protected static final String MODULE = "message";
	
	public MessageSendException(String code, Object... args){
		super(MODULE, code, args, "消息发送失败");
	}
	/**
	 * @param defaultMessage
	 */
	public MessageSendException(String errorMessage) {
		this("exception.jms.send.error", errorMessage);
	}

	public MessageSendException(Throwable e) {
		this(e.getMessage());
		LOGGER.info("#################jms 失败###############" + e.getMessage());
	}
}
