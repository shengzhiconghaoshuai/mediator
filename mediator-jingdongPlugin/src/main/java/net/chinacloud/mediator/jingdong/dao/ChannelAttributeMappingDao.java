/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelAttributeMapping.java
 * 描述： 
 */
package net.chinacloud.mediator.jingdong.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.jingdong.domain.ChannelAttributeMapping;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.springframework.jdbc.core.RowMapper;

/**
 * @description 京东渠道属性映射DAO
 * @author yejunwu123@gmail.com
 * @since 2015年6月29日 上午11:23:12
 */
//@Repository("jdChannelAttributeMappingDao")
public class ChannelAttributeMappingDao extends DAO {
	
	public List<ChannelAttributeMapping> getChannelAttributeMappings(Long channelCategoryId, Integer applicationId, boolean keyProperty) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE, ATTR_TYPE, INPUT_TYPE, KEYPROP, SALEPROP, COLORPROP, SIZEPROP, REQUIRED, APPLICATION_ID, STATUS FROM JD_CHANNEL_ATTR_MAPPING WHERE CHANNEL_CAT_ID = ? AND APPLICATION_ID = ? AND STATUS = 1";
		if (keyProperty) {
			sql += " AND KEYPROP = 1";
		} else {
			sql += " AND SALEPROP = 1";
		}
		return super.queryForList(sql, new RowMapper<ChannelAttributeMapping>() {
			@Override
			public ChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractChannelAttributeMapping(rs);
				}
				return null;
			}
		}, channelCategoryId, applicationId);
	}
	
	public ChannelAttributeMapping getChannelAttributeMappings(Long channelCategoryId, String channelAttributeId,
			Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE, ATTR_TYPE, INPUT_TYPE, KEYPROP, SALEPROP, COLORPROP, SIZEPROP, REQUIRED, APPLICATION_ID, STATUS FROM JD_CHANNEL_ATTR_MAPPING WHERE CHANNEL_CAT_ID = ? AND APPLICATION_ID = ? AND CHANNEL_ATTR_ID = ?";
		return super.queryForObject(sql, new RowMapper<ChannelAttributeMapping>() {
			@Override
			public ChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractChannelAttributeMapping(rs);
			}
		}, channelCategoryId, applicationId, channelAttributeId);
	}

	public List<ChannelAttributeMapping> getChannelAttrMappings(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE, ATTR_TYPE, INPUT_TYPE, KEYPROP, SALEPROP, COLORPROP, SIZEPROP, REQUIRED, APPLICATION_ID, STATUS FROM JD_CHANNEL_ATTR_MAPPING ");
		String buildSql = buildSql(sql, params);
//		buildSql += " LIMIT :OFFSET, :ROWS";
		return super.pageableQueryForList(buildSql, new RowMapper<ChannelAttributeMapping>() {
			@Override
			public ChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractChannelAttributeMapping(rs);
			}
		}, params);
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public int saveChannelAttributeMapping(ChannelAttributeMapping c){
		String sql = "INSERT INTO JD_CHANNEL_ATTR_MAPPING(CHANNEL_CAT_ID,CHANNEL_ATTR_ID,CHANNEL_ATTR_NAME,ATTR_CODE,DEFAULT_VALUE,ATTR_TYPE,INPUT_TYPE,KEYPROP,SALEPROP,COLORPROP,SIZEPROP,REQUIRED,APPLICATION_ID,STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	    return super.update(sql,c.getChannelCategoryId(),c.getChannelAttributeId(),c.getChannelAttributeName(),c.getAttributeCode(),c.getDefaultValue(),c.getAttributeType(),c.getInputType(),c.getKeyProperty(),c.getSaleProperty(),c.getColorProperty(),c.getSizeProperty(),c.getRequired(),c.getApplicationId(),c.getStatus());
	}
	
	/**
	 * 根据id删除jd_channel_attr_mapping
	 * @param id
	 * @return
	 */
	public int deletejdChannelAttrMapping(Integer id){
		String sql = "DELETE FROM JD_CHANNEL_ATTR_MAPPING WHERE MAPPING_ID = ?";
		return super.update(sql, id);
	}
	
	/**
	 * 根据id更新jd_channel_attr_mapping
	 * @param c
	 * @return
	 */
	 
	public int updateChannelAttributeMapping(ChannelAttributeMapping c){
		String sql = "UPDATE JD_CHANNEL_ATTR_MAPPING SET ATTR_CODE = ? , DEFAULT_VALUE = ? WHERE MAPPING_ID = ?";
		return super.update(sql, c.getAttributeCode(),c.getDefaultValue(),c.getId());
	}
	
	public Long count(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM JD_CHANNEL_ATTR_MAPPING");
		return ((Number)super.queryForBasicType(buildSql(sql, params), params)).longValue();
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
		mapping.setAttributeType(rs.getInt("ATTR_TYPE"));
		mapping.setInputType(rs.getInt("INPUT_TYPE"));
		mapping.setKeyProperty(rs.getInt("KEYPROP"));
		mapping.setSaleProperty(rs.getInt("SALEPROP"));
		mapping.setColorProperty(rs.getInt("COLORPROP"));
		mapping.setSizeProperty(rs.getInt("SIZEPROP"));
		mapping.setRequired(rs.getInt("REQUIRED"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		mapping.setStatus(rs.getInt("STATUS"));
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
				if (!entry.getKey().equalsIgnoreCase(Constant.PAGE_START_INDEX) && !entry.getKey().equalsIgnoreCase(Constant.PAGE_END_INDEX)) {
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

	public List<ChannelAttributeMapping> getChannelAttributeMapping(
			Long channelCategoryId, Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE, ATTR_TYPE, INPUT_TYPE, KEYPROP, SALEPROP, COLORPROP, SIZEPROP, REQUIRED, APPLICATION_ID, STATUS FROM JD_CHANNEL_ATTR_MAPPING WHERE CHANNEL_CAT_ID = ? AND APPLICATION_ID = ? AND INPUT_TYPE IN (1,2) AND STATUS = 1";
		return super.queryForList(sql, new RowMapper<ChannelAttributeMapping>() {
			@Override
			public ChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractChannelAttributeMapping(rs);
				}
				return null;
			}
		}, channelCategoryId, applicationId);
	}
}
