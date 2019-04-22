/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundCreateTask.java
 * 描述： 退款创建
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
 * <退款创建task>
 * <退款创建task,买家在渠道申请退款,此时卖家可能并没有同意.天猫申请仅退款、退货退款都会触发
 * 此通知,可以根据子订单的状态(是否已发货)来判断是哪种情况下的通知>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月20日
 * @since 2015年1月20日
 */
@Component("refundCreateTask")
@Scope(value="prototype")
public class RefundCreateTask extends RefundTask {
	
	private static final String REFUND_CREATE_TYPE = "create";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundCreateTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_CREATE_TYPE, RefundCreateTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;

	@Override
	protected String getSubType() {
		// 退款创建
		return REFUND_CREATE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("create refund info:" + refund);
		}
		
		refundClient.submitChannelRefund(refund, getContext(), this.id);
	}

}
