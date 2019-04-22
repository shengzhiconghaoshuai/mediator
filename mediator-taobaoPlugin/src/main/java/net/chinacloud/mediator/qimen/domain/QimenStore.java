package net.chinacloud.mediator.qimen.domain;

import java.util.List;



import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("store") 
public class QimenStore {
	@XStreamAlias("warehouseType") 
	private String warehouseType;//库存来源的类型
	@XStreamAlias("warehouseId") 
	private String warehouseId;//门店ID(商户中心)或 电商仓ID
	@XStreamAlias("storeInventories")
	private List<QimenStoreInventory> storeInventories;
	
	public String getWarehouseType() {
		return warehouseType;
	}
	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public List<QimenStoreInventory> getStoreInventories() {
		return storeInventories;
	}
	public void setStoreInventories(List<QimenStoreInventory> storeInventories) {
		this.storeInventories = storeInventories;
	}
	
	
	
	
}
