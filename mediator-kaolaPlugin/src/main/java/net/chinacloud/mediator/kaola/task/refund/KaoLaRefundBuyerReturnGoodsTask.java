package net.chinacloud.mediator.kaola.task.refund;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.kaola.bean.KaolaRefundSku;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.refund.RefundTask;
import net.chinacloud.mediator.translator.ChannelRefundFacadeClient;
import net.chinacloud.mediator.utils.StringUtils;
@Component
@Scope(value="prototype")
public class KaoLaRefundBuyerReturnGoodsTask extends RefundTask {
	private static final String REFUND_KAOLA_BUYER_RETURN_GOODS_TYPE = "kaolaBuyerReturnGoods";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaRefundBuyerReturnGoodsTask.class);
	
	static {
		TaskManager.registTask(REFUND_TYPE, REFUND_KAOLA_BUYER_RETURN_GOODS_TYPE, KaoLaRefundBuyerReturnGoodsTask.class);
	}
	
	@Autowired
	ChannelRefundFacadeClient refundClient;
	
	@Override
	protected String getSubType() {
		return REFUND_KAOLA_BUYER_RETURN_GOODS_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		Refund refund = getRefund();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("kaola buyer return good refund info:" + refund);
		}
		
		if (null != refund) {
			if(!StringUtils.hasText(refund.getChannelOrderItemId())){
				@SuppressWarnings("unchecked")
				List<KaolaRefundSku> refundSkus = (List<KaolaRefundSku>) refund.getAdditionalParam("refundSkus");
				for(KaolaRefundSku refundSku : refundSkus){
					refund.setChannelOrderItemId(refund.getChannelOrderId()+"_"+refundSku.getSku_key());
					refund.setQuantity(Double.valueOf(refundSku.getRefund_count()));
					if(refundSku.getIs_gift()==1){
						refund.setRefundFee(0D);
					}
					refundClient.buyerReturnGoods(refund, getContext(), this.id);
				}
			}else{
				refundClient.buyerReturnGoods(refund, getContext(), this.id);
			}
			
		}
	}


}
