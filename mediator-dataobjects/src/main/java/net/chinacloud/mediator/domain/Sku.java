/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SkuInventory.java
 * 描述： sku数据结构
 */
package net.chinacloud.mediator.domain;
/**
 * <sku数据结构>
 * <sku数据结构,库存同步时使用>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class Sku {
	/**第三方平台的sku id*/
	private String channelSkuId;
	/**sku的外部商家编码,即partnumber*/
	private String outerSkuId;
	/**商品外部商家编码*/
	private String outerProductId;
	/**库存总量*/
	private Double qtyAll;
	/**可卖库存*/
	private Double qtyCanSell;
	/**变化库存*/
	private Double qtyChange;
	/**是否是全量,true:全量更新,false:增量更新,默认false*/
	private boolean isFull = false;
	/**sku销售价*/
	private Double price;
	
	public String getOuterSkuId() {
		return outerSkuId;
	}
	public void setOuterSkuId(String outerSkuId) {
		this.outerSkuId = outerSkuId;
	}
	public Double getQtyAll() {
		return qtyAll;
	}
	public void setQtyAll(Double qtyAll) {
		this.qtyAll = qtyAll;
	}
	public Double getQtyCanSell() {
		return qtyCanSell;
	}
	public void setQtyCanSell(Double qtyCanSell) {
		this.qtyCanSell = qtyCanSell;
	}
	public Double getQtyChange() {
		return qtyChange;
	}
	public void setQtyChange(Double qtyChange) {
		this.qtyChange = qtyChange;
	}
	public boolean isFull() {
		return isFull;
	}
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}
	public String getChannelSkuId() {
		return channelSkuId;
	}
	public void setChannelSkuId(String channelSkuId) {
		this.channelSkuId = channelSkuId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getOuterProductId() {
		return outerProductId;
	}
	public void setOuterProductId(String outerProductId) {
		this.outerProductId = outerProductId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((outerSkuId == null) ? 0 : outerSkuId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sku other = (Sku) obj;
		if (outerSkuId == null) {
			if (other.outerSkuId != null)
				return false;
		} else if (!outerSkuId.equals(other.outerSkuId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Sku [channelSkuId=" + channelSkuId + ", outerSkuId="
				+ outerSkuId + ", outerProductId=" + outerProductId
				+ ", qtyAll=" + qtyAll + ", qtyCanSell=" + qtyCanSell
				+ ", qtyChange=" + qtyChange + ", isFull=" + isFull
				+ ", price=" + price + "]";
	}
	
}
