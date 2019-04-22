package net.chinacloud.mediator.qimen.request;

import net.chinacloud.mediator.qimen.domain.OrderCode;
import net.chinacloud.mediator.qimen.response.SyncOrderResponse;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Octopus8 on 2017/5/16.
 */
@XStreamAlias("request")
public class SyncOrderRequest extends QimenRequest<SyncOrderResponse> {
	
	private String sellerCode;
	//淘系主订单号
	private Long parentOrderCode;
	//淘系子订单号
	@XStreamAlias("orderCodes") 
	private OrderCode orderCodes;
	//目标门店的商户中心门店编码
	private Long storeId;
	//业务类型，String，条件必填（门店发货、门店自提相关状态下
	private String type;
	//订单状态
	private String status;
	//操作人
	private String operator;
	
	//事件发生时间（yyyy-MM-dd HH:mm:ss），必填
	private String actionTime;
	
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	public Long getParentOrderCode() {
		return parentOrderCode;
	}
	public void setParentOrderCode(Long parentOrderCode) {
		this.parentOrderCode = parentOrderCode;
	}
	public OrderCode getOrderCodes() {
		return orderCodes;
	}
	public void setOrderCodes(OrderCode orderCodes) {
		this.orderCodes = orderCodes;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getActionTime() {
		return actionTime;
	}
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}
	@Override
	public String getCustomId() {
		return this.sellerCode;
	}
	@Override
	public String getApiMethodName() {
		return "taobao.qimen.orderStatus.sync";
	}
	@Override
	public Class<SyncOrderResponse> getResponseClass() {
		return SyncOrderResponse.class;
	}

}
