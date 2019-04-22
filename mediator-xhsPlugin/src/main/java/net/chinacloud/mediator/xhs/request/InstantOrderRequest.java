package net.chinacloud.mediator.xhs.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.xhs.response.InstantOrderResponse;

public class InstantOrderRequest extends AbstractRequest implements XhsRequest{
	private int order_time_from;
	
	private int order_time_to;
	
	private int page_no;
	
	private int page_size;

	@Override
	public String getApiMethodName() {
		return "/ark/open_api/v0/packages/latest_packages";
	}

	@Override
	public Map getAppJsonParams() throws IOException {
		Map map = new TreeMap();
		map.put("order_time_from", order_time_from);
		map.put("order_time_to", order_time_to);
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
		return InstantOrderResponse.class;
	}

	public int getOrder_time_from() {
		return order_time_from;
	}

	public void setOrder_time_from(int order_time_from) {
		this.order_time_from = order_time_from;
	}

	public int getOrder_time_to() {
		return order_time_to;
	}

	public void setOrder_time_to(int order_time_to) {
		this.order_time_to = order_time_to;
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

	@Override
	public String getRequestMethod() {
		
		return "GET";
	}

}
