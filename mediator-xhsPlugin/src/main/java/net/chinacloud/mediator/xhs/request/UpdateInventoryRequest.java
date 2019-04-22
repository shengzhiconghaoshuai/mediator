package net.chinacloud.mediator.xhs.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.xhs.response.UpdateInventoryResponse;

public class UpdateInventoryRequest extends AbstractRequest implements XhsRequest{

	private long qty;
	
	private int type;//1为全量更新，2为增量更新

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v0/inventories/urlKey";
	}

	@Override
	public Class<?> getResponseClass() {
		return UpdateInventoryResponse.class;
	}

	@Override
	public Map<Object, Object> getAppJsonParams() throws IOException {
		Map map = new TreeMap();
		map.put("qty", qty);
		return map;
	}

	@Override
	public String getRequestMethod() {
		if(type == 1){
			return "PUT";
		}else {
			return "PATCH";
		}
		
	}

}
