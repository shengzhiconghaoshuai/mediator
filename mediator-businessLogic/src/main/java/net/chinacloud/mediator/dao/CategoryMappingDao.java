/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CategoryMappingDao.java
 * 描述： 
 */
package net.chinacloud.mediator.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.CategoryMapping;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.springframework.jdbc.core.RowMapper;

/**
 * @description 渠道主目录映射DAO
 * @author yejunwu123@gmail.com
 * @since 2015年6月27日 下午6:01:50
 */
//@Repository
public class CategoryMappingDao extends DAO {
	/**
	 * 根据系统叶子类目id获取渠道对应的类目id
	 * @param categoryId 系统叶子类目id
	 * @param applicationId 应用id
	 * @return
	 */
	public CategoryMapping getCategoryMapping(Long categoryId, Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_CAT_NAME, CAT_ID, CAT_NAME, APPLICATION_ID FROM CATEGORY_MAPPING WHERE CAT_ID = ? AND APPLICATION_ID = ?";
		return super.queryForObject(sql, new RowMapper<CategoryMapping>() {
			@Override
			public CategoryMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractCategoryMapping(rs);
				}
				return null;
			}
		}, categoryId, applicationId);
	}

	/**
	 * 保存一个CategroyMapping
	 * @return 
	 */
	public int saveCategroyMapping(CategoryMapping categoryMapping){
		String sql = "INSERT INTO CATEGORY_MAPPING(CHANNEL_CAT_ID,CHANNEL_CAT_NAME,CAT_ID,CAT_NAME,APPLICATION_ID) VALUES(?,?,?,?,?)";
		return super.update(sql,categoryMapping.getChannelCategoryId(),categoryMapping.getChannelCategoryName(),categoryMapping.getCategoryId(), categoryMapping.getCategoryName(),categoryMapping.getApplicationId());
	}
	
	/**
	 * 加载CategroyMapping
	 * @return 
	 */
	public List<CategoryMapping> loadCategroyMapping(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT MAPPING_ID,CHANNEL_CAT_ID,CHANNEL_CAT_NAME,CAT_ID,CAT_NAME,APPLICATION_ID FROM CATEGORY_MAPPING");
		String buildSql = buildSql(sql, params);
		//buildSql += " LIMIT :OFFSET, :ROWS";
		return super.pageableQueryForList(buildSql, new RowMapper<CategoryMapping>() {
			@Override
			public CategoryMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractCategoryMapping(rs);
			}
		}, params);
	}
	
	public Long count(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM CATEGORY_MAPPING");
		return ((Number)super.queryForBasicType(buildSql(sql, params), params)).longValue();
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
					sql.append(entry.getKey()).append(" = ").append(":").append(entry.getKey());
					if (i < paramSize) {
						sql.append(" AND ");
					}
				}
			}
		}
		return sql.toString();
	}
	
	/**
	 * 根据id删除categoryMapping
	 * @param id
	 * @return
	 */
	public int deleteCategoryMapping(Integer id){
		String sql = "DELETE FROM CATEGORY_MAPPING WHERE MAPPING_ID = ?";
		return super.update(sql,id);
	}
	
	/**
	 * 更新CategoryMapping渠道主目录映射
	 * @param categoryMapping
	 * @return
	 */
	public int updateCategoryMapping(CategoryMapping categoryMapping){
		String sql = "UPDATE CATEGORY_MAPPING SET CAT_ID = ? ,CAT_NAME = ? WHERE MAPPING_ID = ? ";
		return super.update(sql, categoryMapping.getCategoryId(),categoryMapping.getCategoryName(),categoryMapping.getId());
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private CategoryMapping extractCategoryMapping(ResultSet rs) throws SQLException {
		CategoryMapping mapping = new CategoryMapping();
		mapping.setId(rs.getInt("MAPPING_ID"));
		mapping.setChannelCategoryId(rs.getLong("CHANNEL_CAT_ID"));
		mapping.setChannelCategoryName(rs.getString("CHANNEL_CAT_NAME"));
		mapping.setCategoryId(rs.getLong("CAT_ID"));
		mapping.setCategoryName(rs.getString("CAT_NAME"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		return mapping;
	}
	
}
