package net.chinacloud.mediator.vip.vop.task;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.vip.vop.client.ChannelVIPOccupiedOrderFacadeClient;
import net.chinacloud.mediator.vip.vop.domain.OccupiedOrderList;
import net.chinacloud.mediator.vip.vop.service.VopProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import vipapis.inventory.OccupiedOrder;


@Component("getInventoryOccupiedOrdersTask")
@Scope(value="prototype")
public class GetInventoryOccupiedOrdersTask extends VopCommonTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetInventoryOccupiedOrdersTask.class);
	
	private static final String ORDER_TYPE = "ORDER";
	
	private static final String GET_INVENTORYOCCUPIEDORDERS = "getInventoryOccupiedOrders";
	
	private static final int TASK_INTERVAL = 1000 * 60 * 5 ;

	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		return GET_INVENTORYOCCUPIEDORDERS;
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	ChannelVIPOccupiedOrderFacadeClient client;
	
	@Override
	public void doTask() throws Exception {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Order getInventoryOccupiedOrders Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
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
        
        VopProductService service = getService(VopProductService.class, getContext().getChannelCode());
        List<OccupiedOrder> orders = service.getInventoryOccupiedOrders(startTime.getTime(), endTime.getTime());
        OccupiedOrderList order = new OccupiedOrderList();
        order.setOrders(orders);
        client.postOccupiedOrder(order, context, this.id);
	}

}
