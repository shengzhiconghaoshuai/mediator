package net.chinacloud.mediator.vip.vop.domain;


import java.io.Serializable;
import java.util.Date;

public class JITDeliveryBean implements Serializable {
	
	private static final long serialVersionUID = -5516646927903294623L;
	
	public String po_no;

	// 送货仓库
	public String delivery_warehouse;

	// 送货单编号
	public String delivery_no;

	// 拣货单
	public String pick_no;
	
	// 送货单delivery_id
	private String delivery_id;

	// 入库单编号
	private String storage_no;
	
	//运输方式
	private String carrier_name;
	
	//出仓单状态
	private String status;
	
	//是否是自动创建的(调度或者手工)
	private String isAuto;
	
	//创建时间
	private Date create_time;
	
	//配送方式
	private String delivery_method;
	
	public String getDelivery_method() {
		return delivery_method;
	}

	public void setDelivery_method(String delivery_method) {
		this.delivery_method = delivery_method;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(String isAuto) {
		this.isAuto = isAuto;
	}

	public String getCarrier_name() {
		return carrier_name;
	}

	public void setCarrier_name(String carrier_name) {
		this.carrier_name = carrier_name;
	}

	public String getPo_no() {
		return po_no;
	}

	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}

	public String getDelivery_warehouse() {
		return delivery_warehouse;
	}

	public void setDelivery_warehouse(String delivery_warehouse) {
		this.delivery_warehouse = delivery_warehouse;
	}

	public String getDelivery_no() {
		return delivery_no;
	}

	public void setDelivery_no(String delivery_no) {
		this.delivery_no = delivery_no;
	}

	public String getPick_no() {
		return pick_no;
	}

	public void setPick_no(String pick_no) {
		this.pick_no = pick_no;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDelivery_id() {
		return delivery_id;
	}

	public void setDelivery_id(String delivery_id) {
		this.delivery_id = delivery_id;
	}

	public String getStorage_no() {
		return storage_no;
	}

	public void setStorage_no(String storage_no) {
		this.storage_no = storage_no;
	}
	
	
}
