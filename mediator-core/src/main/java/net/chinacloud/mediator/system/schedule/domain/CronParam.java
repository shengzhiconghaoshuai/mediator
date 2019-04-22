/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CronParam.java
 * 描述： 调度参数
 */
package net.chinacloud.mediator.system.schedule.domain;
/**
 * <调度参数>
 * <调度参数>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月5日
 * @since 2015年1月5日
 */
public class CronParam {
	/**主键*/
	private Integer paramId;
	/**调度id*/
	private Integer cronId;
	/**参数名称*/
	private String paramName;
	/**参数值*/
	private String paramValue;
	
	public CronParam(Integer paramId, Integer cronId, String paramName, String paramValue) {
		this.paramId = paramId;
		this.cronId = cronId;
		this.paramName = paramName;
		this.paramValue = paramValue;
	}
	
	public CronParam(Integer cronId, String paramName, String paramValue) {
		this(null, cronId, paramName, paramValue);
	}
	
	public CronParam(String paramName, String paramValue) {
		this(null, paramName, paramValue);
	}
	
	public Integer getParamId() {
		return paramId;
	}
	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}
	public Integer getCronId() {
		return cronId;
	}
	public void setCronId(Integer cronId) {
		this.cronId = cronId;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
}
