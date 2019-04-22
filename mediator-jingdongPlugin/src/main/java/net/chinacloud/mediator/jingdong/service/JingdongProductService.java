/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：JingdongProductService.java
 * 描述： 京东渠道产品接口
 */
package net.chinacloud.mediator.jingdong.service;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.jingdong.domain.ChannelAttributeMapping;
import net.chinacloud.mediator.service.ProductService;

/**
 * <京东商品业务接口>
 * <京东商品业务接口>
 * @author jianjunnie
 * @version 0.0.0,2015年2月12日
 * @since 2015年2月12日
 */
public interface JingdongProductService extends ProductService{
	/**
	 * 获取输入类型input_type为1或2的属性
	 * @param channelCategoryId
	 * @param applicationId
	 * @return
	 */
	public List<ChannelAttributeMapping> getChannelAttributeMapping(Long channelCategoryId, Integer applicationId);
	/**
	 * 获取京东属性映射
	 * @param channelCategoryId 渠道类目id
	 * @param channelAttributeId 渠道属性id
	 * @param applicationId 应用id
	 * @return
	 */
	public ChannelAttributeMapping getChannelAttributeMapping(Long channelCategoryId, String channelAttributeId, Integer applicationId);
	
	/**
	 * 获取关键属性
	 * @param channelCategoryId 渠道叶子类目id
	 * @param applicationId 应用id
	 * @return
	 */
	public List<ChannelAttributeMapping> getKeyPropertyMappings(Long channelCategoryId, Integer applicationId);
	/**
	 * 获取销售属性
	 * @param channelCategoryId 渠道叶子类目id
	 * @param applicationId 应用id
	 * @return
	 */
	public List<ChannelAttributeMapping> getSalePropertyMappings(Long channelCategoryId, Integer applicationId);
	
	/**
	 * 保存ChannelAttributeMapping
	 * @param channelAttributeMapping
	 * @return
	 */
	public int saveChannelAttributeMapping(ChannelAttributeMapping channelAttributeMapping);
	
	
	/**
	 * 加载ChannelAttributeMapping
	 * @param applicationId
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public List<ChannelAttributeMapping> loadjdChannelAttrMapping(Map<String, Object> params);
	
	/**
	 * 统计
	 * @param params
	 * @return
	 */
	public Long countChannelAttributeMapping(Map<String, Object> params);
	
	/**
	 * 删除jdChannelAttrMapping
	 * @param id
	 * @return
	 */
	public int deletejdChannelAttrMapping(Integer id);
	
	/**
	 * 更新jdChannelAttrMapping
	 * @param channelAttributeMapping
	 * @return
	 */
	public int updatejdChannelAttrMapping(ChannelAttributeMapping channelAttributeMapping);
}
