/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Attribute.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 属性结构
 * @author yejunwu123@gmail.com
 * @since 2015年7月29日 下午1:50:29
 */
public class Attribute {
	/**输入类型*/
	public static final String ATTRIBUTE_TYPE_INPUT = "input";
	/**多值输入类型*/
	public static final String ATTRIBUTE_TYPE_MULTI_INPUT = "multiinput";
	/**选择类型*/
	public static final String ATTRIBUTE_TYPE_SELECT = "select";
	/**多值选择类型*/
	public static final String ATTRIBUTE_TYPE_MULTI_SELECT = "multiselect";
	
	/**渠道属性id*/
	private String attributeId;
	/**渠道属性名称*/
	private String attributeName;
	/**映射到系统的属性的标识*/
	private String mappedAttributeCode;
	/**是否必填*/
	private boolean required = false;
	/**关键属性*/
	private boolean keyProperty = false;
	/**销售属性*/
	private boolean salesProperty = false;
	/**颜色属性*/
	private boolean colorProperty = false;
	/**尺码属性*/
	private boolean sizeProperty = false;
	/**属性类型,input:用户自行输入,select:从渠道给定的值中选择*/
	private String type;
	/**父级属性id*/
	private String parentAttributeId;
	/**父级属性类型*/
	private String parentType;
	/**属性值列表*/
	private List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
	
	private String defaultValue;
	
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isKeyProperty() {
		return keyProperty;
	}
	public void setKeyProperty(boolean keyProperty) {
		this.keyProperty = keyProperty;
	}
	public boolean isSalesProperty() {
		return salesProperty;
	}
	public void setSalesProperty(boolean salesProperty) {
		this.salesProperty = salesProperty;
	}
	public boolean isColorProperty() {
		return colorProperty;
	}
	public void setColorProperty(boolean colorProperty) {
		this.colorProperty = colorProperty;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<AttributeValue> getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(List<AttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}
	public void addAttributeValue(AttributeValue attributeValue) {
		this.attributeValues.add(attributeValue);
	}
	public String getMappedAttributeCode() {
		return mappedAttributeCode;
	}
	public void setMappedAttributeCode(String mappedAttributeCode) {
		this.mappedAttributeCode = mappedAttributeCode;
	}
	public boolean isSizeProperty() {
		return sizeProperty;
	}
	public void setSizeProperty(boolean sizeProperty) {
		this.sizeProperty = sizeProperty;
	}
	public String getParentAttributeId() {
		return parentAttributeId;
	}
	public void setParentAttributeId(String parentAttributeId) {
		this.parentAttributeId = parentAttributeId;
	}
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	
}
