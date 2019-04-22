package net.chinacloud.mediator.domain;

import java.util.Date;
/**
 * 天猫换货，获取的换货信息  实体类
 * @author liling
 *
 */
public class Exchange {

	/**换货单号ID 目前支持*/
	private String disputeId;
	/**正向单号ID,网店主单号或子单号  目前支持*/
	private String bizOrderId;
	/**购买数  目前支持*/
	private Long num;
	/**买家昵称*/
	private String buyerNick;
	/**当前换货单状态*/
	private String status;
	/**换货单创建时间*/
	private Date created;
	/**换货单最新修改时间*/
	private Date modified;
	/**申请换货原因*/
	private String reason;
	/**商品名称*/
	private String title;
	/**买家发货物流公司*/
	private String buyerLogisticName;
	/**买家发货快递单号*/
	private String buyerLogisticNo;
	/**买家换货地址 信息*/
	private Address buyerAddress;
	/**卖家换货地址(待定感觉没有用)*/
	private String sellerAddress;
	/**卖家收货地址id，用于同意申请换货的请求参数 */
	private Long sellerAddressId;
	/**卖家发货快递公司*/
	private String sellerLogisticName;
	/**卖家发货快递单号*/
	private String sellerLogisticNo;
	/**拒绝换货原因对应ID*/
	private Long sellerRefuseReasonID;
	/**卖家昵称*/
	private String sellerNick;
	/**购买的商品sku*/
	private String boughtSku;
	/**买家申请换货的商品sku*/
	private String exchangeSku;
	/**支付宝ID*/
	private String alipayNo;
	/**超时时间*/
	private Date timeOut;
	/**当前商品状态*/
	private String goodStatus;
	/**卖家留言*/
	private String sellerMessage;
	/**买家留言*/
	private String buyerMessage;
	
	
	//====目前返回值不支持
	/**价格，目前api访问不支持*/
	private String price;
	
	public String getAlipayNo() {
		return alipayNo;
	}
	public void setAlipayNo(String alipayNo) {
		this.alipayNo = alipayNo;
	}
	public String getBizOrderId() {
		return bizOrderId;
	}
	public void setBizOrderId(String bizOrderId) {
		this.bizOrderId = bizOrderId;
	}
	public String getBoughtSku() {
		return boughtSku;
	}
	public void setBoughtSku(String boughtSku) {
		this.boughtSku = boughtSku;
	}
	public String getBuyerLogisticName() {
		return buyerLogisticName;
	}
	public void setBuyerLogisticName(String buyerLogisticName) {
		this.buyerLogisticName = buyerLogisticName;
	}
	public String getBuyerLogisticNo() {
		return buyerLogisticNo;
	}
	public void setBuyerLogisticNo(String buyerLogisticNo) {
		this.buyerLogisticNo = buyerLogisticNo;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getDisputeId() {
		return disputeId;
	}
	public void setDisputeId(String disputeId) {
		this.disputeId = disputeId;
	}
	public String getExchangeSku() {
		return exchangeSku;
	}
	public void setExchangeSku(String exchangeSku) {
		this.exchangeSku = exchangeSku;
	}
	public String getGoodStatus() {
		return goodStatus;
	}
	public void setGoodStatus(String goodStatus) {
		this.goodStatus = goodStatus;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSellerAddress() {
		return sellerAddress;
	}
	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}
	public String getSellerLogisticName() {
		return sellerLogisticName;
	}
	public void setSellerLogisticName(String sellerLogisticName) {
		this.sellerLogisticName = sellerLogisticName;
	}
	public String getSellerLogisticNo() {
		return sellerLogisticNo;
	}
	public void setSellerLogisticNo(String sellerLogisticNo) {
		this.sellerLogisticNo = sellerLogisticNo;
	}
	public String getSellerNick() {
		return sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(Date timeOut) {
		this.timeOut = timeOut;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Address getBuyerAddress() {
		return buyerAddress;
	}
	public void setBuyerAddress(Address buyerAddress) {
		this.buyerAddress = buyerAddress;
	}
	public Long getSellerAddressId() {
		return sellerAddressId;
	}
	public void setSellerAddressId(Long sellerAddressId) {
		this.sellerAddressId = sellerAddressId;
	}
	public String getSellerMessage() {
		return sellerMessage;
	}
	public void setSellerMessage(String sellerMessage) {
		this.sellerMessage = sellerMessage;
	}
	public String getBuyerMessage() {
		return buyerMessage;
	}
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	public Long getSellerRefuseReasonID() {
		return sellerRefuseReasonID;
	}
	public void setSellerRefuseReasonID(Long sellerRefuseReasonID) {
		this.sellerRefuseReasonID = sellerRefuseReasonID;
	}
	
	

}
