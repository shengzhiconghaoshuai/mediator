/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundSellerRefuseTask.java
 * 描述： 
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
 * @description 卖家拒绝退款申请,可能不需要
 * @author yejunwu123@gmail.com
 * @since 2015年7月7日 下午7:26:44
 */
@Component
@Scope(value="prototype")
public class RefundSellerRefuseTask extends RefundTask {

	private static final String REFUND_SELLER_REFUSE_TYPE = "sellerRefuse";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundSellerRefuseTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_SELLER_REFUSE_TYPE, RefundSellerRefuseTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;
	
	@Override
	protected String getSubType() {
		return REFUND_SELLER_REFUSE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("sellerRefuse refund info:" + refund);
		}
		
		if (null != refund) {
			refundClient.sellerRefuse(refund, context, this.id);;
		}
	}

}
