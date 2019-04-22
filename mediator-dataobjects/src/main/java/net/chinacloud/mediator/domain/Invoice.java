/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Invoice.java
 * 描述： 发票
 */
package net.chinacloud.mediator.domain;
/**
 * <发票>
 * <发票结构>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class Invoice {
	/**发票类型*/
	private String type;
	/**发票抬头*/
	private String title;
	/**发票内容*/
	private String content;
	
	public Invoice() {
		
	}
	
	public Invoice(String type, String title,String content) {
		this.type = type;
		this.title = title;
		this.content = content;
	}
	
	public Invoice(String title,String content) {
		this.title = title;
		this.content = content;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Invoice [type=" + type + ", title=" + title + ", content="
				+ content + "]";
	}
}
