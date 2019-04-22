/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DeliveryTemplate.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

import java.util.Date;

/**
 * @description 运费模板
 * @author yejunwu123@gmail.com
 * @since 2015年7月17日 下午7:30:29
 */
public class DeliveryTemplate {
	/**模板id*/
	private long id;
	/**模板名称*/
	private String name;
	/**运费模板上设置的发货地址*/
	private String address;
	/**模板创建时间*/
	private Date createTime;
	/**模板修改时间*/
	private Date updateTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
