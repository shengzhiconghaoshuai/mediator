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
 * <退款关闭>
 * <退款关闭,买家自己关闭或卖家拒绝关闭>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月20日
 * @since 2015年1月20日
 */
@Component
@Scope(value="prototype")
public class RefundClosedTask extends RefundTask {
	
	private static final String REFUND_CLOSE_TYPE = "close";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundClosedTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_CLOSE_TYPE, RefundClosedTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;

	@Override
	protected String getSubType() {
		// 退款关闭
		return REFUND_CLOSE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("close refund info:" + refund);
		}
		
		refundClient.closeRefund(refund, getContext(), this.id);
	}

}
