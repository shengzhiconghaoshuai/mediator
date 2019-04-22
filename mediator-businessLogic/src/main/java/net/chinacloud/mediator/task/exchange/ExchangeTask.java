package net.chinacloud.mediator.task.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.exception.exchange.ExchangeException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.service.ExchangeService;
import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.utils.JsonUtil;

/**
 * 换货task的基类
 * @author liling 
 *
 */
@Component
@Scope(value="prototype")
public abstract class ExchangeTask extends Task {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeTask.class);
	protected static final String EXCHANGE_TYPE = "EXCHANGE";
	
	@Autowired
	private ServiceManager serviceManager;

	@Override
	protected String getType() {
		return EXCHANGE_TYPE;
	}

	protected Exchange getExchange()  {
		Exchange exchange = null;
		ExchangeService exchangeService = null;
		
		// update 2015-06-26
		// 这里需要判断类型,扫尾的时候数据类型在这边转换
		Object data = getData();
		if(data instanceof Exchange){
			exchange = (Exchange)getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				exchange = JsonUtil.jsonString2Object(strData, Exchange.class);
			} else {
				String dataId = null;
				if ('"' == first) {
					dataId = JsonUtil.jsonString2Object(strData, String.class);
				} else {
					dataId = getDataId();
					String [] str = dataId.split("_");
					dataId = str[0];
				}
				
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("get Exchange " + getDataId() + "by service from channel " + getContext().getChannelCode());
				}
				exchangeService = getService(ExchangeService.class, getContext().getChannelCode());
				try {
					exchange = exchangeService.getExchangeById(dataId);
				} catch (TranslateException e) {
					LOGGER.error("重跑根据换货单号:"+dataId+"获取换货数据装换失败,"+e.getMessage());
				} catch (ExchangeException e) {
					LOGGER.error("重跑根据换货单号:"+dataId+"获取换货失败，"+e.getMessage());
				}
			}
		}
		
		return exchange;
	}
	/**
	 * 获取渠道对应的业务接口实现
	 * @param clazz 业务接口
	 * @param channelCode 渠道标识
	 * @return
	 */
	protected <T> T getService(Class<T> clazz, String channelCode){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("get service " + clazz.getSimpleName() + " for channel " + channelCode);
		}
		return serviceManager.getService(clazz, channelCode);
	}
	

}
