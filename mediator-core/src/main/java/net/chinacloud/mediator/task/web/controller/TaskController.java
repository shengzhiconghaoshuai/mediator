/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskController.java
 * 描述： task管理控制器
 */
package net.chinacloud.mediator.task.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.pojo.Page;
import net.chinacloud.mediator.pojo.PageView;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.domain.TaskDomain;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

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
 * <task管理控制器>
 * <task管理控制器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年2月4日
 * @since 2015年2月4日
 */
@Controller
@RequestMapping("/system/task/")
public class TaskController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
	
	@Autowired
	ChannelService channelService;
	@Autowired
	TaskTemplateService taskTemplateService;
	@Autowired
	TaskService taskService;
	@Autowired
	TaskManager taskManager;
	@Autowired
	ApplicationService applicationService;
	
	/**
	 * 跳转到task查询页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list", method=RequestMethod.GET)
	public String toList(Model model) {
		List<Channel> channels = channelService.getAvailableChannels();
		model.addAttribute("channels", channels);
		List<String> templateTypes = taskTemplateService.getAllTaskTemplateType();
		model.addAttribute("templateTypes", templateTypes);
		return "system/task/list";
	}
	
	/**
	 * task条件查询
	 * 这边有点丑陋,直接在控制层拼个查询条件了
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="list", method=RequestMethod.POST)
	public void list(@RequestParam(required=false) Integer channelId, 
						@RequestParam(required=false) Integer applicaitonId, 
						@RequestParam(required=false) Integer status, 
						@RequestParam(required=false) String startTime, 
						@RequestParam(required=false) String endTime, 
						@RequestParam(required=false) String dataId ,
						@RequestParam(required=false) Integer rows,
						@RequestParam(required=false) Integer page,
						@RequestParam(required=false) String taskType,
						@RequestParam(required=false) String subType,
						HttpServletRequest request,
						HttpServletResponse response,
						Model model) throws IOException {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		Page<TaskDomain> pg = new Page<TaskDomain>(page, rows);
		queryParam.put(Constant.PAGE_START_INDEX, pg.getFirstIndex());
		queryParam.put(Constant.PAGE_END_INDEX,pg.getLastIndex());
		if (StringUtils.hasText(dataId)) {
			queryParam.put("DATAID", dataId.trim());
		}
		if (null != channelId) {
			queryParam.put("CHANNEL_ID", channelId);
		}
		if (null != applicaitonId) {
			queryParam.put("APPLICATION_ID", applicaitonId);
		}
		if (null != status) {
			queryParam.put("STATUS", status);
		}
		if (StringUtils.hasText(taskType)) {
			queryParam.put("TYPE", taskType);
		}
		if (StringUtils.hasText(subType)) {
			queryParam.put("SUBTYPE", subType);
		}
		if (StringUtils.hasText(startTime)) {
			queryParam.put("STARTTIME", DateUtil.parse(startTime));
		}
		if (StringUtils.hasText(endTime)) {
			queryParam.put("ENDTIME", DateUtil.parse(endTime));
		}
		List<TaskDomain> taskDimains = taskService.find(queryParam);
		pg.setTotalRecords(taskService.count(queryParam));
		pg.setData(taskDimains);
		response.setContentType("application/json");
		PrintWriter pw = null;
		try{
			pw = response.getWriter();
			pw.write(JsonUtil.object2JsonString(new PageView<TaskDomain>(pg)));
		}catch(IOException e){
			LOGGER.error("",e);
		}finally{
			if(pw!=null){
				pw.close();
				pw = null;
			}
		}
	}
	
	/**
	 * task编辑
	 * @param taskId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="update/{id}",method=RequestMethod.GET)
	public String toUpdate(@PathVariable(value="id") Integer taskId, Model model){
		Map<String, Object> task = taskService.getTaskById(taskId);
		model.addAttribute("task", task);
		return "system/task/edit";
	}
	
	@RequestMapping(value="getdata/{id}",method=RequestMethod.GET)
	public void getTaskTada(@PathVariable(value="id") Integer taskId, HttpServletResponse response) {
		Map<String, Object> taskMap = taskService.getTaskById(taskId.intValue());
		if (null != taskMap) {
			Object dataObj = taskMap.get("DATA");
			if (null != dataObj) {
				response.setContentType("application/text");
				PrintWriter pw = null;
				try {
					pw = response.getWriter();
					pw.write(dataObj.toString());
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
	}
	
	@RequestMapping("/updateTask")
	public void updateTask(@RequestParam(required=false) Long taskId,
			               @RequestParam(required=false) String data,
			               HttpServletRequest request,
						   HttpServletResponse response){
		if (StringUtils.hasText(data)) {
			if (!"null".equals(data)) {
				TaskDomain taskDomain = new TaskDomain();
			    taskDomain.setTaskId(taskId);
			    taskDomain.setData(data);
			    this.taskService.updateTaskData(taskDomain);
			}
		}
	    Map<String, Object> taskMap = taskService.getTaskById(taskId.intValue());
	    if (!CollectionUtil.isEmpty(taskMap)) {
			//根据template type取得对应的task的实际类型
			String type = (String)taskMap.get("TYPE");
			String subType = (String)taskMap.get("SUBTYPE");
			
			TaskTemplate template = taskTemplateService.getTaskTemplateByTypeAndSubType(type, subType);
			
			// 取得具体的task,在task创建时,会注册task与type/subType的关系
			Task task = SpringUtil.getBean(TaskManager.getTaskConcreteClass(type, subType));
			
			//完善task的上下文信息
			TaskContext context = task.getContext();
			Integer channelId = ((Number)taskMap.get("CHANNEL_ID")).intValue();
			Channel channel = channelService.getChannelById(channelId);
			context.setChannelCode(channel.getCode());
			context.setChannelId(channelId);
			
			context.setApplicationId(((Number)taskMap.get("APPLICATION_ID")).intValue());
			Application application = applicationService.getApplicationById(context.getApplicationId());
			context.setApplicationCode(application.getCode());
			context.setStoreId(application.getStoreId());
			
			//完善task的data部分
			task.setDataId((String)taskMap.get("DATAID"));
			//这里取到的data是一串json,这里没法知道数据的具体类型,因此这里不转换,到具体的task中转换
			String data1 = (String)taskMap.get("DATA");
			
			task.setData(data1);
			task.setId(((Number)(taskMap.get("TASK_ID"))).longValue());
			task.setTemplate(template);
			try {
				taskManager.insert(task);
				taskManager.execute(task);
			} catch (TaskException e) {
				LOGGER.error("task更新失败", e);
			}
		}
	}
}
