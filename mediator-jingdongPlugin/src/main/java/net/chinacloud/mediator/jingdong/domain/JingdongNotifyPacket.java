package net.chinacloud.mediator.jingdong.domain;

import net.chinacloud.mediator.task.CommonNotifyPacket;

public class JingdongNotifyPacket<T> extends CommonNotifyPacket<T> {
	
	public static final String STATUS_ORDER_CREATE = "STATUS_ORDER_CREATE";
	public static final String STATUS_RETURN_CREATE = "STATUS_RETURN_CREATE";
	public static final String STATUS_RETURN_AFS_CREATE = "STATUS_RETURN_AFS_CREATE";
	public static final String STATUS_REFUND_AFS_CREATE = "STATUS_REFUND_AFS_CREATE";
    private String status;
    
	public JingdongNotifyPacket(T message, String type,String status) {
		super(message, type);
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
