/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：PersistiveTaskRejectionHandler.java
 * 描述： 由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序
 */
package net.chinacloud.mediator.task;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.chinacloud.mediator.task.service.TaskService;
/**
 * <由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序>
 * <根据task对应的template的>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月4日
 * @since 2015年1月4日
 */
public class PersistiveTaskRejectionHandler implements RejectedExecutionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersistiveTaskRejectionHandler.class);
	
	@Autowired
	TaskService taskService;
	
	//默认暂停时间
	public static final int DEFAULT_RETRY_INTERVAL = 1;
	
	private int retryInterval = DEFAULT_RETRY_INTERVAL;
	
	public int getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		if(r instanceof Task){
			Task task = (Task)r;
			TaskTemplate template = task.getTemplate();
			
			if(null != template){
				if(template.canHung()){
					StringBuilder sb = new StringBuilder();
					sb.append("task").append('[')
						.append(task.getId())
						.append(']')
						.append('[')
						.append(template.getType()).append('/').append(template.getSubType())
						.append(']')
						.append('[')
						.append(task.getContext().getChannelCode()).append('/').append(task.getContext().getApplicationCode())
						.append(']')
						.append(" 由于线程数不够被挂起...");
					LOGGER.error(sb.toString());
					
					//该类型的task允许被挂起,更新task的状态
					task.setStatus(Task.TASK_STATUS_HANG);
					taskService.updateTaskStatus(task);
				}else{
					StringBuilder sb = new StringBuilder();
					sb.append("task").append('[')
						.append(task.getId())
						.append(']')
						.append('[')
						.append(template.getType()).append('/').append(template.getSubType())
						.append(']')
						.append('[')
						.append(task.getContext().getChannelCode()).append('/').append(task.getContext().getApplicationCode())
						.append(']')
						.append(" 由于线程数不够被挂起,但该task不允许被挂起,重新运行...");
					LOGGER.error(sb.toString());
					
					try {
						Thread.sleep(retryInterval * 1000);
					} catch (InterruptedException e) {
						//e.printStackTrace();
						LOGGER.error("", e);
					}
					executor.execute(task);
					//task.run();
				}
			}else{
				StringBuilder sb = new StringBuilder();
				sb.append("task").append('[')
					.append(task.getId())
					.append(']')
					.append('[')
					.append(task.getContext().getChannelCode()).append('/').append(task.getContext().getApplicationCode())
					.append(']')
					.append(" 由于线程数不够被挂起,但该task对应的template为空,直接挂起...");
				LOGGER.error(sb.toString());
				//如果task没有配置对应的模板,则视为可以挂起
				task.setStatus(Task.TASK_STATUS_HANG);
				taskService.updateTaskStatus(task);
			}
		}
	}

}
