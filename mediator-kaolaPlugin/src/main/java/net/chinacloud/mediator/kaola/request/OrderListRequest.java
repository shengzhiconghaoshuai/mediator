package net.chinacloud.mediator.kaola.request;


import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.kaola.response.OrderListResponse;

public class OrderListRequest extends AbstractRequest implements KaoLaRequest{
	
	private int order_status;
	private int date_type;
	private int page_no;
	private int page_size;
	private String start_time;
	private String end_time;

	@Override
	public String getApiMethodName() {
		return "kaola.order.search";
	}

	@Override
	public Map<String,Object> getAppJsonParams() throws IOException {
		Map<String,Object> map = new TreeMap<String,Object>();
		map.put("order_status", order_status);
		map.put("date_type", date_type);
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
	
	@Override
	public String getRequestMethod() {
		return "GET";
	}

	public int getOrder_status() {
		return order_status;
	}

	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}

	public int getData_type() {
		return date_type;
	}

	public void setData_type(int data_type) {
		this.date_type = data_type;
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

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	

}
