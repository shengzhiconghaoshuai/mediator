package net.chinacloud.mediator.kaola.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.kaola.response.RefundDetailReponse;

public class RefundDetailRequest extends AbstractRequest implements
		KaoLaRequest {
	private String refund_id;

	@Override
	public String getApiMethodName() {
		return "kaola.refund.get";
	}

	@Override
	public Class<?> getResponseClass() {
		return RefundDetailReponse.class;
	}

	@Override
	public Map<String, Object> getAppJsonParams() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("refund_id", refund_id);
		return map;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}
	

}
