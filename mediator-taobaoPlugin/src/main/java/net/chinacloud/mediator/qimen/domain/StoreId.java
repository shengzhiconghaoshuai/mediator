package net.chinacloud.mediator.qimen.domain;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Created by Octopus8 on 2017/9/12.
 */

public class StoreId implements Serializable{

	private static final long serialVersionUID = 720527667163199775L;
	
	@XStreamImplicit(itemFieldName="storeId")
	private List<Long> storeId;

	public List<Long> getStoreId() {
		return storeId;
	}

	public void setStoreId(List<Long> storeId) {
		this.storeId = storeId;
	}

	

}
