package net.chinacloud.mediator.kaola.response;

import net.chinacloud.mediator.kaola.bean.KaolaSingleOrder;

public class OrderDetailResponse extends KaoLaResponse{

	private static final long serialVersionUID = -905210844724973699L;
	
	private KaolaSingleOrder kaola_order_get_response;

	public KaolaSingleOrder getKaola_order_get_response() {
		return kaola_order_get_response;
	}

	public void setKaola_order_get_response(
			KaolaSingleOrder kaola_order_get_response) {
		this.kaola_order_get_response = kaola_order_get_response;
	}

	@Override
	public String toString() {
		return "OrderDetailResponse [kaola_order_get_response="
				+ kaola_order_get_response + "]";
	}
	

	
	
}
