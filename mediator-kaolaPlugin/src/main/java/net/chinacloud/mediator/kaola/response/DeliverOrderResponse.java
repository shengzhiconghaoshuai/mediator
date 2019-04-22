package net.chinacloud.mediator.kaola.response;

import net.chinacloud.mediator.kaola.bean.KaoLaOrderDeliver;

public class DeliverOrderResponse extends KaoLaResponse{

	private static final long serialVersionUID = 7245099496294753327L;
	
	private KaoLaOrderDeliver kaola_logistics_deliver_response;

	public KaoLaOrderDeliver getKaola_logistics_deliver_response() {
		return kaola_logistics_deliver_response;
	}

	public void setKaola_logistics_deliver_response(
			KaoLaOrderDeliver kaola_logistics_deliver_response) {
		this.kaola_logistics_deliver_response = kaola_logistics_deliver_response;
	}

	@Override
	public String toString() {
		return "DeliverOrderResponse [kaola_logistics_deliver_response="
				+ kaola_logistics_deliver_response + "]";
	}
	
	
	
	

}
