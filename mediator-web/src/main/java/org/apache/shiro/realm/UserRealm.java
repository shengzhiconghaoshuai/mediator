/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.apache.shiro.realm;

import java.util.List;

import net.chinacloud.mediator.role.domain.Role;
import net.chinacloud.mediator.role.service.RoleService;
import net.chinacloud.mediator.user.domain.User;
import net.chinacloud.mediator.user.exception.PasswordNotMatchException;
import net.chinacloud.mediator.user.exception.UserNotExistsException;
import net.chinacloud.mediator.user.service.UserService;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @description 用户realm
 * @author ywu@wuxicloud.com
 * 2015年5月7日 下午3:24:35
 */
public class UserRealm extends AuthorizingRealm {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);
	
	@Autowired
	UserService userServie;
	@Autowired
	RoleService roleService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// 获取授权信息
		LOGGER.debug("=============获取授权信息============");
		
		String userName = (String)principals.getPrimaryPrincipal();
		List<Role> roles = roleService.getRolesByUserName(userName);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		if (CollectionUtil.isNotEmpty(roles)) {
			for (Role role : roles) {
				authorizationInfo.addRole(role.getIdentifier());
			}
		}
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		// 获取认证信息
		UsernamePasswordToken passwordToken = (UsernamePasswordToken)token;
		String userName = passwordToken.getUsername();	//用户名
		String password = "";	//密码
		if (null != passwordToken.getPassword()) {
			password = new String(passwordToken.getPassword());
		}
		LOGGER.debug("authentication info username = {}; password = {}", userName, password);
		LOGGER.debug("=============获取认证信息============");
		User user = null;
		try {
			user = userServie.login(userName, password);
		} catch (UserNotExistsException e) {
			// 未知的账号
			throw new UnknownAccountException(e.getMessage(), e);
		} catch (PasswordNotMatchException e) {
			// 密码错误
			throw new AuthenticationException(e.getMessage(), e);
		}
		
		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUserName(), password, getName());
		return authenticationInfo;
	}

}
