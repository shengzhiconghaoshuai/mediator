package net.chinacloud.mediator.task.refund;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelRefundFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <整单退款创建task>
 * @author liling@wuxicloud.com
 * @version 0.0.0,2017年6月28日
 * @since 2017年6月28日
 */
@Component("refundWholeCreateTask")
@Scope(value="prototype")
public class RefundWholeCreateTask extends RefundTask {
	
	private static final String REFUND_WHOLECREATE_TYPE = "wholeCreate";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundWholeCreateTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_WHOLECREATE_TYPE, RefundWholeCreateTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;

	@Override
	protected String getSubType() {
		// 退款创建
		return REFUND_WHOLECREATE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("create whole refund info:" + refund);
		}
		
		refundClient.createWholeRefund(refund, getContext(), this.id);
	}

}

