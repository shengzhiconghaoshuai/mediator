/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelAttributeMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.domain;

/**
 * @description 渠道属性映射
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午5:11:41
 */
public class ChannelAttributeMapping {
	/**主键id*/
	private Integer id;
	/**渠道主目录叶子类目id*/
	private Long channelCategoryId;
	/**渠道属性id*/
	private String channelAttributeId;
	/**渠道属性名称*/
	private String channelAttributeName;
	/**系统属性代号*/
	private String attributeCode;
	/**属性默认值*/
	private String defaultValue;
	/**应用id*/
	private Integer applicationId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getChannelCategoryId() {
		return channelCategoryId;
	}
	public void setChannelCategoryId(Long channelCategoryId) {
		this.channelCategoryId = channelCategoryId;
	}
	public String getChannelAttributeId() {
		return channelAttributeId;
	}
	public void setChannelAttributeId(String channelAttributeId) {
		this.channelAttributeId = channelAttributeId;
	}
	public String getChannelAttributeName() {
		return channelAttributeName;
	}
	public void setChannelAttributeName(String channelAttributeName) {
		this.channelAttributeName = channelAttributeName;
	}
	public String getAttributeCode() {
		return attributeCode;
	}
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
}
