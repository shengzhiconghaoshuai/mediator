package net.chinacloud.mediator.xhs.response;

import net.chinacloud.mediator.xhs.bean.XhsOrderList;

public class InstantOrderResponse extends XhsResponse{
	private static final long serialVersionUID = -8877349747082840842L;
	
	private XhsOrderList data;
	
	public XhsOrderList getData() {
		return data;
	}

	public void setData(XhsOrderList data) {
		this.data = data;
	}
}
