package net.chinacloud.mediator.qimen.response;

import java.io.Serializable;

import com.thoughtworks.xstream.XStream;

public abstract class QimenResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//success|failure
	private String flag;
	
	//响应码
	private String code;
	
	//响应信息
	private String message;
	
	 public abstract XStream getXStream();

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
