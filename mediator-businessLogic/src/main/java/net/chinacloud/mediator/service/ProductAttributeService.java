/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductAttributeService.java
 * 描述： 
 */
package net.chinacloud.mediator.service;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.CategoryMapping;
import net.chinacloud.mediator.domain.ChannelAttributeValueMapping;
import net.chinacloud.mediator.domain.InventoryUpdateRemark;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.exception.product.ProductException;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午6:19:06
 */
public interface ProductAttributeService {
	/**
	 * 根据系统叶子类目id获取对应的类目id
	 * @param categoryId
	 * @param applicationId 应用id
	 * @return
	 */
	public CategoryMapping getCategoryMapping(Long categoryId, Integer applicationId);
	/**
	 * 
	 * @param outerId 商品/sku外部商家编号
	 * @param applicationId 应用id
	 * @param type 类型,0:商品,1:sku
	 * @return
	 */
	public ProductPartnumberMapping getProductPartnumberMappingByOuterId(String outerId, Integer applicationId, Integer type) throws ProductException;
	/**
	 * 根据渠道商品id获取外部商品/sku编号
	 * @param channelProductId 渠道商品id
	 * @param applicationId 应用id
	 * @param type 类型,0:product,1:sku
	 * @return
	 */
	public ProductPartnumberMapping getProductPartnumberMappingByChannelId(String channelProductId, Integer applicationId, Integer type);
	/**
	 * 保存商品、外部商家编码映射关系
	 * @param mapping
	 */
	public void saveProductPartnumberMapping(ProductPartnumberMapping mapping);
	/**
	 * 更新商品、外部商家编码映射关系
	 * @param mapping
	 */
	public void updateProductPartnumberMapping(ProductPartnumberMapping mapping);
	/**
	 * 根据渠道类目id、渠道属性编号、系统属性值获取渠道属性值
	 * @param channelCategoryId 渠道类目id
	 * @param channelAttributeId 渠道属性编号
	 * @param attributeValue 系统属性值
	 * @param applicationId 应用id
	 * @return
	 */
	public ChannelAttributeValueMapping getChannelAttributeValueMapping(Long channelCategoryId, String channelAttributeId, String attributeValue, Integer applicationId);

	/**
	 * 加载ChannelAttributeValueMapping
	 * @param applicationId
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public List<ChannelAttributeValueMapping> loadChannelAttrvalMapping(Map<String, Object> params);
	/**
	 * 统计
	 * @param applicationId
	 * @return
	 */
	public Long countChannelAttrvalMapping(Map<String, Object> params);

	/**
	 *加载CategoryMapping 
	 * @param applicationId
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public List<CategoryMapping> loadCaregroyMapping(Map<String, Object> params);
	/**
	 * 统计
	 * @param params
	 * @return
	 */
	public Long countCaregroyMapping(Map<String, Object> params);
	
	/**
	 * 保存categroyMapping
	 * @param categroyMapping
	 * @return
	 */
	public int saveCategroyMapping(CategoryMapping categroyMapping);
	
	/**
	 * 保存ChannelAttributeValueMapping
	 * @param channelAttrvalMapping
	 * @return
	 */
	public int saveChannelAttrvalMapping(ChannelAttributeValueMapping channelAttrvalMapping);
	
	/**
	 * 根据id删除CategroyMapping
	 * @param id
	 */
	public int deleteCategroyMapping(Integer id);
	
	/**
	 * 根据id删除ChannelAttrvalMapping
	 * @param id
	 * @return
	 */
	public int deleteChannelAttrvalMapping(Integer id);
	
	/**
	 * 根据id更新CategroyMapping
	 * @param id
	 * @return
	 */
	public int updateCategroyMapping(CategoryMapping categoryMapping);
	
	/**
	 * 更新channelAttrvalMapping
	 * @param channelAttrvalMapping
	 * @return
	 */
	public int updateChannelAttrvalMapping(ChannelAttributeValueMapping channelAttrvalMapping);
	
	public ProductPartnumberMapping getChannelProductIdByOuterId(String outerId, Integer applicationId, Integer type);
	
	/**
	 * 获取上次推库存数
	 * @param outerId 商家编码
	 * @param applicationId 应用id
	 * @return
	 */
	public InventoryUpdateRemark getInventoryUpdateRemarkByOuterId(String outerId, Integer applicationId);
	
	/**
	 * 初始或更新化库存日志
	 * @param update
	 */
	public void saveOrupdateInventoryUpdateRemark(InventoryUpdateRemark update);
}
