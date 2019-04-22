/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Errors.java
 * 描述： 
 */
package net.chinacloud.mediator.pojo;

import java.io.Serializable;

/**
 * @description 错误信息
 * @author yejunwu123@gmail.com
 * @since 2015年7月10日 下午1:45:18
 */
public class Errors implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9048207706553326655L;
	/**错误码*/
	private String code;
	/**错误提示信息*/
	private String errorMsg;
	
	public Errors() {
		
	}
	
	public Errors(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public Errors(String code, String errorMsg) {
		this.code = code;
		this.errorMsg = errorMsg;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
