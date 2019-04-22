/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ObjectWrapper.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

public class ObjectWrapper {
	private String type;
	private Object content;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "ObjectWrapper [type=" + type + ", content=" + content + "]";
	}
}
