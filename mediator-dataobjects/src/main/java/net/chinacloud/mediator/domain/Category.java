/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Category.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 类目结构
 * @author yejunwu123@gmail.com
 * @since 2015年7月29日 下午1:49:10
 */
public class Category {
	/**渠道类目id*/
	private String channelCategoryId;
	/**系统分类id*/
	private String categoryId;
	/**品牌id,天猫抓取产品创建规则使用*/
	private String brandId;
	
	private String field1;
	/**属性列表*/
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	public String getChannelCategoryId() {
		return channelCategoryId;
	}
	public void setChannelCategoryId(String channelCategoryId) {
		this.channelCategoryId = channelCategoryId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
}
