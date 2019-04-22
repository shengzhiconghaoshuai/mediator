package net.chinacloud.mediator.vip.vop.domain;


import java.io.Serializable;

public class DeliverySkuDetailBean implements Serializable {
	private static final long serialVersionUID = -5192670990633684655L;
	private String sku;
	private String box_no;
	private String pick_no;
	private int sku_num;
	private String vendor_type;
	private String orderCode;
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public DeliverySkuDetailBean() {
	}

	public DeliverySkuDetailBean(String sku, String box_no, int sku_num,
			String vendor_type, String pick_no) {
		this.sku = sku;
		this.box_no = box_no;
		this.sku_num = sku_num;
		this.vendor_type = vendor_type;
		this.pick_no = pick_no;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getVendor_type() {
		return vendor_type;
	}

	public void setVendor_type(String vendor_type) {
		this.vendor_type = vendor_type;
	}

	public String getBox_no() {
		return box_no;
	}

	public void setBox_no(String box_no) {
		this.box_no = box_no;
	}

	public String getPick_no() {
		return pick_no;
	}

	public void setPick_no(String pick_no) {
		this.pick_no = pick_no;
	}

	public int getSku_num() {
		return sku_num;
	}

	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}

}
