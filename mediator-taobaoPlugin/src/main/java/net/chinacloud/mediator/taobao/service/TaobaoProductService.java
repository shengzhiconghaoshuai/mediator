/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoProductService.java
 * 描述： 淘宝商品业务接口
 */
package net.chinacloud.mediator.taobao.service;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.ChannelPictureMapping;
import net.chinacloud.mediator.domain.StoreItems;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.taobao.domain.ItemPropWrapper;
import net.chinacloud.mediator.taobao.domain.TaobaoChannelAttributeMapping;

import com.taobao.api.domain.PropValue;

/**
 * <淘宝商品业务接口>
 * <淘宝商品业务接口>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
public interface TaobaoProductService extends TmallProductService {
	/**
	 * 获取淘宝映射的属性
	 * @param params
	 * @return
	 */
	public List<TaobaoChannelAttributeMapping> getTaobaoChannelAttributeMappings(
			Map<String, Object> params);
	
	/**
	 * 获取淘宝映射的属性
	 * @param channelCategoryId 渠道类目id
	 * @param channelAttrId 渠道属性id
	 * @param applicationId 应用id
	 * @return
	 */
	public TaobaoChannelAttributeMapping getTaobaoChannelAttributeMapping(Long channelCategoryId, String channelAttrId, Integer applicationId);
	/**
	 * 获取淘宝映射的属性的记录数
	 * @param params
	 * @return
	 */
	public long getTaobaoChannelAttributeMappingsCount(Map<String, Object> params);
	/**
	 * 获取淘宝类目属性
	 * @param categoryId 类目id
	 * @return
	 */
	public List<ItemPropWrapper> getChannelAttribute(long categoryId);
	/**
	 * 获取淘宝类目属性值
	 * @param pid
	 * @return
	 */
	public List<PropValue> getChannelAttributeValue(long cid, long pid);
	/**
	 * 保存淘宝属性映射
	 * @param attributeMapping
	 * @return
	 */
	public int saveChannelAttributeMapping(TaobaoChannelAttributeMapping attributeMapping);
	/**
	 * 更新淘宝属性映射
	 * @param attributeMapping
	 * @return
	 */
	public int updateChannelAttributeMapping(TaobaoChannelAttributeMapping attributeMapping);
	/**
	 * 删除淘宝属性映射
	 * @param id
	 * @return
	 */
	public int deleteChannelAttributeMapping(int id);
	/**
	 * 插入商品所属图片
	 */
	public void insertPicture(ChannelPictureMapping cMapping) throws ProductException;
	/**
	 * 更新本地图片信息
	 */
	public void updatePicture(ChannelPictureMapping cMapping) throws ProductException;
	/**
	 * 查询商品图片
	 */
	public List<ChannelPictureMapping> getPictureList(String productId, Integer applicationId,Boolean uploadSucess);
	
	/**
	 * ERP调用奇门的接口，对门店的库存进行初始化
	 * @param storeItems
	 * @return
	 * @throws ProductException 
	 */
	public String qimenStoreInventoryItemInitial(StoreItems storeItems) throws ProductException;
}
