package net.chinacloud.mediator.domain;

public class InventoryUpdateRemark {
	
	/**主键id*/
	private Integer id;
	/**商家编码*/
	private String outerId;
	/**应用id*/
	private Integer applicationId;
	/**库存数量*/
	private Integer stockNumber;
	/**更新时间*/
	private String motifyTime;
	/**是否是全量,true:全量更新,false:增量更新,默认false*/
	private boolean isFull = true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOuterId() {
		return outerId;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}


	public String getMotifyTime() {
		return motifyTime;
	}

	public void setMotifyTime(String motifyTime) {
		this.motifyTime = motifyTime;
	}

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public Integer getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(Integer stockNumber) {
		this.stockNumber = stockNumber;
	}

	
	

}
