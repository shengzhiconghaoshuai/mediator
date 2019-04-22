package net.chinacloud.mediator.xhs.request;


import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.xhs.response.OrderListResponse;

public class OrderListRequest extends AbstractRequest implements XhsRequest{
	
	private String status;
	
	private int page_no;
	
	private int page_size;
	
	private int start_time;
	
	private int end_time;

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v0/packages";
	}

	@Override
	public Map getAppJsonParams() throws IOException {
		Map map = new TreeMap();
		map.put("status", status);
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		map.put("page_no", page_no);
		map.put("page_size", page_size);
		return map;
		
	}

	@Override
	public String getOtherParams() {
		return null;
	}

	@Override
	public Class<?> getResponseClass() {
		return OrderListResponse.class;
	}

	
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
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStart_time() {
		return start_time;
	}

	public void setStart_time(int start_time) {
		this.start_time = start_time;
	}

	public int getEnd_time() {
		return end_time;
	}

	public void setEnd_time(int end_time) {
		this.end_time = end_time;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}

}
