package net.chinacloud.mediator.vip.vop.domain;

import java.io.Serializable;
import java.util.List;

public class PickBean implements Serializable {

	private static final long serialVersionUID = -345588731556523503L;

	public String po_no;
	public String pick_num;
	public String sell_st_time;
	public String sell_et_time;
	public String export_time;
	public String export_num;
	public String warehouse;
	public String order_cate;
	public String pick_type;
	public String total;
	public List<PickOrderDetail> list;
	public List<String> orders;
	public String getPo_no() {
		return po_no;
	}
	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}
	public String getPick_num() {
		return pick_num;
	}
	public void setPick_num(String pick_num) {
		this.pick_num = pick_num;
	}
	public String getSell_st_time() {
		return sell_st_time;
	}
	public void setSell_st_time(String sell_st_time) {
		this.sell_st_time = sell_st_time;
	}
	public String getSell_et_time() {
		return sell_et_time;
	}
	public void setSell_et_time(String sell_et_time) {
		this.sell_et_time = sell_et_time;
	}
	public String getExport_time() {
		return export_time;
	}
	public void setExport_time(String export_time) {
		this.export_time = export_time;
	}
	public String getExport_num() {
		return export_num;
	}
	public void setExport_num(String export_num) {
		this.export_num = export_num;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public String getOrder_cate() {
		return order_cate;
	}
	public void setOrder_cate(String order_cate) {
		this.order_cate = order_cate;
	}
	public String getPick_type() {
		return pick_type;
	}
	public void setPick_type(String pick_type) {
		this.pick_type = pick_type;
	}
	public List<PickOrderDetail> getList() {
		return list;
	}
	public void setList(List<PickOrderDetail> list) {
		this.list = list;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<String> getOrders() {
		return orders;
	}
	public void setOrders(List<String> orders) {
		this.orders = orders;
	}
}
