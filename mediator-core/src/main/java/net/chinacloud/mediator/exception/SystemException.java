/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SystemException.java
 * 描述： 系统级异常
 */
package net.chinacloud.mediator.exception;

import net.chinacloud.mediator.utils.MessageUtils;

import org.springframework.util.StringUtils;

/**
 * <系统级异常>
 * <一般来说比较严重,出现这种异常,应用程序无能为力,一般只能继续往上抛出>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 8801006345988435217L;

	/**所属模块*/
	private String module;
	/**错误码*/
	private String code;
	/**错误码对应的参数*/
	private Object[] args;
	/**默认错误消息*/
	private String defaultMessage;
	
	public SystemException(String module, String code, Object[] args, String defaultMessage) {
		this.module = module;
		this.code = code;
		this.args = args;
		this.defaultMessage = defaultMessage;
	}
	
	public SystemException(String module, String code, Object[] args) {
		this(module, code, args, null);
	}
	
	public SystemException(String code, Object[] args) {
		this(null, code, args);
	}
	
	public SystemException(String defaultMessage) {
		this(null, null, null, defaultMessage);
	}

	public String getModule() {
		return module;
	}

	public String getCode() {
		return code;
	}

	public Object[] getArgs() {
		return args;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
	
	@Override
	public String getMessage() {
		String message = null;
		if(!StringUtils.isEmpty(code)){
			message = MessageUtils.getMessage(code, args);
		}
		if(message == null){
			message = defaultMessage;
		}
		return message;
	}

	@Override
	public String toString() {
		return this.getClass() + " [module=" + module + ", getMessage()="
				+ getMessage() + "]";
	}
}
