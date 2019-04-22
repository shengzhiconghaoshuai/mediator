package net.chinacloud.mediator.vip.vop.domain;



import java.io.Serializable;

public class CreatePick implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2206363291849822698L;
	public String getPick_no() {
		return pick_no;
	}
	public void setPick_no(String pick_no) {
		this.pick_no = pick_no;
	}
	public String getPick_type() {
		return pick_type;
	}
	public void setPick_type(String pick_type) {
		this.pick_type = pick_type;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
	private String pick_no	;
	private String pick_type;
	private String warehouse	;
	private String po_no;
	public String getPo_no() {
		return po_no;
	}
	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}
	
	

}
