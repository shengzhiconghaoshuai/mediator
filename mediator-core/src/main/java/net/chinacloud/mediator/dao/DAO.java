/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DAO.java
 * 描述： 数据库访问基类
 */
package net.chinacloud.mediator.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.dialect.PageSqlGenerator;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * <数据库访问基类>
 * <数据库访问基类,提供一些便利的数据库操作>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
//@Repository
public abstract class DAO {
	//@Autowired
	protected NamedParameterJdbcTemplate jdbcTemplate;
	
	protected PageSqlGenerator pageSqlGenerator;

	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//protected JdbcTemplate jdbcTemplate;
	
	public void setPageSqlGenerator(PageSqlGenerator pageSqlGenerator) {
		this.pageSqlGenerator = pageSqlGenerator;
	}

	@SuppressWarnings("unchecked")
	/**
	 * 返回表中的某一列或记录数,返回类型为java.lang.Number的子类,
	 * 如Long/Integer/Float/BigDecimal
	 * @param sql 查询语句
	 * @param args 参数列表,索引格式
	 * @return
	 */
	protected <T extends Number> T queryForBasicType(String sql, Object... args) {
		SqlRowSet rowSet = jdbcTemplate.getJdbcOperations().queryForRowSet(sql, args);
		//我了个擦,这边rowSet要先next()一下,害我调试了很久
		if (rowSet.next()) {
			return (T)rowSet.getObject(1);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 命名参数查询
	 * 返回表中的某一列或记录数,返回类型为java.lang.Number的子类,
	 * 如Long/Integer/Float/BigDecimal
	 * @param sql 查询语句
	 * @param params 命名参数,key为命名参数的名称,value为参数的值
	 * @return
	 */
	protected <T extends Number> T queryForBasicType(String sql, Map<String, Object> params) {
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, params);
		//我了个擦,这边rowSet要先next()一下,害我调试了很久
		if (rowSet.next()) {
			return (T)rowSet.getObject(1);
		}
		return null;
	}
	
	/**
	 * 命名参数查询
	 * 返回数据库中的一条记录,注意是一条记录
	 * 数据以Map结构封装,key为列名,value为值
	 * @param sql 查询语句
	 * @param params 命名参数
	 * @return Map结构封装的一条记录
	 */
	protected Map<String, Object> queryForMap(String sql, Map<String, Object> params) {
		return jdbcTemplate.queryForMap(sql, params);
	}
	
	/**
	 * 返回数据库中的一条记录,注意是一条记录
	 * 数据以Map结构封装,key为列名,value为值
	 * 查询参数以索引方式传递
	 * @param sql 查询语句
	 * @param args 参数
	 * @return Map结构封装的一条记录
	 */
	protected Map<String, Object> queryForMap(String sql, Object... args) {
		return jdbcTemplate.getJdbcOperations().queryForMap(sql, args);
	}
	
	/**
	 * 命名参数查询
	 * 返回数据库中的一条记录,注意是一条记录
	 * 数据以T类型的对象返回
	 * @param sql 查询语句
	 * @param params 命名参数
	 * @param rowMapper 行映射器
	 * @return T类型的对象
	 */
	protected <T> T queryForObject(String sql, Map<String, Object> params, RowMapper<T> rowMapper) {
		return jdbcTemplate.queryForObject(sql, params, rowMapper);
	}
	
	/**
	 * 返回数据库中的一条记录,注意是一条记录
	 * 数据以T类型的对象返回
	 * 查询参数以索引方式传递
	 * @param sql 查询语句
	 * @param rowMapper 行映射器
	 * @param args 参数列表
	 * @return T类型的对象
	 */
	protected <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
		return jdbcTemplate.getJdbcOperations().queryForObject(sql, rowMapper, args);
	}
	
