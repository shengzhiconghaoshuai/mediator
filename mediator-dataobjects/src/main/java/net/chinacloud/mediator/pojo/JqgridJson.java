package net.chinacloud.mediator.pojo;

public class JqgridJson {

	private int page ; //页数
	
	private int total; //总数
	
	private int records; //记录数
	
	private Object rows; //对象List<E>
	
	public JqgridJson(int page,int total,int records,Object obj) {
       this.page = page;
       this.total = total;
       this.records = records;
       this.rows= obj;
    }
	public JqgridJson() {
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public Object getRows() {
		return rows;
	}
	public void setRows(Object rows) {
		this.rows = rows;
	}
	
}
