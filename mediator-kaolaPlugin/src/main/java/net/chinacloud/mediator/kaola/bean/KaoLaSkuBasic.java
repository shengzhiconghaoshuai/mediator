package net.chinacloud.mediator.kaola.bean;

import java.math.BigDecimal;

public class KaoLaSkuBasic {
	/*SKU Key*/
	private String sku_key;
	/*Sku编号*/
	private String id;
	/*上架外部唯一编码(目前不支持此字段编辑，请期待)*/
	private String outer_id;
	/*条形码*/
	private String barcode;
	/*销售价*/
	private BigDecimal sale_price;
	/*仓库名称*/
	private String warehouse_name;
	/*仓库id*/
	private long warehouse_id;
	/*可售库存*/
	private int stock_cansale;
	/*冻结库存*/
	private int stock_freeze;
	/*全部库存*/
	private int stock_total;
	
	public String getSku_key() {
		return sku_key;
	}
	public void setSku_key(String sku_key) {
		this.sku_key = sku_key;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOuter_id() {
		return outer_id;
	}
	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public BigDecimal getSale_price() {
		return sale_price;
	}
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}
	public String getWarehouse_name() {
		return warehouse_name;
	}
	public void setWarehouse_name(String warehouse_name) {
		this.warehouse_name = warehouse_name;
	}
	public long getWarehouse_id() {
		return warehouse_id;
	}
	public void setWarehouse_id(long warehouse_id) {
		this.warehouse_id = warehouse_id;
	}
	public int getStock_cansale() {
		return stock_cansale;
	}
	public void setStock_cansale(int stock_cansale) {
		this.stock_cansale = stock_cansale;
	}
	public int getStock_freeze() {
		return stock_freeze;
	}
	public void setStock_freeze(int stock_freeze) {
		this.stock_freeze = stock_freeze;
	}
	public int getStock_total() {
		return stock_total;
	}
	public void setStock_total(int stock_total) {
		this.stock_total = stock_total;
	}
	

	
	
}
