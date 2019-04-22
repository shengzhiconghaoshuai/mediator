/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ApplicationException.java
 * 描述： 应用异常
 */
package net.chinacloud.mediator.exception;

import net.chinacloud.mediator.utils.MessageUtils;

import org.springframework.util.StringUtils;
/**
 * <应用级异常>
 * <一般跟业务相关,代码中捕获这种异常可以进行相应的业务处理,如发出邮件等>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class ApplicationException extends Exception{

	private static final long serialVersionUID = 5368778198679814548L;

	/**所属模块*/
	private String module;
	/**错误码*/
	private String code;
	/**错误码对应的参数*/
	private Object[] args;
	/**默认错误消息*/
	private String defaultMessage;
	
	public ApplicationException(String module, String code, Object[] args, String defaultMessage) {
		this.module = module;
		this.code = code;
		this.args = args;
		this.defaultMessage = defaultMessage;
	}
	
	public ApplicationException(String module, String code, Object[] args) {
		this(module, code, args, null);
	}
	
	public ApplicationException(String code, Object[] args) {
		this(null, code, args);
	}
	
	public ApplicationException(String defaultMessage) {
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
		if(StringUtils.hasText(code)){
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
