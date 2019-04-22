/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskContext.java
 * 描述： task上下文
 */
package net.chinacloud.mediator.task;

import java.util.HashMap;
import java.util.Map;
/**
 * <task上下文>
 * <task上下文>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
public class TaskContext {
	/**渠道id*/
	private Integer channelId;
	/**渠道代号*/
	private String channelCode;
	/**应用id*/
	private Integer applicationId;
	/**应用代号*/
	private String applicationCode;
	/**店铺id*/
	private Integer storeId;
	/**上下文参数*/
	private Map<String,Object> params = new HashMap<String, Object>();
	
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public String getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	/**
	 * 放入参数
	 * @param key 参数名
	 * @param value 参数值
	 */
	public void put(String key, Object value) {
		params.put(key, value);
	}
	/**
	 * 获取参数
	 * @param key 参数名
	 * @return
	 */
	public Object get(String key) {
		return params.get(key);
	}
	@Override
	public String toString() {
		return "TaskContext [channelId=" + channelId + ", channelCode="
				+ channelCode + ", applicationId=" + applicationId
				+ ", applicationCode=" + applicationCode + ", storeId="
				+ storeId + ", params=" + params + "]";
	}
	
 }
