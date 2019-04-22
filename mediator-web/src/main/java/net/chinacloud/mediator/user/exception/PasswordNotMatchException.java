/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.user.exception;

import net.chinacloud.mediator.exception.ApplicationException;

/**
 * @description 密码不匹配异常
 * @author ywu@wuxicloud.com
 * 2015年5月12日 下午1:37:49
 */
public class PasswordNotMatchException extends ApplicationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -716099001799917646L;
	
	protected static final String MODULE = "authentication";
	/**
	 * @param code
	 * @param defaultMessage
	 * @param args
	 */
	public PasswordNotMatchException(String code, String defaultMessage,
			Object[] args) {
		super(MODULE, code, args, defaultMessage);
	}

	public PasswordNotMatchException() {
		this("user.password.not.match", null, null);
	}

}
