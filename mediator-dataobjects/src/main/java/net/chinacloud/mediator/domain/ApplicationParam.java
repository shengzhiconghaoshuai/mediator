/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ApplicationParam.java
 * 描述： 应用参数
 */
package net.chinacloud.mediator.domain;

import java.io.Serializable;

/**
 * <应用参数>
 * <调用渠道sdk参数封装>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class ApplicationParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 340792347961496548L;
	//--连接平台,调用api的公用参数
	private String appKey;
	private String appSecret;
	private String appUrl;
	private String sessionKey;
	private String vendorId;
	private String vendorName;
	private String field1;
	private String field2;
	private Integer field3;//是否压测 1是压测，0是不压测
	
	ApplicationParam(){
		
	}
	
	public ApplicationParam(String appKey, String appSecret, String appUrl, 
			String sessionKey, String vendorId, String vendorName){
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.appUrl = appUrl;
		this.sessionKey = sessionKey;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
	}
	
	public ApplicationParam(String appKey, String appSecret, String appUrl, 
			String sessionKey, String vendorId, String vendorName,
			String field1, String field2, Integer field3){
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.appUrl = appUrl;
		this.sessionKey = sessionKey;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
	}
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public Integer getField3() {
		return field3;
	}
	public void setField3(Integer field3) {
		this.field3 = field3;
	}
}
