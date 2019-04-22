/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.user.exception;

import net.chinacloud.mediator.exception.ApplicationException;

/**
 * @description 用户不存在异常
 * @author ywu@wuxicloud.com
 * 2015年5月8日 下午3:10:59
 */
public class UserNotExistsException extends ApplicationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4675469876030651752L;
	
	protected static final String MODULE = "authentication";
	/**
	 * @param code
	 * @param defaultMessage
	 * @param args
	 */
	public UserNotExistsException(String code, String defaultMessage,
			Object[] args) {
		super(MODULE, code, args, defaultMessage);
	}
	
	public UserNotExistsException() {
		this("user.not.exists", null, null);
	}
}
