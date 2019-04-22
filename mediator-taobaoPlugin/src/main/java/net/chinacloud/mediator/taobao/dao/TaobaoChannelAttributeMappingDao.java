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
import net.chinacloud.mediator.taobao.domain.TaobaoChannelAttributeMapping;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.springframework.jdbc.core.RowMapper;

/**
 * @description 渠道属性映射DAO
 * @author yejunwu123@gmail.com
 * @since 2015年6月28日 上午11:02:43
 */
//@Repository("taobaoChannelAttributeMappingDao")
public class TaobaoChannelAttributeMappingDao extends DAO {
	/**
	 * 添加淘宝属性映射
	 * @param attributeMapping
	 * @return
	 */
	public int saveChannelAttributeMapping(
			TaobaoChannelAttributeMapping attributeMapping) {
		final String sql = "INSERT INTO TB_CHANNEL_ATTR_MAPPING (CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE, KEYPROP, SALEPROP, COLORPROP, ENUMPROP, ITEMPROP, MUST, MULTI, STATUS, INPUTPROP, ATTR_TYPE, REQUIRED, APPLICATION_ID) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return super.update(sql, 
				attributeMapping.getChannelCategoryId(),
				attributeMapping.getChannelAttributeId(),
				attributeMapping.getChannelAttributeName(),
				attributeMapping.getAttributeCode(),
				attributeMapping.getDefaultValue(),
				attributeMapping.getKeyProperty(),
				attributeMapping.getSaleProperty(),
				attributeMapping.getColorProperty(),
				attributeMapping.getEnumprop(),
				attributeMapping.getItemprop(),
				attributeMapping.getMust(),
				attributeMapping.getMulti(),
				attributeMapping.getStatus(),
				attributeMapping.getInputprop(),
				attributeMapping.getAttributeType(),
				attributeMapping.getRequired(),
				attributeMapping.getApplicationId());
	}
	
	/**
	 * 更新淘宝属性映射
	 * @param attributeMapping
	 * @return
	 */
	public int updateChannelAttributeMapping(
			TaobaoChannelAttributeMapping attributeMapping) {
		final String sql = "UPDATE TB_CHANNEL_ATTR_MAPPING SET ATTR_CODE = ?, DEFAULT_VALUE = ? WHERE MAPPING_ID = ?";
		return super.update(sql, 
				attributeMapping.getAttributeCode(),
				attributeMapping.getDefaultValue(),
				attributeMapping.getId());
	}
	
	public TaobaoChannelAttributeMapping getTaobaoChannelAttributeMapping(Long channelCategoryId, String channelAttributeId, Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE,KEYPROP,SALEPROP,COLORPROP, ENUMPROP,ITEMPROP,MUST,MULTI,STATUS,INPUTPROP,ATTR_TYPE,REQUIRED,APPLICATION_ID FROM TB_CHANNEL_ATTR_MAPPING WHERE CHANNEL_CAT_ID = ? AND APPLICATION_ID = ? AND CHANNEL_ATTR_ID = ?";
		return super.queryForObject(sql, new RowMapper<TaobaoChannelAttributeMapping>() {
			@Override
			public TaobaoChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractChannelAttributeMapping(rs);
				}
				return null;
			}
		}, channelCategoryId, applicationId, channelAttributeId);
	}
	/**
	 * 删除淘宝属性映射
	 * @param id
	 * @return
	 */
	public int deleteChannelAttributeMapping(int id) {
		final String sql = "DELETE FROM TB_CHANNEL_ATTR_MAPPING WHERE MAPPING_ID = ?";
		return super.update(sql, id);
	}
	/**
	 * 根据系统属性代号获取对应的渠道属性
	 * @param channelCategoryId 渠道类目id
	 * @param channelAttributeId 渠道属性ID
	 * @param applicationId 应用id
	 * @return
	 */
	public List<TaobaoChannelAttributeMapping> getChannelAttributeMapping(Long channelCategoryId, Integer applicationId, boolean keyProperty) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE,KEYPROP,SALEPROP,COLORPROP, ENUMPROP,ITEMPROP,MUST,MULTI,STATUS,INPUTPROP,ATTR_TYPE,REQUIRED,APPLICATION_ID FROM TB_CHANNEL_ATTR_MAPPING WHERE CHANNEL_CAT_ID = ? AND APPLICATION_ID = ? AND STATUS = 1";
		if (keyProperty) {
			sql += " AND KEYPROP = 1";
		} else {
			sql += " AND SALEPROP = 1";
		}
		return super.queryForList(sql, new RowMapper<TaobaoChannelAttributeMapping>() {
			@Override
			public TaobaoChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractChannelAttributeMapping(rs);
				}
				return null;
			}
		}, channelCategoryId, applicationId);
	}
	
	/**
	 * 根据系统属性代号获取对应的渠道属性
	 * @param channelCategoryId 渠道类目id
	 * @param channelAttributeId 渠道属性ID
	 * @param applicationId 应用id
	 * @return
	 */
	public List<TaobaoChannelAttributeMapping> getAllChannelAttributeMapping(Long channelCategoryId, Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE,KEYPROP,SALEPROP,COLORPROP, ENUMPROP,ITEMPROP,MUST,MULTI,STATUS,INPUTPROP,ATTR_TYPE,REQUIRED,APPLICATION_ID FROM TB_CHANNEL_ATTR_MAPPING WHERE CHANNEL_CAT_ID = ? AND APPLICATION_ID = ? AND STATUS = 1 AND SALEPROP = 0";
		return super.queryForList(sql, new RowMapper<TaobaoChannelAttributeMapping>() {
			@Override
			public TaobaoChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractChannelAttributeMapping(rs);
				}
				return null;
			}
		}, channelCategoryId, applicationId);
	}
	
	/**
	 * 查询淘宝属性映射
	 * @param params
	 * @return
	 */
	public List<TaobaoChannelAttributeMapping> list(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTR_NAME, ATTR_CODE, DEFAULT_VALUE,KEYPROP,SALEPROP,COLORPROP, ENUMPROP,ITEMPROP,MUST,MULTI,STATUS,INPUTPROP,ATTR_TYPE,REQUIRED,APPLICATION_ID FROM TB_CHANNEL_ATTR_MAPPING");
		String buildSql = buildSql(sql, params);
		//buildSql += " LIMIT :OFFSET, :ROWS";
		return super.pageableQueryForList(buildSql, new RowMapper<TaobaoChannelAttributeMapping>() {
			@Override
			public TaobaoChannelAttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractChannelAttributeMapping(rs);
			}
		}, params);
	}
	
	public Long count(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM TB_CHANNEL_ATTR_MAPPING");
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
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private TaobaoChannelAttributeMapping extractChannelAttributeMapping(
			ResultSet rs) throws SQLException {
		TaobaoChannelAttributeMapping mapping = new TaobaoChannelAttributeMapping();
		mapping.setId(rs.getInt("MAPPING_ID"));
		mapping.setChannelCategoryId(rs.getLong("CHANNEL_CAT_ID"));
		mapping.setChannelAttributeId(rs.getString("CHANNEL_ATTR_ID"));
		mapping.setChannelAttributeName(rs.getString("CHANNEL_ATTR_NAME"));
		mapping.setAttributeCode(rs.getString("ATTR_CODE"));
		mapping.setDefaultValue(rs.getString("DEFAULT_VALUE"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		mapping.setKeyProperty(rs.getInt("KEYPROP"));
		mapping.setSaleProperty(rs.getInt("SALEPROP"));
		mapping.setColorProperty(rs.getInt("COLORPROP"));
		mapping.setEnumprop(rs.getInt("ENUMPROP"));;
		mapping.setItemprop(rs.getInt("ITEMPROP"));
		mapping.setMust(rs.getInt("MUST"));
		mapping.setMulti(rs.getInt("MULTI"));
		mapping.setStatus(rs.getInt("STATUS"));
		mapping.setInputprop(rs.getInt("INPUTPROP"));
		mapping.setAttributeType(rs.getInt("ATTR_TYPE"));
		mapping.setRequired(rs.getInt("REQUIRED"));
		return mapping;
	}
}
