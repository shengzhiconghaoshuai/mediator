package net.chinacloud.mediator.qimen.request;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.chinacloud.mediator.qimen.domain.QimenStore;
import net.chinacloud.mediator.qimen.response.QimenStoreinventoryIteminitialResponse;

@XStreamAlias("request") 
public class QimenStoreinventoryIteminitialRequest extends QimenRequest<QimenStoreinventoryIteminitialResponse>{
	
	@XStreamAlias("userId") 
	private Long userId;//淘宝id
	@XStreamAlias("operationTime") 
	private String operationTime;//操作时间
	@XStreamAlias("stores")
	private List<QimenStore> stores;//
	
	@Override
	public String getCustomId() {
		return String.valueOf(userId);
	}

	@Override
	public String getApiMethodName() {
		return "taobao.qimen.storeinventory.iteminitial";
	}

	@Override
	public Class<QimenStoreinventoryIteminitialResponse> getResponseClass() {
		return QimenStoreinventoryIteminitialResponse.class;
	}

	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	public List<QimenStore> getStores() {
		return stores;
	}

	public void setStores(List<QimenStore> stores) {
		this.stores = stores;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	
	
 
}
  