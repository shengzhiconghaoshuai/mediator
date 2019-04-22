/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductAttributeServiceImpl.java
 * 描述： 
 */
package net.chinacloud.mediator.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.CategoryMappingDao;
import net.chinacloud.mediator.dao.ChannelAttributeValueMappingDao;
import net.chinacloud.mediator.dao.ProductPartnumberMappingDao;
import net.chinacloud.mediator.domain.CategoryMapping;
import net.chinacloud.mediator.domain.ChannelAttributeValueMapping;
import net.chinacloud.mediator.domain.InventoryUpdateRemark;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.exception.product.ChannelProductIdNotFoundException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.service.ProductAttributeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description 商品相关公共业务
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午6:19:31
 */
@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductAttributeServiceImpl.class);
	
	@Autowired
	CategoryMappingDao categoryMappingDao;
	@Autowired
	ProductPartnumberMappingDao productPartnumberMappingDao;
	@Autowired
	ChannelAttributeValueMappingDao channelAttributeValueMappingDao;

	@Override
	public CategoryMapping getCategoryMapping(Long categoryId,
			Integer applicationId) {
		try {
			return categoryMappingDao.getCategoryMapping(categoryId, applicationId);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public ProductPartnumberMapping getProductPartnumberMappingByOuterId(String outerId,
			Integer applicationId, Integer type) throws ProductException{
		try{
			return productPartnumberMappingDao.getProductPartnumberMappingByOuterId(outerId, applicationId, type);
		}catch(Exception e){
			// e.printStackTrace();
			String errorMessage = MessageFormat.format("product partnumber mapping not found, applicationId = {0}, type = {1}, outerId = {2}, errors = {3}", 
					applicationId, type, outerId, e.getMessage());
			LOGGER.error(errorMessage);
			MailSendUtil.sendEmail("product partnumber mapping not found", 
					errorMessage);
		}
		/*String channelCode = ContextUtil.get(Constant.CHANNEL_CODE);
		ProductService productService = serviceManager.getService(ProductService.class, channelCode);
		String channelProdId = null;	// 渠道商品/skuid
		if (type == 0) {	//商品
			channelProdId = productService.getChannelProductIdByOuterProductId(outerId);
		} else {	//sku
			channelProdId = productService.getChannelSkuIdByOuterSkuId(outerId);
		}
		if (StringUtils.hasText(channelProdId)) {
			ProductPartnumberMapping mapping = new ProductPartnumberMapping();
			mapping.setChannelProductId(channelProdId);
			mapping.setOuterId(outerId);
			mapping.setType(type);
			mapping.setApplicationId(applicationId);
			try {
				productPartnumberMappingDao.saveProductPartnumberMapping(mapping);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mapping;
		}*/
		throw new ChannelProductIdNotFoundException(outerId);
	}
	
	@Override
	public ProductPartnumberMapping getProductPartnumberMappingByChannelId(
			String channelProductId, Integer applicationId, Integer type) {
		try {
			return productPartnumberMappingDao.getProductPartnumberMappingChannelId(channelProductId, applicationId, type);
		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public void saveProductPartnumberMapping(ProductPartnumberMapping mapping) {
		try {
			productPartnumberMappingDao.saveProductPartnumberMapping(mapping);
		} catch (Exception e) {
			//e.printStackTrace();
			
			String errorMessage = MessageFormat.format("product partnumber mapping persist error, applicationId = {0}, type = {1}, outerId = {2}, channelProductId = {3}, errors = {4}", 
					mapping.getApplicationId(), mapping.getType(), mapping.getOuterId(), mapping.getChannelProductId(), e.getMessage());
			LOGGER.error(errorMessage);
			MailSendUtil.sendEmail("product partnumber mapping persist error", 
					errorMessage);
		}
	}

	@Override
	public ChannelAttributeValueMapping getChannelAttributeValueMapping(
			Long channelCategoryId, String channelAttributeId,
			String attributeValue, Integer applicationId) {
		try {
			return channelAttributeValueMappingDao.getChannelAttributeValueMapping(channelCategoryId, channelAttributeId, attributeValue, applicationId);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public List<ChannelAttributeValueMapping> loadChannelAttrvalMapping(
			Map<String, Object> params) {
		return channelAttributeValueMappingDao.loadChannelAttributeValueMapping(params);
	}
	
	@Override
	public int saveCategroyMapping(CategoryMapping categroyMapping) {
		 return categoryMappingDao.saveCategroyMapping(categroyMapping);
	}

	@Override
	public List<CategoryMapping> loadCaregroyMapping(Map<String, Object> params) {
		return categoryMappingDao.loadCategroyMapping(params);
	}
	
	@Override
	public Long countCaregroyMapping(Map<String, Object> params) {
		return categoryMappingDao.count(params);
	}

	@Override
	public int saveChannelAttrvalMapping(
			ChannelAttributeValueMapping channelAttrvalMapping) {
		return channelAttributeValueMappingDao.saveChannelAttributeValueMapping(channelAttrvalMapping);
	}

	@Override
	public int deleteCategroyMapping(Integer id) {
		return categoryMappingDao.deleteCategoryMapping(id); 
	}

	@Override
	public int deleteChannelAttrvalMapping(Integer id) {
		return channelAttributeValueMappingDao.deleteChannelAttrvalMapping(id);
	}

	@Override
	public int updateCategroyMapping(CategoryMapping categoryMapping) {
		return categoryMappingDao.updateCategoryMapping(categoryMapping);
	}

	@Override
	public int updateChannelAttrvalMapping(
			ChannelAttributeValueMapping channelAttrvalMapping) {
		return channelAttributeValueMappingDao.updateChannelAttrvalMapping(channelAttrvalMapping);
	}

	@Override
	public Long countChannelAttrvalMapping(Map<String, Object> params) {
		return channelAttributeValueMappingDao.count(params);
	}

	@Override
	public void updateProductPartnumberMapping(ProductPartnumberMapping mapping) {
		try {
			Integer result = productPartnumberMappingDao.updateProductPartnumberMapping(mapping);
			if (result != 1) {
				String errorMessage = MessageFormat.format("product partnumber mapping update error, no mapping updated, applicationId = {0}, type = {1}, outerId = {2}, channelProductId = {3}", 
						mapping.getApplicationId(), mapping.getType(), mapping.getOuterId(), mapping.getChannelProductId());
				LOGGER.error(errorMessage);
				MailSendUtil.sendEmail("product partnumber mapping update error", 
						errorMessage);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			
			String errorMessage = MessageFormat.format("product partnumber mapping update error, applicationId = {0}, type = {1}, outerId = {2}, channelProductId = {3}, errors = {4}", 
					mapping.getApplicationId(), mapping.getType(), mapping.getOuterId(), mapping.getChannelProductId(), e.getMessage());
			LOGGER.error(errorMessage);
			MailSendUtil.sendEmail("product partnumber mapping update error", 
					errorMessage);
		}
	}

	@Override
	public ProductPartnumberMapping getChannelProductIdByOuterId(
			String outerId, Integer applicationId, Integer type) {
		
		try {
			return productPartnumberMappingDao.getProductPartnumberMappingByOuterId(outerId, applicationId, type);
		} catch (Exception e) {
			LOGGER.error(outerId+"的映射不存在，的应用id"+applicationId);
			return null;
		}
	}

	@Override
	public InventoryUpdateRemark getInventoryUpdateRemarkByOuterId(String outerId, Integer applicationId) {
		try {
			return productPartnumberMappingDao.getInventoryUpdateRemarkByOuterId(outerId,applicationId);
		} catch (Exception e) {
			LOGGER.error(outerId+"的更新库存日志未初始化，的应用id"+applicationId);
			return null;
		}
	}
	

	@Override
	public void saveOrupdateInventoryUpdateRemark(InventoryUpdateRemark update) {
		int count = 0;
		try {
			count = productPartnumberMappingDao.getCountByOuterId(update);
			if(count > 0){
				productPartnumberMappingDao.updateInventoryUpdateRemark(update);
			}else{
				productPartnumberMappingDao.saveInventoryUpdateRemark(update);
			}
		} catch (Exception e) {
			LOGGER.error("初始化或更新库存日志失败:"+e.getMessage());
		}
		
	}
	
	
}
