package net.chinacloud.mediator.kaola.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.kaola.dao.KaoLaSkuMappingDao;
import net.chinacloud.mediator.kaola.service.KaoLaSkuMappingSevice;

@Service
public class KaoLaSkuMappingSeviceImpl implements KaoLaSkuMappingSevice{
	@Autowired
	private KaoLaSkuMappingDao kaoLaSkuMappingDao;
	
	
	@Override
	public Boolean saveOrupdateSkuPartnumberMapping(Sku sku, int applicationId,
			String modifiedtime) {
		int count = 0;
		if (isHasOuterId(sku,applicationId)) {
			count = kaoLaSkuMappingDao.updateProductPartnumberMapping(sku.getChannelSkuId(),sku.getOuterSkuId(),modifiedtime,applicationId);
		} else {
			count = kaoLaSkuMappingDao.saveProductPartnumberMapping(sku.getChannelSkuId(),sku.getOuterSkuId(),modifiedtime,applicationId);
		}
		if(count>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 检查映射表中是否有映射关系，false表示不存在该映射关系，true表示存在该映射关系
	 * @param p
	 * @return
	 */
	public boolean isHasOuterId(Sku sku, int applicationId) {
		int count = 0;
		count =	kaoLaSkuMappingDao.getCountByOuterId(sku.getOuterSkuId(), applicationId);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	

}
