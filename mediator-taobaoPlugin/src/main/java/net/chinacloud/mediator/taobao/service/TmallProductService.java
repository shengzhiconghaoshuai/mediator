/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TmallProductService.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.service;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.StoreItems;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.taobao.domain.AttributeMapping;
import net.chinacloud.mediator.taobao.domain.ChannelAttributeMapping;
import net.chinacloud.mediator.taobao.domain.ProductCategoryMapping;
import net.chinacloud.mediator.taobao.domain.TmallItemPropValWrapper;
import net.chinacloud.mediator.taobao.domain.TmallItemPropWrapper;

import com.taobao.api.domain.ItemCat;

/**
 * @description 天猫商品接口
 * @author yejunwu123@gmail.com
 * @since 2015年7月15日 下午5:27:35
 */
public interface TmallProductService extends ProductService {
	/**
	 * 根据渠道主目录叶子类目id获取产品id
	 * @param channelCategoryId 渠道主目录叶子类目id
	 * @param applicationId 应用id
	 * @return
	 */
	public ProductCategoryMapping getProductCategoryMapping(
			Long channelCategoryId, Integer applicationId);
	/**
	 * 根据系统属性代号获取对应的渠道属性
	 * @param channelCategoryId 渠道类目id
	 * @param attributeCode 系统属性代号
	 * @param applicationId 应用id
	 * @return
	 */
	public ChannelAttributeMapping getChannelAttributeMapping(Long channelCategoryId, String attributeCode, Integer applicationId);
	/**
	 * 获取渠道类目信息
	 * @param parentCid 父类目id
	 * @return
	 */
	public List<ItemCat> getChannelCategory(long parentCid);
	/**
	 * 获取天猫属性
	 */
	public List<TmallItemPropWrapper> getItemPropWrappers(long categoryId);
	/**
	 * 获取天猫属性值
	 */
	public List<TmallItemPropValWrapper> getItemPropValWrappers(long categoryId,String pid);
	/**
	 * 添加天猫属性
	 */
	public int saveTmallAttr(ChannelAttributeMapping channelAttributeMapping);
	/**
	 * 编辑天猫属性
	 */
	public int updateTmallAttr(ChannelAttributeMapping channelAttributeMapping);
	/**
	 * 删除天猫属性
	 */
	public int deleteTmallAttr(int attrid);
	/**
	 * 查询属性
	 */
	public List<ChannelAttributeMapping> getChannelAttributeMappings(
			Map<String, Object> params);
	/**
	 * 获取淘宝映射的属性的记录数
	 * @param params
	 * @return
	 */
	public long getChannelAttributeMappingsCount(Map<String, Object> params);
	/**
	 * 搜索天猫产品id
	 * @param categoryId 天猫类目id
	 * @param properties 属性
	 * @return 天猫产品id
	 */
	public Long getChannelProductId(Long categoryId, String properties);
	/**
	 * 保存天猫产品、类目关系
	 * @param mapping
	 */
	public void saveProductCategoryMapping(ProductCategoryMapping mapping);
	
	/**
	 * 获取属性映射
	 */
	public AttributeMapping getAttrMapping(Long cid,String channelKey );
	/**
	 * 初始化产品类目
	 */
	public void initProductCat();
	
	public String qimenStoreInventoryItemInitial(StoreItems storeItems) throws ProductException;
}
