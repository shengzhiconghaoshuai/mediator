package net.chinacloud.mediator.vip.vop.domain;

import java.io.Serializable;
import java.util.List;

public class PoList implements Serializable {

	private static final long serialVersionUID = -3988091821449754235L;
	private List<PoMessage> po_nos;

	public List<PoMessage> getPo_nos() {
		return po_nos;
	}

	public void setPo_nos(List<PoMessage> po_nos) {
		this.po_nos = po_nos;
	}

}
