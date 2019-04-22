package net.chinacloud.mediator.xhs.response;

import net.chinacloud.mediator.xhs.bean.XhsOrderCancelList;

public class OrderCancelListResponse extends XhsResponse {

	private static final long serialVersionUID = -7823004113041293509L;
	
	private XhsOrderCancelList data;
	
	public XhsOrderCancelList getData() {
		return data;
	}

	public void setData(XhsOrderCancelList data) {
		this.data = data;
	}
}
