/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.apache.shiro.web.filter.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @description 自定义表单过滤器
 * 重新设置登录失败的消息,由原来的异常全限定类名改为异常对象,存入request
 * @author ywu@wuxicloud.com
 * 2015年5月12日 下午3:16:41
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
	
	@Override
	protected void setFailureAttribute(ServletRequest request,
			AuthenticationException ae) {
		request.setAttribute(getFailureKeyAttribute(), ae);
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		return super.isAccessAllowed(request, response, mappedValue) &&
                (!isLoginRequest(request, response) && isPermissive(mappedValue));
	}
}
