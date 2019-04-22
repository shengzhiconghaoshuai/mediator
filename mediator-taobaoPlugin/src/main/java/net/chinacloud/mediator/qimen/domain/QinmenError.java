package net.chinacloud.mediator.qimen.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("error") 
public class QinmenError {
	@XStreamAsAttribute
	private String description;
	@XStreamAsAttribute
	private String failedBillNum;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFailedBillNum() {
		return failedBillNum;
	}
	public void setFailedBillNum(String failedBillNum) {
		this.failedBillNum = failedBillNum;
	}
	
	

}
