/**
 * 版权：Copyright 2016- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RepeatOrder.java
 * 描述： 重复订单
 */
package net.chinacloud.mediator.domain;

import java.util.Date;

public class RepeatOrder {
	/**订单号*/
	private String tid;
	/**插入时间*/
	private Date starttime;
	/**渠道*/
	private Integer applicationId;
	/**类型*/
	private Integer type;

	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
