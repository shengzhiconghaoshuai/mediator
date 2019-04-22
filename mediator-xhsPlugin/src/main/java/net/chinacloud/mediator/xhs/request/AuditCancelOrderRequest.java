package net.chinacloud.mediator.xhs.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.util.StringUtils;

import net.chinacloud.mediator.xhs.response.AuditCancelOrderResponse;

public class AuditCancelOrderRequest extends AbstractRequest implements XhsRequest{

	private String package_id;//订单ID
	
	private String audit_result;//审核结果，当值为"refused"时为拒绝，当值为"canceled"为取消成功
	
	private String audit_reason;//审核理由
	
	
	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public String getAudit_result() {
		return audit_result;
	}

	public void setAudit_result(String audit_result) {
		this.audit_result = audit_result;
	}

	public String getAudit_reason() {
		return audit_reason;
	}

	public void setAudit_reason(String audit_reason) {
		this.audit_reason = audit_reason;
	}

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v0/packages/canceling/audit";
	}

	@Override
	public Class<?> getResponseClass() {
		return AuditCancelOrderResponse.class;
	}

	@Override
	public Map<Object, Object> getAppJsonParams() throws IOException {
		Map<Object,Object> map = new TreeMap<Object,Object>();
		map.put("package_id", package_id);
		map.put("audit_result", audit_result);
		if(!StringUtils.isEmpty(audit_reason)){
			map.put("audit_reason", audit_reason);
		}
		return map;
	}

	@Override
	public String getRequestMethod() {
		return "PUT";
	}

}
