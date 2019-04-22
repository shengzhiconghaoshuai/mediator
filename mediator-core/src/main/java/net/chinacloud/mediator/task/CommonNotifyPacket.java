/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CommonNotifyPacket.java
 * 描述： 
 */
package net.chinacloud.mediator.task;

public class CommonNotifyPacket<T> {
	/**消息内容*/
	protected T message;
	protected String type;
	
	public CommonNotifyPacket(T message){
		this.message = message;
	}
	
	public CommonNotifyPacket(T message,String type){
		this.message = message;
		this.type = type;
	}
	
    public T getMessage() {
        return message;
    }

    public CommonNotifyPacket<T> setMessage(T message) {
        this.message = message;
        return this;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CommonNotifyPacket [message=" + message + ", type=" + type
				+ "]";
	}
	
}
