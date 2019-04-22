package net.chinacloud.mediator.xhs.bean;


public class XhsOrderCancel {
	private String package_id;
	/**
	 * 取消订单审核状态，当值为"audited"时为已审核，当值为"unaudited"为待审核
	 */
	private String status;
	private Long cancel_time; //订单取消时间
	private String cancel_reason;//取消原因
	private Long max_audit_time;//剩余审核时间,单位：秒
	private String logistics;//该包裹对应的物流模式
	private String audit_result;//审核结果，当值为"refused"时为拒绝，当值为"canceled"为取消成功
	private String audit_reason;//审核理由
	private Long audit_time;//审核时间
	
	public String getPackage_id() {
		return package_id;
	}
	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCancel_time() {
		return cancel_time;
	}
	public void setCancel_time(Long cancel_time) {
		this.cancel_time = cancel_time;
	}
	public String getCancel_reason() {
		return cancel_reason;
	}
	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}
	public Long getMax_audit_time() {
		return max_audit_time;
	}
	public void setMax_audit_time(Long max_audit_time) {
		this.max_audit_time = max_audit_time;
	}
	public String getLogistics() {
		return logistics;
	}
	public void setLogistics(String logistics) {
		this.logistics = logistics;
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
	public Long getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(Long audit_time) {
		this.audit_time = audit_time;
	}

	
	
}
