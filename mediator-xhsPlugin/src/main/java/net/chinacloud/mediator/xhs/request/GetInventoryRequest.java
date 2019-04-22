package net.chinacloud.mediator.xhs.request;

import java.io.IOException;
import java.util.Map;

import net.chinacloud.mediator.xhs.response.GetInventoryResponse;

public class GetInventoryRequest extends AbstractRequest implements XhsRequest{

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v0/items/urlKey/stock";
	}

	@Override
	public Class<?> getResponseClass() {
		return GetInventoryResponse.class;
	}

	@Override
	public Map<Object, Object> getAppJsonParams() throws IOException {
		return null;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}

}
