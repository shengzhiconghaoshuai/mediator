/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：B2CResponse.java
 * 描述： 
 */
package net.chinacloud.mediator.mediator.b2c.response;

import java.io.Serializable;
import java.util.Map;

/**
 * @description B2C响应
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 下午6:07:12
 */
public abstract class B2CResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1855931287801799571L;

	private String errorCode;

	private String msg;

	private String subCode;

	private String subMsg;
	
	private String body;
	private Map<String, String> params;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSubCode() {
		return this.subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getSubMsg() {
		return this.subMsg;
	}

	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public boolean isSuccess() {
		return this.errorCode == null && this.subCode == null;
	}

}
