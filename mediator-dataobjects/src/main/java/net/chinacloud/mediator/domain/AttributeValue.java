/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：AttributeValue.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

/**
 * @description 属性值结构
 * @author yejunwu123@gmail.com
 * @since 2015年7月29日 下午2:01:01
 */
public class AttributeValue {
	/**属性值标识*/
	private String id;
	/**属性值名称*/
	private String name;
	
	public AttributeValue() {
		
	}
	
	public AttributeValue(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
