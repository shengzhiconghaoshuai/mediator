package net.chinacloud.mediator.task.exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.service.ExchangeService;
import net.chinacloud.mediator.system.registry.RegistryData;
import net.chinacloud.mediator.system.registry.dao.RegistryDao;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component
@Scope(value="prototype")
public class ExchangeListTask extends ExchangeTask {
private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeListTask.class);
	
	private static final int TASK_INTERVAL_BUFFER = 3*60;
	
	private static final int TASK_INTERVAL = 1000 * 60 * TASK_INTERVAL_BUFFER;
	
	protected static final String EXCHANGE_LIST_TYPE = "list";
	private static final int TASK_PREHOUR = 1000 * 60 * 60;//往前推的时间，特殊情况
	
	static {
		TaskManager.registTask(EXCHANGE_TYPE, EXCHANGE_LIST_TYPE, ExchangeListTask.class);
	}
	
	@Autowired
	CronLasttimeService lasttimeService;
	
	@Autowired
	private TaskManager taskManager;
	@Autowired
	private RegistryDao registryDao;

	@Override
	protected String getSubType() {
		return EXCHANGE_LIST_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		ExchangeService exchangeService = getService(ExchangeService.class, getContext().getChannelCode());
		List<CommonNotifyPacket<Exchange>> exchanges = new ArrayList<CommonNotifyPacket<Exchange>>();
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		LOGGER.info("Exchange List Cron - applicationCode:" + ContextUtil.get(Constant.APPLICATION_CODE));
    	
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
    	long endtimePreHour;
		try {
			RegistryData registryData = registryDao.getRegistry("endtime_exchange_hour");
			endtimePreHour = Long.valueOf(registryData.getValue());
		} catch (NumberFormatException e1) {
			endtimePreHour = 0;
			LOGGER.error("-------endtime_exchange_hour,格式有问题-------",e1);
		}
    	
        Date endTime = DateUtil.modify(new Date(),endtimePreHour*TASK_PREHOUR);
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("-------endTime:" + DateUtil.format(endTime) + "-------");
        }
		
			
		List<CommonNotifyPacket<Exchange>> list = exchangeService.getExchangeListByTime(startTime, endTime);
		exchanges.addAll(list);
		
		//更新时间
		lasttimeService.insertOrUpdateLasttime(applicationId, templateId, endTime.getTime());
		
		if(!exchanges.isEmpty()){
			for(CommonNotifyPacket<Exchange> exchange : exchanges){
				try {
					exchange.setType("info");//查出换货list中所有状态的换货单
					Task task = taskManager.generateTask(getContext().getChannelCode(), exchange);
					if (null != task) {
						//传递上下文
						task.setContext(getContext());
						
						taskManager.executeTask(task);
					}
				} catch (Exception e) {
					if(!(e instanceof DuplicateTaskException)){
						String channelExchangeId = ((null == exchange.getMessage()) ? "" : exchange.getMessage().getDisputeId());
						MailSendUtil.sendEmail("Exchange job process Exchange failure, channelExchangeId:" + channelExchangeId, 
								JsonUtil.object2JsonString(exchange.getMessage()));
						LOGGER.error("Exchange job process Exchange failure, channelExchangeId:" + channelExchangeId, e);
					}
				}
			}
		}
	}

}
