package net.chinacloud.mediator.kaola.task.skuBasic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.utils.JsonUtil;

@Component
@Scope(value="prototype")
public abstract class SkuTask extends Task{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SkuTask.class);
	protected static final String SKU_TYPE = "SKU";
	
	@Autowired
	private ServiceManager serviceManager;
	
	@Override
	protected String getType() {
		
		return SKU_TYPE;
	}
	
	protected Sku getSku()  {
		Sku Sku = null;
		Object data = getData();
		if(data instanceof Sku){
			Sku = (Sku)getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				Sku = JsonUtil.jsonString2Object(strData, Sku.class);
			}
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("get Sku " + getDataId() + "by service from channel " + getContext().getChannelCode());
			}
		}
		return Sku;
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
