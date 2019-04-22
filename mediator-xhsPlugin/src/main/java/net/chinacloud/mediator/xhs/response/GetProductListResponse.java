package net.chinacloud.mediator.xhs.response;

import net.chinacloud.mediator.xhs.bean.XhsProductList;

public class GetProductListResponse extends XhsResponse{
	private static final long serialVersionUID = -3404367806150813587L;
	
	private XhsProductList data;

	public XhsProductList getData() {
		return data;
	}

	public void setData(XhsProductList data) {
		this.data = data;
	}
	
	
}
