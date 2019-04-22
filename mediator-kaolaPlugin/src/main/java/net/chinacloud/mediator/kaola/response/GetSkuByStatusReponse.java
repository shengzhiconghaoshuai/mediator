package net.chinacloud.mediator.kaola.response;

import net.chinacloud.mediator.kaola.bean.KaoLaProductList;

public class GetSkuByStatusReponse extends KaoLaResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private KaoLaProductList kaola_item_searchBasicByCondition_response;

	public KaoLaProductList getKaola_item_searchBasicByCondition_response() {
		return kaola_item_searchBasicByCondition_response;
	}

	public void setKaola_item_searchBasicByCondition_response(
			KaoLaProductList kaola_item_searchBasicByCondition_response) {
		this.kaola_item_searchBasicByCondition_response = kaola_item_searchBasicByCondition_response;
	}
	

}
