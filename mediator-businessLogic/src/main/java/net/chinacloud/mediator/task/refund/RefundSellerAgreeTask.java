/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundSellerAgreeTask.java
 * 描述： 
 */
package net.chinacloud.mediator.task.refund;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelRefundFacadeClient;

/**
 * @description 卖家同意退款
 * @author yejunwu123@gmail.com
 * @since 2015年7月20日 下午1:32:59
 */
@Component
@Scope(value="prototype")
public class RefundSellerAgreeTask extends RefundTask {
	
	private static final String REFUND_SELLER_AGREE_TYPE = "sellerAgree";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundSellerAgreeTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_SELLER_AGREE_TYPE, RefundSellerAgreeTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;

	@Override
	protected String getSubType() {
		return REFUND_SELLER_AGREE_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("sellerAgree refund info:" + refund);
		}
		
		if (null != refund) {
			refundClient.sellerAgree(refund, context, this.id);;
		}
	}

}
