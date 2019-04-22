package net.chinacloud.mediator.kaola.bean;

import java.util.List;

import net.chinacloud.mediator.kaola.bean.KaoLaRefund;

public class KaoLaRefundList {

	
	private List<KaoLaRefund> refunds;
	private int total_count;
	public List<KaoLaRefund> getRefunds() {
		return refunds;
	}
	public void setRefunds(List<KaoLaRefund> refunds) {
		this.refunds = refunds;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	@Override
	public String toString() {
		return "RefundListResponse [refunds=" + refunds + ", total_count="
				+ total_count + "]";
	}
	
	

}
