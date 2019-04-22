package net.chinacloud.mediator.xhs.response;

import net.chinacloud.mediator.xhs.bean.XhsOrder;

public class OrderDetailResponse extends XhsResponse{

	private static final long serialVersionUID = -905210844724973699L;
	
	private XhsOrder data;

	public XhsOrder getData() {
		return data;
	}

	public void setData(XhsOrder data) {
		this.data = data;
	}
	
	
}
