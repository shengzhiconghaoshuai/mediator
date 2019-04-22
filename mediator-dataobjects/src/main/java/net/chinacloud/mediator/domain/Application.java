/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Application.java
 * 描述： 应用
 */
package net.chinacloud.mediator.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * <应用>
 * <应用,类似店铺store的概念,与channel是多对一的关系>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class Application implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2585938710560281619L;
	/**店铺在Mediator子系统的id,初步使用自增长*/
	private Integer id;
	/**店铺名称,前端显示用*/
	private String name;
	/**代号*/
	private String code;
	/**对应的后端系统的storeId*/
	private Integer storeId;
	/**渠道id,Channel与store的关系是one-to-many*/
	private Integer channelId;
	
	/**应用参数*/
	private ApplicationParam param = new ApplicationParam();
	
	/**店铺注册用户昵称*/
	private String nick;
	/**店铺排序号,用于前端按顺序显示*/
	private int priority = 0;
	/**店铺状态,0表示关闭,1表示开启*/
	private int status = 1;
	/**创建时间*/
	private Date createTime = new Date();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public ApplicationParam getParam() {
		return param;
	}
	public void setParam(ApplicationParam param) {
		this.param = param;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "Application [id=" + id + ", name=" + name + ", code=" + code
				+ ", storeId=" + storeId + ", channelId=" + channelId
				+ ", param=" + param + ", nick=" + nick + ", priority="
				+ priority + ", status=" + status + ", createTime="
				+ createTime + "]";
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
		Application other = (Application) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
