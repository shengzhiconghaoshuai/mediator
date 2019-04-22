package net.chinacloud.mediator.xhs.bean;

public class XhsOrderItem {
	private String barcode;
	private String skucode;
	private String item_name;
	private int qty;
	private String specification;
	private String price;
	private String pay_price;
	private Double net_weight;
	private String register_name;
	private String merchant_discount;
	private String red_discount;
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPay_price() {
		return pay_price;
	}
	public void setPay_price(String pay_price) {
		this.pay_price = pay_price;
	}
	public Double getNet_weight() {
		return net_weight;
	}
	public void setNet_weight(Double net_weight) {
		this.net_weight = net_weight;
	}
	public String getRegister_name() {
		return register_name;
	}
	public void setRegister_name(String register_name) {
		this.register_name = register_name;
	}
	public String getMerchant_discount() {
		return merchant_discount;
	}
	public void setMerchant_discount(String merchant_discount) {
		this.merchant_discount = merchant_discount;
	}
	public String getRed_discount() {
		return red_discount;
	}
	public void setRed_discount(String red_discount) {
		this.red_discount = red_discount;
	}
	
}
