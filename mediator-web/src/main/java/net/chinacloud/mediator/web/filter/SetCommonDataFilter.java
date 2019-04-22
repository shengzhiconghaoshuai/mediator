/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SetCommonDataFilter.java
 * 描述： 
 */
package net.chinacloud.mediator.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.chinacloud.mediator.web.Constants;

public class SetCommonDataFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		if(request.getAttribute(Constants.CONTEXT_PATH) == null){
			request.setAttribute(Constants.CONTEXT_PATH, httpServletRequest.getContextPath());
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		//do nothing
	}
}
