package net.chinacloud.mediator.vip.vop.domain;

import java.io.Serializable;
import java.util.List;

public class VipInventoryUpList implements Serializable {

	private static final long serialVersionUID = -2653920798960119219L;
	private List<VipInventoryMessage> vipInventoryMessageList;

	public List<VipInventoryMessage> getVipInventoryMessageList() {
		return vipInventoryMessageList;
	}

	public void setVipInventoryMessageList(List<VipInventoryMessage> vipInventoryMessageList) {
		this.vipInventoryMessageList = vipInventoryMessageList;
	}
}
