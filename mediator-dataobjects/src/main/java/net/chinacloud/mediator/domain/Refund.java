/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Refund.java
 * 描述： 退款
 */
package net.chinacloud.mediator.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <退款>
 * <退款数据结构>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class Refund {
	//-----------退款状态,个渠道的退款状态需要统一映射成对应的状态(基本按照淘宝状态定义,但并非与淘宝渠道耦合)-----------
	/**买家已经申请退款,等待卖家同意*/
	public static final String STATUS_WAIT_SELLER_AGREE = "WAIT_SELLER_AGREE";
	public static final String STATUS_WAIT_SELLER_AGREE_DESC = "\u9000\u6b3e\u7533\u8bf7\u002c\u7b49\u5f85\u5356\u5bb6\u786e\u8ba4\u4e2d";
	/**卖家已经同意退款,等待买家退货*/
	public static final String STATUS_WAIT_BUYER_RETURN_GOODS = "WAIT_BUYER_RETURN_GOODS";
	public static final String STATUS_WAIT_BUYER_RETURN_GOODS_DESC = "\u9000\u6b3e\u7533\u8bf7\u8fbe\u6210\u002c\u7b49\u5f85\u4e70\u5bb6\u9000\u8d27";
	/**买家已经退货,等待卖家确认收货*/
	public static final String STATUS_WAIT_SELLER_CONFIRM_GOODS = "WAIT_SELLER_CONFIRM_GOODS";
	public static final String STATUS_WAIT_SELLER_CONFIRM_GOODS_DESC = "\u4e70\u5bb6\u5df2\u7ecf\u9000\u8d27\u002c\u7b49\u5f85\u5356\u5bb6\u786e\u8ba4\u6536\u8d27";
	/**卖家拒绝退款*/
	public static final String STATUS_SELLER_REFUSE_BUYER = "SELLER_REFUSE_BUYER";
	public static final String STATUS_SELLER_REFUSE_BUYER_DESC = "\u5356\u5bb6\u4e0d\u540c\u610f\u534f\u8bae\u002c\u7b49\u5f85\u4e70\u5bb6\u4fee\u6539";
	/**退款成功*/
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_SUCCESS_DESC = "\u9000\u6b3e\u6210\u529f";
	/**退款关闭*/
	public static final String STATUS_CLOSED = "CLOSED";
	public static final String STATUS_CLOSED_DESC = "\u9000\u6b3e\u5173\u95ed";
	
	/**
     *买家是否需要退货,默认是空，need表示需要 noneed表示不需要
     */
	public static final String HAS_GOOD_RETURN_NEED = "need";
	public static final String HAS_GOOD_RETURN_NONEED = "noneed";
	
	/**渠道退款编号*/
	private String channelRefundId;
	/**渠道订单号*/
	private String channelOrderId;
	/**渠道子订单id*/
	private String channelOrderItemId;
	/**退款商品数量,这个没实际意义*/
	private Double quantity = 0D;
	/**退款金额*/
	private Double refundFee = 0D;
	/**订单支付金额*/
	private Double payment = 0D;
	/**退款申请时间*/
	private Date createTime;
	/**成功时间*/
	private Date endTime;
	/**关闭时间*/
	private Date closeTime;
	/**修改时间*/
	private Date modifyTime;
	/**退款状态*/
	private String status;
	/**状态描述*/
	private String statusDesc;
	/**退款原因*/
	private String reason;
	/**退款描述*/
	private String description;
	/**买家账号*/
	private String buyerNick;
	/**
     * 创建交易时的物流方式(交易完成前，物流方式有可能改变,但系统里的这个字段一直不变)
     * 可选值：free(卖家包邮),post(平邮),express(快递),ems(EMS),virtual(虚拟发货)
     * 默认express
     */
    private String shippingType = "express";
    /**
     * 物流公司代码,eg:SF(顺丰)、STO
     */
    private String shippingCompany;
    /**物流编号*/
    private String shippingId;
    /**超时时间*/
    private Date expireTime;
    /**买家是否需要退货 00表示未知，01表示需要 02表示不需要*/
    private String hasGoodReturn;
    /**存放一些额外的参数,供扩展使用*/
	private Map<String,Object> additionalParams = new HashMap<String, Object>();
	
	public String getChannelRefundId() {
		return channelRefundId;
	}
	public void setChannelRefundId(String channelRefundId) {
		this.channelRefundId = channelRefundId;
	}
	public String getChannelOrderId() {
		return channelOrderId;
	}
	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}
	public Double getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(Double refundFee) {
		this.refundFee = refundFee;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getChannelOrderItemId() {
		return channelOrderItemId;
	}
	public void setChannelOrderItemId(String channelOrderItemId) {
		this.channelOrderItemId = channelOrderItemId;
	}
	public Map<String, Object> getAdditionalParams() {
		return additionalParams;
	}
	public void setAdditionalParams(Map<String, Object> additionalParams) {
		this.additionalParams = additionalParams;
	}
	public Object getAdditionalParam(String key){
		return this.additionalParams.get(key);
	}
	public void addAdditionalParam(String key, Object value){
		this.additionalParams.put(key, value);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getPayment() {
		return payment;
	}
	public void setPayment(Double payment) {
		this.payment = payment;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	public String getShippingType() {
		return shippingType;
	}
	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}
	public String getShippingCompany() {
		return shippingCompany;
	}
	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}
	public String getShippingId() {
		return shippingId;
	}
	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getHasGoodReturn() {
		return hasGoodReturn;
	}
	public void setHasGoodReturn(String hasGoodReturn) {
		this.hasGoodReturn = hasGoodReturn;
	}
	
	
}
