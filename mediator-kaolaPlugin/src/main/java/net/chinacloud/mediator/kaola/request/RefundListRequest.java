package net.chinacloud.mediator.kaola.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.kaola.response.RefundListResponse;

public class RefundListRequest extends AbstractRequest implements KaoLaRequest{

	private int status;
	private String start_time;
	private String end_time;
	private int page_no;
	private int page_size;
	
	@Override
	public String getApiMethodName() {
		return "kaola.refund.search";
	}

	@Override
	public Class<?> getResponseClass() {
		return RefundListResponse.class;
	}

	@Override
	public Map<String, Object> getAppJsonParams() throws IOException {
		Map<String,Object> map = new TreeMap<String,Object>();
		map.put("refund_status", status);
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		map.put("page_no", page_no);
		map.put("page_size", page_size);
		return map;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	public String toString() {
		return "RefundListRequest [status=" + status + ", start_time="
				+ start_time + ", end_time=" + end_time + ", page_no="
				+ page_no + ", page_size=" + page_size + "]";
	}

	
}
