/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelPictureMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

/**
 * @description 淘宝图片
 * @author huangxitao
 * @since 2015年7月15日 上午10:52:21
 */
public class ChannelPictureMapping {
	/**主键*/
	private Integer id;
	/**渠道商品ID*/
	private String channelProductId;
	/**图片id*/
	private String imageId;
	/**图片位置*/
	private Integer sort;
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
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
}
