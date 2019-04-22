/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：HomeController.java
 * 描述： 主页
 */
package net.chinacloud.mediator.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <主页>
 * <主页>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月19日
 * @since 2014年12月19日
 */
@RequestMapping("/system/home")
@Controller
public class HomeController {
	
	@RequestMapping(value={"/"})
	public String home(){
		return "system/index";
	}
	
	@RequestMapping("calendar")
	public String calendar(){
		return "system/calendar";
	}
	
	@RequestMapping("sidebar")
	public String sidebar(){
		return "common/sidebar";
	}
}
