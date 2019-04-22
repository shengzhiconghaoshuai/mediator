/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductInventory.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
	/**第三方平台的商品编号*/
	private String channelProductId;
	/**商品外部商家编码*/
	private String outerProductId;
	/**
	 * 是否是全量,true:全量更新,false:增量更新,默认false
	 * 供批量更新库存使用
	 * SkuInventory结构中也有该字段,由业务决定使用哪个结构中的标记
	 */
	private boolean isFull = false;
	/**商品销售价*/
	private Double price;
	/**sku列表*/
	private List<Sku> skuList = new ArrayList<Sku>();
	
	private Map<String, String> params = new HashMap<String, String>();
	
	public String getOuterProductId() {
		return outerProductId;
	}
	public void setOuterProductId(String outerProductId) {
		this.outerProductId = outerProductId;
	}
	public boolean isFull() {
		return isFull;
	}
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}
	public List<Sku> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<Sku> skuList) {
		this.skuList = skuList;
	}
	public void addSku(Sku sku){
		this.skuList.add(sku);
	}
	public String getChannelProductId() {
		return channelProductId;
	}
	public void setChannelProductId(String channelProductId) {
		this.channelProductId = channelProductId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public void putParam(String key, String value) {
		this.params.put(key, value);
	}
	public String getParam(String key) {
		return this.params.get(key);
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return "Product [channelProductId=" + channelProductId
				+ ", outerProductId=" + outerProductId + ", isFull=" + isFull
				+ ", price=" + price + ", skuList=" + skuList + "]";
	}
	
}
