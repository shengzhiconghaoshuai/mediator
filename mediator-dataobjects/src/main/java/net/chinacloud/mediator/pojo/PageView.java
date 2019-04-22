/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.pojo;

import java.util.List;

/**
 * @description 分页显示数据,便于转换成json格式
 * @author ywu@wuxicloud.com
 * 2015年5月15日 下午10:43:53
 */
public class PageView<T> {
	
	public PageView(Page<T> page) {
		setTotal(page.getTotalPage());
		setPage(page.getCurrentPage());
		setRecords(page.getTotalRecords());
		setRows(page.getData());
	}
	/**
	 * 总页数
	 */
	private int total;
	/**
	 * 当前页
	 */
	private int page;
	/**
	 * 总记录数
	 */
	private long records;
	/**
	 * 数据
	 */
	private List<T> rows;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public long getRecords() {
		return records;
	}
	public void setRecords(long records) {
		this.records = records;
	}
	
}
