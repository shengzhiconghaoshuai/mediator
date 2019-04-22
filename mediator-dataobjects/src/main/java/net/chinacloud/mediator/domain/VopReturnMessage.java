package net.chinacloud.mediator.domain;

import java.util.List;

public class VopReturnMessage {

	private String order_id;
	
	private String carrier_code;
	
	private String carrier_name;
	
	private String delivery_no;
	
	private String note;
	
	private List<RefuseGoods> refuseGoods;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getCarrier_code() {
		return carrier_code;
	}

	public void setCarrier_code(String carrier_code) {
		this.carrier_code = carrier_code;
	}

	public String getCarrier_name() {
		return carrier_name;
	}

	public void setCarrier_name(String carrier_name) {
		this.carrier_name = carrier_name;
	}

	public String getDelivery_no() {
		return delivery_no;
	}

	public void setDelivery_no(String delivery_no) {
		this.delivery_no = delivery_no;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<RefuseGoods> getRefuseGoods() {
		return refuseGoods;
	}

	public void setRefuseGoods(List<RefuseGoods> refuseGoods) {
		this.refuseGoods = refuseGoods;
	}

	@Override
	public String toString() {
		return "VopReturnMessage [order_id=" + order_id + ", carrier_code="
				+ carrier_code + ", carrier_name=" + carrier_name
				+ ", delivery_no=" + delivery_no + ", note=" + note
				+ ", refuseGoods=" + refuseGoods + "]";
	}
}
