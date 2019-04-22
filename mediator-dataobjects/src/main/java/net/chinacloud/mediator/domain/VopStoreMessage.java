package net.chinacloud.mediator.domain;

public class VopStoreMessage {
	
	private String order_id;
	
	private String store_sn;
	
	private String package_sn;
	
	private Long estimate_delivery_time;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getStore_sn() {
		return store_sn;
	}

	public void setStore_sn(String store_sn) {
		this.store_sn = store_sn;
	}

	public String getPackage_sn() {
		return package_sn;
	}

	public void setPackage_sn(String package_sn) {
		this.package_sn = package_sn;
	}

	public Long getEstimate_delivery_time() {
		return estimate_delivery_time;
	}

	public void setEstimate_delivery_time(Long estimate_delivery_time) {
		this.estimate_delivery_time = estimate_delivery_time;
	}

	@Override
	public String toString() {
		return "VopStoreMessage [order_id=" + order_id + ", store_sn="
				+ store_sn + ", package_sn=" + package_sn
				+ ", estimate_delivery_time=" + estimate_delivery_time + "]";
	}

}
