/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：HomeController.java
 * 描述： 主页
 */
package net.chinacloud.mediator.web.controller;

import javax.servlet.http.HttpServletRequest;

import net.chinacloud.mediator.web.Constants;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <登录>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月19日
 * @since 2014年12月19日
 */
@RequestMapping("/")
@Controller
public class LoginController {
	
	@RequestMapping("login")
	public String login(HttpServletRequest request, Model model) {
		Exception shiroLoginFailureException = (Exception)request.getAttribute(Constants.SHIRO_LOGIN_FAILURE_KEY);
		if (null != shiroLoginFailureException) {
			shiroLoginFailureException.printStackTrace();
			model.addAttribute(Constants.LOGIN_ERROR, shiroLoginFailureException.getMessage());
		}
		
		Subject subject = SecurityUtils.getSubject();
		if (null != subject && subject.isAuthenticated()) {
			subject.logout();
		}
		
		return "login";
	}
}
