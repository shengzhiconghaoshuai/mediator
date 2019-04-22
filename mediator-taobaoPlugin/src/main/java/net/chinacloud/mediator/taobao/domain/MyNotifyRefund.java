/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MyRefund.java
 * 描述： 对淘宝退款结构的修改
 */
package net.chinacloud.mediator.taobao.domain;

import java.util.Date;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
/**
 * <淘宝退款结构映射修改>
 * <淘宝个SB,退款编号rid在通知json中为refund_id,使用@ApiField注解
 * 映射时名称却为rid,导致退款编号解析不出来>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月26日
 * @since 2015年1月26日
 */
public class MyNotifyRefund extends TaobaoObject {
	private static final long serialVersionUID = 1525997265888188919L;

	/**
	 * 买家昵称
	 */
	@ApiField("buyer_nick")
	private String buyerNick;

	/**
	 * 退款修改时间（格式：yyyy-MM-dd HH:mm:ss）
	 */
	@ApiField("modified")
	private Date modified;

	/**
	 * 消息所属的用户昵称
	 */
	@ApiField("nick")
	private String nick;

	/**
	 * 子订单退款交易编号
	 */
	@ApiField("oid")
	private Long oid;

	/**
	 * 退款金额
	 */
	@ApiField("refund_fee")
	private String refundFee;

	/**
	 * 退款编号
	 */
	@ApiField("refund_id")
	private Long rid;

	/**
	 * 卖家昵称
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 * 退款操作所对应的退款增量消息状态 
		可选值： 
		RefundSuccess（退款成功） 
		RefundClosed（退款关闭） 
		RefundCreated（退款创建） 
		RefundSellerAgreeAgreement（卖家同意退款协议） 
		RefundSellerRefuseAgreement（卖家拒绝退款协议） 
		RefundBuyerModifyAgreement（买家修改退款协议） 
		RefundBuyerReturnGoods（买家退货给卖家） 
		RefundCreateMessage（发表留言） 
		RefundBlockMessage（屏蔽留言） 
		RefundTimeoutRemind（退款超时提醒）
	 */
	@ApiField("status")
	private String status;

	/**
	 * 父订单退款交易编号
	 */
	@ApiField("tid")
	private Long tid;

	/**
	 * 消息所属的用户编号
	 */
	@ApiField("user_id")
	private Long userId;

	public String getBuyerNick() {
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

	public String getRefundFee() {
		return this.refundFee;
	}
	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public Long getRid() {
		return this.rid;
	}
	public void setRid(Long rid) {
		this.rid = rid;
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

	public Long getUserId() {
		return this.userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
