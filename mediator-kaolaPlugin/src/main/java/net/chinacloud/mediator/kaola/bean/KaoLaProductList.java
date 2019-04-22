package net.chinacloud.mediator.kaola.bean;

import java.util.List;

public class KaoLaProductList {
	private String message;
	private String errorCode;
	private boolean success;
	private List<KaoLaProduct> item_list;

	public List<KaoLaProduct> getItem_list() {
		return item_list;
	}

	public void setItem_list(List<KaoLaProduct> item_list) {
		this.item_list = item_list;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}


	
	

}
