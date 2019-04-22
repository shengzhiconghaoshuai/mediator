/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoChannelAttributeMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.domain;

/**
 * @description 淘宝属性映射
 * @author yejunwu123@gmail.com
 * @since 2015年6月29日 上午10:52:21
 */
public class TaobaoChannelAttributeMapping {
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
	/**是否是关键属性,1:是,0:否*/
	private int keyProperty;
	/**是否是销售属性,1:是,0:否*/
	private int saleProperty;
	/**是否是颜色属性,1:是,0:否*/
	private Integer colorProperty;
	/**是否是枚举属性,1:是,0:否*/
	private Integer enumprop;
	/**是否是商品属性,1:是,0:否*/
	private Integer itemprop;
	/**是否是必填属性,1:是,0:否*/
	private Integer must;
	/**是否是多选属性,1:是,0:否*/
	private Integer multi;
	
	private Integer inputprop;
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
	public Integer getEnumprop() {
		return enumprop;
	}
	public void setEnumprop(Integer enumprop) {
		this.enumprop = enumprop;
	}
	public Integer getItemprop() {
		return itemprop;
	}
	public void setItemprop(Integer itemprop) {
		this.itemprop = itemprop;
	}
	public Integer getMust() {
		return must;
	}
	public void setMust(Integer must) {
		this.must = must;
	}
	public Integer getMulti() {
		return multi;
	}
	public void setMulti(Integer multi) {
		this.multi = multi;
	}
	public Integer getInputprop() {
		return inputprop;
	}
	public void setInputprop(Integer inputprop) {
		this.inputprop = inputprop;
	}
	
}
