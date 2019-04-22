package net.chinacloud.mediator.domain;

import java.util.Date;

public class ExchangeMessage {
	/**换货单号ID*/
	private Long disputeId;
	/**留言创建时间*/
	private Date created;
	/**留言ID*/
	private Long id;
	/**留言者ID*/
	private Long ownerId;
	/**留言者昵称*/
	private String  ownerNick;
	/**留言内容*/
	private String content;
	/**留言类型：系统或是人工*/
	private String messageType;
	/**留言者角色*/
	private String ownerRole;
	public Long getDisputeId() {
		return disputeId;
	}
	public void setDisputeId(Long disputeId) {
		this.disputeId = disputeId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerNick() {
		return ownerNick;
	}
	public void setOwnerNick(String ownerNick) {
		this.ownerNick = ownerNick;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getOwnerRole() {
		return ownerRole;
	}
	public void setOwnerRole(String ownerRole) {
		this.ownerRole = ownerRole;
	}
	@Override
	public String toString() {
		return "ExchangeMessage [disputeId=" + disputeId + ", created="
				+ created + ", id=" + id + ", ownerId=" + ownerId
				+ ", ownerNick=" + ownerNick + ", content=" + content
				+ ", messageType=" + messageType + ", ownerRole=" + ownerRole
				+ "]";
	}
	

}
