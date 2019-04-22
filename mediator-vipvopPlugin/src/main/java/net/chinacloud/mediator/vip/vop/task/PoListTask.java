/**
 * 文件名：PoListTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.vip.vop.domain.PoBean;
import net.chinacloud.mediator.vip.vop.service.VopPoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("poListTask")
@Scope(value="prototype")
public class PoListTask extends VopCommonTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(PoListTask.class);
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	int TASK_INTERVAL_BUFFER = 50;

	@Override
	protected String getType() {
		return "ORDER";
	}

	@Override
	protected String getSubType() {
		return "poList";
	}

	
	@Override
	public void doTask() throws ApplicationException {
		VopPoService poService = getService(VopPoService.class, getContext().getChannelCode());
		List<CommonNotifyPacket<PoBean>> notifys = new ArrayList<CommonNotifyPacket<PoBean>>();
		
		Integer appId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("PO List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
		TaskTemplate template = getTemplate();
		int templateId = 0;
		if (null != template) {
			templateId = template.getId();
		}
		
		Date lastDate = lasttimeService.getLastime(appId, templateId);
		long startTimeLong  = lastDate.getTime();
    	startTimeLong =  startTimeLong - 1000*60*TASK_INTERVAL_BUFFER;
    	
        Date endTime = DateUtil.modify(new Date(), 60 * TASK_INTERVAL_BUFFER);
        
        List<CommonNotifyPacket<PoBean>> temp = poService.getPoListByStatus(null, startTime, endTime);
        if(temp!=null && temp.size()>0){
    		notifys.addAll(temp);
    	}
        
        if(!notifys.isEmpty()){
        	for(CommonNotifyPacket<PoBean> packet : notifys){
        		Task task = taskManager.generateTask(getContext().getChannelCode(), packet);//packet已经设置type
				
				if (null != task) {
					//传递上下文
					task.setContext(getContext());
					
					taskManager.executeTask(task);
				}
        	}
        }
		
	}

}
