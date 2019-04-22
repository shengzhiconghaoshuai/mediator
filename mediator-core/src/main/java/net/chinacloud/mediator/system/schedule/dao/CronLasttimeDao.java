/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CronLasttimeDao.java
 * 描述： 
 */
package net.chinacloud.mediator.system.schedule.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.DAO;

import org.springframework.jdbc.core.RowMapper;

/**
 * <调度时间DAO>
 * <调度时间DAO>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年2月26日
 * @since 2015年2月26日
 */
//@Repository
public class CronLasttimeDao extends DAO {
	/**
	 * 获取上次调度运行的时间
	 * @param applicationId 应用id
	 * @param templateId 模板id
	 * @return
	 */
	public Long getLastime(int applicationId, int templateId) {
		final String sql = "SELECT LASTTIME FROM CRON_LASTTIME WHERE APPLICATION_ID = ? AND TEMPLATE_ID = ?";

		return ((Number)super.queryForBasicType(sql, applicationId, templateId)).longValue();
	}
	
	/**
	 * 更新上次调度运行的时间
	 * @param applicationId 应用id
	 * @param templateId 模板id
	 * @param lasttime 上次运行时间
	 * @return
	 */
	public int updateLasttime(int applicationId, int templateId, long lasttime) {
		final String sql = "UPDATE CRON_LASTTIME SET LASTTIME = ? WHERE APPLICATION_ID = ? AND TEMPLATE_ID = ?";
		return super.update(sql, lasttime, applicationId, templateId);
	}
	
	/**
	 * 插入上次调度运行的时间
	 * @param applicationId 应用id
	 * @param templateId 模板id
	 * @param lasttime 上次运行时间
	 * @return
	 */
	public int insertLasttime(int applicationId, int templateId, long lasttime) {
		final String sql = "INSERT INTO CRON_LASTTIME (APPLICATION_ID, TEMPLATE_ID, LASTTIME) VALUES (?, ?, ?)";
		return super.update(sql, applicationId, templateId, lasttime);
	}
	
	public List<Map<String, Object>> list() {
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT CRON_LASTTIME.APPLICATION_ID, APPLICATION.NAME, CRON_LASTTIME.TEMPLATE_ID, TASK_TEMPLATE.DESCRIPTION, CRON_LASTTIME.LASTTIME FROM CRON_LASTTIME ");
		sql.append("LEFT JOIN APPLICATION ON APPLICATION.APPLICATION_ID = CRON_LASTTIME.APPLICATION_ID ");
		sql.append("LEFT JOIN TASK_TEMPLATE ON TASK_TEMPLATE.TEMPLATE_ID = CRON_LASTTIME.TEMPLATE_ID");
		return super.queryForList(sql.toString(), new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("APPLICATION_ID", rs.getInt("APPLICATION_ID"));
				map.put("NAME", rs.getString("NAME"));
				map.put("TEMPLATE_ID", rs.getInt("TEMPLATE_ID"));
				map.put("DESCRIPTION", rs.getString("DESCRIPTION"));
				map.put("LASTTIME", new Date(rs.getLong("LASTTIME")));
				
				return map;
			}
			
		});
	}
}
