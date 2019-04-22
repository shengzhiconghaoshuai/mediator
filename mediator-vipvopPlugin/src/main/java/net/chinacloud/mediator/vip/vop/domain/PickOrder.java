package net.chinacloud.mediator.vip.vop.domain;



public class PickOrder {
	public String getPo_no() {
		return po_no;
	}
	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}
	public String getPick_no() {
		return pick_no;
	}
	public void setPick_no(String pick_no) {
		this.pick_no = pick_no;
	}
	public String getCo_mode() {
		return co_mode;
	}
	public void setCo_mode(String co_mode) {
		this.co_mode = co_mode;
	}
	public String getSell_site() {
		return sell_site;
	}
	public void setSell_site(String sell_site) {
		this.sell_site = sell_site;
	}
	public String getOrder_cate() {
		return order_cate;
	}
	public void setOrder_cate(String order_cate) {
		this.order_cate = order_cate;
	}
	public int getPick_num() {
		return pick_num;
	}
	public void setPick_num(int pick_num) {
		this.pick_num = pick_num;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getEx_time() {
		return ex_time;
	}
	public void setEx_time(String ex_time) {
		this.ex_time = ex_time;
	}
	public int getEx_num() {
		return ex_num;
	}
	public void setEx_num(int ex_num) {
		this.ex_num = ex_num;
	}
	public int getDelivery_status() {
		return delivery_status;
	}
	public void setDelivery_status(int delivery_status) {
		this.delivery_status = delivery_status;
	}
	private String po_no;	
	private String pick_no	;
	private String co_mode;
	private String sell_site	;
	private String order_cate	;
	private int pick_num	;
	private String create_time	;
	private String ex_time	;
	private int ex_num	;
	private int delivery_status	;

}