	/**
	 * 命名参数查询
	 * 返回查询列表
	 * 列表中的每一行记录以Map的格式封装
	 * @param sql 查询语句
	 * @param params 命名参数
	 * @return List列表,List中的每一项是一个Map,对应查询结果中的每一行记录
	 */
	protected List<Map<String, Object>> queryForList(String sql, Map<String, Object> params) {
		return jdbcTemplate.queryForList(sql, params);
	}
	
	/**
	 * 分页命名参数查询
	 * 返回查询列表
	 * 列表中的每一行记录以Map的格式封装
	 * @param sql
	 * @param params
	 * @return
	 */
	protected List<Map<String, Object>> pageableQueryForList(String sql, Map<String, Object> params) {
		String pageableSql = pageSqlGenerator.generatePageableSql(sql, params);
		return queryForList(pageableSql, params);
	}
	
	/**
	 * 返回查询列表
	 * 列表中的每一行记录以Map的格式封装
	 * 查询参数以索引方式传递
	 * @param sql 查询语句
	 * @param args 参数列表
	 * @return List列表,List中的每一项是一个Map,对应查询结果中的每一行记录
	 */
	protected List<Map<String, Object>> queryForList(String sql, Object... args) {
		return jdbcTemplate.getJdbcOperations().queryForList(sql, args);
	}
	
	/**
	 * 返回查询列表
	 * 列表中的每一行记录以T类型的对象格式封装
	 * 查询参数以索引方式传递
	 * @param sql 查询语句
	 * @param rowMapper 行映射器
	 * @param args 参数列表
	 * @return List列表,List中的每一项是一个T类型的对象,对应查询结果中的每一行记录
	 */
	protected <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... args) {
		return jdbcTemplate.getJdbcOperations().query(sql, rowMapper, args);
	}
	
	/**
	 * 命名查询
	 * 返回查询列表
	 * 列表中的每一行记录以T类型的对象格式封装
	 * @param sql 查询语句
	 * @param rowMapper 行映射器
	 * @param params 参数列表
	 * @return List列表,List中的每一项是一个T类型的对象,对应查询结果中的每一行记录
	 */
	protected <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Map<String, Object> params) {
		return jdbcTemplate.query(sql, params, rowMapper);
	}
	
	/**
	 * 分页命名查询
	 * 返回查询列表
	 * 列表中的每一行记录以T类型的对象格式封装
	 * @param sql
	 * @param rowMapper
	 * @param params
	 * @return
	 */
	protected <T> List<T> pageableQueryForList(String sql, RowMapper<T> rowMapper, Map<String, Object> params) {
		String pageableSql = pageSqlGenerator.generatePageableSql(sql, params);
		return jdbcTemplate.query(pageableSql, params, rowMapper);
	}
	
	/**
	 * 命名参数更新
	 * @param sql sql语句
	 * @param params 命名参数
	 * @return 受影响的记录行数
	 */
	public int update(final String sql, Map<String, Object> params) {
		return jdbcTemplate.update(sql, params);
	}
	
	/**
	 * 更新
	 * @param sql sql语句
	 * @param args 参数列表
	 * @return 受影响的记录行数
	 */
	public int update(final String sql, Object... args) {
		return jdbcTemplate.getJdbcOperations().update(sql, args);
	}
	
	/**
	 * 更新,并返回生成的主键
	 * 主要用于insert操作并获取自动生成的记录主键
	 * @param sql sql语句
	 * @param paramSource 参数源
	 * @param holder 主键容器
	 * @return 受影响的记录行数
	 */
	public int update(final String sql, SqlParameterSource paramSource, KeyHolder holder) {
		return jdbcTemplate.update(sql, paramSource, holder);
	}
	
	/**
	 * 批量更行
	 * @param sql sql语句
	 * @param batchValues 批量参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int[] batchUpdate(String sql, List<Map<String, Object>> params) {
		Map<String, Object>[] batchValues = (Map<String, Object>[])params.toArray(new HashMap[params.size()]);
		return jdbcTemplate.batchUpdate(sql, batchValues);
	}
}
