/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TmallMessageProcessor.java
 * 描述： 天猫主动通知消息处理器
 */
package net.chinacloud.mediator.taobao.notify;

import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;
import net.chinacloud.mediator.task.exception.TaskException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.api.ApiException;
import com.taobao.api.internal.tmc.Message;

/**
 * <天猫主动通知消息处理器>
 * <天猫主动通知消息处理器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月12日
 * @since 2015年1月12日
 */
@Component
public class TmallMessageProcessor {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(TmallMessageProcessor.class);
	
	@Autowired
	TmallMessageConverter converter;
	@Autowired
	TaskManager taskManager;
	@Autowired
	ChannelService channelService;
	
	public void process(Integer channelId, Integer applicationId, String applicationCode, Integer storeId, Message message) 
		throws TaskException, ApiException {
		try {
			CommonNotifyPacket<?> packet = converter.convert(message);
			
			Channel channel = channelService.getChannelById(channelId);
			
			Task task = taskManager.generateTask(channel.getCode(), packet);
			if(null != task){
	        	//set task context
	        	task.getContext().setApplicationId(applicationId);
	    		task.getContext().setApplicationCode(applicationCode);
	    		task.getContext().setChannelId(channelId);
	    		task.getContext().setChannelCode(channel.getCode());
	    		task.getContext().setStoreId(storeId);
	    		
	    		taskManager.executeTask(task);
	        }
		} catch (TaskException e) {
			//e.printStackTrace();
			//LOGGER.error("淘宝通知task生成失败", e);
			if (!(e instanceof DuplicateTaskException)) {
				throw e;
			}
		} catch (ApiException e) {
			//e.printStackTrace();
			//LOGGER.error("淘宝通知task生成失败", e);
			throw e;
		}
	}
}
