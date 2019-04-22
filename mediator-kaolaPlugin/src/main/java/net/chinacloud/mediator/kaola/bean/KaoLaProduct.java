package net.chinacloud.mediator.kaola.bean;

import java.util.List;

public class KaoLaProduct {
	/*商品id*/
	private int id;
	/*商品名称*/
	private String name;
	/*商品货号*/
	private String item_no;
	/*商品状态*/
	private int item_status;
	/*商品key*/
	private String item_key;
	
	private String outer_id;
	
	/*SKU基本信息列表*/
	private List<KaoLaSkuBasic> sku_list;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getItem_no() {
		return item_no;
	}
	public void setItem_no(String item_no) {
		this.item_no = item_no;
	}
	public int getItem_status() {
		return item_status;
	}
	public void setItem_status(int item_status) {
		this.item_status = item_status;
	}
	public String getItem_key() {
		return item_key;
	}
	public void setItem_key(String item_key) {
		this.item_key = item_key;
	}
	public List<KaoLaSkuBasic> getSku_list() {
		return sku_list;
	}
	public void setSku_list(List<KaoLaSkuBasic> sku_list) {
		this.sku_list = sku_list;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOuter_id() {
		return outer_id;
	}
	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}
	@Override
	public String toString() {
		return "KaoLaProduct [id=" + id + ", name=" + name + ", item_no="
				+ item_no + ", item_status=" + item_status + ", item_key="
				+ item_key + ", outer_id=" + outer_id + ", sku_list="
				+ sku_list + "]";
	}
	
	
	
}
