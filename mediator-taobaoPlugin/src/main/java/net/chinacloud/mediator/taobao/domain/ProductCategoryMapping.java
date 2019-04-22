/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductCategoryMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.domain;

/**
 * @description 天猫产品id与主目录叶子类目id映射
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午4:55:29
 */
public class ProductCategoryMapping {
	/**主键*/
	private Integer id;
	/**渠道主目录叶子类目id*/
	private Long channelCategoryId;
	/**渠道产品id*/
	private Long productId;
	/**应用id*/
	private Integer applicationId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getChannelCategoryId() {
		return channelCategoryId;
	}
	public void setChannelCategoryId(Long channelCategoryId) {
		this.channelCategoryId = channelCategoryId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
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
				+ ((channelCategoryId == null) ? 0 : channelCategoryId
						.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
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
		ProductCategoryMapping other = (ProductCategoryMapping) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (channelCategoryId == null) {
			if (other.channelCategoryId != null)
				return false;
		} else if (!channelCategoryId.equals(other.channelCategoryId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
	
}
