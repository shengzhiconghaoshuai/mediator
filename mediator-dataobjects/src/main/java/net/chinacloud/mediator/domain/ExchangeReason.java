package net.chinacloud.mediator.domain;
/**
 * 天猫换货，获取的原因及对应的id  实体类
 * @author liling
 *
 */
public class ExchangeReason {
	/**拒绝原因ID*/
	private Long reasonId;
	/**拒绝原因内容*/
	private String reasonText;
	
	public Long getReasonId() {
		return reasonId;
	}
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}
	public String getReasonText() {
		return reasonText;
	}
	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}
	@Override
	public String toString() {
		return "ExchangeReason [reasonId=" + reasonId + ", reasonText="
				+ reasonText + "]";
	}
	
	

}
