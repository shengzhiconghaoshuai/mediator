/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Order.java
 * 描述： 订单数据结构
 */
package net.chinacloud.mediator.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <订单数据结构>
 * <订单数据结构>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月8日
 * @since 2014年12月8日
 */
public class Order {
	
	//-----------预售订单状态,目前仅淘宝渠道有预售,本系统直接使用淘宝的预售状态,以后有其他渠道使用预售,统一转换成该状态-----------
	/**定金未付尾款未付*/
	public static final String STEP_STATUS_FRONT_NOPAID_FINAL_NOPAID = "FRONT_NOPAID_FINAL_NOPAID";
	/**定金已付尾款未付*/
	public static final String STEP_STATUS_FRONT_PAID_FINAL_NOPAID = "FRONT_PAID_FINAL_NOPAID";
	/**定金已付尾款已付*/
	public static final String STEP_STATUS_FRONT_PAID_FINAL_PAID = "FRONT_PAID_FINAL_PAID";
	
	//-----------订单状态,个渠道的订单状态需要统一映射成对应的状态(基本按照淘宝状态定义,但并非与淘宝渠道耦合)-----------
	/**已付款,等待卖家发货*/
	public static final String STATUS_WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";
	public static final String STATUS_WAIT_SELLER_SEND_GOODS_DESC = "\u4e70\u5bb6\u5df2\u4ed8\u6b3e";
	/**卖家已发货,等待买家收货*/
	public static final String STATUS_WAIT_BUYER_CONFIRM_GOODS = "WAIT_BUYER_CONFIRM_GOODS";
	public static final String STATUS_WAIT_BUYER_CONFIRM_GOODS_DESC = "\u5356\u5bb6\u5df2\u53d1\u8d27";
	/**交易正常成功(支付-发货-确认收货)*/
	public static final String STATUS_FINISHED = "FINISHED";
	public static final String STATUS_FINISHED_DESC = "\u4ea4\u6613\u6210\u529f";
	/**付款以后用户退款成功,交易自动关闭*/
	public static final String STATUS_CLOSED = "CLOSED";
	public static final String STATUS_CLOSED_DESC = "\u4ea4\u6613\u5173\u95ed";
	/**卖家部分发货*/
	public static final String STATUS_SELLER_CONSIGNED_PART = "SELLER_CONSIGNED_PART";
	public static final String STATUS_SELLER_CONSIGNED_PART_DESC = "\u5356\u5bb6\u90e8\u5206\u53d1\u8d27";
	/**买家已签收,货到付款专用*/
	public static final String STATUS_BUYER_SIGNED = "BUYER_SIGNED";
	public static final String STATUS_BUYER_SIGNED_DESC = "\u4e70\u5bb6\u5df2\u7b7e\u6536";
	/**渠道关闭*/
	public static final String STATUS_CLOSED_BY_CHANNEL = "CLOSED_BY_CHANNEL";
	
	//-----------订单类型,如一口价、货到付款、预售等,这些类型基本沿用淘宝的订单类型-----------
	/**一口价订单*/
	public static final String ORDER_TYPE_FIXED = "fixed";
	/**预售分期付款订单*/
	public static final String ORDER_TYPE_STEP = "step";
	/**货到付款订单*/
	public static final String ORDER_TYPE_COD = "cod";
	/**拍卖订单*/
	public static final String ORDER_TYPE_AUCTION = "auction";
	/**自提订单*/
	public static final String STORE_COLLECT = "STORE_COLLECT";
	/**线下订单*/
	public static final String ORDER_TYPE_OFFLINE = "offline";
	
	
	/**内部系统订单号*/
	private Long orderId;
	/**渠道订单号(淘宝、京东等各平台上的单号)*/
	private String channelOrderId;
	
	/**交易创建时间(下单时间)。格式:yyyy-MM-dd HH:mm:ss*/
	private Date createTime;
	/**支付时间*/
	private Date payTime;
	/**修改时间*/
	private Date modified;
	/**交易成功时间*/
	private Date endTime;
	/**取消时间*/
	private Date cancelTime;
	/**交易类型,eg:fixed-一口价,step-分期,cod-货到付款,auction-拍卖*/
	private String type = ORDER_TYPE_FIXED;
	/**
     * 交易内部来源,如手机、PC、PAD等
     */
    private String tradeFrom;
	/**订单状态*/
	private String status;
	/**状态说明*/
	private String statusDesc;
	
	/**分阶段付款的订单状态*/
	private String stepStatus;
	/**分阶段付款的已付金额*/
	private Double stepPaidFee;
	
	/**支付方式,默认支付宝*/
	private String payType = "alipay";
	
	/**货到付款还需支付的金额*/
	private Double codPrice = 0D;
	
	/**wcs是否计算分摊折扣*/
	private String calculateShareDiscount = "Y";
	/**
     * 物流公司代码,eg:SF(顺丰)、STO
     */
    private String shippingCompany;
    /**
     * 创建交易时的物流方式(交易完成前，物流方式有可能改变,但系统里的这个字段一直不变)
     * 可选值：free(卖家包邮),post(平邮),express(快递),ems(EMS),virtual(虚拟发货)
     * 默认express
     */
    private String shippingType = "express";
    /**物流编号*/
    private String shippingId;
	
