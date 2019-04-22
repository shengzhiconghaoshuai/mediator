package net.chinacloud.mediator.vip.vop.domain;



import java.util.List;




public class PoBean {
	/**
	 * 
	 */
	public String po_no;
	public String co_mode;
	public String sell_st_time;
	public String sell_et_time;
	public int stock; 
	public List<PoSku> poSkus;
	public String not_pick;
}
