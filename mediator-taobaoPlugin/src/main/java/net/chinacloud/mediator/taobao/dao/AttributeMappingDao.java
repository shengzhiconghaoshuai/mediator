package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.taobao.domain.AttributeMapping;

import org.springframework.jdbc.core.RowMapper;

/**
 * @description 渠道属性和OMS映射DAO
 * @author 黄锡涛
 * @since 2015年12月16日
 */
public class AttributeMappingDao extends DAO {
	public AttributeMapping getAttrMapping(Long cid,String channelKey ){
		String sql = "SELECT CID , CHANNELKEY , OMSKEY  FROM ATTRIBUTE_MAPPING WHERE CID = ? AND CHANNELKEY = ?";
		return super.queryForObject(sql, new RowMapper<AttributeMapping>() {
			@Override
			public AttributeMapping mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractAttributeMapping(rs);
				}
				return null;
			}
		}, cid, channelKey);
	}
	
	private AttributeMapping extractAttributeMapping(ResultSet rs) throws SQLException {
		AttributeMapping mapping = new AttributeMapping();
		mapping.setCid(rs.getInt("CID"));
		mapping.setChannelkey(rs.getString("CHANNELKEY"));
		mapping.setOmskey(rs.getString("OMSKEY"));
		return mapping;
	}
}
