/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：User.java
 * 描述： 用户
 */
package net.chinacloud.mediator.domain;

import java.util.Date;
import java.util.List;

/**
 * <买家信息>
 * <买家信息>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class User {
	/**渠道用户id*/
	private String channelUserId;
	/**性别*/
	private String sex;
	/**用户注册时间*/
	private Date created;
	/**最后一次登录时间*/
	private Date lastVisit;
	/**用户类型*/
	private String type;
	/**买家信用*/
	private UserCredit buyerCredit;
	/**卖家信用*/
	private UserCredit sellerCredit;
	/**用户主地址*/
	private Address location;
	/**买家的收货地址,可以有多个*/
	private List<Address> recipients;
	
	public String getChannelUserId() {
		return channelUserId;
	}
	public void setChannelUserId(String channelUserId) {
		this.channelUserId = channelUserId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getLastVisit() {
		return lastVisit;
	}
	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public UserCredit getBuyerCredit() {
		return buyerCredit;
	}
	public void setBuyerCredit(UserCredit buyerCredit) {
		this.buyerCredit = buyerCredit;
	}
	public UserCredit getSellerCredit() {
		return sellerCredit;
	}
	public void setSellerCredit(UserCredit sellerCredit) {
		this.sellerCredit = sellerCredit;
	}
	public Address getLocation() {
		return location;
	}
	public void setLocation(Address location) {
		this.location = location;
	}
	public List<Address> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<Address> recipients) {
		this.recipients = recipients;
	}
	@Override
	public String toString() {
		return "User [channelUserId=" + channelUserId + ", sex=" + sex
				+ ", created=" + created + ", lastVisit=" + lastVisit
				+ ", type=" + type + ", buyerCredit=" + buyerCredit
				+ ", sellerCredit=" + sellerCredit + ", location=" + location
				+ ", recipients=" + recipients + "]";
	}
}
