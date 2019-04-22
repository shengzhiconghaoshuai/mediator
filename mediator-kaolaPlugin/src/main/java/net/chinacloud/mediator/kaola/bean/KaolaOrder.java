package net.chinacloud.mediator.kaola.bean;

import java.util.ArrayList;
import java.util.List;

public class KaolaOrder {
	private String order_id;
	/**
	 * 即时订单order_status含义：1(已付款)、2(已发货)、3(已签收)、5(取消待确认)、6(已取消)
	 * 订单列表，1(已付款)、2(已发货)、3(已签收)、4(缺货订单)、5(取消待确认)、6(已取消)、7(取消处理中)、8(发货处理中)说明:7是处于取消和已取消的一个中间状态,当查询条件为5的时候会返回该状态；8是处于已付款和待发货的一个中间状态,当查询条件为1的时候会返回该状态
	 */
	private int order_status;
	private String buyer_account;//买家用户名
	private String buyer_phone;
	private String receiver_name;
	private String receiver_phone;
	private String receiver_province_name;
	private String receiver_city_name;
	private String receiver_district_name;
	private String receiver_address_detail;//收件人详细地址
	private String receiver_post_code;//收件人邮政编码
	private String pay_success_time;//支付成功时间
	private String order_real_price;//订单实际价格
	private String order_origin_price;//订单原始价格
	private String express_fee;//邮费
	private String pay_method_subname;//支付方式
	private String coupon_amount;//优惠劵优惠金额
	private String finish_time;//订单完成时间
	private String deliver_time;//发货时间
	private List<KaoLaOrderSku> order_skus = new ArrayList<KaoLaOrderSku>();
	
	private String cert_name;//实名认证姓名
	private String cert_id_no;//实名认证身份证号
	private String trade_no;//订单交易流水号
	private String tax_fee;//订单税费
	private int need_invoice;//是否需要发票 1：是  0：否
	private String invoice_amount;//发票金额
	private String invoice_title;//发票抬头
	private String waiting_deliver_time;//订单待发货时间
	private String waiting_cancel_time;//取消待商家处理时间
	private int presale_order;//0-非预售订单；1-预售订单
	private String pre_deliver_time;//预计发货时间
	private List<KaoLaOrderExpress> order_expresses = new ArrayList<KaoLaOrderExpress>();//快递信息
	
