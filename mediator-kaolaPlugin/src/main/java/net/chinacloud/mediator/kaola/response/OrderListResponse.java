package net.chinacloud.mediator.kaola.response;


import net.chinacloud.mediator.kaola.bean.KaoLaOrderList;

public class OrderListResponse extends KaoLaResponse {

	private static final long serialVersionUID = -7823004113041293509L;
	
	private KaoLaOrderList kaola_order_search_response;

	public KaoLaOrderList getKaola_order_search_response() {
		return kaola_order_search_response;
	}

	public void setKaola_order_search_response(
			KaoLaOrderList kaola_order_search_response) {
		this.kaola_order_search_response = kaola_order_search_response;
	}
	
	
	
	
}
