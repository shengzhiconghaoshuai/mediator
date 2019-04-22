/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundTask.java
 * 描述： 退款抽象task
 */
package net.chinacloud.mediator.task.refund;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.refund.RefundException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.service.RefundService;
import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <退款抽象task>
 * <退款抽象task>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月19日
 * @since 2015年1月19日
 */
@Component
@Scope(value="prototype")
public abstract class RefundTask extends Task {
	
	protected static final String REFUND_TYPE = "REFUND";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundTask.class);
	
	@Autowired
	private ServiceManager serviceManager;
	
	@Override
	protected String getType() {
		return REFUND_TYPE;
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
	
	/**
	 * 
	 * @return
	 * @throws RefundException
	 * @throws TranslateException
	 */
	protected Refund getRefund() throws RefundException, TranslateException {
		Refund refund = null;
		Object data = getData();
		if (data instanceof Refund) {
			refund = (Refund)getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				refund = JsonUtil.jsonString2Object(strData, Refund.class);
			} else {
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("get refund " + getDataId() + "by service from channel " + getContext().getChannelCode());
				}
				RefundService refundService = getService(RefundService.class, getContext().getChannelCode());
				refund = refundService.getRefundByRefundId(getDataId());
			}
		}
		return refund;
	}
}
