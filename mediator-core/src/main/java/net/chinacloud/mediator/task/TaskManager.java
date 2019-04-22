/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskManager.java
 * 描述： task管理器
 */
package net.chinacloud.mediator.task;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.chinacloud.mediator.task.cache.TaskAdapterManager;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;
import net.chinacloud.mediator.task.exception.TaskException;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.task.service.TaskTemplateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * <task管理器>
 * <提供task的生成、执行条件判断等功能>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
@Component
public final class TaskManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);
	//private static Logger STATICLOGGER = LoggerFactory.getLogger("taskStatics");
	
	private static final String TASK_TYPE_SPLIT = "&";
	
	@Autowired
	private TaskTemplateService templateService;
	@Autowired
	TaskAdapterManager taskAdapterManager;
	@Autowired
	TaskService taskService;
	@Autowired
	ThreadPoolTaskExecutor taskExecutor;
	/**
	 * 存放各渠道的task adapter
	 * key:渠道标识(channel code)
	 * value:各渠道TaskAdapter接口实现
	 */
	//private static Map<String,TaskAdapter> TASK_ADAPTER_MAP;
	/**
	 * task执行权限,主要用于库存同步时停止task的执行
	 * key:应用标识,application code
	 * value:taskType + "/" + taskSubType,value:true/false
	 */
	private static Map<String,Map<String,Boolean>> TASK_EXECUTION_PERMISSION = new ConcurrentHashMap<String, Map<String,Boolean>>();
	
	private static Map<String, Class<? extends Task>> TASK_TYPE_MAP_REVERSE = new ConcurrentHashMap<String, Class<? extends Task>>();
	
	/**
	 * task扫尾或者直接页面点击重跑时,需要知道task的实际类型,
	 * 而Task类是abstract的,所以需要映射Task的type和class类型,
	 * 按理这些task注册一次就够了,应该放在静态代码块中,但是每个具体task是prototype,
	 * 由于设计的原因,不太好放在静态代码块中,导致每次task的创建都会注册一次
	 * TODO 留待下一版改进
	 * update 2015-06-26 bug fix 原来的注册方式在系统重启后,没有task创建的情况下,扫尾的时候就取不到concrete task,导致扫尾失败
	 * 现在改成在task的static代码块中注册
	 * @param type
	 * @param subType
	 * @param clazz
	 */
	public static void registTask(String type, String subType, Class<? extends Task> clazz) {
		String key = type + TASK_TYPE_SPLIT + subType;
		if (!TASK_TYPE_MAP_REVERSE.containsKey(key)) {
			LOGGER.debug("注册task {},type = {},subType = {}", new Object[]{clazz.getName(), type, subType});
			TASK_TYPE_MAP_REVERSE.put(key, clazz);
		} else {
			if (LOGGER.isDebugEnabled()) {
				String msg = "type=" + type + ",subType=" + subType + "类型的task已经注册过;";
				msg += "当前task为 " + clazz.getName() + ",已注册task为 " + TASK_TYPE_MAP_REVERSE.get(key).getName();
				LOGGER.debug(msg);
			}
		}
	}
	
	/**
	 * 根据task的type和subType获取task的具体类型
	 * @param type
	 * @param subType
	 * @return
	 */
	public static Class<? extends Task> getTaskConcreteClass(String type, String subType) {
		String key = type + TASK_TYPE_SPLIT + subType;
		return TASK_TYPE_MAP_REVERSE.get(key);
	}
	
	/**
	 * 暂停所有后续新创建的task的执行
	 * 暂时控制到channel级别
	 * @param channel 渠道标识
	 */
	public static void disableTask(String applicationCode, String type, String subType){
		LOGGER.debug("disable task in application {}, type = {}, subType = {}", new Object[]{applicationCode, type, subType});
		Map<String,Boolean> map = TASK_EXECUTION_PERMISSION.get(applicationCode);
		if(null == map){
			map = new ConcurrentHashMap<String, Boolean>();
			TASK_EXECUTION_PERMISSION.put(applicationCode, map);
		}
		map.put(type + "/" + subType, false);
	}
	/**
	 * 开启task的执行
	 * @param channel 渠道标识
	 */
	public static void enableTask(String applicationCode, String type, String subType){
		LOGGER.debug("enable task in application {}, type = {}, subType = {}", new Object[]{applicationCode, type, subType});
		Map<String,Boolean> map = TASK_EXECUTION_PERMISSION.get(applicationCode);
		if(null == map){
			return;
		}
		map.put(type + "/" + subType, true);
	}
	/**
	 * 生成合适的task
	 * @param channel 渠道标识,用于获取相应的TaskAdapter
	 * @param packet 数据
	 * @return 相应的task
	 */
	public <T> Task generateTask(String channel,CommonNotifyPacket<T> packet) {
		if(null != packet){
			//Task task = TASK_ADAPTER_MAP.get(channel).generateTask(packet);
			//通过缓存获取task adapter
			TaskAdapter taskAdapter = taskAdapterManager.getTaskAdapter(channel);
			Task task = taskAdapter.generateTask(packet);
			
			if(null != task){
				//根据task的type及subType匹配对应的模板
				String type = task.getType();
				String subType = task.getSubType();
				TaskTemplate template = templateService.getTaskTemplateByTypeAndSubType(type, subType);
				if(null == template){
					LOGGER.warn("task [" + task.getType() + " / " + task.getSubType() + "] 找不到对应的模板");
				}else{
					LOGGER.debug("set template:{}", template);
					task.setTemplate(template);
					//判断task重复不能在这边判断,这边还没有task context
					/*if(template.getRepeatable() == 0){	//此类型task不允许重复创建
						if(taskService.isRepeat(task)){
							throw new DuplicateTaskException(task.getContext().getChannelCode(), 
									task.getContext().getApplicationCode(), task.getType(), task.getSubType());
						}
					}*/
				}
			}
			
			return task;
		}
		return null;
	}
	/**
	 * task是否允许执行
	 * @param task
	 * @return
	 */
	public boolean isReadyToExcute(Task task){
		Map<String,Boolean> map = TASK_EXECUTION_PERMISSION.get(task.getContext().getApplicationCode());
		if(null == map){
			LOGGER.debug("task {} isReadyToExcute:{}", task.getId(), true);
			return true;
		}
		Boolean ready = map.get(task.getType() + "/" + task.getSubType());
		if(null == ready){
			ready = true;
		}
		LOGGER.debug("task {} isReadyToExcute:{}", task.getId(), ready);
		return ready;
	}
	/**
	 * 执行task,将task丢入线程池
	 * @param task
	 * @throws DuplicateTaskException  
	 */
	public void executeTask(Task task) throws TaskException {
		insertTask(task);
		execute(task);
	}
	
	public void execute(Task task) throws TaskException {
		if(isReadyToExcute(task)){
			//将task放入线程池
			taskExecutor.execute(task);
		}
	}
	
	/**
	 * 将task持久化
	 * @param task
	 * @throws TaskException
	 */
	public void insertTask(Task task) throws TaskException {
		if (checkRepeat(task)) {
			throw new DuplicateTaskException(task.getContext().getChannelCode(), 
					task.getContext().getApplicationCode(), task.getType(), task.getSubType());
		}
		
		insert(task);
	}
	
	public void insert(Task task) throws TaskException {
		//注册task的type & subType 与 class 的映射
		// update 2015-06-26 task在这里注册没有什么卵用,扫尾的时候task有可能没有注册,导致扫尾创建task失败,只能在task
		// 加载的时候就注册
		//registTask(task.getType(), task.getSubType(), task.getClass());
		
		task.setStartTime(new Date());
		if (isReadyToExcute(task)) {
			if (null == task.getId()) {
				LOGGER.debug("create task:{}", task);
				
				taskService.createTask(task);
			} else {
				LOGGER.debug("update task status:{}", task);
				task.setStatus(Task.TASK_STATUS_RUNNING);
				
				taskService.updateTaskStatus(task);
			}
			
			if (LOGGER.isInfoEnabled()) {
				if (null != task.getTemplate()) {
					LOGGER.info("task[" + task.getId() + "][" + task.getTemplate().getType() + "/" + task.getTemplate().getSubType() + "]"
							+ " in [" + task.getContext().getChannelCode() + "/" + task.getContext().getApplicationCode() + "]"
							+ " start to execute");
				} else {
					LOGGER.info("task[" + task.getId() + "]"
							+ " in [" + task.getContext().getChannelCode() + "/" + task.getContext().getApplicationCode() + "]"
							+ " start to execute");
				}
			}
		} else {
			LOGGER.debug("task {} not ready to excute", task.getId());
			task.setStatus(Task.TASK_STATUS_HANG);
			if (null == task.getId()) {
				taskService.createTask(task);
			} else {
				taskService.updateTaskStatus(task);
			}
		}
	}
	
	public boolean checkRepeat(Task task) {
		TaskTemplate template = task.getTemplate();
		if (null != template) {
			if (template.getRepeatable() == 0) {	//此类型task不允许重复创建
				return taskService.isRepeat(task);
			}
		}
		return false;
	}
}
