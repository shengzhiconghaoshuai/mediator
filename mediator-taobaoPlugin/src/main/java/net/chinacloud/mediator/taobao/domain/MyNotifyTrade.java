/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MyRefund.java
 * 描述： 对淘宝退款结构的修改
 */
package net.chinacloud.mediator.taobao.domain;

import java.util.Date;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
public class MyNotifyTrade extends TaobaoObject {
	private static final long serialVersionUID = 8758245988317637167L;

	  @ApiField("buyer_nick")
	  private String buyerNick;

	  @ApiField("modified")
	  private Date modified;

	  @ApiField("nick")
	  private String nick;

	  @ApiField("oid")
	  private Long oid;

	  @ApiField("payment")
	  private String payment;

	  @ApiField("seller_nick")
	  private String sellerNick;

	  @ApiField("status")
	  private String status;

	  @ApiField("tid")
	  private Long tid;

	  @ApiField("trade_mark")
	  private String tradeMark;

	  @ApiField("type")
	  private String type;

	  @ApiField("user_id")
	  private Long userId;
	  
	  @ApiField("action_type")
	  private Long actionType;

	  public String getBuyerNick()
	  {
	    return this.buyerNick;
	  }
	  public void setBuyerNick(String buyerNick) {
	    this.buyerNick = buyerNick;
	  }

	  public Date getModified() {
	    return this.modified;
	  }
	  public void setModified(Date modified) {
	    this.modified = modified;
	  }

	  public String getNick() {
	    return this.nick;
	  }
	  public void setNick(String nick) {
	    this.nick = nick;
	  }

	  public Long getOid() {
	    return this.oid;
	  }
	  public void setOid(Long oid) {
	    this.oid = oid;
	  }

	  public String getPayment() {
	    return this.payment;
	  }
	  public void setPayment(String payment) {
	    this.payment = payment;
	  }

	  public String getSellerNick() {
	    return this.sellerNick;
	  }
	  public void setSellerNick(String sellerNick) {
	    this.sellerNick = sellerNick;
	  }

	  public String getStatus() {
	    return this.status;
	  }
	  public void setStatus(String status) {
	    this.status = status;
	  }

	  public Long getTid() {
	    return this.tid;
	  }
	  public void setTid(Long tid) {
	    this.tid = tid;
	  }

	  public String getTradeMark() {
	    return this.tradeMark;
	  }
	  public void setTradeMark(String tradeMark) {
	    this.tradeMark = tradeMark;
	  }

	  public String getType() {
	    return this.type;
	  }
	  public void setType(String type) {
	    this.type = type;
	  }

	  public Long getUserId() {
	    return this.userId;
	  }
	  public void setUserId(Long userId) {
	    this.userId = userId;
	  }
	  
	public Long getActionType() {
		return actionType;
	}
	public void setActionType(Long actionType) {
		this.actionType = actionType;
	}
		
	  
	  
	  
}
