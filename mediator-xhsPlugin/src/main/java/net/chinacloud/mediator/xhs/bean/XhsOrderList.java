package net.chinacloud.mediator.xhs.bean;

import java.util.ArrayList;
import java.util.List;

public class XhsOrderList {
	private int current_page;//查询当前页，从1开始计数
	private int total_page;//总页数
	private int page_size;//分页大小，默认50
	private int total_num;//结果总数(即时订单)
	private int total_number;//结果总数(订单列表)
	private List<XhsOrder> packages = new ArrayList<XhsOrder>();//即时订单
	private List<XhsOrder> package_list = new ArrayList<XhsOrder>();//订单列表
	
	public int getCurrent_page() {
		return current_page;
	}
	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}
	public int getTotal_page() {
		return total_page;
	}
	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	public int getTotal_num() {
		return total_num;
	}
	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}
	public List<XhsOrder> getPackages() {
		return packages;
	}
	public void setPackages(List<XhsOrder> packages) {
		this.packages = packages;
	}
	public List<XhsOrder> getPackage_list() {
		return package_list;
	}
	public void setPackage_list(List<XhsOrder> package_list) {
		this.package_list = package_list;
	}
	public int getTotal_number() {
		return total_number;
	}
	public void setTotal_number(int total_number) {
		this.total_number = total_number;
	}

}
