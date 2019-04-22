/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductService.java
 * 描述： 商品接口
 */
package net.chinacloud.mediator.service;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.product.ProductException;

/**
 * <商品接口>
 * <商品相关接口定义>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public interface ProductService {
	/**
	 * 根据商品的渠道编码获取其对应的外部编码
	 * @param channelProductId 渠道商品(款级)编号
	 * @return 商品(款级)外部编码
	 * @throws ProductException
	 */
	public String getOuterProductIdByChannelProductId(String channelProductId) 
			throws ProductException;
	/**
	 * 根据商品的外部编码获取其对应的渠道编码
	 * @param outerProductId
	 * @return
	 */
	public String getChannelProductIdByOuterProductId(String outerProductId);
	/**
	 * 获取sku的外部商家编码
	 * @param channelSkuId 渠道sku编码
	 * @param channelProductId 渠道sku所属款的编码
	 * @return sku的外部商家编码
	 * @throws ProductException
	 */
	public String getOuterSkuIdByChannelSkuId(String channelSkuId, String channelProductId)
			throws ProductException;
	/**
	 * 根据sku外部商家编码获取渠道sku编码
	 * @param OuterSkuId
	 * @return
	 */
	public String getChannelSkuIdByOuterSkuId(String OuterSkuId);
	/***
	 * 更新sku库存,单个sku更新,以sku为单位
	 * @param sku sku库存信息
	 * @throws ProductException
	 */
	public void updateSkuInventory(Sku sku) throws ProductException;
	/**
	 * 批量更新库存
	 * @param skus sku列表
	 * @throws ProductException
	 */
	public void updateSkuInventoryBatch(List<Sku> skus) throws ProductException;
	/**
	 * 批量更新库存,以款为单位
	 * @param product 款-sku
	 * @throws ProductException
	 */
	public void updateSkuInventoryBatch(Product product) throws ProductException;
	/**
	 * 创建商品
	 * @param product Map结构,渠道的属性会变,想不出有什么合适的结构
	 * map的key映射到渠道的属性,值可以是手动填写的,也可以是映射到渠道的属性值
	 * map为两层结构,一层为商品的信息,二层为sku的List结构信息,解析是需要判断value的类型是否是List,list中
	 * 存放的也是一个Map结构的sku信息
	 * 商品创建步骤：
	 * 1、根据map中的类目CODE,到CATEGORY_MAPPING表中取得渠道的category id;
	 * 2、如果是天猫渠道,需要根据category id 到TMALL_PRODUCTCAT_MAPPING表中获取产品id
	 * 3、获取商品上传时需要填写的属性,到CHANNEL_ATTRIBUTE_MAPPING表中获取渠道属性对应的系统属性的key,然后从map中
	 * 取得属性值,如果是必填属性,而map中没有获取到,则停止发布商品,返回xxx属性未填写信息,如果是非必填属性,而map中没有获取到,则再
	 * 查看CHANNEL_ATTRIBUTE_MAPPING表中有没有配置默认值;如果属性值是从渠道提供的属性值列表中选择的,则需要根据map中取得
	 * 的系统的属性值,到CHANNEL_ATTRVAL_MAPPING表中获取对应的渠道的属性值再填写
	 */
	public void createProduct(Map<String,Object> product) throws ProductException;
	/**
	 * 更新商品信息
	 * 步骤同创建商品
	 * 需要获取渠道商品、sku id时,根据外部商家编号到PRODUCT_PARTNUMBER_MAPPING表中获取
	 * 更新时可能会增加sku
	 * @param product
	 */
	public void updateProduct(Map<String,Object> product) throws ProductException;
	/**
	 * 商品上架
	 * @param product
	 */
	public void onShelf(Product product) throws ProductException;
	/**
	 * 商品下架
	 * @param product
	 */
	public void offShelf(Product product) throws ProductException;
	/**
	 * 更新商品销售价格,款&sku
	 * @param product
	 * @throws ProductException 
	 */
	public void updatePrice(Product product) throws ProductException;
	/**
	 * 获取所有的运费模板信息
	 * @return
	 */
	public List<DeliveryTemplate> getDeliveryTemplates();
	/**
	 * 获取渠道的属性信息
	 * @param category
	 * @return
	 * @throws ProductException
	 */
	public Category getChannelProperty(Category category) throws ProductException;
	
	/**
	 * 获取所有类目
	 */
	public List<CategoryList> getAllCategory() throws ProductException;
	
	/**
	 * 获取渠道所有商品信息
	 * @param params
	 * @return
	 */
	public List<Product> getTotalProducts(Map<String, Object> params);
	/**
	 * 获取在售商品信息
	 * @param params
	 * @return
	 */
	public List<Product> getOnSaleProducts(Map<String, Object> params);
	/**
	 * 获取仓库中的商品信息
	 * @param params
	 * @return
	 */
	public List<Product> getInstockProducts(Map<String, Object> params);
	/**
	 * 判断渠道中是否存在重复的outerId
	 * @param productInfo
	 * @return
	 */
	public Map<String,String> isRepeatOuterId(Product productInfo);
	/**
	 * 全渠道绑定商品
	 * @param product
	 */
	public void BindItemStore(Product product) throws ProductException ;
	
	/**
	 * 获取sku_id所对应的outer_id sku_id对应的sku要属于传入的nick对应的卖家
	 * outer_id:商家设置的外部id。天猫和集市的卖家，需要登录才能获取到自己的商家编码，不能获取到他人的商家编码。
	 * @throws ProductException 
	 */
	public String getOuterIdbySkuIdandNick(String skuId) throws ProductException;
}
