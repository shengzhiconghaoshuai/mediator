/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CronConfig.java
 * 描述： cron配置
 */
package net.chinacloud.mediator.system.schedule.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <调度配置>
 * <调度配置>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月18日
 * @since 2014年12月18日
 */
public class CronConfig {
	/**主键*/
	private Integer id;
	/**描述*/
	private String description;
	/**job类名*/
	private String className;
	/**是否是全局的,全局任务只会在集群的一台机器运行,本地任务会在集群的每一台机器上运行*/
	private int global = 1;
	/**状态,1表示启用,0表示停用*/
	private int status = 1;
	/**调度参数*/
	private List<CronParam> params = new ArrayList<CronParam>(0);
	/**channel id*/
	//private Integer channelId;
	/**应用 id,eg:1,2,3*/
	//private String applicationIds;
	
	/**任务是否是cron类型,默认为是*/
	private int cron = 1;
	/**cron表达式*/
	private String expression;
	
	/**以下配置为非cron行任务*/
	/**开始时间*/
	private Date startTime;
	/**结束时间*/
	private Date endTime;
	/**执行次数*/
	private int repeatCount = 0;
	/**间隔(秒)*/
	private int repeatInterval = 0;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<CronParam> getParams() {
		return params;
	}
	public void setParams(List<CronParam> params) {
		this.params = params;
	}
	/*public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public String getApplicationIds() {
		return applicationIds;
	}
	public void setApplicationIds(String applicationIds) {
		this.applicationIds = applicationIds;
	}*/
	public String getExpression() {
		return expression;
	}
	public int getGlobal() {
		return global;
	}
	public void setGlobal(int global) {
		this.global = global;
	}
	public int getCron() {
		return cron;
	}
	public void setCron(int cron) {
		this.cron = cron;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
	public int getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(int repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	public void addParam(String name, Object value){
		CronParam param = new CronParam(this.id, name, value.toString());
		this.params.add(param);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
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
		CronConfig other = (CronConfig) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CronConfig [id=" + id + ", description=" + description
				+ ", className=" + className + ", global=" + global
				+ ", status=" + status + ", params=" + params + ", cron="
				+ cron + ", expression=" + expression + ", startTime="
				+ startTime + ", endTime=" + endTime + ", repeatCount="
				+ repeatCount + ", repeatInterval=" + repeatInterval + "]";
	}
}
