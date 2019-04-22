package net.chinacloud.mediator.kaola.bean;

public class KaoLaOrderSku {
	
	private String sku_key; //skuId
	private String product_name;//商品名称
	private String origin_price;//订单原始单价
	private int count;//购买数量
	private String real_totle_price;//实付总价
	private String activity_totle_amount;//活动优惠总金额
	private String goods_no;//货号
	private String coupon_totle_amount;//优惠劵优惠总金额
	
	private String barcode;//商品条形码
	private String warehouse_code;//仓库编码
	private String warehouse_name;//仓库名称
	
	private String order_serial_num;//序列号:报关时使用到排序
	private String sku_outer_id;//ku 的outerid
	public String getSku_key() {
		return sku_key;
	}
	public void setSku_key(String sku_key) {
		this.sku_key = sku_key;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getOrigin_price() {
		return origin_price;
	}
	public void setOrigin_price(String origin_price) {
		this.origin_price = origin_price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getReal_totle_price() {
		return real_totle_price;
	}
	public void setReal_totle_price(String real_totle_price) {
		this.real_totle_price = real_totle_price;
	}
	public String getActivity_totle_amount() {
		return activity_totle_amount;
	}
	public void setActivity_totle_amount(String activity_totle_amount) {
		this.activity_totle_amount = activity_totle_amount;
	}
	public String getGoods_no() {
		return goods_no;
	}
	public void setGoods_no(String goods_no) {
		this.goods_no = goods_no;
	}
	
	public String getCoupon_totle_amount() {
		return coupon_totle_amount;
	}
	public void setCoupon_totle_amount(String coupon_totle_amount) {
		this.coupon_totle_amount = coupon_totle_amount;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getWarehouse_code() {
		return warehouse_code;
	}
	public void setWarehouse_code(String warehouse_code) {
		this.warehouse_code = warehouse_code;
	}
	public String getWarehouse_name() {
		return warehouse_name;
	}
	public void setWarehouse_name(String warehouse_name) {
		this.warehouse_name = warehouse_name;
	}
	public String getOrder_serial_num() {
		return order_serial_num;
	}
	public void setOrder_serial_num(String order_serial_num) {
		this.order_serial_num = order_serial_num;
	}
	public String getSku_outer_id() {
		return sku_outer_id;
	}
	public void setSku_outer_id(String sku_outer_id) {
		this.sku_outer_id = sku_outer_id;
	}
	@Override
	public String toString() {
		return "KaoLaOrderSku [sku_key=" + sku_key + ", product_name="
				+ product_name + ", origin_price=" + origin_price + ", count="
				+ count + ", real_totle_price=" + real_totle_price
				+ ", activity_totle_amount=" + activity_totle_amount
				+ ", goods_no=" + goods_no + ", coupon_totle_amount="
				+ coupon_totle_amount + ", barcode=" + barcode
				+ ", warehouse_code=" + warehouse_code + ", warehouse_name="
				+ warehouse_name + ", order_serial_num=" + order_serial_num
				+ ", sku_outer_id=" + sku_outer_id + "]";
	}
	
	
}
