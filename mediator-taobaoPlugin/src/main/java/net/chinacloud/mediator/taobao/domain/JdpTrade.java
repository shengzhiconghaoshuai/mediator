/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：JdpTrade.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.domain;

import java.util.Date;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月11日 下午5:36:19
 */
public class JdpTrade {
	private Integer id;
	/**交易编号*/
	private Long tid;
	/**状态*/
	private String status;
	/**交易类型*/
	private String type;
	/**卖家昵称*/
	private String sellerNick;
	/**卖家昵称*/
	private String buyerNick;
	/**trade结构*/
	private String jdpResponse;
	/**入库修改时间*/
	private Date jdpModified;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSellerNick() {
		return sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	public String getJdpResponse() {
		return jdpResponse;
	}
	public void setJdpResponse(String jdpResponse) {
		this.jdpResponse = jdpResponse;
	}
	public Date getJdpModified() {
		return jdpModified;
	}
	public void setJdpModified(Date jdpModified) {
		this.jdpModified = jdpModified;
	}
	
}
