/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoController.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.notify.TmallMessageProcessor;
import net.chinacloud.mediator.task.exception.TaskException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taobao.api.ApiException;
import com.taobao.api.internal.tmc.Message;

@Controller
@RequestMapping("/system/taobao/order/")
public class TaobaoController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(TaobaoController.class);
	
	@Autowired
	TmallMessageProcessor messageProcessor;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	ChannelService channelService;
	
	@RequestMapping(value = "tocreate", method = RequestMethod.GET)
	public String toCreate(Model model) throws TaskException, ApiException {
		List<Channel> channels = new ArrayList<Channel>();
		
		Channel channelTaobal = channelService.getChannelByCode(TaobaoConstant.CHANNEL_TAOBAO);
		if (null != channelTaobal) {
			channels.add(channelTaobal);
		}
		
		Channel channelTmall = channelService.getChannelByCode(TaobaoConstant.CHANNEL_TMALL);
		if (null != channelTmall) {
			channels.add(channelTmall);
		}
		
		model.addAttribute("channels", channels);
		return "taobao/create";
	}
	
	@RequestMapping(value = "create")
	public void create(String topic, String content, Integer applicationId, HttpServletResponse response) throws TaskException, ApiException {
		//Date startTime = new Date();
		Application application = applicationService.getApplicationById(applicationId);
		
		Message message = new Message();
		message.setTopic(topic);
		message.setContent(content);
		
		messageProcessor.process(
				application.getChannelId(), 
				application.getId(), 
				application.getCode(), 
				application.getStoreId(),
				message);
		//STATICLOGGER.info("message process time " + (new Date().getTime() - startTime.getTime()));
		response.setContentType("text/plain");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write("success");
		} catch(IOException e) {
			LOGGER.error("", e);
		} finally {
			if (pw!=null) {
				pw.close();
				pw = null;
			}
		}
	}
}
