/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CronConfigDao.java
 * 描述： 调度dao
 */
package net.chinacloud.mediator.system.schedule.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.system.schedule.domain.CronConfig;
import net.chinacloud.mediator.system.schedule.domain.CronParam;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * <调度dao>
 * <调度dao>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月18日
 * @since 2014年12月18日
 */
//@Repository
public class CronConfigDao extends DAO {
	/**
	 * 创建一个cron
	 * @param cronConfig cron配置
	 */
	public CronConfig createCron(CronConfig cronConfig) {
		final String sql = "INSERT INTO CRON_CONFIG (DESCRIPTION, CLASS_NAME, EXPRESSION, IS_GLOBAL, STATUS) "
				+ "VALUES (:DESCRIPTION, :CLASSNAME, :EXPRESSION, :ISGLOBAL, :STATUS)";
		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put("DESCRIPTION", cronConfig.getDescription());
		params.put("CLASSNAME", cronConfig.getClassName());
		params.put("EXPRESSION", cronConfig.getExpression());
		params.put("ISGLOBAL", cronConfig.getGlobal());
		params.put("STATUS", cronConfig.getStatus());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder,new String[]{"CRON_ID"});
		
		int cronId = keyHolder.getKey().intValue();
		cronConfig.setId(cronId);
		
		return cronConfig;
	}
	
	public void createCronParam(CronConfig cronConfig) {
		final String sql = "INSERT INTO CRON_PARAM (CRON_ID, PARAM_NAME, PARAM_VALUE) "
				+ "VALUES (:CRONID, :PARAMNAME, :PARAMVALUE)";
		List<Map<String, Object>> params = new ArrayList<Map<String,Object>>();
		for(CronParam param : cronConfig.getParams()){
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("CRONID", cronConfig.getId());
			temp.put("PARAMNAME", param.getParamName());
			temp.put("PARAMVALUE", param.getParamValue());
			params.add(temp);
		}
		super.batchUpdate(sql, params);
	}
	/**
	 * 查找指定的cron
	 * @param id cron id
	 * @return
	 */
	public CronConfig findCron(Integer id){
		final String sql = "SELECT CRON_ID, DESCRIPTION, CLASS_NAME, EXPRESSION, IS_GLOBAL, STATUS FROM CRON_CONFIG WHERE CRON_ID = ?";
		return super.queryForObject(sql, new RowMapper<CronConfig>() {
			@Override
			public CronConfig mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractCronConfig(rs);
			}
		}, id);
	}
	
	/**
	 * 获取调度配置对应的参数
	 * @param config
	 * @return
	 */
	public List<CronParam> getCronConfigParams(CronConfig config) {
		final String sql = "SELECT PARAM_NAME, PARAM_VALUE FROM CRON_PARAM WHERE CRON_ID = ?";
		return super.queryForList(sql, new RowMapper<CronParam>(){
			@Override
			public CronParam mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CronParam param = new CronParam(rs.getString("PARAM_NAME"), rs.getString("PARAM_VALUE"));
				return param;
			}
		}, config.getId());
	}
	
	/**
	 * 获取所有调度配置
	 * @return
	 */
	public List<CronConfig> getAllCronConfigs() {
		final String sql = "SELECT CRON_ID, DESCRIPTION, CLASS_NAME, EXPRESSION, IS_GLOBAL, STATUS FROM CRON_CONFIG";
		return super.queryForList(sql, new RowMapper<CronConfig>(){
			@Override
			public CronConfig mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractCronConfig(rs);
			}
		});
	}
	
	/**
	 * 删除调度配置
	 * @param cronId
	 */
	public void delete(int cronId) {
		final String sql = "DELETE FROM CRON_CONFIG WHERE CRON_ID = ?";
		super.update(sql, cronId);
	}
	
	public void deleteCronParam(int cronId) {
		final String sql = "DELETE FROM CRON_PARAM WHERE CRON_ID = ?";
		super.update(sql, cronId);
	}
	
	public void updateCronConfig(CronConfig config) {
		final String sql = "UPDATE CRON_CONFIG SET DESCRIPTION = :DESCRIPTION, CLASS_NAME = :CLASSNAME, EXPRESSION = :EXPRESSION, IS_GLOBAL = :ISGLOBAL, STATUS = :STATUS WHERE CRON_ID = :CRONID";
		Map<String, Object> params = new HashMap<String, Object>(6);
		params.put("DESCRIPTION", config.getDescription());
		params.put("CLASSNAME", config.getClassName());
		params.put("EXPRESSION", config.getExpression());
		params.put("ISGLOBAL", config.getGlobal());
		params.put("STATUS", config.getStatus());
		params.put("CRONID", config.getId());
		super.update(sql, params);
	}
	
	/**
	 * 从result set中抽取CronConfig需要的参数
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected CronConfig extractCronConfig(ResultSet rs) throws SQLException {
		CronConfig cronConfig = new CronConfig();
		cronConfig.setId(rs.getInt("CRON_ID"));
		cronConfig.setDescription(rs.getString("DESCRIPTION"));
		cronConfig.setClassName(rs.getString("CLASS_NAME"));
		cronConfig.setExpression(rs.getString("EXPRESSION"));
		cronConfig.setGlobal(rs.getInt("IS_GLOBAL"));
		cronConfig.setStatus(rs.getInt("STATUS"));
		
		return cronConfig;
	}
}
