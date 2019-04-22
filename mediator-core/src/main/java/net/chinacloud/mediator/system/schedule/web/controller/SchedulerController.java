/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SchedulerController.java
 * 描述： 调度控制器
 */
package net.chinacloud.mediator.system.schedule.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.system.schedule.domain.CronConfig;
import net.chinacloud.mediator.system.schedule.domain.CronParam;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.system.schedule.service.SchedulerService;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.utils.StringUtils;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * <调度控制器>
 * <调度控制器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月18日
 * @since 2014年12月18日
 */
@Controller
@RequestMapping("/system/schedule/")
public class SchedulerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerController.class);
	
	@Autowired
	SchedulerService schedulerService;
	@Autowired
	ChannelService channelService;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	CronLasttimeService cronLasttimeService;
	@Autowired
	TaskTemplateService taskTemplateService;
	
	@RequestMapping(value="list")
	public String list(Model model) {
		model.addAttribute("cronConfigs", schedulerService.getAllCronConfigs());
		return "system/schedule/list";
	}
	
	@RequestMapping(value="crontime/list")
	public String crontime(Model model) {
		model.addAttribute("cronTimes", cronLasttimeService.list());
		List<Channel> channels = channelService.getAvailableChannels();
		model.addAttribute("channels", channels);
		List<String> templateTypes = taskTemplateService.getAllTaskTemplateType();
		model.addAttribute("templateTypes", templateTypes);
		return "system/schedule/cronLasttimeList";
	}
	
	@RequestMapping(value="crontime/update")
	public void updateCronTime(Integer applicationId, Integer templateId, Date lastTime, Model model, HttpServletResponse response) {
		cronLasttimeService.insertOrUpdateLasttime(applicationId, templateId, lastTime.getTime());
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
	
	@RequestMapping(value="crontime/add")
	public void addCronTime(Integer applicationId, String taskType, String subType, Date lastTime, Model model, HttpServletResponse response) {
		TaskTemplate template = taskTemplateService.getTaskTemplateByTypeAndSubType(taskType, subType);
		cronLasttimeService.insertOrUpdateLasttime(applicationId, template.getId(), lastTime.getTime());
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
	
	/**
	 * 调度添加页面
	 * @return
	 */
	@RequestMapping(value="add",method=RequestMethod.GET)
	public String toAdd(Model model) {
		List<Channel> channels = channelService.getAvailableChannels();
		model.addAttribute("channels", channels);
		return "system/schedule/add";
	}
	
	@RequestMapping(value="add",method=RequestMethod.POST)
	public String add(
			@RequestParam(required=false) String description, 
			@RequestParam(required=true) String className, 
			@RequestParam(required=true) int global, 
			@RequestParam(required=false) String channelId, 
			@RequestParam(required=false) String applicationIds,
			@RequestParam(required=true) int cron,
			@RequestParam(required=false) String expression,
			@RequestParam(required=false) Date startTime,
			@RequestParam(required=false) Date endTime,
			@RequestParam(required=false) Integer repeatCount,
			@RequestParam(required=false) Integer repeatInterval,
			@RequestParam(required=false) String[] paramName, 
			@RequestParam(required=false) String[] paramValue) throws ParseException, SchedulerException {
		
		CronConfig cronConfig = new CronConfig();
		cronConfig.setDescription(description);
		cronConfig.setClassName(className);
		cronConfig.setGlobal(global);
		if(StringUtils.hasLength(channelId)){
			cronConfig.addParam(Constant.CHANNEL_ID, channelId.trim());
		}
		if(StringUtils.hasLength(applicationIds)){
			cronConfig.addParam(Constant.APPLICATION_ID, applicationIds.trim());
		}
		cronConfig.setCron(cron);
		cronConfig.setExpression(expression);
		cronConfig.setStartTime(startTime);
		cronConfig.setEndTime(endTime);
		if (null != repeatCount) {
			cronConfig.setRepeatCount(repeatCount);
		}
		if (null != repeatInterval && repeatInterval > 0) {
			cronConfig.setRepeatInterval(repeatInterval);
		}
		//添加额外的调度参数
		if (null != paramName && paramName.length > 0) {
			for (int i = 0; i < paramName.length; i++) {
				String pn = paramName[i];
				String pv = paramValue[i];
				if (StringUtils.hasLength(pn)) {
					cronConfig.addParam(pn.trim(), pv.trim());
				}
			}
		}
		schedulerService.createCron(cronConfig);
		return "redirect:list";
	}
	
	/**
	 * 删除调度
	 * @param cronId
	 * @return
	 * @throws SchedulerException
	 */
	@RequestMapping(value="delete/{id}")
	public String delete(@PathVariable(value="id") Integer cronId) throws SchedulerException {
		schedulerService.delete(cronId);
		return "redirect:../list";
	}
	
	/**
	 * 调度修改页面
	 * @param cronId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="update/{id}",method=RequestMethod.GET)
	public String toUpdate(@PathVariable(value="id") Integer cronId, Model model) {
		CronConfig cronConfig = schedulerService.findCron(cronId);
		List<CronParam> cronParams = schedulerService.getCronConfigParams(cronConfig);
		cronConfig.setParams(cronParams);
		model.addAttribute("cronConfig", cronConfig);
		
		Integer channelId = null;
		for (CronParam param : cronParams) {
			if (param.getParamName().equals(Constant.CHANNEL_ID)) {
				channelId = Integer.valueOf(param.getParamValue());
				break;
			}
		}
		
		List<Application> applications = applicationService.getApplicationsByChannelId(channelId);
		
		List<Channel> channels = channelService.getAvailableChannels();
		model.addAttribute("channels", channels);
		model.addAttribute("applications", applications);
		return "system/schedule/edit";
	}
	
	/**
	 * 修改调度
	 * @param cronConfig
	 * @param channelId
	 * @param applicationIds
	 * @param paramName
	 * @param paramValue
	 * @return
	 * @throws ParseException
	 * @throws SchedulerException
	 */
	@RequestMapping(value="update",method=RequestMethod.POST)
	public String update(CronConfig cronConfig, @RequestParam(required=false) String channelId, @RequestParam(required=false) String applicationIds,
			@RequestParam(required=false) String[] paramName, @RequestParam(required=false) String[] paramValue) throws ParseException, SchedulerException {
		if(null != channelId && !"".equals(channelId.trim())){
			cronConfig.addParam(Constant.CHANNEL_ID, channelId.trim());
		}
		if(null != applicationIds && !"".equals(applicationIds.trim())){
			cronConfig.addParam(Constant.APPLICATION_ID, applicationIds.trim());
		}
		//添加额外的调度参数
		if (null != paramName && paramName.length > 0) {
			for (int i = 0; i < paramName.length; i++) {
				String pn = paramName[i];
				String pv = paramValue[i];
				if (null != pn && !"".equals(pn.trim())) {
					cronConfig.addParam(pn.trim(), pv.trim());
				}
			}
		}
		schedulerService.update(cronConfig);
		
		return "redirect:list";
	}
}
