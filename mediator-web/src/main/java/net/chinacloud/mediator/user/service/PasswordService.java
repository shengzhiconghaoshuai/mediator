/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.user.service;

import net.chinacloud.mediator.user.domain.User;
import net.chinacloud.mediator.user.exception.PasswordNotMatchException;
import net.chinacloud.mediator.utils.MD5Util;

import org.springframework.stereotype.Service;

/**
 * @description 密码加密操作实现类
 * @author ywu@wuxicloud.com
 * 2015年5月12日 下午1:04:42
 */
@Service
public class PasswordService {

	public String encrypt(String userName, String password, String salt) {
		return MD5Util.getMD5(userName + password + salt);
	}

	public void validate(User user, String originPassword) throws PasswordNotMatchException {
		String encryptedPassword = MD5Util.getMD5(user.getUserName() + originPassword + user.getSalt());
		if (!encryptedPassword.equals(user.getPassword())) {
			throw new PasswordNotMatchException();
		}
	}

}
