/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundSuccessTask.java
 * 描述： 退款成功task
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
 * <退款成功>
 * <退款成功,卖家退款成功>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月20日
 * @since 2015年1月20日
 */
@Component
@Scope(value="prototype")
public class RefundSuccessTask extends RefundTask {
	
	private static final String REFUND_SUCCESS_TYPE = "success";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundSuccessTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_SUCCESS_TYPE, RefundSuccessTask.class);
	}

	@Autowired
	ChannelRefundFacadeClient refundClient;
	
	@Override
	protected String getSubType() {
		// 退款成功
		return REFUND_SUCCESS_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("success refund info:" + refund);
		}
		
		refundClient.successRefund(refund, getContext(), this.id);
	}

}
