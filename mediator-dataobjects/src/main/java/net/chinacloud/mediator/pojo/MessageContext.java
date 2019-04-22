/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MessageContext.java
 * 描述： 
 */
package net.chinacloud.mediator.pojo;

import java.io.Serializable;

/**
 * @description 消息上下文
 * @author yejunwu123@gmail.com
 * @since 2015年6月30日 下午2:26:02
 */
public class MessageContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5724104351225427054L;
	/**店铺id*/
	private Integer storeId;
	/**任务id*/
	private Long taskId;
	
	public MessageContext() {
		
	}
	
	public MessageContext(Integer storeId) {
		this.storeId = storeId;
	}
	
	public MessageContext(Integer storeId, Long taskId) {
		this.storeId = storeId;
		this.taskId = taskId;
	}
	
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
}
