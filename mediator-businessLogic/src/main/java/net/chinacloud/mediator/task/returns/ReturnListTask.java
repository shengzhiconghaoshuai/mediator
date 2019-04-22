/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ReturnListTask.java
 * 描述： 退货列表
 */
package net.chinacloud.mediator.task.returns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.service.ReturnService;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <退货列表>
 * <退货列表>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年3月12日
 * @since 2015年3月12日
 */
@Component("returnListTask")
@Scope(value="prototype")
public class ReturnListTask extends ReturnTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnListTask.class);
	
	private static final int TASK_INTERVAL_BUFFER = 5;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;

	@Override
	protected String getSubType() {
		return "list";
	}

	@Override
	public void doTask() throws ApplicationException {
		ReturnService returnService = getService(ReturnService.class, getContext().getChannelCode());
		List<CommonNotifyPacket<Return>> returns = new ArrayList<CommonNotifyPacket<Return>>();
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Return List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
		TaskTemplate template = getTemplate();
		int templateId = 0;
		if (null != template) {
			templateId = template.getId();
		}
		
		//每次调度运行时,开始时间往前推 TASK_INTERVAL
    	Date lastDate = lasttimeService.getLastime(applicationId, templateId);
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("-------lastDate:" + DateUtil.format(lastDate) + "-------");
    	}
    	
    	Date startTime = DateUtil.modify(lastDate, -TASK_INTERVAL);
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("-------startTime:" + DateUtil.format(startTime) + "-------");
    	}
    	
        Date endTime = DateUtil.modify(new Date(), TASK_INTERVAL);
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("-------endTime:" + DateUtil.format(endTime) + "-------");
        }
		
		String statusStr = (String)getContext().get(Constant.SCHEDULE_PARAM_STATUS);
		if(null != statusStr && !"".equals(statusStr)){
			for(String status : statusStr.split(",")){
				List<CommonNotifyPacket<Return>> list = returnService.getReturnListByStatus(status, startTime, endTime);
				returns.addAll(list);
			}
		}else{
			List<CommonNotifyPacket<Return>> list = returnService.getReturnListByStatus(null, startTime, endTime);
			returns.addAll(list);
		}
		
		//更新时间
		lasttimeService.insertOrUpdateLasttime(applicationId, templateId, endTime.getTime());
		
		if(!returns.isEmpty()){
			for(CommonNotifyPacket<Return> rtn : returns){
				Task task = taskManager.generateTask(getContext().getChannelCode(), rtn);
				
				if (null != task) {
					//传递上下文
					task.setContext(getContext());
					
					taskManager.executeTask(task);
				}
			}
		}
	}

}
