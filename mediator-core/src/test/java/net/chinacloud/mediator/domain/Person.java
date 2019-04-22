/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Order.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

public class Person {
	private String channelOrderId;
	private Double price;
	
	public String getChannelOrderId() {
		return channelOrderId;
	}
	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "Person [channelOrderId=" + channelOrderId + ", price=" + price
				+ "]";
	}
}
