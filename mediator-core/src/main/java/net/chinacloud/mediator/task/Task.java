/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Task.java
 * 描述： 任务抽象
 */
package net.chinacloud.mediator.task;

import java.util.Date;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <任务抽象>
 * <实现了Runnable接口,一个Task代表一个完整的业务处理,具体业务逻辑由子类完成,
 * 父类控制执行流程以及统一的异常处理>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
@Component
public abstract class Task implements Runnable {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Task.class);
	//private static Logger STATICLOGGER = LoggerFactory.getLogger("taskStatics");
	
	@Autowired
	TaskService taskService;
	
	//task 状态
	public static Integer TASK_STATUS_NEW = 1;			//新创建
	public static Integer TASK_STATUS_RUNNING = 2;		//正在运行
	public static Integer TASK_STATUS_HANG = 3;			//挂起
	public static Integer TASK_STATUS_FAIL = 4;			//失败
	public static Integer TASK_STATUS_FINISH = 5;		//成功
	
	/**task id,一般来说是自增长*/
	protected Long id;
	/**task对应的模板*/
	protected TaskTemplate template;
	/**
	 * 任务所对应的数据的meta data,可作为某些任务的业务标志
	 * 比如创建订单任务的订单Id,上下架任务的产品partNumber,一般用于"仅需要数据头就可以完成所需任务的任务类型"
	 * 对于库存同步,需要"产品partNumber"
	 * 与"同步库存量",甚至同步模型(full/delta)三种数据才能完成的任务类型，dataId就不是完备的了
	 */
	protected String dataId;
	/**任务所对应的json格式的数据,对于重跑的task,可以直接取到该参数运行*/
	protected Object data = null;
	
	/**任务开始时间*/
	protected Date startTime;
	/**任务结束时间*/
	protected Date endTime;
	/**task运行的上下文*/
	protected TaskContext context = new TaskContext();
	/**task状态*/
	protected Integer status = TASK_STATUS_NEW;
	/**
	 * 得到该task的一级类别,由子类实现
	 * @return task的一级类别,eg:ORDER/RETURN
	 */
	protected abstract String getType();
	/**
	 * 得到该task的二级类别,由子类实现
	 * @return task的二级类别,eg：create/update/list
	 */
	protected abstract String getSubType();
	
	public void run() {
		//long startTime = new Date().getTime();
		//放入application code
		ContextUtil.set(Constant.APPLICATION_CODE, getContext().getApplicationCode());
		ContextUtil.set(Constant.CHANNEL_CODE, getContext().getChannelCode());
		ContextUtil.set(Constant.APPLICATION_ID, getContext().getApplicationId());
		try {
			doTask();
			finishTask();
		} catch (MessageSendException e) {
			LOGGER.error("jms send error, task id " + id, e);
			// 发送邮件
			MailSendUtil.sendEmail("jms send error, task id " + id, e.getMessage());
			
			failTask(e);
		} catch (OrderException e) {
			//e.printStackTrace();
			LOGGER.error("订单相关task异常, task id " + id, e);
			failTask(e);
		} catch (ApplicationException e) {
			//e.printStackTrace();
			LOGGER.error("应用相关异常, task id " + id, e);
			failTask(e);
		} catch (Exception e){
			//e.printStackTrace();
			LOGGER.error("task fail, task id " + id, e);
			failTask(e);
		} finally {
			//清除线程范围的application code
			ContextUtil.clear();
		}
		//STATICLOGGER.debug("task " + id + " execution time " + (new Date().getTime() - startTime));
	}
	
	/**
	 * task业务执行
	 * @throws ApplicationException
	 */
	public abstract void doTask() throws Exception;
	
	/**
	 * 结束任务
	 */
	public void finishTask() {
		if(LOGGER.isInfoEnabled()){
			StringBuilder sb = new StringBuilder();
			sb.append("task[").append(this.id).append(']')
				.append(" at channel ").append(context.getChannelCode())
				.append('[').append(context.getApplicationCode()).append(']')
				.append(" finished for data ").append(dataId);
			LOGGER.info(sb.toString());
		}
		//update task status and finish time
		this.status = TASK_STATUS_FINISH;
		this.endTime = new Date();
		taskService.updateTaskStatusAndEndTime(this, null);
	}
	
	/**
	 * task失败
	 */
	public void failTask(Exception e) {
		//update task status and end time
		this.status = TASK_STATUS_FAIL;
		this.endTime = new Date();
		String errorMessage = e.getMessage();
		if (!StringUtils.hasText(errorMessage)) {
			errorMessage = e.getClass().getSimpleName();
		}
		LOGGER.error("task " + id + " run fail, set status = " + this.status + ", errorMessage = " + errorMessage);
		if (errorMessage.length() > 150) {
			errorMessage = errorMessage.substring(0, 150);
		}
		taskService.updateTaskStatusAndEndTime(this, errorMessage);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TaskTemplate getTemplate() {
		return template;
	}
	public void setTemplate(TaskTemplate template) {
		this.template = template;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public TaskContext getContext() {
		return context;
	}
	public void setContext(TaskContext context) {
		this.context = context;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Task [taskService=" + taskService + ", id=" + id
				+ ", template=" + template + ", dataId=" + dataId + ", data="
				+ data + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", context=" + context + ", status=" + status + "]";
	}
	
}
