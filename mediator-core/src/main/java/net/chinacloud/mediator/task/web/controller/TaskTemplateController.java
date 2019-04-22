/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskTemplateController.java
 * 描述： task模板管理
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
import net.chinacloud.mediator.pojo.Page;
import net.chinacloud.mediator.pojo.PageView;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.service.TaskTemplateService;
import net.chinacloud.mediator.utils.JsonUtil;
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
 * <task模板管理>
 * <task模板管理>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月29日
 * @since 2014年12月29日
 */
@Controller
@RequestMapping("/system/task/template/")
public class TaskTemplateController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskTemplateController.class);
	
	@Autowired
	TaskTemplateService templateService;
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(Model model){
		List<String> templateTypes = templateService.getAllTaskTemplateType();
		model.addAttribute("templateTypes", templateTypes);
		return "system/task/template/list";
	}
	
	/**
	 * 加载数据
	 * @throws IOException 
	 */
	@RequestMapping(value="loadTaskTemplates")
	public void loadTaskTemplates(Model model,HttpServletRequest request,HttpServletResponse response,
			int rows,int page,@RequestParam(required=false) String type,
			@RequestParam(required=false) String subType ) throws IOException{
		Page<TaskTemplate> pages = new Page<TaskTemplate>(page,rows);
		Map<String,Object> params = new HashMap<String, Object>();
		params.put(Constant.PAGE_START_INDEX, pages.getFirstIndex());
		params.put(Constant.PAGE_END_INDEX, pages.getLastIndex());
		if (StringUtils.hasText(type)) {
			params.put("TYPE", type);
		}
		if (StringUtils.hasText(subType)) {
			params.put("SUBTYPE", subType);
		}
		List<TaskTemplate> taskTemplates = templateService.getAllTaskTemplates(params);
		pages.setData(taskTemplates);
		pages.setTotalRecords(templateService.countAllTaskTemplates(params));
		response.setContentType("application/json");
		response.getWriter().write(JsonUtil.object2JsonString(new PageView<TaskTemplate>(pages)));
	}
	
	/**
	 * 根据taskTemplate的id删除
	 * @throws IOException 
	 */
	@RequestMapping(value="deleteTaskTemplate")
	public void deleteTaskTemplate(@RequestParam(required=false) int id,HttpServletResponse response) throws IOException{
		int result = templateService.deleteTaskTemplate(id);
		response.setContentType("application/json");
	    response.getWriter().write(JsonUtil.object2JsonString(result));
	}
	
	/**
	 * 添加目模板
	 * @param template
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="add")
	public void add(TaskTemplate template,HttpServletResponse response) throws IOException{
		int result = templateService.save(template);
		response.setContentType("application/json");
		PrintWriter pw = null;
		try{
			pw = response.getWriter();
			pw.write(JsonUtil.object2JsonString(result));
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
	 * 修改页面
	 * @param templateId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="update/{id}",method=RequestMethod.GET)
	public String toUpdate(@PathVariable(value="id") Integer templateId,Model model){
		TaskTemplate template = templateService.getTaskTemplateById(templateId);
		model.addAttribute("template", template);
		return "system/task/template/edit";
	}
	
	/**
	 * 更新task模板
	 * @param template
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="update")
	public void update(TaskTemplate template,HttpServletResponse response) {
		int result = templateService.update(template);
		response.setContentType("application/json");
		PrintWriter pw = null;
		try{
			pw = response.getWriter();
			pw.write(JsonUtil.object2JsonString(result));
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
	 * 根据task template的type获取所有template
	 * @param taskType
	 * @param response
	 */
	@RequestMapping(value="getByType", method=RequestMethod.POST)
	public void getTemplateByType(String taskType, HttpServletResponse response) {
		List<TaskTemplate> taskTemplates = templateService.getTaskTemplateByType(taskType);
		response.setContentType("application/json");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(JsonUtil.object2JsonString(taskTemplates));
		} catch (IOException e) {
			LOGGER.error("", e);
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
}
