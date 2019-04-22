package net.chinacloud.mediator.xhs.bean;

public class XhsProduct {
	private String status;//商品状态(0为编辑中，1为待审核，2为审核通过)
	private String spu_id;
	private int update_time;
	private String skucode;
	private Double price;
	private String barcode;
	private String spl_id;
	private String spv_id;
	private int craete_time;
	private String item_id;
	private String name;
	private Boolean buyable;//true为可售卖， false为不可售卖
	private int stock;
	private Boolean is_frozen;//true为已冻结，false为未冻结
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSpu_id() {
		return spu_id;
	}
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}
	public int getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSpl_id() {
		return spl_id;
	}
	public void setSpl_id(String spl_id) {
		this.spl_id = spl_id;
	}
	public String getSpv_id() {
		return spv_id;
	}
	public void setSpv_id(String spv_id) {
		this.spv_id = spv_id;
	}
	public int getCraete_time() {
		return craete_time;
	}
	public void setCraete_time(int craete_time) {
		this.craete_time = craete_time;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getBuyable() {
		return buyable;
	}
	public void setBuyable(Boolean buyable) {
		this.buyable = buyable;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public Boolean getIs_frozen() {
		return is_frozen;
	}
	public void setIs_frozen(Boolean is_frozen) {
		this.is_frozen = is_frozen;
	}
	
}
