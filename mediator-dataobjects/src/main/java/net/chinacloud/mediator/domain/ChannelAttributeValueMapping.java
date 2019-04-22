/**
 * 文件名：ChannelAttributeValueMapping.java
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.domain;

/**
 * @description 渠道属性值映射,渠道属性值与系统属性值应该是一对多关系,配置映射时需要注意
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午5:18:37
 */
public class ChannelAttributeValueMapping {
	/**主键id*/
	private Integer id;
	/**渠道主目录叶子类目id*/
	private Long channelCategoryId;
	/**渠道属性id*/
	private String channelAttributeId;
	/**渠道属性值id*/
	private String channelAttributeValueId;
	/**渠道属性值*/
	private String channelAttributeValue;
	/**系统属性代号*/
	private String attributeCode;
	/**系统属性值代号*/
	private String attributeValueCode;
	/**系统属性值*/
	private String attributeValue;
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
	public String getChannelAttributeValueId() {
		return channelAttributeValueId;
	}
	public void setChannelAttributeValueId(String channelAttributeValueId) {
		this.channelAttributeValueId = channelAttributeValueId;
	}
	public String getChannelAttributeValue() {
		return channelAttributeValue;
	}
	public void setChannelAttributeValue(String channelAttributeValue) {
		this.channelAttributeValue = channelAttributeValue;
	}
	public String getAttributeCode() {
		return attributeCode;
	}
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	public String getAttributeValueCode() {
		return attributeValueCode;
	}
	public void setAttributeValueCode(String attributeValueCode) {
		this.attributeValueCode = attributeValueCode;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
}
