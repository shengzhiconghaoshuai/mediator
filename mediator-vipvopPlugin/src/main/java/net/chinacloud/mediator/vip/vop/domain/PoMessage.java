package net.chinacloud.mediator.vip.vop.domain;

import java.io.Serializable;

public class PoMessage implements Serializable{
	
	private static final long serialVersionUID = -1441783357963141053L;
	
	private String po_no;
	
	private String flag;
	
	private boolean immediately;
	
	private String sell_st_time;
	
	private String sell_et_time;

	public boolean isImmediately() {
		return immediately;
	}

	public void setImmediately(boolean immediately) {
		this.immediately = immediately;
	}

	public String getPo_no() {
		return po_no;
	}

	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}

	public String getSell_st_time() {
		return sell_st_time;
	}

	public void setSell_st_time(String sell_st_time) {
		this.sell_st_time = sell_st_time;
	}

	public String getSell_et_time() {
		return sell_et_time;
	}

	public void setSell_et_time(String sell_et_time) {
		this.sell_et_time = sell_et_time;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
