package net.chinacloud.mediator.kaola.response;


import net.chinacloud.mediator.kaola.bean.KaoLaRefundList;

public class RefundListResponse extends KaoLaResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private  KaoLaRefundList kaola_refund_search_response;

	public KaoLaRefundList getKaola_refund_search_response() {
		return kaola_refund_search_response;
	}

	public void setKaola_refund_search_response(
			KaoLaRefundList kaola_refund_search_response) {
		this.kaola_refund_search_response = kaola_refund_search_response;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "RefundListResponse [kaola_refund_search_response="
				+ kaola_refund_search_response + "]";
	}
	
	
	
	

}
