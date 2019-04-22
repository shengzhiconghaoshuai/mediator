/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CategoryMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

/**
 * @description 渠道主目录映射
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午4:48:48
 */
public class CategoryMapping {
	/**主键*/
	private Integer id;
	/**渠道叶子目录id*/
	private Long channelCategoryId;
	/**渠道叶子目录名称*/
	private String channelCategoryName;
	/**系统叶子目录id*/
	private Long categoryId;
	/**系统叶子目录名称*/
	private String categoryName;
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
	public String getChannelCategoryName() {
		return channelCategoryName;
	}
	public void setChannelCategoryName(String channelCategoryName) {
		this.channelCategoryName = channelCategoryName;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
		result = prime * result
				+ ((categoryId == null) ? 0 : categoryId.hashCode());
		result = prime
				* result
				+ ((channelCategoryId == null) ? 0 : channelCategoryId
						.hashCode());
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
		CategoryMapping other = (CategoryMapping) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (categoryId == null) {
			if (other.categoryId != null)
				return false;
		} else if (!categoryId.equals(other.categoryId))
			return false;
		if (channelCategoryId == null) {
			if (other.channelCategoryId != null)
				return false;
		} else if (!channelCategoryId.equals(other.channelCategoryId))
			return false;
		return true;
	}
	
}
