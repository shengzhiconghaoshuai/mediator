package net.chinacloud.mediator.xhs.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.xhs.response.GetProductListResponse;

public class GetProductListRequest extends AbstractRequest implements XhsRequest{

	
	private int page_no;
	
	private int page_size;
	
	public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		this.page_no = page_no;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v1/items/lite";
	}

	@Override
	public Class<?> getResponseClass() {
		// TODO Auto-generated method stub
		return GetProductListResponse.class;
	}

	@Override
	public Map<Object, Object> getAppJsonParams() throws IOException {
		Map map = new TreeMap();
		map.put("page_no", page_no);
		map.put("page_size", page_size);
		return map;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}


}
