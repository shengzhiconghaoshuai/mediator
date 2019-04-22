/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ReturnItem.java
 * 描述： 退货明细
 */
package net.chinacloud.mediator.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * <退货明细>
 * <退货明细>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public class ReturnItem {
	/**渠道退货明细id*/
	private String channelReturnItemId;
	/**渠道子订单id*/
	private String channelOrderItemId;
	/**渠道sku编码*/
	private String channelSkuId;
	/**sku外部商家编码*/
	private String outerSkuId;
	/**退货数量*/
	private Double quantity;
	/**商品单价*/
	private Double price;
	/**存放一些额外的参数,供扩展使用*/
	private Map<String,Object> additionalParams = new HashMap<String, Object>();
	
	public String getChannelReturnItemId() {
		return channelReturnItemId;
	}
	public void setChannelReturnItemId(String channelReturnItemId) {
		this.channelReturnItemId = channelReturnItemId;
	}
	public String getChannelOrderItemId() {
		return channelOrderItemId;
	}
	public void setChannelOrderItemId(String channelOrderItemId) {
		this.channelOrderItemId = channelOrderItemId;
	}
	public String getChannelSkuId() {
		return channelSkuId;
	}
	public void setChannelSkuId(String channelSkuId) {
		this.channelSkuId = channelSkuId;
	}
	public String getOuterSkuId() {
		return outerSkuId;
	}
	public void setOuterSkuId(String outerSkuId) {
		this.outerSkuId = outerSkuId;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
	@Override
	public String toString() {
		return "ReturnItem [channelReturnItemId=" + channelReturnItemId
				+ ", channelOrderItemId=" + channelOrderItemId
				+ ", channelSkuId=" + channelSkuId + ", outerSkuId="
				+ outerSkuId + ", quantity=" + quantity + ", price=" + price
				+ ", additionalParams=" + additionalParams + "]";
	}
}
