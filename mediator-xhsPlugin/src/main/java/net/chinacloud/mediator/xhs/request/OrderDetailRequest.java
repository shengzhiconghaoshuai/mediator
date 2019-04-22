package net.chinacloud.mediator.xhs.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.xhs.response.OrderDetailResponse;

public class OrderDetailRequest extends AbstractRequest implements XhsRequest{

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v0/packages/urlKey";
	}

	@Override
	public Map getAppJsonParams() throws IOException {
		Map map = new TreeMap();
		return map;
		
	}

	@Override
	public String getOtherParams() {
		return null;
	}

	@Override
	public Class<?> getResponseClass() {
		return OrderDetailResponse.class;
	}

	@Override
	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return "GET";
	}

}
