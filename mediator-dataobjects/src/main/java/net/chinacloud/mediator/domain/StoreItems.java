package net.chinacloud.mediator.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreItems {
	private Date operationTime;//操作时间
	private String warehouseType;//库存来源的类型
	private String warehouseId;//门店ID(商户中心)或 电商仓ID
	
	private List<StoreInventory> storeInventorys;

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

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

	public List<StoreInventory> getStoreInventory() {
		return storeInventorys;
	}

	public void setStoreInventory(List<StoreInventory> storeInventory) {
		this.storeInventorys = storeInventory;
	}
	
	public void addStoreInventory(StoreInventory storeInventory) {
		if(storeInventorys == null){
			storeInventorys = new ArrayList<StoreInventory>();
		}
		storeInventorys.add(storeInventory);
	}
	

}
