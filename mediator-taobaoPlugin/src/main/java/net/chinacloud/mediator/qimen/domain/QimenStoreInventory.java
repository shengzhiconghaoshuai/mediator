package net.chinacloud.mediator.qimen.domain;


import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("storeInventory") 
public class QimenStoreInventory {
	@XStreamAlias("billNum")
	private String billNum;//单据流水号，用于幂等操作
	@XStreamAlias("itemId")
	private Long itemId;//淘宝前端商品id
	@XStreamAlias("outerId")
	private String outerId;//OMS系统中商品编码(sku)
	@XStreamAlias("skuId")
	private Long skuId;//商品的SKU编码 不是必须的
	@XStreamAlias("inventoryType")
	private String inventoryType;//库存类型
	@XStreamAlias("quantity")
	private int quantity; //对应类型的库存数量（正数）
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public String getOuterId() {
		return outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	public String getInventoryType() {
		return inventoryType;
	}
	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	
	

}
