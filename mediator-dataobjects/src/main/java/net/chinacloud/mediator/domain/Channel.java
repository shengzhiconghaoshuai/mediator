/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Channel.java
 * 描述： 渠道
 */
package net.chinacloud.mediator.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * <渠道信息>
 * <对渠道的抽象,如渠道名称、代号等>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class Channel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6909796614274836816L;
	/**渠道id,一般来说无业务意义*/
	private Integer id;
	/**渠道代号,缩略格式,如Taobao、Jingdong、YHD等*/
	private String code;
	/**渠道名称,用于页面友好显示,如淘宝、京东、一号店等*/
	private String name;
	/**排序号,用于页面按顺序显示*/
	private int priority = 0;
	/**渠道状态,0表示关闭,2表示开启*/
	private int status = 1;
	/**创建时间*/
	private Date createTime = new Date();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return "Channel [id=" + id + ", code=" + code + ", name=" + name
				+ ", priority=" + priority + ", status=" + status
				+ ", createTime=" + createTime + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Channel other = (Channel) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
