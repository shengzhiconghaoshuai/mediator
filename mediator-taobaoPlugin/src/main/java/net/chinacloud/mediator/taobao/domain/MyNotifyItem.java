/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MyRefund.java
 * 描述： 对淘宝退款结构的修改
 */
package net.chinacloud.mediator.taobao.domain;

import java.util.Date;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
public class MyNotifyItem extends TaobaoObject {
	private static final long serialVersionUID = 8639996714951399716L;

	  @ApiField("changed_fields")
	  private String changedFields;

	  @ApiField("iid")
	  private String iid;

	  @ApiField("increment")
	  private Long increment;

	  @ApiField("modified")
	  private Date modified;

	  @ApiField("nick")
	  private String nick;

	  @ApiField("num")
	  private Long num;

	  @ApiField("num_iid")
	  private Long numIid;

	  @ApiField("price")
	  private String price;

	  @ApiField("sku_id")
	  private Long skuId;

	  @ApiField("sku_num")
	  private Long skuNum;

	  @ApiField("status")
	  private String status;

	  @ApiField("title")
	  private String title;

	  @ApiField("user_id")
	  private Long userId;

	  public String getChangedFields()
	  {
	    return this.changedFields;
	  }
	  public void setChangedFields(String changedFields) {
	    this.changedFields = changedFields;
	  }

	  public String getIid() {
	    return this.iid;
	  }
	  public void setIid(String iid) {
	    this.iid = iid;
	  }

	  public Long getIncrement() {
	    return this.increment;
	  }
	  public void setIncrement(Long increment) {
	    this.increment = increment;
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

	  public Long getNum() {
	    return this.num;
	  }
	  public void setNum(Long num) {
	    this.num = num;
	  }

	  public Long getNumIid() {
	    return this.numIid;
	  }
	  public void setNumIid(Long numIid) {
	    this.numIid = numIid;
	  }

	  public String getPrice() {
	    return this.price;
	  }
	  public void setPrice(String price) {
	    this.price = price;
	  }

	  public Long getSkuId() {
	    return this.skuId;
	  }
	  public void setSkuId(Long skuId) {
	    this.skuId = skuId;
	  }

	  public Long getSkuNum() {
	    return this.skuNum;
	  }
	  public void setSkuNum(Long skuNum) {
	    this.skuNum = skuNum;
	  }

	  public String getStatus() {
	    return this.status;
	  }
	  public void setStatus(String status) {
	    this.status = status;
	  }

	  public String getTitle() {
	    return this.title;
	  }
	  public void setTitle(String title) {
	    this.title = title;
	  }

	  public Long getUserId() {
	    return this.userId;
	  }
	  public void setUserId(Long userId) {
	    this.userId = userId;
	  }
}
