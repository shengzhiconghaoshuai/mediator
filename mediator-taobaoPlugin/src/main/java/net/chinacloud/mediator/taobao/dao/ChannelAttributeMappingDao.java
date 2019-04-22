/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelAttributeMappingDao.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.taobao.domain.ChannelAttributeMapping;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.springframework.jdbc.core.RowMapper;

/**
 * @description 渠道属性映射DAO
 * @author yejunwu123@gmail.com
 * @since 2015年6月28日 上午11:02:43
 */
//@Repository("tbChannelAttributeMappingDao")
public class ChannelAttributeMappingDao extends DAO {
	/**
	 * 根据系统属性代号获取对应的渠道属性
	 * @param channelCategoryId 渠道类目id
	 * @param channelAttributeId 渠道属性ID
	 * @param applicationId 应用id
	 * @return
	 */
	public ChannelAttributeMapping getChannelAttributeMapping(Long channelCategoryId, String channelAttributeId, Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE, APPLICATION_ID FROM TMALL_CHANNEL_ATTR_MAPPING WHERE CHANNEL_ATTR_ID = ? AND CHANNEL_CAT_ID = ? AND APPLICATION_ID = ?";
		return super.queryForObject(sql, new RowMapper<ChannelAttributeMapping>() {
			@Override
			public ChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractChannelAttributeMapping(rs);
				}
				return null;
			}
		}, channelAttributeId, channelCategoryId, applicationId);
	}
	/**
	 * 添加天猫属性映射
	 * @param attributeMapping
	 * @return
	 */
	public int saveChannelAttributeMapping(
			ChannelAttributeMapping attributeMapping) {
		final String sql = "INSERT INTO TMALL_CHANNEL_ATTR_MAPPING (CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE, APPLICATION_ID) "
						+ "VALUES (?, ?, ?, ?, ?, ?)";
		return super.update(sql, 
				attributeMapping.getChannelCategoryId(),
				attributeMapping.getChannelAttributeId(),
				attributeMapping.getChannelAttributeName(),
				attributeMapping.getAttributeCode(),
				attributeMapping.getDefaultValue(),
				attributeMapping.getApplicationId());
	}
	/**
	 * 更新天猫属性映射
	 * @param attributeMapping
	 * @return
	 */
	public int updateChannelAttributeMapping(
			ChannelAttributeMapping attributeMapping) {
		final String sql = "UPDATE TMALL_CHANNEL_ATTR_MAPPING SET ATTR_CODE = ?, DEFAULT_VALUE = ? WHERE MAPPING_ID = ?";
		return super.update(sql, 
				attributeMapping.getAttributeCode(),
				attributeMapping.getDefaultValue(),
				attributeMapping.getId());
	}
	/**
	 * 删除淘宝属性映射
	 * @param id
	 * @return
	 */
	public int deleteChannelAttributeMapping(int id) {
		final String sql = "DELETE FROM TMALL_CHANNEL_ATTR_MAPPING WHERE MAPPING_ID = ?";
		return super.update(sql, id);
	}
	/**
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private ChannelAttributeMapping extractChannelAttributeMapping(
			ResultSet rs) throws SQLException {
		ChannelAttributeMapping mapping = new ChannelAttributeMapping();
		mapping.setId(rs.getInt("MAPPING_ID"));
		mapping.setChannelCategoryId(rs.getLong("CHANNEL_CAT_ID"));
		mapping.setChannelAttributeId(rs.getString("CHANNEL_ATTR_ID"));
		mapping.setChannelAttributeName(rs.getString("CHANNEL_ATTR_NAME"));
		mapping.setAttributeCode(rs.getString("ATTR_CODE"));
		mapping.setDefaultValue(rs.getString("DEFAULT_VALUE"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		return mapping;
	}
	/**
	 * 查询淘宝属性映射
	 * @param params
	 * @return
	 */
	public List<ChannelAttributeMapping> list(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE,APPLICATION_ID FROM TMALL_CHANNEL_ATTR_MAPPING");
		String buildSql = buildSql(sql, params);
		//buildSql += " LIMIT :OFFSET, :ROWS";
		return super.pageableQueryForList(buildSql, new RowMapper<ChannelAttributeMapping>() {
			@Override
			public ChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractChannelAttributeMapping(rs);
			}
		}, params);
	}
	/**
	 * 查询属性条数
	 * @param params
	 * @return
	 */
	public Long count(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM TMALL_CHANNEL_ATTR_MAPPING");
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
}
