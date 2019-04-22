package net.chinacloud.mediator.domain;

import java.util.ArrayList;
import java.util.List;

public class VopOrderReturnList {

	private List<VopOrderReturn> orderRetrunList;

	public List<VopOrderReturn> getOrderRetrunList() {
		return orderRetrunList;
	}

	public void setOrderRetrunList(List<VopOrderReturn> orderRetrunList) {
		this.orderRetrunList = orderRetrunList;
	}
	
	public void addOrderRetrun(VopOrderReturn vopOrderReturn){
		orderRetrunList = new ArrayList<VopOrderReturn>();
		orderRetrunList.add(vopOrderReturn);
	}
}
