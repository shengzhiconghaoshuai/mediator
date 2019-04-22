package net.chinacloud.mediator.vip.vop.domain;


import java.io.Serializable;

public class CreateDeliverSuccessData implements Serializable {
	
	private static final long serialVersionUID = -622928532490801737L;
	
	// 送货单ID,delivery_id
	private String id;

	// 入库单编号
	private String storage_no;

	private String msg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStorage_no() {
		return storage_no;
	}

	public void setStorage_no(String storage_no) {
		this.storage_no = storage_no;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
