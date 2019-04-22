package net.chinacloud.mediator.kaola.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.kaola.response.OrderDetailResponse;

public class OrderDetailRequest extends AbstractRequest implements KaoLaRequest{

	private String order_id;
	
	@Override
	public String getApiMethodName() {
		return "kaola.order.get";
	}

	@Override
	public Map<String, Object> getAppJsonParams() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
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
		return "GET";
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	
	

}
