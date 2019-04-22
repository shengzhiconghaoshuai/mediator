package net.chinacloud.mediator.xhs.response;

import java.io.Serializable;

public abstract class XhsResponse implements Serializable{
	
	private static final long serialVersionUID = 8437727684826991635L;
	
	private String error_code;
	private Boolean success;
	private String error_msg;
	
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	
	
}
