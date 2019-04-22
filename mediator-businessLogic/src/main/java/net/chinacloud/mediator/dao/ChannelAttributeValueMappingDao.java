/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelAttributeValueMappingDao.java
 * 描述： 
 */
package net.chinacloud.mediator.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.ChannelAttributeValueMapping;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @description 渠道属性值映射DAO
 * @author yejunwu123@gmail.com
 * @since 2015年6月28日 上午11:19:59
 */
//@Repository
public class ChannelAttributeValueMappingDao extends DAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelAttributeValueMappingDao.class);
	/**
	 * 根据渠道类目id、渠道属性编号、系统属性值获取渠道属性值
	 * @param channelCategoryId 渠道类目id
	 * @param channelAttributeId 渠道属性编号
	 * @param attributeValue 系统属性值
	 * @param applicationId 应用id
	 * @return
	 */
	public ChannelAttributeValueMapping getChannelAttributeValueMapping(Long channelCategoryId, String channelAttributeId, String attributeValue, Integer applicationId) {
		String sql = "SELECT MAPPING_ID, CHANNEL_CAT_ID, CHANNEL_ATTR_ID, CHANNEL_ATTRVAL_ID, CHANNEL_ATTRVAL, ATTR_CODE, ATTRVAL_CODE, ATTRVAL, APPLICATION_ID FROM CHANNEL_ATTRVAL_MAPPING WHERE CHANNEL_CAT_ID = ? AND CHANNEL_ATTR_ID = ? AND ATTRVAL = ? AND APPLICATION_ID = ?";
		return super.queryForObject(sql, new RowMapper<ChannelAttributeValueMapping>() {
			@Override
			public ChannelAttributeValueMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractChannelAttributeValueMapping(rs);
				}
				return null;
			}
		}, channelCategoryId, channelAttributeId, attributeValue, applicationId);
	}
	
	/**
	 * 保存一个ChannelAttributeValueMapping
	 */
	public int saveChannelAttributeValueMapping(ChannelAttributeValueMapping channelAttributeValueMapping){
		String sql = "INSERT INTO CHANNEL_ATTRVAL_MAPPING(CHANNEL_CAT_ID,CHANNEL_ATTR_ID,CHANNEL_ATTRVAL_ID,CHANNEL_ATTRVAL,ATTR_CODE,ATTRVAL_CODE,ATTRVAL,APPLICATION_ID) VALUES(?,?,?,?,?,?,?,?)";
	    return super.update(sql, channelAttributeValueMapping.getChannelCategoryId(),channelAttributeValueMapping.getChannelAttributeId(),channelAttributeValueMapping.getChannelAttributeValueId(),
	    		channelAttributeValueMapping.getChannelAttributeValue(),channelAttributeValueMapping.getAttributeCode(),channelAttributeValueMapping.getAttributeValueCode(),channelAttributeValueMapping.getAttributeValue(),
	    		channelAttributeValueMapping.getApplicationId());
	}
	
	/**
	 * 更新渠道属性值映射
	 * @param channelAttributeValueMapping
	 * @return
	 */
	public int updateChannelAttrvalMapping(ChannelAttributeValueMapping channelAttributeValueMapping){
		String sql ="UPDATE CHANNEL_ATTRVAL_MAPPING SET ATTR_CODE = ? , ATTRVAL_CODE= ? , ATTRVAL= ? WHERE MAPPING_ID = ? ";
		return super.update(sql, channelAttributeValueMapping.getAttributeCode(),channelAttributeValueMapping.getAttributeValueCode(),channelAttributeValueMapping.getAttributeValue(),channelAttributeValueMapping.getId());
	}
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	public int deleteChannelAttrvalMapping(Integer id){
		String sql = "DELETE FROM CHANNEL_ATTRVAL_MAPPING WHERE MAPPING_ID = ?";
		return super.update(sql, id);
	}
	
	/**
	 * 加载ChannelAttributeValueMapping
	 * @param applicationId
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public List<ChannelAttributeValueMapping> loadChannelAttributeValueMapping(Map<String, Object> params){
		final StringBuilder sql = new StringBuilder("SELECT MAPPING_ID,CHANNEL_CAT_ID,CHANNEL_ATTR_ID,CHANNEL_ATTRVAL_ID,CHANNEL_ATTRVAL,ATTR_CODE,ATTRVAL_CODE,ATTRVAL,APPLICATION_ID FROM CHANNEL_ATTRVAL_MAPPING");
		String buildSql = buildSql(sql, params);
		//buildSql += " LIMIT :OFFSET, :ROWS";
		return super.pageableQueryForList(buildSql, new RowMapper<ChannelAttributeValueMapping>() {
			@Override
			public ChannelAttributeValueMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractChannelAttributeValueMapping(rs);
			}
		}, params);
	}
	
	public Long count(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM CHANNEL_ATTRVAL_MAPPING");
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
		LOGGER.debug(sql.toString());
		return sql.toString();
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	protected ChannelAttributeValueMapping extractChannelAttributeValueMapping(
			ResultSet rs) throws SQLException {
		ChannelAttributeValueMapping mapping = new ChannelAttributeValueMapping();
		mapping.setId(rs.getInt("MAPPING_ID"));
		mapping.setChannelCategoryId(rs.getLong("CHANNEL_CAT_ID"));
		mapping.setChannelAttributeId(rs.getString("CHANNEL_ATTR_ID"));
		mapping.setChannelAttributeValueId(rs.getString("CHANNEL_ATTRVAL_ID"));
		mapping.setChannelAttributeValue(rs.getString("CHANNEL_ATTRVAL"));
		mapping.setAttributeCode(rs.getString("ATTR_CODE"));
		mapping.setAttributeValueCode(rs.getString("ATTRVAL_CODE"));
		mapping.setAttributeValue(rs.getString("ATTRVAL"));
		mapping.setApplicationId(rs.getInt("APPLICATION_ID"));
		return mapping;
	}
	
}
