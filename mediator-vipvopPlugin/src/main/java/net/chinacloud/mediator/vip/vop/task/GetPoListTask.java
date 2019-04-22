package net.chinacloud.mediator.vip.vop.task;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.vip.vop.client.ChannelVIPPoOrdersFacadeClient;
import net.chinacloud.mediator.vip.vop.domain.PoList;
import net.chinacloud.mediator.vip.vop.domain.PoMessage;
import net.chinacloud.mediator.vip.vop.service.VopPoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2017年9月13日 下午3:25:23
 */
@Component("getPoListTask")
@Scope(value="prototype")
public class GetPoListTask extends VopCommonTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetPoListTask.class);

	private static final String ORDER_TYPE = "ORDER";
	
	private static final String GET_POLIST = "getPoList";
	
	private static final int TASK_INTERVAL = 1000 * 60 * 5 ;
	
	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		return GET_POLIST;
	}
	
	@Autowired
	ChannelVIPPoOrdersFacadeClient client;
	
	@Autowired
	CronLasttimeService lasttimeService;

	@Override
	public void doTask() throws Exception {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		
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
        
		VopPoService poService = getService(VopPoService.class, getContext().getChannelCode());
		List<PoMessage> poMessage = poService.getPoOrders(startTime, endTime);
		PoList poList = new PoList();
		poList.setPo_nos(poMessage);
		client.postPoList(poList, context, this.id);
	}

}
