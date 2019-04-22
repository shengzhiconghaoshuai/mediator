package net.chinacloud.mediator.kaola.bean;

import java.util.List;

public class KaoLaRefund {
	private String order_id;
	private String refund_id;
	
	/**
	 * 售后订单详细状态（1.申请中2.同意退款3. 拒绝退款,等待用户修改4. 同意退货,等待用户发货5. 用户已退货，等待验货6. 已确认收货7. 售后完成：发起退款8. 系统超时关闭9. 用户已撤销10. 售后完成：退款完成11. 等待商家确认12. 商家拒绝退货退款-待审核13. 商家拒绝仅退款-待审核14. 客服关闭售后15. 客服挂起售后）
	 * */
	private int refund_status_detail;
	private String refund_fee;//退款金额
	private String refund_type;//售后类型0：退货退款 1：仅退款
	private String refund_reason;//退款理由
	private String refund_create_time;//售后订单创建时间
	private String user_name;//用户名称
	private String user_account;//用户账户
	
	private String user_comment;//用户备注
	
	private String express_company_name;//用户退回的快递公司
	private String express_no;//用户退回的快递编号
	
	private String refund_desc;
	private String refund_postage_bearer;
	private String refund_postage_amount;
	private String refund_postage_status;
	
	private List<KaolaRefundSku> refund_skus; 
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getRefund_id() {
		return refund_id;
	}
	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}
	public int getRefund_status_detail() {
		return refund_status_detail;
	}
	public void setRefund_status_detail(int refund_status_detail) {
		this.refund_status_detail = refund_status_detail;
	}
	
	public String getRefund_type() {
		return refund_type;
	}
	public void setRefund_type(String refund_type) {
		this.refund_type = refund_type;
	}
	public String getRefund_reason() {
		return refund_reason;
	}
	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}
	public String getRefund_create_time() {
		return refund_create_time;
	}
	public void setRefund_create_time(String refund_create_time) {
		this.refund_create_time = refund_create_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	public String getUser_comment() {
		return user_comment;
	}
	public void setUser_comment(String user_comment) {
		this.user_comment = user_comment;
	}
	public String getExpress_company_name() {
		return express_company_name;
	}
	public void setExpress_company_name(String express_company_name) {
		this.express_company_name = express_company_name;
	}
	public String getRefund_desc() {
		return refund_desc;
	}
	public void setRefund_desc(String refund_desc) {
		this.refund_desc = refund_desc;
	}
	public List<KaolaRefundSku> getRefund_skus() {
		return refund_skus;
	}
	public void setRefund_skus(List<KaolaRefundSku> refund_skus) {
		this.refund_skus = refund_skus;
	}
	public String getRefund_postage_bearer() {
		return refund_postage_bearer;
	}
	public void setRefund_postage_bearer(String refund_postage_bearer) {
		this.refund_postage_bearer = refund_postage_bearer;
	}
	public String getRefund_postage_amount() {
		return refund_postage_amount;
	}
	public void setRefund_postage_amount(String refund_postage_amount) {
		this.refund_postage_amount = refund_postage_amount;
	}
	public String getRefund_postage_status() {
		return refund_postage_status;
	}
	public void setRefund_postage_status(String refund_postage_status) {
		this.refund_postage_status = refund_postage_status;
	}
	
	
	public String getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}
	public String getExpress_no() {
		return express_no;
	}
	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}
	@Override
	public String toString() {
		return "KaoLaRefund [order_id=" + order_id + ", refund_id=" + refund_id
				+ ", refund_status_detail=" + refund_status_detail
				+ ", refund_fee=" + refund_fee + ", refund_type=" + refund_type
				+ ", refund_reason=" + refund_reason + ", refund_create_time="
				+ refund_create_time + ", user_name=" + user_name
				+ ", user_account=" + user_account + ", user_comment="
				+ user_comment + ", express_company_name="
				+ express_company_name + ", express_no=" + express_no
				+ ", refund_desc=" + refund_desc + ", refund_postage_bearer="
				+ refund_postage_bearer + ", refund_postage_amount="
				+ refund_postage_amount + ", refund_postage_status="
				+ refund_postage_status + ", refund_skus=" + refund_skus + "]";
	}
	
	
	
	

}
