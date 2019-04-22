package net.chinacloud.mediator.domain;

public class VopOrderReturn {
	private String channelOrderId;
	private int returnType;
	private String returnTime;

	public String getChannelOrderId() {
		return channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	@Override
	public String toString() {
		return "VopOrderReturn [channelOrderId=" + channelOrderId
				+ ", returnType=" + returnType + ", returnTime=" + returnTime
				+ "]";
	}

}