	private String order_time; //订单下单时间
	private String order_business_remark;//卖家备注信息
	private String taxpayer_no;//纳税人识别号
	private String order_buyer_remark;//买家备注信息
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}
	public String getBuyer_account() {
		return buyer_account;
	}
	public void setBuyer_account(String buyer_account) {
		this.buyer_account = buyer_account;
	}
	public String getBuyer_phone() {
		return buyer_phone;
	}
	public void setBuyer_phone(String buyer_phone) {
		this.buyer_phone = buyer_phone;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getReceiver_phone() {
		return receiver_phone;
	}
	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}
	public String getReceiver_province_name() {
		return receiver_province_name;
	}
	public void setReceiver_province_name(String receiver_province_name) {
		this.receiver_province_name = receiver_province_name;
	}
	public String getReceiver_city_name() {
		return receiver_city_name;
	}
	public void setReceiver_city_name(String receiver_city_name) {
		this.receiver_city_name = receiver_city_name;
	}
	public String getReceiver_district_name() {
		return receiver_district_name;
	}
	public void setReceiver_district_name(String receiver_district_name) {
		this.receiver_district_name = receiver_district_name;
	}
	public String getReceiver_address_detail() {
		return receiver_address_detail;
	}
	public void setReceiver_address_detail(String receiver_address_detail) {
		this.receiver_address_detail = receiver_address_detail;
	}
	public String getReceiver_post_code() {
		return receiver_post_code;
	}
	public void setReceiver_post_code(String receiver_post_code) {
		this.receiver_post_code = receiver_post_code;
	}
	public String getPay_success_time() {
		return pay_success_time;
	}
	public void setPay_success_time(String pay_success_time) {
		this.pay_success_time = pay_success_time;
	}
	public String getOrder_real_price() {
		return order_real_price;
	}
	public void setOrder_real_price(String order_real_price) {
		this.order_real_price = order_real_price;
	}
	public String getOrder_origin_price() {
		return order_origin_price;
	}
	public void setOrder_origin_price(String order_origin_price) {
		this.order_origin_price = order_origin_price;
	}
	public String getExpress_fee() {
		return express_fee;
	}
	public void setExpress_fee(String express_fee) {
		this.express_fee = express_fee;
	}
	public String getPay_method_subname() {
		return pay_method_subname;
	}
	public void setPay_method_subname(String pay_method_subname) {
		this.pay_method_subname = pay_method_subname;
	}
	public String getCoupon_amount() {
		return coupon_amount;
	}
	public void setCoupon_amount(String coupon_amount) {
		this.coupon_amount = coupon_amount;
	}
	public String getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(String finish_time) {
		this.finish_time = finish_time;
	}
	public String getDeliver_time() {
		return deliver_time;
	}
	public void setDeliver_time(String deliver_time) {
		this.deliver_time = deliver_time;
	}
	public List<KaoLaOrderSku> getOrder_skus() {
		return order_skus;
	}
	public void setOrder_skus(List<KaoLaOrderSku> order_skus) {
		this.order_skus = order_skus;
	}
	public String getCert_name() {
		return cert_name;
	}
	public void setCert_name(String cert_name) {
		this.cert_name = cert_name;
	}
	public String getCert_id_no() {
		return cert_id_no;
	}
	public void setCert_id_no(String cert_id_no) {
		this.cert_id_no = cert_id_no;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getTax_fee() {
		return tax_fee;
	}
	public void setTax_fee(String tax_fee) {
		this.tax_fee = tax_fee;
	}
	public int getNeed_invoice() {
		return need_invoice;
	}
	public void setNeed_invoice(int need_invoice) {
		this.need_invoice = need_invoice;
	}
	public String getInvoice_amount() {
		return invoice_amount;
	}
	public void setInvoice_amount(String invoice_amount) {
		this.invoice_amount = invoice_amount;
	}
	public String getInvoice_title() {
		return invoice_title;
	}
	public void setInvoice_title(String invoice_title) {
		this.invoice_title = invoice_title;
	}
	public String getWaiting_deliver_time() {
		return waiting_deliver_time;
	}
	public void setWaiting_deliver_time(String waiting_deliver_time) {
		this.waiting_deliver_time = waiting_deliver_time;
	}
	public String getWaiting_cancel_time() {
		return waiting_cancel_time;
	}
	public void setWaiting_cancel_time(String waiting_cancel_time) {
		this.waiting_cancel_time = waiting_cancel_time;
	}
	public int getPresale_order() {
		return presale_order;
	}
	public void setPresale_order(int presale_order) {
		this.presale_order = presale_order;
	}
	public String getPre_deliver_time() {
		return pre_deliver_time;
	}
	public void setPre_deliver_time(String pre_deliver_time) {
		this.pre_deliver_time = pre_deliver_time;
	}
	public List<KaoLaOrderExpress> getOrder_expresses() {
		return order_expresses;
	}
	public void setOrder_expresses(List<KaoLaOrderExpress> order_expresses) {
		this.order_expresses = order_expresses;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public String getOrder_business_remark() {
		return order_business_remark;
	}
	public void setOrder_business_remark(String order_business_remark) {
		this.order_business_remark = order_business_remark;
	}
	public String getTaxpayer_no() {
		return taxpayer_no;
	}
	public void setTaxpayer_no(String taxpayer_no) {
		this.taxpayer_no = taxpayer_no;
	}
	public String getOrder_buyer_remark() {
		return order_buyer_remark;
	}
	public void setOrder_buyer_remark(String order_buyer_remark) {
		this.order_buyer_remark = order_buyer_remark;
	}
	@Override
	public String toString() {
		return "KaolaOrder [order_id=" + order_id + ", order_status="
				+ order_status + ", buyer_account=" + buyer_account
				+ ", buyer_phone=" + buyer_phone + ", receiver_name="
				+ receiver_name + ", receiver_phone=" + receiver_phone
				+ ", receiver_province_name=" + receiver_province_name
				+ ", receiver_city_name=" + receiver_city_name
				+ ", receiver_district_name=" + receiver_district_name
				+ ", receiver_address_detail=" + receiver_address_detail
				+ ", receiver_post_code=" + receiver_post_code
				+ ", pay_success_time=" + pay_success_time
				+ ", order_real_price=" + order_real_price
				+ ", order_origin_price=" + order_origin_price
				+ ", express_fee=" + express_fee + ", pay_method_subname="
				+ pay_method_subname + ", coupon_amount=" + coupon_amount
				+ ", finish_time=" + finish_time + ", deliver_time="
				+ deliver_time + ", order_skus=" + order_skus + ", cert_name="
				+ cert_name + ", cert_id_no=" + cert_id_no + ", trade_no="
				+ trade_no + ", tax_fee=" + tax_fee + ", need_invoice="
				+ need_invoice + ", invoice_amount=" + invoice_amount
				+ ", invoice_title=" + invoice_title
				+ ", waiting_deliver_time=" + waiting_deliver_time
				+ ", waiting_cancel_time=" + waiting_cancel_time
				+ ", presale_order=" + presale_order + ", pre_deliver_time="
				+ pre_deliver_time + ", order_expresses=" + order_expresses
				+ ", order_time=" + order_time + ", order_business_remark="
				+ order_business_remark + ", taxpayer_no=" + taxpayer_no
				+ ", order_buyer_remark=" + order_buyer_remark + "]";
	}
	
	
}
