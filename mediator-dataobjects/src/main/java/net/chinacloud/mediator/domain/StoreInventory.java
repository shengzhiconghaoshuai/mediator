package net.chinacloud.mediator.domain;

public class StoreInventory {
	private String billNum;//单据流水号，用于幂等操作
	private String outerProductId;//oms系统的款号，用于在映射表中获取淘宝前端商品id
	private String channelProductId;//淘宝前端商品id
	private String outerSkuId;//OMS系统中商品编码(sku)
	private String channelSkuId;//商品的SKU编码 不是必须的
	private String inventoryType;//库存类型
	private int quantity; //对应类型的库存数量（正数）
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getOuterProductId() {
		return outerProductId;
	}
	public void setOuterProductId(String outerProductId) {
		this.outerProductId = outerProductId;
	}
	public String getChannelProductId() {
		return channelProductId;
	}
	public void setChannelProductId(String channelProductId) {
		this.channelProductId = channelProductId;
	}
	
	public String getOuterSkuId() {
		return outerSkuId;
	}
	public void setOuterSkuId(String outerSkuId) {
		this.outerSkuId = outerSkuId;
	}
	public String getChannelSkuId() {
		return channelSkuId;
	}
	public void setChannelSkuId(String channelSkuId) {
		this.channelSkuId = channelSkuId;
	}
	public String getInventoryType() {
		return inventoryType;
	}
	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
