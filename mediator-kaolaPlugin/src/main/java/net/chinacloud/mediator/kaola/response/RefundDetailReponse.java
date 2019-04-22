package net.chinacloud.mediator.kaola.response;

import net.chinacloud.mediator.kaola.bean.KaoLaRefund;
import net.chinacloud.mediator.kaola.bean.KaoLarefundDetailInfoDTO;

public class RefundDetailReponse extends KaoLaResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 741956915381975110L;
	
	private KaoLarefundDetailInfoDTO kaola_refund_get_response;


	public KaoLarefundDetailInfoDTO getKaola_refund_get_response() {
		return kaola_refund_get_response;
	}

	public void setKaola_refund_get_response(
			KaoLarefundDetailInfoDTO kaola_refund_get_response) {
		this.kaola_refund_get_response = kaola_refund_get_response;
	}

	

	@Override
	public String toString() {
		return "RefundDetailReponse [kaola_refund_get_response="
				+ kaola_refund_get_response + "]";
	}



	
}
