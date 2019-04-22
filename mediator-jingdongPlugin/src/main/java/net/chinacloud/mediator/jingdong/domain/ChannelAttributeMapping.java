/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelAttributeMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.jingdong.domain;

/**
 * @description 京东属性映射
 * @author yejunwu123@gmail.com
 * @since 2015年6月29日 上午10:52:21
 */
public class ChannelAttributeMapping {
	/**主键*/
	private Integer id;
	/**渠道主目录叶子类目id*/
	private Long channelCategoryId;
	/**渠道属性id*/
	private String channelAttributeId;
	/**渠道属性名称*/
	private String channelAttributeName;
	/**对应的系统属性代号*/
	private String attributeCode;
	/**默认值*/
	private String defaultValue;
	/**属性类型,1、关键属性2、不变属性3、可变属性4、销售属性*/
	private int attributeType;
	/**输入类型,1、单选， 2、多选3、输入 */
	private int inputType;
	/**是否是关键属性,1:是,0:否*/
	private int keyProperty;
	/**是否是销售属性,1:是,0:否*/
	private int saleProperty;
	/**是否是颜色属性,1:是,0:否*/
	private Integer colorProperty;
	/**是否是尺码属性,1:是,0:否*/
	private Integer sizeProperty;
	/**是否必填,1:是,0:否 */
	private int required;
	/**应用id*/
	private Integer applicationId;
	/**状态,1:可用,0:不可用*/
	private Integer status;
	
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
	public int getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(int attributeType) {
		this.attributeType = attributeType;
	}
	public int getInputType() {
		return inputType;
	}
	public void setInputType(int inputType) {
		this.inputType = inputType;
	}
	public int getKeyProperty() {
		return keyProperty;
	}
	public void setKeyProperty(int keyProperty) {
		this.keyProperty = keyProperty;
	}
	public int getSaleProperty() {
		return saleProperty;
	}
	public void setSaleProperty(int saleProperty) {
		this.saleProperty = saleProperty;
	}
	public Integer getColorProperty() {
		return colorProperty;
	}
	public void setColorProperty(Integer colorProperty) {
		this.colorProperty = colorProperty;
	}
	public Integer getSizeProperty() {
		return sizeProperty;
	}
	public void setSizeProperty(Integer sizeProperty) {
		this.sizeProperty = sizeProperty;
	}
	public int getRequired() {
		return required;
	}
	public void setRequired(int required) {
		this.required = required;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
