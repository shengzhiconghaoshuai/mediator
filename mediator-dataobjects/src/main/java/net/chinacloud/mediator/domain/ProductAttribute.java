/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductAttribute.java
 * 描述： 
 */
package net.chinacloud.mediator.domain;

/**
 * @description 商品属性
 * @author yejunwu123@gmail.com
 * @since 2015年6月30日 下午6:06:05
 */
public abstract class ProductAttribute {
	//============================PRODUCT============================
	/**商品数量*/
	public static final String QUANTIRY = "quantity";
	/**商品吊牌价格*/
	public static final String LIST_PRICE = "listPrice";
	/**商品销售价格*/
	public static final String SALES_PRICE = "salesPrice";
	/**发布类型.可选值:fixed(一口价),auction(拍卖)*/
	public static final String TYPE = "type";
	/**新旧程度.可选值：new(新),second(二手)*/
	public static final String STUFF_STATUS = "stuffStatus";
	/**商品标题*/
	public static final String TITLE = "title";
	/**无线短标题*/
	public static final String WIRELESS_SHORT_TITLE = "wirelessShortTitle";
	/**商品长描述*/
	public static final String DESCRIPTION = "desc";
	/**所在地省份*/
	public static final String LOCATION_STATE = "locationState";
	/**所在地城市*/
	public static final String LOCATION_CITY = "locationCity";
	/**商品上传后的状态.可选值:onsale(出售中),instock(仓库中);默认值:onsale*/
	public static final String APPROVE_STATUS = "approveStatus";
	/**系统叶子类目id */
	public static final String CATEGORY_ID = "categoryId";
	/**品牌id*/
	public static final String BRAND_ID = "brandId";
	/**主图地址*/
	public static final String MAIN_IMAGE = "mainImage";
	/**运费承担方式。
	 * 可选值:seller（卖家承担）,buyer(买家承担);默认值:seller.
	 * 卖家承担不用设置邮费和postage_id.买家承担的时候，必填邮费和postage_id
	 * 如果用户设置了运费模板会优先使用运费模板,否则要同步设置邮费（post_fee,express_fee,ems_fee） */
	public static final String FREIGHT_PAYER = "freightPayer";
	/**商品所属的运费模板ID*/
	public static final String POSTAGE_ID = "postageId";
	/**商品外部编码*/
	public static final String OUTER_ID = "outer_id";
	/**
	 * add by ywu@wuxicloud.com at 2016-05-23
	 * 渠道商品id
	 */
	public static final String CHANNEL_PRODUCT_ID = "channel_product_id";
	/**商品的重量*/
	public static final String WEIGHT = "weight";
	/**长(单位:mm)*/
	public static final String LENGTH = "length";
	/**宽(单位:mm)*/
	public static final String WIDTH = "width";
	/**高(单位:mm)*/
	public static final String HEIGHT = "height";
	/**1:拍下减库存;2:付款减库存;0:默认不更改*/
	public static final String SUB_STOCK = "subStock";
	/**商品属性、属性值串*/
	public static final String PROPERTIES = "properties";
	/**重新发布*/
	public static final String REPUBLISH = "rePublish";
	/**是否橱窗推荐*/
	public static final String SHOWCASE = "showCase";
	
	public static final String IMAGE_0 = "image0";
	public static final String IMAGE_1 = "image1";
	public static final String IMAGE_2 = "image2";
	public static final String IMAGE_3 = "image3";
	
	//============================SKU============================
	/**sku信息*/
	public static final String SKU = "sku";
	/**sku的属性、属性值串*/
	public static final String SKU_PROPERTIES = "skuProperties";
	/**颜色*/
	public static final String SKU_COLOR = "color";
	/**尺码*/
	public static final String SKU_SIZE = "size";
	/**数量*/
	public static final String SKU_QUANTITY = "skuQuantity";
	/**价格*/
	public static final String SKU_PRICE = "skuPrice";
	/**外部id*/
	public static final String SKU_OUTER_ID = "skuOuterId";
	/**属性图*/
	public static final String PROPERTY_IMAGE = "propertyImage";
	/**颜色别名*/
	public static final String COLOR_ALIAS = "colorAlias";
	/**尺码别名*/
	public static final String SIZE_ALIAS = "sizeAlias";
}
