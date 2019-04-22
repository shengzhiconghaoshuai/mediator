package net.chinacloud.mediator.vip.vop.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.translator.ChannelReturnFacadeClient;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.vip.vop.service.VopReturnService;
import net.chinacloud.mediator.domain.VopOrderReturn;
import net.chinacloud.mediator.domain.VopOrderReturnList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component("orderReturnListTask")
@Scope(value="prototype")
public class OrderReturnListTask extends VopCommonTask{
	
private static final Logger LOGGER = LoggerFactory.getLogger(OrderReturnListTask.class);
	
	private static final int TASK_INTERVAL_BUFFER = 60;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	private static final String OXO_RETURN_TYPE = "oxoReturnList";
	
	static {
		TaskManager.registTask("RETURN", OXO_RETURN_TYPE, OrderReturnListTask.class);
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	ChannelReturnFacadeClient returnClient;

	@Override
	protected String getSubType() {
		// 退款列表
		return OXO_RETURN_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		VopReturnService service = getService(VopReturnService.class, getContext().getChannelCode());	
		List<VopOrderReturn> orderReturns = new ArrayList<VopOrderReturn>();
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Refund List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
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
		String statusStr = (String)getContext().get(Constant.SCHEDULE_PARAM_STATUS);
		if(null != statusStr && !"".equals(statusStr)){
			for(String status : statusStr.split(",")){
				List<VopOrderReturn> list = service.getOrderReturnList(startTime, endTime, status);
				orderReturns.addAll(list);
			}
		}else{
			List<VopOrderReturn> list = service.getOrderReturnList(startTime, endTime, "0");
			orderReturns.addAll(list);
		}
		
		//更新时间
		lasttimeService.insertOrUpdateLasttime(applicationId, templateId, (new Date()).getTime());
		
		if(CollectionUtil.isNotEmpty(orderReturns)){
			LOGGER.debug("发送客退或拒签的订单列表");
			VopOrderReturnList vopOrderReturnList = new VopOrderReturnList();
			vopOrderReturnList.setOrderRetrunList(orderReturns);
			returnClient.sendVopOrderReturn(vopOrderReturnList, context, this.id);
		}
	}

	@Override
	protected String getType() {
		// TODO Auto-generated method stub
		return "RETURN";
	}

}


