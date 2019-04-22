package net.chinacloud.mediator.kaola.response;

import java.util.Date;


public class UpdateInventoryResponse extends KaoLaResponse{
	
	private static final long serialVersionUID = 2356053296697037L;
	
	private int result;
	private Date modify_time;
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	@Override
	public String toString() {
		return "UpdateInventoryResponse [result=" + result + ", modify_time="
				+ modify_time + "]";
	}
	
}
