package net.chinacloud.mediator.kaola.bean;

public class KaolaRefundSku {
	private String barcode;//商品条形码
	private int is_gift;//0 不是赠品；1 是赠品
	private String item_key;//商品编号
	private String item_name;//商品名称
	private String item_outer_id;//品外部编码
	private int refund_count;//退货数量
	private String sku_key;//sku key
	private String sku_outer_id;//sku 外键
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public int getIs_gift() {
		return is_gift;
	}
	public void setIs_gift(int is_gift) {
		this.is_gift = is_gift;
	}
	public String getItem_key() {
		return item_key;
	}
	public void setItem_key(String item_key) {
		this.item_key = item_key;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getItem_outer_id() {
		return item_outer_id;
	}
	public void setItem_outer_id(String item_outer_id) {
		this.item_outer_id = item_outer_id;
	}
	public int getRefund_count() {
		return refund_count;
	}
	public void setRefund_count(int refund_count) {
		this.refund_count = refund_count;
	}
	public String getSku_key() {
		return sku_key;
	}
	public void setSku_key(String sku_key) {
		this.sku_key = sku_key;
	}
	public String getSku_outer_id() {
		return sku_outer_id;
	}
	public void setSku_outer_id(String sku_outer_id) {
		this.sku_outer_id = sku_outer_id;
	}
	@Override
	public String toString() {
		return "KaolaRefundSku [barcode=" + barcode + ", is_gift=" + is_gift
				+ ", item_key=" + item_key + ", item_name=" + item_name
				+ ", item_outer_id=" + item_outer_id + ", refund_count="
				+ refund_count + ", sku_key=" + sku_key + ", sku_outer_id="
				+ sku_outer_id + "]";
	}
	

}
