package net.chinacloud.mediator.xhs.bean;

import java.util.ArrayList;
import java.util.List;

public class XhsProductList {
	private int current_page;//查询当前页，从1开始计数
	private int page_size;//分页大小，默认50
	private int total;
	private List<XhsProduct> hits = new ArrayList<XhsProduct>();
	public int getCurrent_page() {
		return current_page;
	}
	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<XhsProduct> getHits() {
		return hits;
	}
	public void setHits(List<XhsProduct> hits) {
		this.hits = hits;
	}
	
}
