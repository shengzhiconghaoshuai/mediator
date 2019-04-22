/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskDao.java
 * 描述： Task相关DAO
 */
package net.chinacloud.mediator.task.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.domain.TaskDomain;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * <Task相关DAO操作实现>
 * <Task相关DAO操作实现>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
//@Repository
public class TaskDao extends DAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskDao.class);
	
	/**
	 * 判断是否有重复的task
	 * @param task
	 * @return
	 */
	public Long findRepeatTask(Task task) {
		final String sql = "SELECT COUNT(1) FROM TASK WHERE CHANNEL_ID = :CHANNELID AND APPLICATION_ID = :APPLICATIONID AND TEMPLATE_ID = :TEMPLATEID AND DATAID = :DATAID";
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("CHANNELID", task.getContext().getChannelId());
		params.put("APPLICATIONID", task.getContext().getApplicationId());
		params.put("TEMPLATEID", task.getTemplate().getId());
		params.put("DATAID", task.getDataId());
		return ((Number)super.queryForBasicType(sql, params)).longValue();
	}
	
	/**
	 * 创建task
	 * @param task
	 */
	public void createTask(Task task) {
		final String sql = "INSERT INTO TASK (TEMPLATE_ID, DATAID, DATA, STARTTIME, CHANNEL_ID, APPLICATION_ID, STATUS) "
				+ "VALUES (:TEMPLATEID, :DATAID, :DATA, :STARTTIME, :CHANNELID, :APPLICATIONID, :STATUS)";
		Map<String, Object> params = new HashMap<String, Object>(7);
		params.put("TEMPLATEID", null == task.getTemplate() ? null : task.getTemplate().getId());
		params.put("DATAID", task.getDataId());
		params.put("DATA", JsonUtil.object2JsonString(task.getData()));
		params.put("STARTTIME", task.getStartTime());
		params.put("CHANNELID", task.getContext().getChannelId());
		params.put("APPLICATIONID", task.getContext().getApplicationId());
		params.put("STATUS", task.getStatus());
		
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(sql, new MapSqlParameterSource(params), holder, new String[]{"TASK_ID"});
		
		task.setId(holder.getKey().longValue());
	}

	/**
	 * 更新task状态
	 * @param task
	 */
	public void updateTaskStatus(Task task) {
		final String sql = "UPDATE TASK SET STATUS = ? WHERE TASK_ID = ?";
		super.update(sql, task.getStatus(), task.getId());
	}
	
	/**
	 * 更新task状态
	 * @param taskId task id
	 * @param newStatus 新的状态
	 * @param originalStatus 原来的状态
	 * @param message 错误消息
	 */
	public int updateTaskStatus(Long taskId, Integer newStatus, Integer originalStatus, String message) {
		final String sql = "UPDATE TASK SET STATUS = ?, ERRORMESSAGE = ? WHERE TASK_ID = ? AND STATUS = ?";
		return super.update(sql, newStatus, message, taskId, originalStatus);
	}

	/**
	 * 更新task的data
	 * @param taskDomain
	 * @return
	 */
    public int updateData(TaskDomain task) {
    	final String sql = "UPDATE TASK SET DATA = ? WHERE TASK_ID = ?";
    	return super.update(sql, task.getData(),task.getTaskId());
    }
	
	/**
	 * 更新task状态和结束时间
	 * @param task
	 */
	public void updateTaskStatusAndEndTime(Task task, String message) {
		final String sql = "UPDATE TASK SET STATUS = ?, ENDTIME = ?, ERRORMESSAGE = ? WHERE TASK_ID = ?";
		super.update(sql, task.getStatus(), task.getEndTime(), message, task.getId());
	}
	
	/**
	 * 返回可重跑的task,条件满足:
	 * task状态为TASK_STATUS_HANG || TASK_STATUS_FAIL
	 * task关联的模板的reRun为1
	 * @return
	 */
	public List<Map<String, Object>> getAllReRunTask(Date startDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TASK_ID, TASK.TEMPLATE_ID, TASK_TEMPLATE.TYPE, TASK_TEMPLATE.SUBTYPE, DATAID, DATA,CHANNEL_ID, APPLICATION_ID FROM TASK ")
			.append("LEFT JOIN TASK_TEMPLATE ON TASK_TEMPLATE.TEMPLATE_ID = TASK.TEMPLATE_ID ")
			.append("WHERE TASK.STATUS IN (?, ?) AND TASK_TEMPLATE.RERUN = ? ");
		
		if (null != startDate) {
			sql.append("AND TASK.STARTTIME >= ?");
			return super.queryForList(sql.toString(), Task.TASK_STATUS_HANG, Task.TASK_STATUS_FAIL, 1, startDate);
		}
		
		return super.queryForList(sql.toString(), Task.TASK_STATUS_HANG, Task.TASK_STATUS_FAIL, 1);
	}
	
	/**
	 * 根据dataId和模板查询task
	 * @param dataId dataId
	 * @param templateId 模板id
	 * @param applicationId 应用id
	 * @return
	 */
	public List<Map<String, Object>> find(String dataId, Integer templateId, Integer applicationId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TASK_ID, TASK.TEMPLATE_ID, TASK_TEMPLATE.TYPE, TASK_TEMPLATE.SUBTYPE, DATAID, DATA,CHANNEL_ID, APPLICATION_ID FROM TASK ")
			.append("LEFT JOIN TASK_TEMPLATE ON TASK_TEMPLATE.TEMPLATE_ID = TASK.TEMPLATE_ID ")
			.append("WHERE TASK.DATAID = ? AND TASK_TEMPLATE.TEMPLATE_ID = ? AND TASK.APPLICATION_ID = ?");
		return super.queryForList(sql.toString(), dataId, templateId, applicationId);
	}
	
	/**
	 * task条件查询
	 * @param queryParam
	 * @return
	 */
	public List<TaskDomain> find(Map<String,Object> queryParam) {
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT TASK_ID, CHANNEL.NAME AS CNAME, APPLICATION.NAME AS ANAME, TASK_TEMPLATE.TYPE, TASK_TEMPLATE.SUBTYPE, DATAID, STARTTIME, ENDTIME, TASK.STATUS, TASK.DATA ")
			.append("FROM TASK ")
			.append("LEFT JOIN TASK_TEMPLATE ON TASK.TEMPLATE_ID = TASK_TEMPLATE.TEMPLATE_ID ")
			.append("LEFT JOIN CHANNEL ON CHANNEL.CHANNEL_ID = TASK.CHANNEL_ID ")
			.append("LEFT JOIN APPLICATION ON APPLICATION.APPLICATION_ID = TASK.APPLICATION_ID ");
		String buildSql = buildSql(sql, queryParam);
		LOGGER.info("task query sql:" + buildSql.toString());
		return super.pageableQueryForList(buildSql.toString(),new RowMapper<TaskDomain>() {
			@Override
			public TaskDomain mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractTaskDomain(rs);
			}
		} , queryParam);
	}
	
	public Long count(Map<String,Object> queryParam) {
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(1) ")
			.append("FROM TASK ")
			.append("LEFT JOIN TASK_TEMPLATE ON TASK.TEMPLATE_ID = TASK_TEMPLATE.TEMPLATE_ID ")
			.append("LEFT JOIN CHANNEL ON CHANNEL.CHANNEL_ID = TASK.CHANNEL_ID ")
			.append("LEFT JOIN APPLICATION ON APPLICATION.APPLICATION_ID = TASK.APPLICATION_ID ");
		String buildSql = buildSql(sql, queryParam);
		LOGGER.info("task query sql:" + buildSql.toString());
		return ((Number)super.queryForBasicType(buildSql.toString(), queryParam)).longValue();
	}
	
	
	/**
	 * 根据taskid获取task
	 * @param id
	 * @return
	 */
	public Map<String, Object> getTaskById(Integer id) {
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT TASK_ID, CHANNEL.NAME AS CNAME, APPLICATION.NAME AS ANAME, TASK_TEMPLATE.TYPE, TASK_TEMPLATE.SUBTYPE, DATAID, DATA, STARTTIME, ENDTIME, TASK.STATUS , TASK.CHANNEL_ID, TASK.APPLICATION_ID ")
			.append("FROM TASK ")
			.append("LEFT JOIN TASK_TEMPLATE ON TASK.TEMPLATE_ID = TASK_TEMPLATE.TEMPLATE_ID ")
			.append("LEFT JOIN CHANNEL ON CHANNEL.CHANNEL_ID = TASK.CHANNEL_ID ")
			.append("LEFT JOIN APPLICATION ON APPLICATION.APPLICATION_ID = TASK.APPLICATION_ID ")
			.append("WHERE TASK.TASK_ID = ?");
		return super.queryForMap(sql.toString(), id);
	}
	
	private TaskDomain extractTaskDomain(
			ResultSet rs) throws SQLException {
		TaskDomain taskDomain = new TaskDomain();
		taskDomain.setTaskId(rs.getLong("TASK_ID"));
		taskDomain.setCname(rs.getString("CNAME"));
		taskDomain.setAname(rs.getString("ANAME"));
		taskDomain.setDataId(rs.getString("DATAID"));
		taskDomain.setStartTime(rs.getDate("STARTTIME")+" "+rs.getTime("STARTTIME"));
		taskDomain.setEndTime(rs.getDate("ENDTIME")+" "+rs.getTime("ENDTIME"));
		taskDomain.setStatus(rs.getString("STATUS"));
		taskDomain.setType(rs.getString("TYPE"));
		taskDomain.setSubtype(rs.getString("SUBTYPE"));
		taskDomain.setData(rs.getString("DATA"));
		return taskDomain;
	}
	
	public Long countTaskByStatus(Integer status) {
		final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM TASK WHERE STATUS = ? ");
		return ((Number)super.queryForBasicType(sql.toString(), status)).longValue();
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
					if (entry.getKey().equals("TYPE") || entry.getKey().equals("SUBTYPE")) {
						sql.append("TASK_TEMPLATE.").append(entry.getKey()).append(" = ").append(":").append(entry.getKey());
					}else if (entry.getKey().equals("STARTTIME")) {
						sql.append(entry.getKey()).append(" > ").append(":").append(entry.getKey());
					}else if(entry.getKey().equals("ENDTIME")) {
						sql.append(entry.getKey()).append(" < ").append(":").append(entry.getKey());
					} else {
						sql.append("TASK.").append(entry.getKey()).append(" = ").append(":").append(entry.getKey());	
					}
					if (i < paramSize) {
						sql.append(" AND ");
					}
				}
			}
		}
		return sql.toString();
	}
	
	
	public Integer isExistTask(String dataId, Integer templateId ){
		final String sql = "SELECT COUNT(1) FROM TASK WHERE DATAID = ? AND TEMPLATE_ID = ? ";
		return ((Number)super.queryForBasicType(sql, dataId,templateId)).intValue();
	}
	
}
