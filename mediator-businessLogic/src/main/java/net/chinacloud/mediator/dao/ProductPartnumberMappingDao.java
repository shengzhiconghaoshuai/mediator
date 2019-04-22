/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductPartnumberMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.InventoryUpdateRemark;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @description 渠道商品与外部商家编码映射DAO
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午6:26:42
 */
//@Repository
public class ProductPartnumberMappingDao extends DAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPartnumberMappingDao.class);
	/**
	 * 根据外部商家编码取得渠道对应的商品id
	 * @param outerId 商品/sku外部商家编码
	 * @param applicationId 应用id
	 * @param type 类型,0:product,1:sku
	 * @return
	 */
	public ProductPartnumberMapping getProductPartnumberMappingByOuterId(String outerId, Integer applicationId, Integer type) {
		String sql = "SELECT MAPPING_ID, CHANNEL_PRODUCT_ID, OUTER_ID, TYPE, APPLICATION_ID FROM PRODUCT_PARTNUMBER_MAPPING WHERE TYPE = ? AND APPLICATION_ID = ? AND OUTER_ID = ?";
		return super.queryForObject(sql, new RowMapper<ProductPartnumberMapping>() {
			@Override
			public ProductPartnumberMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractProductPartnumberMapping(rs);
				}
				return null;
			}
		}, type, applicationId, outerId);
	}
	
	/**
	 * 根据渠道商品id获取外部商品/sku编码
	 * @param channelId 渠道商品/sku id
	 * @param applicationId 应用编号
	 * @param type 类型,0:product,1:sku
	 * @return
	 */
	public ProductPartnumberMapping getProductPartnumberMappingChannelId(String channelId, Integer applicationId, Integer type) {
		String sql = "SELECT MAPPING_ID, CHANNEL_PRODUCT_ID, OUTER_ID, TYPE, APPLICATION_ID FROM PRODUCT_PARTNUMBER_MAPPING WHERE TYPE = ? AND APPLICATION_ID = ? AND CHANNEL_PRODUCT_ID = ?";
		return super.queryForObject(sql, new RowMapper<ProductPartnumberMapping>() {
			@Override
			public ProductPartnumberMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractProductPartnumberMapping(rs);
				}
				return null;
			}
		}, type, applicationId, channelId);
	}
	
	/**
	 * 保存渠道商品编号和外部商家编码映射关系
	 * @param mapping
	 */
	public void saveProductPartnumberMapping(ProductPartnumberMapping mapping) {
		final String sql = "INSERT INTO PRODUCT_PARTNUMBER_MAPPING (CHANNEL_PRODUCT_ID, OUTER_ID, TYPE, APPLICATION_ID) VALUES (?, ?, ?, ?)";
		super.update(sql, 
				mapping.getChannelProductId(),
				mapping.getOuterId(),
				mapping.getType(),
				mapping.getApplicationId());
	}
	
	/**
	 * 保存渠道商品编号和外部商家编码映射关系
	 * @param channleProductId
	 * @param outerId
	 * @param applicationId
	 * @param type
	 */
	public void saveProductPartnumberMapping(String channleProductId,String outerId,int applicationId,int type) {
		final String sql = "INSERT INTO PRODUCT_PARTNUMBER_MAPPING (CHANNEL_PRODUCT_ID, OUTER_ID, TYPE, APPLICATION_ID) VALUES (?, ?, ?, ?)";
		super.update(sql, channleProductId, outerId, type, applicationId);

	}
	
	/**
	 * 更新渠道商品编号和外部商家编码映射关系
	 */
	public Integer updateProductPartnumberMapping(ProductPartnumberMapping mapping) {
		final String sql = "UPDATE PRODUCT_PARTNUMBER_MAPPING SET CHANNEL_PRODUCT_ID = ? WHERE OUTER_ID = ? AND APPLICATION_ID = ?";
		return super.update(sql, mapping.getChannelProductId(), mapping.getOuterId(), mapping.getApplicationId());
	}
	
	/**
	 * 根据外部商家编码更新渠道商品编号
	 * @param channleProductId
	 * @param outerId
	 * @param applicationId
	 * @param type
	 * @return
	 */
	public Integer updateProductPartnumberMapping(String channleProductId,String outerId,int applicationId,int type){
		final String sql = "UPDATE PRODUCT_PARTNUMBER_MAPPING SET CHANNEL_PRODUCT_ID = ? WHERE OUTER_ID = ? AND APPLICATION_ID = ? AND TYPE = ?";
		return super.update(sql, channleProductId, outerId, applicationId,type);
	}
	
	public List<ProductPartnumberMapping> queryProductPartnumberMapping(Map<String,Object> queryParam){
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT MAPPING_ID, CHANNEL_PRODUCT_ID, OUTER_ID, TYPE, APPLICATION_ID FROM PRODUCT_PARTNUMBER_MAPPING");
		String buildSql = buildSql(sql, queryParam);
		LOGGER.info(buildSql.toString());
		return super.pageableQueryForList(buildSql.toString(),new RowMapper<ProductPartnumberMapping>(){
			@Override
			public ProductPartnumberMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractProductPartnumberMapping(rs);
				}
				return null;
			}
		}, queryParam);
	}
	
	
	public Long count(Map<String,Object> queryParam){
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(1) ")
			.append("FROM PRODUCT_PARTNUMBER_MAPPING ");
		String buildSql = buildSql(sql, queryParam);
		LOGGER.info(buildSql.toString());
		return ((Number)super.queryForBasicType(buildSql.toString(), queryParam)).longValue();
	}
	
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private ProductPartnumberMapping extractProductPartnumberMapping(
			ResultSet rs) throws SQLException {
		ProductPartnumberMapping mapping = new ProductPartnumberMapping();
		mapping.setId(rs.getInt("MAPPING_ID"));
		mapping.setChannelProductId(rs.getString("CHANNEL_PRODUCT_ID"));
		mapping.setOuterId(rs.getString("OUTER_ID"));
		mapping.setType(rs.getInt("TYPE"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		return mapping;
	}
	
	private String buildSql(StringBuilder sql, Map<String, Object> params) {
		if (!CollectionUtil.isEmpty(params)) {
			if(params.size()>2) {
				sql.append(" WHERE ");	
			}
			int i = 0;
			int paramSize = params.size() -  2;
			for (Entry<String, Object> entry : params.entrySet()) {
				if (!entry.getKey().equalsIgnoreCase(Constant.PAGE_END_INDEX) && !entry.getKey().equalsIgnoreCase(Constant.PAGE_START_INDEX)) {
					i++;
					sql.append("PRODUCT_PARTNUMBER_MAPPING.").append(entry.getKey()).append(" = ").append(":").append(entry.getKey());	
					if (i < paramSize) {
						sql.append(" AND ");
					}
				}
			}
		}
		return sql.toString();
	}

	/**
	 * 获取指定的库存更新日志
	 * @param outerId
	 * @param applicationId
	 * @return
	 */
	public InventoryUpdateRemark getInventoryUpdateRemarkByOuterId(String outerId, Integer applicationId) {
		String sql = "SELECT ID, OUTER_SKU_ID, STOCKNUMBER, ISFULL, MODIFYTIME, APPLICATIONID FROM INVENTORY_UPDATE_REMARK WHERE APPLICATIONID = ? AND OUTER_SKU_ID = ?";
		return super.queryForObject(sql, new RowMapper<InventoryUpdateRemark>() {
			@Override
			public InventoryUpdateRemark mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractInventoryUpdateRemark(rs);
				}
				return null;
			}
		}, applicationId, outerId);
	}
	
	
	/**
	 * 保存调用接口后是库存数
	 * @param mapping
	 */
	public void saveInventoryUpdateRemark(InventoryUpdateRemark update) {
		final String sql = "INSERT INTO INVENTORY_UPDATE_REMARK (OUTER_SKU_ID, STOCKNUMBER, ISFULL, MODIFYTIME, APPLICATIONID) VALUES (?, ?, ?, ?,?)";
		super.update(sql, 
				update.getOuterId(),
				update.getStockNumber(),
				update.isFull(),
				update.getMotifyTime(),
				update.getApplicationId());
	}
	
	/**
	 * 更新调用接口后是库存数
	 * @param mapping
	 */
	public Integer updateInventoryUpdateRemark(InventoryUpdateRemark update){
		final String sql = "UPDATE INVENTORY_UPDATE_REMARK SET STOCKNUMBER = ?,ISFULL= ?,MODIFYTIME= ? WHERE OUTER_SKU_ID = ? AND APPLICATIONID = ?";
		return super.update(sql, 
				update.getStockNumber(),
				update.isFull(),
				update.getMotifyTime(),
				update.getOuterId(),
				update.getApplicationId());
	}
	
	public Integer getCountByOuterId(InventoryUpdateRemark update){
		final String sql = "SELECT COUNT(1) FROM INVENTORY_UPDATE_REMARK WHERE OUTER_SKU_ID = ? AND APPLICATIONID = ? ";
		return ((Number)super.queryForBasicType(sql,update.getOuterId(),update.getApplicationId())).intValue();
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private InventoryUpdateRemark extractInventoryUpdateRemark(
			ResultSet rs) throws SQLException {
		InventoryUpdateRemark update = new InventoryUpdateRemark();
		update.setId(rs.getInt("ID"));
		update.setOuterId(rs.getString("OUTER_SKU_ID"));
		update.setStockNumber(rs.getInt("STOCKNUMBER"));
		update.setFull(rs.getBoolean("ISFULL"));
		update.setApplicationId(rs.getInt("APPLICATIONID"));
		update.setMotifyTime(rs.getString("MODIFYTIME"));
		return update;
	}
	
}
