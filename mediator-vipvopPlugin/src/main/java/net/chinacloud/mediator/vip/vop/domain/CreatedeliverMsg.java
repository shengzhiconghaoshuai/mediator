package net.chinacloud.mediator.vip.vop.domain;


public class CreatedeliverMsg {


	// PO单编号
	private String po_no;

	// 送货单编号
	private String delivery_no;

	// 送货仓库
	private String delivery_warehouse;

	// 承运商名称
	private String carrier;
	
	//承运商编码
	private String carrierCode;

	// 司机联系电话
	private String driver_tel;

	// 拣货单
	private String pick_no;

	// 预计到达时间
	private String arrival_time;
	
	//配送方式
	private String delivery_method;
	
	//是否是手动创建的
	private String isAuto;

	public String getDelivery_method() {
		return delivery_method;
	}

	public void setDelivery_method(String delivery_method) {
		this.delivery_method = delivery_method;
	}
	
	public String getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(String isAuto) {
		this.isAuto = isAuto;
	}

	public String getArrival_time() {
		return arrival_time;
	}

	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}

	public String getPick_no() {
		return pick_no;
	}

	public void setPick_no(String pick_no) {
		this.pick_no = pick_no;
	}

	public String getPo_no() {
		return po_no;
	}

	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}

	public String getDelivery_no() {
		return delivery_no;
	}

	public void setDelivery_no(String delivery_no) {
		this.delivery_no = delivery_no;
	}

	public String getDelivery_warehouse() {
		return delivery_warehouse;
	}

	public void setDelivery_warehouse(String delivery_warehouse) {
		this.delivery_warehouse = delivery_warehouse;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getDriver_tel() {
		return driver_tel;
	}

	public void setDriver_tel(String driver_tel) {
		this.driver_tel = driver_tel;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

}
