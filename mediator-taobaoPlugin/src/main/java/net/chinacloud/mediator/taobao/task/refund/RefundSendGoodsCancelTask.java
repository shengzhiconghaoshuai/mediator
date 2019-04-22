package net.chinacloud.mediator.taobao.task.refund;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.taobao.service.TaobaoRefundService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.refund.RefundTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class RefundSendGoodsCancelTask extends RefundTask{
	
	private static final String REFUND_SENDGOODSCANCEL_TYPE = "SendGoodsCancel";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundSendGoodsCancelTask.class);

	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_SENDGOODSCANCEL_TYPE, RefundSendGoodsCancelTask.class);
	}
	
	@Override
	protected String getSubType() {
		return REFUND_SENDGOODSCANCEL_TYPE;
	}

	@Autowired
	TaobaoRefundService refundService;
	
	
	@Override
	public void doTask() throws Exception {
		Refund refund = getRefund();
		refundService.AgSendGoodsCancel(refund);
	}

}
