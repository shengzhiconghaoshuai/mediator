/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductCategoryMappingDao.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.taobao.domain.ProductCategoryMapping;

import org.springframework.jdbc.core.RowMapper;

/**
 * @description 天猫产品类目映射DAO
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午5:32:15
 */
//@Repository
public class ProductCategoryMappingDao extends DAO {
	/**
	 * 根据天猫类目id获取产品id
	 * @param channelCategoryId 渠道类目id
	 * @param applicationId 应用id
	 * @return
	 */
	public ProductCategoryMapping getProductCategoryMapping(
			Long channelCategoryId, Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, PRODUCT_ID, APPLICATION_ID FROM TMALL_PRODUCTCAT_MAPPING WHERE CHANNEL_CAT_ID = :CHANNEL_CAT_ID AND APPLICATION_ID = :APPLICATION_ID";
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("CHANNEL_CAT_ID", channelCategoryId);
		params.put("APPLICATION_ID", applicationId);
		return super.queryForObject(sql, params, new RowMapper<ProductCategoryMapping>() {
			@Override
			public ProductCategoryMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if(null != rs){
					return extractProductCategoryMapping(rs);
				}
				return null;
			}
		});
	}
	
	/**
	 * 插入映射
	 * @param mapping
	 * @param applicationId
	 * @return
	 */
	public int insertProductCategoryMapping(ProductCategoryMapping mapping) {
		final String sql = "INSERT INTO TMALL_PRODUCTCAT_MAPPING (CHANNEL_CAT_ID, PRODUCT_ID, APPLICATION_ID) VALUES (?, ?, ?)";
		return super.update(sql, mapping.getChannelCategoryId(), mapping.getProductId(), mapping.getApplicationId());
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private ProductCategoryMapping extractProductCategoryMapping(ResultSet rs) throws SQLException {
		ProductCategoryMapping mapping = new ProductCategoryMapping();
		mapping.setId(rs.getInt("MAPPING_ID"));
		mapping.setChannelCategoryId(rs.getLong("CHANNEL_CAT_ID"));
		mapping.setProductId(rs.getLong("PRODUCT_ID"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		return mapping;
	}
}
