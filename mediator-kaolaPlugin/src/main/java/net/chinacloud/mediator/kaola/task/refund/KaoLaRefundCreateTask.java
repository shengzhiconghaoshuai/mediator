package net.chinacloud.mediator.kaola.task.refund;

import java.util.List;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.refund.RefundTask;
import net.chinacloud.mediator.translator.ChannelRefundFacadeClient;
import net.chinacloud.mediator.utils.StringUtils;
import net.chinacloud.mediator.kaola.bean.KaolaRefundSku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("kaoLaRefundCreateTask")
@Scope(value="prototype")
public class KaoLaRefundCreateTask extends RefundTask {

private static final String REFUND_KAOLA_CREATE_TYPE = "kaolaCreate";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaRefundCreateTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_KAOLA_CREATE_TYPE, KaoLaRefundCreateTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;

	@Override
	protected String getSubType() {
		// 考拉退款创建
		return REFUND_KAOLA_CREATE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("create kaola refund info:" + refund);
		}
//		
		if(refund !=null){
			if(!StringUtils.hasText(refund.getChannelOrderItemId())){
				@SuppressWarnings("unchecked")
				List<KaolaRefundSku> refundSkus = (List<KaolaRefundSku>) refund.getAdditionalParam("refundSkus");
				for(KaolaRefundSku refundSku : refundSkus){
					refund.setChannelOrderItemId(refund.getChannelOrderId()+"_"+refundSku.getSku_key());
					refund.setQuantity(Double.valueOf(refundSku.getRefund_count()));
					if(refundSku.getIs_gift()==1){
						refund.setRefundFee(0D);
					}
					refundClient.submitChannelRefund(refund, getContext(), this.id);
				}
			}else{
				refundClient.submitChannelRefund(refund, getContext(), this.id);
			}
		}
	}

}
