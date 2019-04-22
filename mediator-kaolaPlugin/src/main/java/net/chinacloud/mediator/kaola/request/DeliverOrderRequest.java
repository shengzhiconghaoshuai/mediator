package net.chinacloud.mediator.kaola.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.kaola.response.DeliverOrderResponse;

public class DeliverOrderRequest extends AbstractRequest implements KaoLaRequest{

	private String order_id;
	
	private String express_company_code;
	
	private String express_no;
	
	@Override
	public String getApiMethodName() {
		return "kaola.logistics.deliver";
	}

	@Override
	public Class<?> getResponseClass() {
		return DeliverOrderResponse.class;
	}

	@Override
	public Map<String, Object> getAppJsonParams() throws IOException {
		Map<String,Object> map = new TreeMap<String,Object>();
		map.put("order_id", order_id);
		map.put("express_company_code", express_company_code);
		map.put("express_no", express_no);
		return map;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
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

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	

}
