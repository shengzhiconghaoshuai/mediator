package net.chinacloud.mediator.jingdong.product;

import java.util.List;

public class JDProduct {

	private String channelProductId;
	
    private String itemNum;
    
    private String status;
    
    private String title;

    private List<com.jd.open.api.sdk.domain.ware.Sku> sku;
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<com.jd.open.api.sdk.domain.ware.Sku> getSku() {
		return sku;
	}

	public void setSku(List<com.jd.open.api.sdk.domain.ware.Sku> sku) {
		this.sku = sku;
	}

	public String getChannelProductId() {
		return channelProductId;
	}

	public void setChannelProductId(String channelProductId) {
		this.channelProductId = channelProductId;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
    
	
}
