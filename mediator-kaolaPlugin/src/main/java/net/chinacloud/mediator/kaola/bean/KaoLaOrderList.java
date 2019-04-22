package net.chinacloud.mediator.kaola.bean;

import java.util.List;


public class KaoLaOrderList {
	
	private List<KaolaOrder> orders;
	private int total_count;
	public List<KaolaOrder> getOrders() {
		return orders;
	}
	public void setOrders(List<KaolaOrder> orders) {
		this.orders = orders;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	@Override
	public String toString() {
		return "KaoLaOrderList [orders=" + orders + ", total_count="
				+ total_count + "]";
	}
	
}
