/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskService.java
 * 描述： task业务类
 */
package net.chinacloud.mediator.task.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.dao.TaskDao;
import net.chinacloud.mediator.task.domain.TaskDomain;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.SpringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * <task业务类>
 * <task业务类>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月31日
 * @since 2014年12月31日
 */
@Service
public class TaskService {
	@Autowired
	TaskDao taskDao;
	@Autowired
	ChannelService channelService;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	TaskTemplateService taskTemplateService;
	
	/**
	 * 判断task是否重复
	 * @param task
	 * @return
	 */
	public boolean isRepeat(Task task) {
		return (taskDao.findRepeatTask(task) > 0) ? true : false;
	}
	
	/**
	 * 创建task
	 * @param task
	 */
	public void createTask(Task task) {
		taskDao.createTask(task);
	}

	/**
	 * 更新task状态
	 * @param task
	 */
	public void updateTaskStatus(Task task) {
		taskDao.updateTaskStatus(task);
	}
	
	/**
	 * 更新task的data
	 * @param task
	 * @return
	 */
	public Integer updateTaskData(TaskDomain task) {
		return taskDao.updateData(task);
	}
	
	/**
	 * 更新task状态
	 * @param taskId task id
	 * @param newStatus 新的状态
	 * @param originalStatus 原来的状态
	 * @param message 错误消息
	 */
	public int updateTaskStatus(Long taskId, Integer newStatus, Integer originalStatus, String message) {
		return taskDao.updateTaskStatus(taskId, newStatus, originalStatus, message);
	}

	/**
	 * 更新task状态和结束时间
	 * @param task
	 */
	public void updateTaskStatusAndEndTime(Task task, String message) {
		taskDao.updateTaskStatusAndEndTime(task, message);
	}
	
	/**
	 * 获取所有需要重新运行的task列表,条件满足:
	 * (task状态为TASK_STATUS_HANG || TASK_STATUS_FAIL) && task关联的模板的reRun为1
	 * @return
	 */
	public List<Task> getAllReRunTask(Date startDate) {
		List<Task> tasks = new ArrayList<Task>();
		List<Map<String, Object>> tasksMap = taskDao.getAllReRunTask(startDate);
		if (CollectionUtil.isNotEmpty(tasksMap)) {
			for (Map<String, Object> taskMap : tasksMap) {
				Task task = extractTask(taskMap);
				tasks.add(task);
			}
		}
		return tasks;
	}
	
	/**
	 * 根据task模板和applicationId获取task
	 * 可能会获取到多个task
	 * @param template
	 * @param applicationId
	 * @return
	 */
	public List<Task> find(String dataId, TaskTemplate template, Integer applicationId) {
		List<Task> tasks = new ArrayList<Task>();
		List<Map<String, Object>> tasksMap = taskDao.find(dataId, template.getId(), applicationId);
		if (CollectionUtil.isNotEmpty(tasksMap)) {
			for (Map<String, Object> taskMap : tasksMap) {
				Task task = extractTask(taskMap);
				tasks.add(task);
			}
		}
		return tasks;
	}
	
	private Task extractTask(Map<String, Object> taskMap) {
		//根据template type取得对应的task的实际类型
		String type = (String)taskMap.get("TYPE");
		String subType = (String)taskMap.get("SUBTYPE");
		
		// 取得具体的task,在task创建时,会注册task与type/subType的关系
		Task task = SpringUtil.getBean(TaskManager.getTaskConcreteClass(type, subType));
		TaskTemplate template = taskTemplateService.getTaskTemplateByTypeAndSubType(type, subType);
		
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
		//TODO 这里取到的data是一串json,这里没法知道数据的具体类型,因此这里不转换,到具体的task中转换
		String data = (String)taskMap.get("DATA");
		
		task.setData(data);
		task.setId(((Number)(taskMap.get("TASK_ID"))).longValue());
		task.setTemplate(template);
		
		return task;
	}
	
	/**
	 * task条件查询
	 * @param queryParam
	 * @return
	 */
	public List<TaskDomain> find(Map<String,Object> queryParam) {
		return taskDao.find(queryParam);
	}
	
	/**
	 * task条件查询总记录数
	 * @param queryParam
	 * @return
	 */
	public Long count(Map<String,Object> queryParam) {
		return taskDao.count(queryParam);
	}
	
	/**
	 * 根据task id获取task信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getTaskById(Integer id) {
		return taskDao.getTaskById(id);
	}
	
	/**
	 * 查询所有正在运行的task的数量
	 * @return
	 */
	public Long findRunningTask() {
		return taskDao.countTaskByStatus(Task.TASK_STATUS_NEW);
	}
	
	/**查询TEMPLATE_ID为整单退款创建和dataid为网店单号时是否存在
	 * */
	public Boolean isExistTask(String dataId, Integer templateId ){
		if(taskDao.isExistTask(dataId, templateId)>0){
			return true;
		}
		return false;
	}
}
