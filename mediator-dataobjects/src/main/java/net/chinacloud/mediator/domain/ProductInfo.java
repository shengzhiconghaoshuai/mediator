/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductInfo.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 商品信息
 * @author yejunwu123@gmail.com
 * @since 2015年7月29日 下午2:48:48
 */
public class ProductInfo {
	/**商品数量*/
	private Double quantity;
	/**商品吊牌价格*/
	private Double listPrice;
	/**商品销售价格*/
	private Double salesPrice;
	/**发布类型.可选值:fixed(一口价),auction(拍卖)*/
	private String type = "fixed";
	/**新旧程度.可选值：new(新),second(二手)*/
	private String stuffStatus = "new";
	/**商品标题*/
	private String title;
	/**无线短标题*/
	private String wirelessShortTitle;
	/**商品长描述*/
	private String desc;
	/**所在地省份*/
	private String locationState;
	/**所在地城市*/
	private String locationCity;
	/**商品上传后的状态.可选值:onsale(出售中),instock(仓库中);默认值:onsale*/
	private String approveStatus = "instock";
	/**叶子类目id */
	private String categoryId;
	/**主图地址*/
	private String image;
	/**运费承担方式。
	 * 可选值:seller（卖家承担）,buyer(买家承担);默认值:seller.
	 * 卖家承担不用设置邮费和postage_id.买家承担的时候，必填邮费和postage_id
	 * 如果用户设置了运费模板会优先使用运费模板,否则要同步设置邮费（post_fee,express_fee,ems_fee） */
	private String freightPayer;
	/**商品所属的运费模板ID*/
	private String postageId;
	/**商品渠道编码*/
	private String channelProductId;
	/**商品外部编码*/
	private String outerId;
	/**商品的重量*/
	private Double weight;
	/**长(单位:mm)*/
	private String length;
	/**宽(单位:mm)*/
	private String width;
	/**高(单位:mm)*/
	private String height;
	/**1:拍下减库存;2:付款减库存;0:默认不更改*/
	private String subStock = "2";
	/**sku信息*/
	private List<SkuInfo> skuInfos = new ArrayList<SkuInfo>();
	/**商品属性、属性值串*/
	private Map<String, String> properties;
	/**sku的属性、属性值串*/
	private Map<String, String> skuProperties;
	
	private Map<String, String> params = new HashMap<String, String>();
	
	private String image0;
	private String image1;
	private String image2;
	private String image3;
	
	/**重新发布*/
	private boolean rePublish;
	/**是否橱窗推荐*/
	private boolean showCase;
	
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getListPrice() {
		return listPrice;
	}
	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}
	public Double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStuffStatus() {
		return stuffStatus;
	}
	public void setStuffStatus(String stuffStatus) {
		this.stuffStatus = stuffStatus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLocationState() {
		return locationState;
	}
	public void setLocationState(String locationState) {
		this.locationState = locationState;
	}
	public String getLocationCity() {
		return locationCity;
	}
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getFreightPayer() {
		return freightPayer;
	}
	public void setFreightPayer(String freightPayer) {
		this.freightPayer = freightPayer;
	}
	public String getPostageId() {
		return postageId;
	}
	public void setPostageId(String postageId) {
		this.postageId = postageId;
	}
	public String getOuterId() {
		return outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getSubStock() {
		return subStock;
	}
	public void setSubStock(String subStock) {
		this.subStock = subStock;
	}
	public List<SkuInfo> getSkuInfos() {
		return skuInfos;
	}
	public void setSkuInfos(List<SkuInfo> skuInfos) {
		this.skuInfos = skuInfos;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	public Map<String, String> getSkuProperties() {
		return skuProperties;
	}
	public void setSkuProperties(Map<String, String> skuProperties) {
		this.skuProperties = skuProperties;
	}
	public String getImage0() {
		return image0;
	}
	public void setImage0(String image0) {
		this.image0 = image0;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public String getImage3() {
		return image3;
	}
	public void setImage3(String image3) {
		this.image3 = image3;
	}
	public void addParam(String key, String value) {
		this.params.put(key, value);
	}
	public String getParam(String key) {
		return this.params.get(key);
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getWirelessShortTitle() {
		return wirelessShortTitle;
	}
	public void setWirelessShortTitle(String wirelessShortTitle) {
		this.wirelessShortTitle = wirelessShortTitle;
	}
	public boolean isRePublish() {
		return rePublish;
	}
	public void setRePublish(boolean rePublish) {
		this.rePublish = rePublish;
	}
	public boolean isShowCase() {
		return showCase;
	}
	public void setShowCase(boolean showCase) {
		this.showCase = showCase;
	}
	public String getChannelProductId() {
		return channelProductId;
	}
	public void setChannelProductId(String channelProductId) {
		this.channelProductId = channelProductId;
	}
	
}
