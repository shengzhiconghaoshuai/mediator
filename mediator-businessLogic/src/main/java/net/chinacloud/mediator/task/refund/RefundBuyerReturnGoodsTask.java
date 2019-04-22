/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundBuyerReturnGoodsTask.java
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
 * @description 买家退货,上传物流信息
 * @author yejunwu123@gmail.com
 * @since 2015年7月7日 下午7:54:25
 */
@Component
@Scope(value="prototype")
public class RefundBuyerReturnGoodsTask extends RefundTask {

	private static final String REFUND_BUYER_RETURN_GOODS_TYPE = "buyerReturnGoods";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundBuyerReturnGoodsTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_BUYER_RETURN_GOODS_TYPE, RefundBuyerReturnGoodsTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;
	
	@Override
	protected String getSubType() {
		return REFUND_BUYER_RETURN_GOODS_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("buyer return good refund info:" + refund);
		}
		
		if (null != refund) {
			refundClient.buyerReturnGoods(refund, getContext(), this.id);
		}
	}

}
