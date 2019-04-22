package net.chinacloud.mediator.vip.vop.domain;

import java.io.Serializable;
import java.util.List;

import vipapis.inventory.OccupiedOrder;

public class OccupiedOrderList implements Serializable{

	private static final long serialVersionUID = 846476309537075387L;
	
	private List<OccupiedOrder> orders;

	public List<OccupiedOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<OccupiedOrder> orders) {
		this.orders = orders;
	}

}
