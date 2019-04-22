package net.chinacloud.mediator.vip.vop.domain;



import java.io.Serializable;

public class PoSku implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2858474637536764312L;
	public String getSo_no() {
		return so_no;
	}
	public void setSo_no(String so_no) {
		this.so_no = so_no;
	}
	public String getSell_site() {
		return sell_site;
	}
	public void setSell_site(String sell_site) {
		this.sell_site = sell_site;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public int getSku_num() {
		return sku_num;
	}
	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}
	public String getOrder_cate() {
		return order_cate;
	}
	public void setOrder_cate(String order_cate) {
		this.order_cate = order_cate;
	}
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getAudity_time() {
		return audity_time;
	}
	public void setAudity_time(String audity_time) {
		this.audity_time = audity_time;
	}
	private String so_no;
	private String sell_site;
	private String warehouse;
	private String sku;
	private int sku_num	;
	private String order_cate;
	private int order_status;
	private String create_time;
	private String audity_time;


}
