package net.chinacloud.mediator.domain;

public class QimenEventProduce {
	
	private String qimemStatus;//事件状态，如QIMEN_ERP_TRANSFER，QIMEN_ERP_CHECK
	private String channelOrderId;//淘宝订单号
	private String ext;//JSON格式扩展字段 
	private String platform;//商家平台编码.MAIN:官方渠道,JD:京东,DD:当当,PP:拍拍,YX:易讯,EBAY:ebay,AMAZON:亚马逊,SN:苏宁,GM:国美,WPH:唯品会,JM:聚美,MGJ:蘑菇街,YT:银泰,YHD:1号店,1688:1688,POS:POS门店,OTHER:其他
	private Long create;//订单创建时间,数字,如：20170808
	private String nick;//外部商家名称。必须同时填写platform
	
	
	public String getQimemStatus() {
		return qimemStatus;
	}
	public void setQimemStatus(String qimemStatus) {
		this.qimemStatus = qimemStatus;
	}
	public String getChannelOrderId() {
		return channelOrderId;
	}
	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public Long getCreate() {
		return create;
	}
	public void setCreate(Long create) {
		this.create = create;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	
}
