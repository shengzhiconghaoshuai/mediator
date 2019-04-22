package net.chinacloud.mediator.xhs.response;

import net.chinacloud.mediator.xhs.bean.XhsOrderList;

public class OrderListResponse extends XhsResponse {

	private static final long serialVersionUID = -7823004113041293509L;
	
	private XhsOrderList data;
	
	public XhsOrderList getData() {
		return data;
	}

	public void setData(XhsOrderList data) {
		this.data = data;
	}
}
