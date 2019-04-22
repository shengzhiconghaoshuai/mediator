package net.chinacloud.mediator.kaola.exception;

import java.util.List;

import net.chinacloud.mediator.kaola.bean.KaoLaError;

public class KaoLaErrorResponse {
	private String msg;
	private String code;
	private List<KaoLaError> subErrors;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<KaoLaError> getSubErrors() {
		return subErrors;
	}
	public void setSubErrors(List<KaoLaError> subErrors) {
		this.subErrors = subErrors;
	}
	@Override
	public String toString() {
		return "KaoLaErrorResponse [msg=" + msg + ", code=" + code
				+ ", subErrors=" + subErrors + "]";
	}
	

}
