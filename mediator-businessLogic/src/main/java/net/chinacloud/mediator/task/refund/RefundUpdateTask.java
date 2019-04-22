/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundUpdateTask.java
 * 描述： 退款修改task
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
 * <退款修改task>
 * <退款修改task,买家修改退款信息>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月20日
 * @since 2015年1月20日
 */
@Component
@Scope(value="prototype")
public class RefundUpdateTask extends RefundTask {
	
	private static final String REFUND_UPDATE_TYPE = "update";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundUpdateTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_UPDATE_TYPE, RefundUpdateTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;

	@Override
	protected String getSubType() {
		// 退款修改
		return REFUND_UPDATE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("update refund info:" + refund);
		}
		
		refundClient.updateRefund(refund, getContext(), this.id);
	}

}
