package net.chinacloud.mediator.domain;

public class RefuseGoods {

	private String barcode;
	
	private String product_name;
	
	private Integer amount;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "RefuseGoods [barcode=" + barcode + ", product_name="
				+ product_name + ", amount=" + amount + "]";
	}
	
}
