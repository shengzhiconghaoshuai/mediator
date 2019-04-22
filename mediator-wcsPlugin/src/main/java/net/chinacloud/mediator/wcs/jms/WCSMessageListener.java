/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：WCSMessageListener.java
 * 描述： 消息监听器
 */
package net.chinacloud.mediator.wcs.jms;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.pojo.Errors;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageContext;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.service.TaskFailureService;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;
import net.chinacloud.mediator.wcs.WCSConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * <消息监听器>
 * <监听来自WCS的jms消息,统一处理>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月8日
 * @since 2015年1月8日
 */
@Component("wcsMessageListener")
public class WCSMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(WCSMessageListener.class);
	
	@Autowired
	ApplicationService applicationService;
	@Autowired
	ChannelService channelService;
	@Autowired
	TaskManager taskManager;
	@Autowired
	TaskService taskService;
	@Autowired
	TaskFailureService taskFailureService;
	
	public void processJMSMessage(String json) throws ApplicationException {
		try {
			//json = StringEscapeUtils.unescapeHtml(json);
			LOGGER.info("Received original JMS message {}", json);
			
			Map<String, Object> map = JsonUtil.jsonString2Map(json);
			
			String actionCode = (String)map.get("actionCode");
			
			//失败消息回传
			if (MessageActionCode.ACTION_CODE_BUSINESS_PROCESS_ERROR.equals(actionCode)) {
				processFailureMessage(json);
			}
			
			String content = JsonUtil.object2JsonString(map.get("content"));
			
			String contextStr = JsonUtil.object2JsonString(map.get("context"));
			MessageContext context =  JsonUtil.jsonString2Object(contextStr, MessageContext.class);
			MessageObject<String> messageObject = new MessageObject<String>(actionCode, content, context);
			
			//对context处理,判断消息是某个渠道的还是所有渠道的
			//ApplicationService applicationService = SpringUtil.getBean(ApplicationService.class);
			//ChannelService channelService = SpringUtil.getBean(ChannelService.class);
	        Integer storeId = null;
	        Application application = null;
	        if (context != null) {
	        	storeId = context.getStoreId();
	        	if (storeId != null) {
	        		application = applicationService.getApplicationByStoreId(storeId);
	        		LOGGER.debug("retrive application {}", application);
	        	}
	        }
	        //TaskManager taskManager = SpringUtil.getBean(TaskManager.class);
	        Task task = null;
	        if (null != application) {	//单渠道
	        	CommonNotifyPacket<MessageObject<String>> packet = new CommonNotifyPacket<MessageObject<String>>(messageObject);
	    		task = taskManager.generateTask(WCSConstant.CHANNEL_FLAG_WCS, packet);
	    		
	    		if (null != task) {
	    			Channel channel = channelService.getChannelById(application.getChannelId());
	    			task.getContext().setChannelCode(channel.getCode());
	    			task.getContext().setChannelId(application.getChannelId());
	    			task.getContext().setApplicationCode(application.getCode());
	    			task.getContext().setApplicationId(application.getId());
	    			task.getContext().setStoreId(application.getStoreId());
	    			
	    			//库存同步的单独用线程池跑,不占用原来的线程池
	    			if (MessageActionCode.ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_SKUS.equals(actionCode) 
	    					|| MessageActionCode.ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_PRODUCTS.equals(actionCode) ) {
	    				ThreadPoolExecutor executor = SpringUtil.getBean("wcsThreadPool", ThreadPoolExecutor.class);
	    				executor.execute(task);
	    			} else {
	    				taskManager.executeTask(task);
	    			}
	    		}
	        } 
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error("jms message process error", e);
			MailSendUtil.sendEmail("jms message process error", 
					MessageFormat.format("jms message process error, original json is {0}, exception {1}", json, e.getMessage()));
		}
	}
	
	/**
	 * 处理失败的消息
	 * @param errorJson
	 */
	public void processFailureMessage(String errorJson) {
		LOGGER.info("Received original error JMS message {}", errorJson);
		
		Map<String, Object> map = JsonUtil.jsonString2Map(errorJson);
		
		String contextStr = JsonUtil.object2JsonString(map.get("context"));
		MessageContext context =  JsonUtil.jsonString2Object(contextStr, MessageContext.class);
		Long taskId = context.getTaskId();
		
		String errorsStr = JsonUtil.object2JsonString(map.get("errors"));
		List<Errors> errors = JsonUtil.jsonString2List(errorsStr, Errors.class);
		
		//由于多线程的原因,可能这边已经收到失败的消息,原来的task还没有完全跑完(状态还没更新上)
		//有可能会造成Task.finishTask()更新的状态覆盖taskDao.updateTaskStatus(...)更新的状态
		//导致本来task应该是失败状态,而task表中却显示成功状态
		if (null != taskId) {
			String errorCode = null;
			if (!CollectionUtil.isEmpty(errors)) {
				Errors error = errors.get(0);
				if (null != error) {
					errorCode = error.getErrorMsg();
					if (StringUtils.hasText(errorCode)) {
						if (errorCode.length() > 150) {
							errorCode = errorCode.substring(0, 150);
						}
					}
				}
			}
			int result = taskService.updateTaskStatus(taskId, Task.TASK_STATUS_FAIL, Task.TASK_STATUS_FINISH, errorCode);
			if(result == 0){
				LOGGER.debug("task {} 状态更新异常,error code = {}", taskId, errorCode);
				//result = 0 表示出现上述情况,将该记录持久化到临时表中
				try {
					taskFailureService.createTaskFailureMsg(taskId, errorCode, new Date(), 0);
				} catch (Exception e) {
					//task第二次重跑还出现这种情况,插入到表中会报主键重复异常,一般不会出现这种情况
					//出现这种情况说明线程争抢比较严重了,task表数据需要清理,系统需要优化了
					if (e instanceof DuplicateKeyException) {
						LOGGER.error("失败task {} 重复创建", taskId);
						taskFailureService.updateTaskFailureMsgStatus(taskId, 0, new Date());
					}
				}
			}
		}
	}
}
