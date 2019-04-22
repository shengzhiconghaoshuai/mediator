/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundListTask.java
 * 描述： 抓取退款列表task
 */
package net.chinacloud.mediator.kaola.task.refund;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.service.RefundService;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.refund.RefundTask;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <抓取退款列表task>
 * <抓取退款列表task>
 * @description kaola抓取退款列表
 * @since 2018年3月22日 下午4:00:02
 */
@Component
@Scope(value="prototype")
public class KaoLaRefundListTask extends RefundTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaRefundListTask.class);
	
	private static final int TASK_INTERVAL_BUFFER = 48*60;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	private static final String KAOLA_REFUND_LIST_TYPE = "KaoLaList";
	
	static {
		TaskManager.registTask(REFUND_TYPE, KAOLA_REFUND_LIST_TYPE, KaoLaRefundListTask.class);
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;

	@Override
	protected String getSubType() {
		// 退款列表
		return KAOLA_REFUND_LIST_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		RefundService refundService = getService(RefundService.class, getContext().getChannelCode());
		List<CommonNotifyPacket<Refund>> refunds = new ArrayList<CommonNotifyPacket<Refund>>();
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Kaola Refund List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
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
    	
        Date endTime = DateUtil.modify(new Date(),0);
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("-------endTime:" + DateUtil.format(endTime) + "-------");
        }
		String name=null;
		String statusStr = (String)getContext().get(Constant.SCHEDULE_PARAM_STATUS);
		if(null != statusStr && !"".equals(statusStr)){
			for(String status : statusStr.split(",")){
				List<CommonNotifyPacket<Refund>> list = refundService.getRefundListByStatus(status, startTime, endTime,name);
				refunds.addAll(list);
			}
		}else{
			List<CommonNotifyPacket<Refund>> list = refundService.getRefundListByStatus(null, startTime, endTime,name);
			refunds.addAll(list);
		}
		
		//更新时间
		lasttimeService.insertOrUpdateLasttime(applicationId, templateId, (new Date()).getTime());
		
		if(CollectionUtil.isNotEmpty(refunds)){
			for(CommonNotifyPacket<Refund> refund : refunds){
				try {
					Task task = taskManager.generateTask(getContext().getChannelCode(), refund);
					
					if (null != task) {
						//传递上下文
						task.setContext(getContext());
						taskManager.executeTask(task);
					}
				} catch (Exception e) {
					LOGGER.error("Kaola refund job process failure"+e.getMessage());
				}
			}
		}
	}

}
