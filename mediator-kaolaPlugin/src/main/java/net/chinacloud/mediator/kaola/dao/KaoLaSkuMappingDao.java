package net.chinacloud.mediator.kaola.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;


public class KaoLaSkuMappingDao extends DAO {
	
	public Integer getCountByOuterId(String outerSkuId,int applicationId){
		final String sql = "SELECT COUNT(1) FROM kaola_sku_mapping WHERE OUTER_SKU_ID = ? AND APPLICATIONID = ? ";
		return ((Number)super.queryForBasicType(sql, outerSkuId, applicationId)).intValue();
	}
	
	public Integer updateProductPartnumberMapping(String channleSkuId,String outerSkuId,String modifiedtime,int applicationId ){
		final String sql = "UPDATE kaola_sku_mapping SET CHANNEL_SKU_ID = ?, MODIFYTIME = ? WHERE OUTER_SKU_ID = ? AND APPLICATIONID = ?";
		return super.update(sql, channleSkuId,modifiedtime, outerSkuId, applicationId);		
	}
	
	public Integer saveProductPartnumberMapping(String channleSkuId,String outerSkuId,String modifiedtime,int applicationId ){
		final String sql = "INSERT INTO kaola_sku_mapping (CHANNEL_SKU_ID, OUTER_SKU_ID, MODIFYTIME, APPLICATIONID) VALUES (?, ?, ?, ?)";
		return super.update(sql, channleSkuId, outerSkuId,modifiedtime, applicationId);
		
	}
	
	
	public ProductPartnumberMapping getSkuPartnumberMappingByOuterSkuId(String outerId, Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_SKU_ID, OUTER_SKU_ID, APPLICATIONID FROM kaola_sku_mapping WHERE APPLICATIONID = ? AND OUTER_SKU_ID = ?";
		return super.queryForObject(sql, new RowMapper<ProductPartnumberMapping>() {
			@Override
			public ProductPartnumberMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractProductPartnumberMapping(rs);
				}
				return null;
			}
		}, applicationId, outerId);
	}
	
	
	private ProductPartnumberMapping extractProductPartnumberMapping(
			ResultSet rs) throws SQLException {
		ProductPartnumberMapping mapping = new ProductPartnumberMapping();
		mapping.setId(rs.getInt("MAPPING_ID"));
		mapping.setChannelProductId(rs.getString("CHANNEL_SKU_ID"));
		mapping.setOuterId(rs.getString("OUTER_SKU_ID"));
		mapping.setApplicationId(rs.getInt("APPLICATIONID"));
		return mapping;
	}
	

}