	/**卖家备注*/
	private String sellerMemo;
	/**买家留言*/
	private String buyerMessage;
	
	/**商品金额(商品价格乘以数量的总金额)*/
	public Double orderAmount = 0D;
	/**实付金额*/
	private Double payment = 0D;
	/**邮费*/
	private Double shippingCharge = 0D;

	/**订单级的优惠金额*/
	private Double orderDiscount = 0D;
	/**总优惠,计算方式为payment(实付金额) - shippingCharge(邮费) - orderAmount(商品金额)*/
	private Double totalAdjustment = 0D;
    
    /**发票信息*/
    private Invoice invoice;
    
    /**买家信息*/
    private User shopper;
    /**收货人地址*/
    private Address receiverAddress;
    
    /**拆单发货,默认为非拆单发货*/
    private boolean split = false;
    
    /**子订单信息*/
    private List<OrderItem> orderItems = new ArrayList<OrderItem>(2);
    
    /**存放一些额外的参数,供扩展使用*/
	private Map<String,Object> additionalParams = new HashMap<String, Object>();
	
	/**奇门订单全链路接口参数*/
	private QimenOrderAllocation qimenOrderAllocation;

	/**
	 * 无需物流（虚拟）发货处理
	 * add by ywu 2016-06-16
	 */
	private boolean dummy;
	
	/**
	 * 预约退款的处理状况
	 */
	private Long advanceStatus;
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getChannelOrderId() {
		return channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}
	
	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Double getCodPrice() {
		return codPrice;
	}

	public void setCodPrice(Double codPrice) {
		this.codPrice = codPrice;
	}

	public String getCalculateShareDiscount() {
		return calculateShareDiscount;
	}

	public void setCalculateShareDiscount(String calculateShareDiscount) {
		this.calculateShareDiscount = calculateShareDiscount;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public String getSellerMemo() {
		return sellerMemo;
	}

	public void setSellerMemo(String sellerMemo) {
		this.sellerMemo = sellerMemo;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Double getShippingCharge() {
		return shippingCharge;
	}

	public void setShippingCharge(Double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public Double getOrderDiscount() {
		return orderDiscount;
	}

	public void setOrderDiscount(Double orderDiscount) {
		this.orderDiscount = orderDiscount;
	}
	
	public Double getTotalAdjustment() {
		return totalAdjustment;
	}

	public void setTotalAdjustment(Double totalAdjustment) {
		this.totalAdjustment = totalAdjustment;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public User getShopper() {
		return shopper;
	}

	public void setShopper(User shopper) {
		this.shopper = shopper;
	}

	public Address getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(Address receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public String getStepStatus() {
		return stepStatus;
	}

	public void setStepStatus(String stepStatus) {
		this.stepStatus = stepStatus;
	}

	public Double getStepPaidFee() {
		return stepPaidFee;
	}

	public void setStepPaidFee(Double stepPaidFee) {
		this.stepPaidFee = stepPaidFee;
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
	
	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	
	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}
	
	public boolean isDummy() {
		return dummy;
	}

	public void setDummy(boolean dummy) {
		this.dummy = dummy;
	}
	
	public QimenOrderAllocation getQimenOrderAllocation() {
		return qimenOrderAllocation;
	}

	public void setQimenOrderAllocation(QimenOrderAllocation qimenOrderAllocation) {
		this.qimenOrderAllocation = qimenOrderAllocation;
	}
	
	public Long getAdvanceStatus() {
		return advanceStatus;
	}

	public void setAdvanceStatus(Long advanceStatus) {
		this.advanceStatus = advanceStatus;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", channelOrderId="
				+ channelOrderId + ", createTime=" + createTime + ", payTime="
				+ payTime + ", modified=" + modified + ", endTime=" + endTime
				+ ", cancelTime=" + cancelTime + ", type=" + type
				+ ", tradeFrom=" + tradeFrom + ", status=" + status
				+ ", statusDesc=" + statusDesc + ", stepStatus=" + stepStatus
				+ ", stepPaidFee=" + stepPaidFee + ", payType=" + payType
				+ ", codPrice=" + codPrice + ", calculateShareDiscount="
				+ calculateShareDiscount + ", shippingCompany="
				+ shippingCompany + ", shippingType=" + shippingType
				+ ", shippingId=" + shippingId + ", sellerMemo=" + sellerMemo
				+ ", buyerMessage=" + buyerMessage + ", orderAmount="
				+ orderAmount + ", payment=" + payment + ", shippingCharge="
				+ shippingCharge + ", orderDiscount=" + orderDiscount
				+ ", totalAdjustment=" + totalAdjustment + ", invoice="
				+ invoice + ", shopper=" + shopper + ", receiverAddress="
				+ receiverAddress + ", split=" + split + ", orderItems="
				+ orderItems + ", additionalParams=" + additionalParams
				+ ", qimenOrderAllocation=" + qimenOrderAllocation + ", dummy="
				+ dummy + "]";
	}

	
	

	
}
