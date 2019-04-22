/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderItem.java
 * 描述： 子订单数据结构
 */
package net.chinacloud.mediator.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <子订单>
 * <订单项数据结构>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月8日
 * @since 2014年12月8日
 */
public class OrderItem {
	
	//-----------子订单状态,个渠道的子订单状态需要统一映射成对应的状态(基本按照淘宝状态定义,但并非与淘宝渠道耦合)-----------
	/**已付款,等待卖家发货*/
	public static final String STATUS_WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";
	public static final String STATUS_WAIT_SELLER_SEND_GOODS_DESC = "\u4e70\u5bb6\u5df2\u4ed8\u6b3e";
	/**卖家已发货,等待买家收货*/
	public static final String STATUS_WAIT_BUYER_CONFIRM_GOODS = "WAIT_BUYER_CONFIRM_GOODS";
	public static final String STATUS_WAIT_BUYER_CONFIRM_GOODS_DESC = "\u5356\u5bb6\u5df2\u53d1\u8d27";
	/**买家已签收,货到付款专用*/
	public static final String STATUS_BUYER_SIGNED = "BUYER_SIGNED";
	public static final String STATUS_BUYER_SIGNED_DESC = "\u4e70\u5bb6\u5df2\u7b7e\u6536";
	/**交易正常成功(支付-发货-确认收货)*/
	public static final String STATUS_FINISHED = "FINISHED";
	public static final String STATUS_FINISHED_DESC = "\u4ea4\u6613\u6210\u529f";
	/**付款以后用户退款成功,交易自动关闭*/
	public static final String STATUS_CLOSED = "CLOSED";
	public static final String STATUS_CLOSED_DESC = "\u4ea4\u6613\u5173\u95ed";
	
	/**内部系统子订单号(订单项id)*/
	private Long orderItemId;
	/**渠道子订单id*/
	private String channelOrderItemId;
	/**sku的外部商家编码,即partnumber*/
	private String outerSkuId;
	/**渠道sku编码,有些订单接口没有返回外部商家编码,需要根据渠道sku编码再去获取外部编码*/
	private String channelSkuId;
	/**商品的外部商家编码,主要用于补邮费、补差价,这种情况Order接口返回的是商品编码,不是sku编码*/
	private String outerProductId;
	/**渠道商品编码*/
	private String channelProductId;
	/**商品数量*/
	private Long quantity;
	/**商品价格(单价)*/
	private Double price;
	/**子订单实付金额*/
	private Double payment;
	/**订单项级别的优惠金额*/
	private Double itemDiscount = 0D;
	/**是否超卖*/
	private Boolean overSold = false;
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
	/**子订单状态*/
	private String status;
	/**子订单状态描述*/
	private String statusDesc;
	/**发货时间*/
	private Date deliveryTime;
	/**交易成功时间*/
	private Date endTime;
	/**商品标题*/
	private String title;
	/**优惠分摊金额*/
	private Double sharedDiscount = 0D;
	/**存放一些额外的参数,供扩展使用*/
	private Map<String,Object> additionalParams = new HashMap<String, Object>();
	
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getChannelOrderItemId() {
		return channelOrderItemId;
	}
	public void setChannelOrderItemId(String channelOrderItemId) {
		this.channelOrderItemId = channelOrderItemId;
	}
	public String getOuterSkuId() {
		return outerSkuId;
	}
	public void setOuterSkuId(String outerSkuId) {
		this.outerSkuId = outerSkuId;
	}
	public String getChannelSkuId() {
		return channelSkuId;
	}
	public void setChannelSkuId(String channelSkuId) {
		this.channelSkuId = channelSkuId;
	}
	public String getOuterProductId() {
		return outerProductId;
	}
	public void setOuterProductId(String outerProductId) {
		this.outerProductId = outerProductId;
	}
	public String getChannelProductId() {
		return channelProductId;
	}
	public void setChannelProductId(String channelProductId) {
		this.channelProductId = channelProductId;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getItemDiscount() {
		return itemDiscount;
	}
	public void setItemDiscount(Double itemDiscount) {
		this.itemDiscount = itemDiscount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Map<String, Object> getAdditionalParams() {
		return additionalParams;
	}
	public void setAdditionalParams(Map<String, Object> additionalParams) {
		this.additionalParams = additionalParams;
	}
	public Object getAdditionalParam(String key) {
		return this.additionalParams.get(key);
	}
	public void addAdditionalParam(String key, Object value) {
		this.additionalParams.put(key, value);
	}
	public Boolean getOverSold() {
		return overSold;
	}
	public void setOverSold(Boolean overSold) {
		this.overSold = overSold;
	}
	public String getShippingCompany() {
		return shippingCompany;
	}
	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}
	public String getShippingType() {
		return shippingType;
	}
	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}
	public String getShippingId() {
		return shippingId;
	}
	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getPayment() {
		return payment;
	}
	public void setPayment(Double payment) {
		this.payment = payment;
	}
	public Double getSharedDiscount() {
		return sharedDiscount;
	}
	public void setSharedDiscount(Double sharedDiscount) {
		this.sharedDiscount = sharedDiscount;
	}
	
	@Override
	public String toString() {
		return "OrderItem [orderItemId=" + orderItemId
				+ ", channelOrderItemId=" + channelOrderItemId
				+ ", outerSkuId=" + outerSkuId + ", channelSkuId="
				+ channelSkuId + ", outerProductId=" + outerProductId
				+ ", channelProductId=" + channelProductId + ", quantity="
				+ quantity + ", price=" + price + ", payment=" + payment
				+ ", itemDiscount=" + itemDiscount + ", overSold=" + overSold
				+ ", shippingCompany=" + shippingCompany + ", shippingType="
				+ shippingType + ", shippingId=" + shippingId + ", status="
				+ status + ", statusDesc=" + statusDesc + ", deliveryTime="
				+ deliveryTime + ", endTime=" + endTime + ", title=" + title
				+ ", sharedDiscount=" + sharedDiscount + ", additionalParams="
				+ additionalParams + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((channelOrderItemId == null) ? 0 : channelOrderItemId
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItem other = (OrderItem) obj;
		if (channelOrderItemId == null) {
			if (other.channelOrderItemId != null)
				return false;
		} else if (!channelOrderItemId.equals(other.channelOrderItemId))
			return false;
		return true;
	}
	
}
