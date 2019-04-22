/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskTemplate.java
 * 描述： Task模板
 */
package net.chinacloud.mediator.task;

import java.io.Serializable;

/**
 * <Task模板>
 * <Task模板,控制Task的一些配置,如是否可以重跑>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class TaskTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8750605287320999465L;
	/**模板id,一般来说自增长*/
	private Integer id;
	/**模板描述,该template产生的task的功能,是否可重复,是否可以重跑等*/
	private String description;
	/**
	 * task分类,二级分类,一级可选值如ORDER/RETURN/REFUND等,
	 * 二级分类可选值如create/list/delivery等
	 */
	private String type;
	private String subType;
	/**
	 * 是否可重复创建,比如ORDER/CREATE类型的task,如果之前已经创建,
	 * 则不允许再创建新的相同orderId的task0表示不可重复,1表示可以重复
	 */
	private int repeatable = 0;
	/**是否可以重跑,扫尾程序会处理允许重跑的失败的task*/
	private int reRun = 0;
	/**
	 * 当线程池或者task队列容量不够,任务无法提交时,判断task是否允许挂起,
	 * 1表示允许挂起,0表示不允许挂起;对于不允许挂起的task,会继续提交任务,
	 * 而对于允许挂起的task,将task的状态置为挂起状态,不会立即提交任务
	 */
	private int hung = 1;
	/**排序号,用于页面按顺序显示*/
	private int priority = 0;
	/**渠道状态,0表示关闭,2表示开启*/
	private int status = 1;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public int getRepeatable() {
		return repeatable;
	}
	public void setRepeatable(int repeatable) {
		this.repeatable = repeatable;
	}
	public int getReRun() {
		return reRun;
	}
	public void setReRun(int reRun) {
		this.reRun = reRun;
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
	public int getHung() {
		return hung;
	}
	public void setHung(int hung) {
		this.hung = hung;
	}
	/**
	 * task是否允许挂起
	 * @return
	 */
	public boolean canHung(){
		return hung == 1 ? true : false;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subType == null) ? 0 : subType.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		TaskTemplate other = (TaskTemplate) obj;
		if (subType == null) {
			if (other.subType != null)
				return false;
		} else if (!subType.equals(other.subType))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TaskTemplate [id=" + id + ", description=" + description
				+ ", type=" + type + ", subType=" + subType + ", repeatable="
				+ repeatable + ", reRun=" + reRun + ", priority=" + priority
				+ ", status=" + status + ", canHung()=" + canHung() + "]";
	}
	
}
