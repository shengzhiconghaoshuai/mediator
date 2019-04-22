/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Address.java
 * 描述： 地址
 */
package net.chinacloud.mediator.domain;
/**
 * <收货地址>
 * <收货地址>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class Address {
	/**国家*/
	private String country = "";
	/**省份*/
	private String state;
	/**市*/
	private String city;
	/**区*/
	private String district = "";
	/**详细地址*/
	private String address;
	/**邮政编码*/
	private String zipCode = "";
	/**手机(移动电话)*/
	private String mobile;
	/**固定电话*/
	private String phone = "";
	/**电子邮件*/
	private String email = "";
	/**联系人姓名*/
	private String contactName;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		if (null != phone) {
			this.phone = phone;
		}
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	@Override
	public String toString() {
		return "Address [country=" + country + ", state=" + state + ", city="
				+ city + ", district=" + district + ", address=" + address
				+ ", zipCode=" + zipCode + ", mobile=" + mobile + ", phone="
				+ phone + ", email=" + email + ", contactName=" + contactName
				+ "]";
	}
}
