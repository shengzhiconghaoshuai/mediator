package net.chinacloud.mediator.qimen.request;

import net.chinacloud.mediator.qimen.domain.StoreId;
import net.chinacloud.mediator.qimen.response.ItemStoreBanDingResponse;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("request") 
public class ItemStoreBanDingRequest extends QimenRequest<ItemStoreBanDingResponse>{
	
	
	@Override
	public String getCustomId() {
		return null;
	}

	@Override
	public String getApiMethodName() {
		return "taobao.qimen.itemstore.banding";
	}

	@Override
	public Class<ItemStoreBanDingResponse> getResponseClass() {
		return ItemStoreBanDingResponse.class;
	}
	
	private String actionType; //操作类型,ADD=新建，DELETE=删除，必填
	
	private String remark; //备注
	
	private Long itemId; //线上商品ID,必填
	
	@XStreamAlias("storeIds") 
	private StoreId storeIds;

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public StoreId getStoreIds() {
		return storeIds;
	}

	public void setStoreIds(StoreId storeIds) {
		this.storeIds = storeIds;
	}

}
