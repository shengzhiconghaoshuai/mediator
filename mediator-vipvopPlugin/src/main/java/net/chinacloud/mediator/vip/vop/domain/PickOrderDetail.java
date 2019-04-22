package net.chinacloud.mediator.vip.vop.domain;

import java.io.Serializable;

/**
 * 拣货单明细
 * 
 */
public class PickOrderDetail implements Serializable {

	private static final long serialVersionUID = -4377127402055254541L;
	
	private String sku;
	private String pro_name;
	private String art_no;
	private int stock;
	private String size;

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getArt_no() {
		return art_no;
	}

	public void setArt_no(String art_no) {
		this.art_no = art_no;
	}

	public String getPro_name() {
		return pro_name;
	}

	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
