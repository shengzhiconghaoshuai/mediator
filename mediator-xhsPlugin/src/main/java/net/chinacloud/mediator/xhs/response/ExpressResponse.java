package net.chinacloud.mediator.xhs.response;
import java.util.List;

import net.chinacloud.mediator.xhs.bean.XhsExpress;

public class ExpressResponse extends XhsResponse{
	private static final long serialVersionUID = 5698299760018905462L;
	
	private List<XhsExpress> data;

	public List<XhsExpress> getData() {
		return data;
	}

	public void setData(List<XhsExpress> data) {
		this.data = data;
	}
	
}
