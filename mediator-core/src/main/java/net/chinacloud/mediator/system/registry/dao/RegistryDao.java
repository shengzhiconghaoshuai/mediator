/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RegistryDao.java
 * 描述： 
 */
package net.chinacloud.mediator.system.registry.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.RowMapper;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.system.registry.RegistryData;
import net.chinacloud.mediator.utils.CollectionUtil;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月27日 下午7:35:01
 */
public class RegistryDao extends DAO {
	
	public int addRegistry(RegistryData registryData) {
		final String sql = "INSERT INTO REGISTRYDATA (REGISTRY_KEY, REGISTRY_VALUE, REGISTRY_DESCRIPTION ) VALUES (?, ?, ?)";
		return super.update(sql, registryData.getKey(), registryData.getValue(), registryData.getDescription());
	}
	
	public int updateRegistry(RegistryData registryData) {
		final String sql = "UPDATE REGISTRYDATA SET REGISTRY_VALUE = ?, REGISTRY_DESCRIPTION = ? WHERE REGISTRY_KEY = ? ";
		return super.update(sql, registryData.getValue(), registryData.getDescription(), registryData.getKey());
	}
	
	public RegistryData getRegistry(String key) {
		final String sql = "SELECT REGISTRY_KEY, REGISTRY_VALUE, REGISTRY_DESCRIPTION FROM REGISTRYDATA WHERE REGISTRY_KEY = ?";
		return super.queryForObject(sql, new RowMapper<RegistryData>() {
			@Override
			public RegistryData mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractRegistryData(rs);
				}
				return null;
			}
		}, key);
	}
	
	public List<RegistryData> getRegistries(Map<String, Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT REGISTRY_KEY, REGISTRY_VALUE, REGISTRY_DESCRIPTION FROM REGISTRYDATA");
		buildSql(sql, params);
		return super.pageableQueryForList(sql.toString(), new RowMapper<RegistryData>() {
			@Override
			public RegistryData mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractRegistryData(rs);
				}
				return null;
			}
		},params);
	}
	
	public Integer removeRegistry(String key){
		final StringBuilder sql = new StringBuilder(" DELETE FROM REGISTRYDATA WHERE REGISTRY_KEY = ?");
		return super.update(sql.toString(), key);
		
	}
	
	public Long countRegistries(Map<String,Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM REGISTRYDATA ");
		return ((Number)super.queryForBasicType(buildSql(sql, params), params)).longValue();
	}
	
	
	private String buildSql(StringBuilder sql, Map<String, Object> params) {
		if (!CollectionUtil.isEmpty(params)) {
			if(params.size()>2){
				sql.append(" WHERE ");
			}
			int i = 0;
			int paramSize = params.size() -  2;
			for (Entry<String, Object> entry : params.entrySet()) {
				if (!entry.getKey().equalsIgnoreCase(Constant.PAGE_START_INDEX) && !entry.getKey().equalsIgnoreCase(Constant.PAGE_END_INDEX)) {
					i++;
					sql.append(entry.getKey()).append(" LIKE ").append(":").append(entry.getKey());
					if (i < paramSize) {
						sql.append(" AND ");
					}
				}
			}
		}
		return sql.toString();
	}
	
	private RegistryData extractRegistryData(ResultSet rs) throws SQLException {
		RegistryData data = new RegistryData();
		data.setKey(rs.getString("REGISTRY_KEY"));
		data.setValue(rs.getString("REGISTRY_VALUE"));
		data.setDescription(rs.getString("REGISTRY_DESCRIPTION"));
		return data;
	}
}
