/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundClosedTask.java
 * 描述： 退款关闭task
 */
package net.chinacloud.mediator.task.refund;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelRefundFacadeClient;

/**
 * <整单退款关闭>
 * @author liling@wuxicloud.com
 * @version 0.0.0,2017年6月28日
 * @since 2017年6月28日
 */
@Component
@Scope(value="prototype")
public class RefundWholeClosedTask extends RefundTask {
	
	private static final String REFUND_WHOLECLOSE_TYPE = "wholeClose";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundWholeClosedTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_WHOLECLOSE_TYPE, RefundWholeClosedTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;

	@Override
	protected String getSubType() {
		// 整单退款关闭
		return REFUND_WHOLECLOSE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("close whole refund info:" + refund);
		}
		
		refundClient.closeWholeRefund(refund, getContext(), this.id);
	}

}
