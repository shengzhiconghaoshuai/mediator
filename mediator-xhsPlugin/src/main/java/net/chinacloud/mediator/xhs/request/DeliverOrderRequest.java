package net.chinacloud.mediator.xhs.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.xhs.response.DeliverOrderResponse;

public class DeliverOrderRequest extends AbstractRequest implements XhsRequest{

	private String status;
	
	private String express_company_code;
	
	private String express_no;
	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpress_company_code() {
		return express_company_code;
	}

	public void setExpress_company_code(String express_company_code) {
		this.express_company_code = express_company_code;
	}

	public String getExpress_no() {
		return express_no;
	}

	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v0/packages/urlKey";
	}

	@Override
	public Class<?> getResponseClass() {
		return DeliverOrderResponse.class;
	}

	@Override
	public Map<Object, Object> getAppJsonParams() throws IOException {
		Map<Object,Object> map = new TreeMap<Object,Object>();
		map.put("status", status);
		map.put("express_company_code", express_company_code);
		map.put("express_no", express_no);
		return map;
	}

	@Override
	public String getRequestMethod() {
		return "PUT";
	}

}
