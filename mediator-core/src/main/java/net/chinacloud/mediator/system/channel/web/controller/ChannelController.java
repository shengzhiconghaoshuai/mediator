/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelController.java
 * 描述： 渠道控制器
 */
package net.chinacloud.mediator.system.channel.web.controller;

import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.system.channel.service.ChannelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <渠道控制器>
 * <渠道控制器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月23日
 * @since 2014年12月23日
 */
@Controller
@RequestMapping("/system/channel/")
public class ChannelController {
	
	@Autowired
	ChannelService channelService;
	
	/**
	 * 渠道列表
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Model model){
		model.addAttribute("channels", channelService.getAllChannels());
		return "system/channel/list";
	}
	
	/**
	 * 渠道添加页面
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String toAdd(){
		return "system/channel/add";
	}
	
	/**
	 * 添加渠道
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.POST)
	public String add(Channel channel){
		channelService.save(channel);
		return "redirect:list";
	}
	
	/**
	 * 渠道更新页面
	 * @param channelId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="update/{id}",method=RequestMethod.GET)
	public String toUpdate(@PathVariable(value="id") Integer channelId,Model model){
		model.addAttribute("channel", channelService.getChannelById(channelId));
		return "system/channel/edit";
	}
	
	/**
	 * 更新渠道信息
	 * @param channelId
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="update",method=RequestMethod.POST)
	public String update(Channel channel){
		channelService.update(channel);
		return "redirect:list";
	}
}
