package net.chinacloud.mediator.vip.vop.domain;

import java.io.Serializable;

public class VipInventoryMessage implements Serializable{


	private static final long serialVersionUID = -2665882899927825690L;
	private String SKU;
	
	private Integer quantity;
	private Integer syncMode=0;


	public String getSKU() {
		return SKU;
	}

	public void setSKU(String SKU) {
		this.SKU = SKU;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSyncMode() {
		return syncMode;
	}

	public void setSyncMode(Integer syncMode) {
		this.syncMode = syncMode;
	}
}