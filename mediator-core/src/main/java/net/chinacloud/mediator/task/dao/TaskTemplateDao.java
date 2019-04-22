/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskTemplateDao.java
 * 描述： task模板操作
 */
package net.chinacloud.mediator.task.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.springframework.jdbc.core.RowMapper;
/**
 * <task模板操作>
 * <task模板操作>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月29日
 * @since 2014年12月29日
 */
//@Repository
public class TaskTemplateDao extends DAO {
	
	/**
	 * 查询所有TaskTemplate
	 * @return
	 */
	public List<TaskTemplate> getAllTaskTemplates(Map<String,Object> params){
		final StringBuilder sql = new StringBuilder("SELECT TEMPLATE_ID, DESCRIPTION, TYPE, SUBTYPE, REPEATABLE, RERUN, HUNG, PRIORITY, STATUS FROM TASK_TEMPLATE ");
		buildSql(sql, params);
		sql.append(" ORDER BY PRIORITY DESC ");
		return super.pageableQueryForList(sql.toString(), new RowMapper<TaskTemplate>(){
			@Override
			public TaskTemplate mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractTaskTemplate(rs);
			}
		},params);
	}
	
	/**
	 * 查询TaskTemplate数量
	 * @return
	 */
	public Integer countAllTaskTemplates(Map<String,Object> params) {
		final StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM TASK_TEMPLATE ");
		buildSql(sql,params);
		return ((Number)super.queryForBasicType(sql.toString(),params)).intValue();
	}
	
	
	/**
	 * 保存模板信息
	 * @param template
	 */
	public int save(TaskTemplate template){
		final String sql = "INSERT INTO TASK_TEMPLATE (DESCRIPTION, TYPE, SUBTYPE, REPEATABLE, RERUN, HUNG, PRIORITY, STATUS) "
				+ "VALUES (:DESCRIPTION, :TYPE, :SUBTYPE, :REPEATABLE, :RERUN, :HUNG, :PRIORITY, :STATUS)";
		Map<String, Object> params = new HashMap<String, Object>(8);
		params.put("DESCRIPTION", template.getDescription());
		params.put("TYPE", template.getType());
		params.put("SUBTYPE", template.getSubType());
		params.put("REPEATABLE", template.getRepeatable());
		params.put("RERUN", template.getReRun());
		params.put("HUNG", template.getHung());
		params.put("PRIORITY", template.getPriority());
		params.put("STATUS", template.getStatus());
		return super.update(sql, params);
	}
	
	/**
	 * 根据id删除模版信息
	 * @param id
	 * @return
	 */
	public int delete(int id){
	   final String sql = "DELETE FROM TASK_TEMPLATE  WHERE TEMPLATE_ID = ? ";
	   return super.update(sql,id);
	}
	
	/**
	 * 根据templateId获取模板信息
	 * @param templateId
	 * @return
	 */
	public TaskTemplate getTaskTemplateById(Integer templateId) {
		final String sql = "SELECT TEMPLATE_ID, DESCRIPTION, TYPE, SUBTYPE, REPEATABLE, RERUN, HUNG, PRIORITY, STATUS FROM TASK_TEMPLATE WHERE TEMPLATE_ID = :TEMPLATEID";
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("TEMPLATEID", templateId);
		return super.queryForObject(sql, params, new RowMapper<TaskTemplate>() {
			@Override
			public TaskTemplate mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractTaskTemplate(rs);
			}
		});
	}
	
	/**
	 * 更新
	 * @param template
	 */
	public int update(TaskTemplate template) {
		final String sql = "UPDATE TASK_TEMPLATE SET DESCRIPTION = :DESCRIPTION, TYPE = :TYPE, SUBTYPE = :SUBTYPE, REPEATABLE = :REPEATABLE, RERUN = :RERUN, HUNG = :HUNG, PRIORITY = :PRIORITY, STATUS = :STATUS WHERE TEMPLATE_ID = :TEMPLATEID";
		Map<String, Object> params = new HashMap<String, Object>(9);
		params.put("DESCRIPTION", template.getDescription());
		params.put("TYPE", template.getType());
		params.put("SUBTYPE", template.getSubType());
		params.put("REPEATABLE", template.getRepeatable());
		params.put("RERUN", template.getReRun());
		params.put("HUNG", template.getHung());
		params.put("PRIORITY", template.getPriority());
		params.put("STATUS", template.getStatus());
		params.put("TEMPLATEID", template.getId());
		return super.update(sql, params);
	}
	
	/**
	 * 根据type及subType查询task template
	 * @param type
	 * @param subType
	 * @return
	 */
	public TaskTemplate getTaskTemplateByTypeAndSubType(String type, String subType) {
		final String sql = "SELECT TEMPLATE_ID, DESCRIPTION, TYPE, SUBTYPE, REPEATABLE, RERUN, HUNG, PRIORITY, STATUS FROM TASK_TEMPLATE WHERE TYPE = :TYPE AND SUBTYPE = :SUBTYPE";
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("TYPE", type);
		params.put("SUBTYPE", subType);
		return super.queryForObject(sql, params, new RowMapper<TaskTemplate>() {
			@Override
			public TaskTemplate mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractTaskTemplate(rs);
			}
		});
	}
	
	/**
	 * 获取所有已知的task template的type
	 * @return
	 */
	public List<String> getAllTaskTemplateType() {
		final String sql = "SELECT DISTINCT TYPE FROM TASK_TEMPLATE WHERE STATUS = ?";
		return super.queryForList(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("TYPE");
			}
		}, 1);
	}
	
	/**
	 * 根据task template的type查询所有task template
	 * @param type
	 * @return
	 */
	public List<TaskTemplate> getTaskTemplateByType(String type) {
		final String sql = "SELECT TEMPLATE_ID, DESCRIPTION, TYPE, SUBTYPE, REPEATABLE, RERUN, HUNG, PRIORITY, STATUS FROM TASK_TEMPLATE WHERE TYPE = ?";
		return super.queryForList(sql, new RowMapper<TaskTemplate>() {
			@Override
			public TaskTemplate mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractTaskTemplate(rs);
			}
		}, type);
	}
	
	/**
	 * 从result set中抽取TaskTemplate需要的数据
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected TaskTemplate extractTaskTemplate(ResultSet rs) throws SQLException {
		if(null != rs){
			TaskTemplate template = new TaskTemplate();
			template.setId(rs.getInt("TEMPLATE_ID"));
			template.setDescription(rs.getString("DESCRIPTION"));
			template.setType(rs.getString("TYPE"));
			template.setSubType(rs.getString("SUBTYPE"));
			template.setRepeatable(rs.getInt("REPEATABLE"));
			template.setReRun(rs.getInt("RERUN"));
			template.setHung(rs.getInt("HUNG"));
			template.setPriority(rs.getInt("PRIORITY"));
			template.setStatus(rs.getInt("STATUS"));
			
			return template;
		}
		return null;
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
	
}
