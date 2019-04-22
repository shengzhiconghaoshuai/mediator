package net.chinacloud.mediator.service.impl;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.ProductPartnumberMappingDao;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.service.ProductPartnumberMappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2015年12月7日 上午10:04:35
 */
@Service
public class ProductPartnumberMappingServiceImpl implements ProductPartnumberMappingService{

	@Autowired
	ProductPartnumberMappingDao productPartnumberMappingDao;
	
	/**
	 * 批量保存或者更新映射
	 */
	public void saveOrUpdateProductPartnumberMapping(List<Product> list, int applicationId, int type) {
		for (Product p : list) {
			saveOrUpdateProductPartnumberMapping(p, applicationId, type);
		}
	}
	
	/**
	 * 保存或者更新映射
	 * @param p
	 * @param applicationId
	 * @param type
	 */
	public Boolean saveOrUpdateProductPartnumberMapping(Product p, int applicationId, int type) {
		if (isHasOuterId(p,applicationId,type)) {
			productPartnumberMappingDao.updateProductPartnumberMapping(p.getChannelProductId(),p.getOuterProductId(),applicationId,type);
			return true;
		} else {
			productPartnumberMappingDao.saveProductPartnumberMapping(p.getChannelProductId(),p.getOuterProductId(),applicationId,type);
			return false;
		}
	}
	

	/**
	 * 检查映射表中是否有映射关系，false表示不存在该映射关系，true表示存在该映射关系
	 * @param p
	 * @return
	 */
	public boolean isHasOuterId(Product p, int applicationId, int type) {
		try {
			productPartnumberMappingDao.getProductPartnumberMappingByOuterId(p.getOuterProductId(), applicationId, type);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<ProductPartnumberMapping> queryProductPartnumberMapping(
			Map<String, Object> queryParam) {
		return productPartnumberMappingDao.queryProductPartnumberMapping(queryParam);
	}

	@Override
	public Long count(Map<String, Object> queryParam) {
		return productPartnumberMappingDao.count(queryParam);
	}

	
}
