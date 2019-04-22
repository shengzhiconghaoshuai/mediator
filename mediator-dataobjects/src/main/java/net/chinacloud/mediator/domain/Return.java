/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Return.java
 * 描述： 退货
 */
package net.chinacloud.mediator.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <退货>
 * <退货数据结构>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class Return {
	/**渠道退货单id*/
	private String channelReturnId;
	/**渠道订单号*/
	private String channelOrderId;
	/**退货原因*/
	private String returnReason;
	/**问题描述*/
	private String description;
	/**退款商品金额,不含运费*/
	private Double refundProductAmount;
	/**退款运费*/
	private Double refundDeliveryFee;
	/**退款总额*/
	private Double refundAmount;
	/**状态*/
	private String status;
	/**退货明细*/
	private List<ReturnItem> returnItems;
	/**存放一些额外的参数,供扩展使用*/
	private Map<String,Object> additionalParams = new HashMap<String, Object>();
	
	public String getChannelReturnId() {
		return channelReturnId;
	}
	public void setChannelReturnId(String channelReturnId) {
		this.channelReturnId = channelReturnId;
	}
	public String getChannelOrderId() {
		return channelOrderId;
	}
	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getRefundProductAmount() {
		return refundProductAmount;
	}
	public void setRefundProductAmount(Double refundProductAmount) {
		this.refundProductAmount = refundProductAmount;
	}
	public Double getRefundDeliveryFee() {
		return refundDeliveryFee;
	}
	public void setRefundDeliveryFee(Double refundDeliveryFee) {
		this.refundDeliveryFee = refundDeliveryFee;
	}
	public Double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public List<ReturnItem> getReturnItems() {
		return returnItems;
	}
	public void setReturnItems(List<ReturnItem> returnItems) {
		this.returnItems = returnItems;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Return [channelReturnId=" + channelReturnId
				+ ", channelOrderId=" + channelOrderId + ", returnReason="
				+ returnReason + ", description=" + description
				+ ", refundProductAmount=" + refundProductAmount
				+ ", refundDeliveryFee=" + refundDeliveryFee
				+ ", refundAmount=" + refundAmount + ", status=" + status
				+ ", returnItems=" + returnItems + ", additionalParams="
				+ additionalParams + "]";
	}
}
