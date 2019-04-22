package net.chinacloud.mediator.xhs.request;
import java.io.IOException;
import java.util.Map;

import net.chinacloud.mediator.xhs.response.ExpressResponse;

public class ExpressRequest extends AbstractRequest implements XhsRequest{

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v0/express_companies";
	}

	@Override
	public Class<?> getResponseClass() {
		return ExpressResponse.class;
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
