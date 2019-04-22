package net.chinacloud.mediator.xhs.bean;

import java.util.ArrayList;
import java.util.List;

public class XhsOrder {
	private String package_id;
	/**
	 * 即时订单status含义："unconfirmed" 未确认订单，"confirmed"确认订单, "canceled" 取消订单
	 * 订单列表，订单详情含义：waiting待配货,shipped已发货,received已收货
	 */
	private String status;
	private int time;
	private int pay_time;
	private Long confirm_time;
	private String express_company_code;
	private String express_company_name;
	private String express_no;
	private String receiver_name;
	private String receiver_phone;
	private String receiver_address;
	private String province;
	private String city;
	
	private String district;
	private Double total_net_weight;
	private Double pay_amount;
	private String international_express_no;
	private String delivery_time_preference;
	private String order_declared_amount;
	
	private int item_number;
	
	private List<XhsOrderItem> item_list = new ArrayList<XhsOrderItem>();

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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getPay_time() {
		return pay_time;
	}

	public void setPay_time(int pay_time) {
		this.pay_time = pay_time;
	}

	public Long getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(Long confirm_time) {
		this.confirm_time = confirm_time;
	}

	public String getExpress_company_code() {
		return express_company_code;
	}

	public void setExpress_company_code(String express_company_code) {
		this.express_company_code = express_company_code;
	}

	public String getExpress_company_name() {
		return express_company_name;
	}

	public void setExpress_company_name(String express_company_name) {
		this.express_company_name = express_company_name;
	}

	public String getExpress_no() {
		return express_no;
	}

	public void setExpress_no(String express_no) {
		this.express_no = express_no;
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

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Double getTotal_net_weight() {
		return total_net_weight;
	}

	public void setTotal_net_weight(Double total_net_weight) {
		this.total_net_weight = total_net_weight;
	}

	public Double getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(Double pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getInternational_express_no() {
		return international_express_no;
	}

	public void setInternational_express_no(String international_express_no) {
		this.international_express_no = international_express_no;
	}

	public String getDelivery_time_preference() {
		return delivery_time_preference;
	}

	public void setDelivery_time_preference(String delivery_time_preference) {
		this.delivery_time_preference = delivery_time_preference;
	}

	public String getOrder_declared_amount() {
		return order_declared_amount;
	}

	public void setOrder_declared_amount(String order_declared_amount) {
		this.order_declared_amount = order_declared_amount;
	}

	public int getItem_number() {
		return item_number;
	}

	public void setItem_number(int item_number) {
		this.item_number = item_number;
	}

	public List<XhsOrderItem> getItem_list() {
		return item_list;
	}

	public void setItem_list(List<XhsOrderItem> item_list) {
		this.item_list = item_list;
	}
	
	
}
