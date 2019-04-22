package net.chinacloud.mediator.service;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2015年12月7日 上午10:04:05
 */
public interface ProductPartnumberMappingService {
      public void saveOrUpdateProductPartnumberMapping(List<Product> list,int applicationId,int type);  
      
      public Boolean saveOrUpdateProductPartnumberMapping(Product p, int applicationId, int type);
      
      public List<ProductPartnumberMapping> queryProductPartnumberMapping(Map<String,Object> queryParam);
      
      public Long count(Map<String,Object> queryParam);
      
}
