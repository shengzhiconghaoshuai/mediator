/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ApplicationController.java
 * 描述： 
 */
package net.chinacloud.mediator.system.application.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <应用管理>
 * <应用管理>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月25日
 * @since 2014年12月25日
 */
@Controller
@RequestMapping("/system/application/")
public class ApplicationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
	
	@Autowired
	ChannelService channelService;
	@Autowired
	ApplicationService applicationService;
	
	/**
	 * 添加页面
	 * @param channelId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="add/{id}",method=RequestMethod.GET)
	public String toAdd(@PathVariable(value="id") Integer channelId,Model model){
		model.addAttribute("channel", channelService.getChannelById(channelId));
		return "system/application/add";
	}
	
	/**
	 * 添加
	 * @param application
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.POST)
	public String add(Application application){
		applicationService.save(application);
		return "redirect:list";
	}
	
	/**
	 * 列表
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Model model){
		model.addAttribute("applications", applicationService.getAllApplications());
		return "system/application/list";
	}
	
	/**
	 * 应用详情
	 * @param applicationId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="detail/{id}")
	public String detail(@PathVariable(value="id") Integer applicationId,Model model){
		Application application = applicationService.getApplicationById(applicationId);
		Channel channel = channelService.getChannelById(application.getChannelId());
		model.addAttribute("application", application);
		model.addAttribute("channel", channel);
		return "system/application/detail";
	}
	
	@RequestMapping(value="update/{id}",method=RequestMethod.GET)
	public String toUpdate(@PathVariable(value="id") Integer applicationId,Model model){
		Application application = applicationService.getApplicationById(applicationId);
		Channel channel = channelService.getChannelById(application.getChannelId());
		model.addAttribute("application", application);
		model.addAttribute("channel", channel);
		return "system/application/edit";
	}
	
	/**
	 * 更新渠道信息
	 * @param channelId
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="update",method=RequestMethod.POST)
	public String update(Application application){
		applicationService.update(application);
		return "redirect:list";
	}
	
	/**
	 * 根据渠道id获取应用,返回map,实际是json格式
	 * @param channelId
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="getByChannel/{id}")
	//TODO @ResponseBody 不起作用,悲剧,fuck,后来又搞了很久,还是没搞出来,草草草
	public String getApplicationsByChannelId(@PathVariable(value="id") Integer channelId, Model model) {
		List<Application> applications = applicationService.getAvailableApplicationsByChannelId(channelId);
		model.addAttribute("applications", applications);
		return "system/application/applications";
	}
	
	/**
	 * 根据渠道id获取应用,返回map,实际是json格式
	 * @param channelId
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="getByChannelId/{id}", method=RequestMethod.POST)
	//@ResponseBody
	public void getApplicationsByChannelId(@PathVariable(value="id") Integer channelId, HttpServletResponse response) {
		List<Application> applications = applicationService.getAvailableApplicationsByChannelId(channelId);
		//model.addAttribute("applications", applications);
		//return applications;
		
		response.setContentType("application/json");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(JsonUtil.object2JsonString(applications));
		} catch (IOException e) {
			//e.printStackTrace();
			LOGGER.error("", e);
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
}
