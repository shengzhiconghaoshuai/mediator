/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductPartnumberMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

/**
 * @description 渠道商品编号与外部商家编码映射
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午5:02:28
 */
public class ProductPartnumberMapping {
	/**主键id*/
	private Integer id;
	/**渠道商品或sku id,由type决定*/
	private String channelProductId;
	/**商品或sku商家编码,由type决定*/
	private String outerId;
	/**商品或sku,0:商品,1:sku*/
	private int type;
	/**应用id*/
	private Integer applicationId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getChannelProductId() {
		return channelProductId;
	}
	public void setChannelProductId(String channelProductId) {
		this.channelProductId = channelProductId;
	}
	public String getOuterId() {
		return outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime
				* result
				+ ((channelProductId == null) ? 0 : channelProductId.hashCode());
		result = prime * result + ((outerId == null) ? 0 : outerId.hashCode());
		result = prime * result + type;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductPartnumberMapping other = (ProductPartnumberMapping) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (channelProductId == null) {
			if (other.channelProductId != null)
				return false;
		} else if (!channelProductId.equals(other.channelProductId))
			return false;
		if (outerId == null) {
			if (other.outerId != null)
				return false;
		} else if (!outerId.equals(other.outerId))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
