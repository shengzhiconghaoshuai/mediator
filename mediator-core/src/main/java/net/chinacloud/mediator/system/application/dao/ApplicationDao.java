/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ApplicationDao.java
 * 描述： 应用相关DAO
 */
package net.chinacloud.mediator.system.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.ApplicationParam;

import org.springframework.jdbc.core.RowMapper;

/**
 * <应用相关的DAO操作>
 * <应用相关的DAO操作>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
//@Repository
public class ApplicationDao extends DAO {
	/**
	 * 根据应用代码获取应用信息
	 * @param code 应用编号
	 * @return 应用信息
	 */
	public Application getApplicationByCode(String code){
		return null;
	}
	
	/**
	 * 根据应用id获取应用信息
	 * @param applicationId
	 * @return
	 */
	public Application getApplicationById(Integer applicationId){
		final String sql = "SELECT APPLICATION_ID, NAME, CODE, STORE_ID, CHANNEL_ID, CREATE_TIME, APPKEY, APPSECRET, APPURL, SESSIONKEY, VENDOR_ID, VENDOR_NAME, NICK, PRIORITY, STATUS, FIELD1, FIELD2, FIELD3 FROM APPLICATION WHERE APPLICATION_ID = :APPLICATIONID";
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("APPLICATIONID", applicationId);
		return super.queryForObject(sql, params, new RowMapper<Application>() {
			@Override
			public Application mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractApplication(rs);
			}
		});
	}
	
	/**
	 * 保存渠道信息
	 * @param application
	 */
	public void save(Application application) {
		final String sql = "INSERT INTO APPLICATION (NAME, CODE, STORE_ID, CHANNEL_ID, CREATE_TIME, APPKEY, APPSECRET, APPURL, SESSIONKEY, VENDOR_ID, VENDOR_NAME, NICK, PRIORITY, STATUS, FIELD1, FIELD2, FIELD3) "
						+ "VALUES (:NAME, :CODE, :STOREID, :CHANNELID, :CREATETIME, :APPKEY, :APPSECRET, :APPURL, :SESSIONKEY, :VENDORID, :VENDORNAME, :NICK, :PRIORITY, :STATUS, :FIELD1, :FIELD2, :FIELD3)";
		Map<String, Object> params = new HashMap<String, Object>(16);
		params.put("NAME", application.getName());
		params.put("CODE", application.getCode());
		params.put("STOREID", application.getStoreId());
		params.put("CHANNELID", application.getChannelId());
		params.put("CREATETIME", application.getCreateTime());
		params.put("APPKEY", application.getParam().getAppKey());
		params.put("APPSECRET", application.getParam().getAppSecret());
		params.put("APPURL", application.getParam().getAppUrl());
		params.put("SESSIONKEY", application.getParam().getSessionKey());
		params.put("VENDORID", application.getParam().getVendorId());
		params.put("VENDORNAME", application.getParam().getVendorName());
		params.put("NICK", application.getNick());
		params.put("PRIORITY", application.getPriority());
		params.put("STATUS", application.getStatus());
		params.put("FIELD1", application.getParam().getField1());
		params.put("FIELD2", application.getParam().getField2());
		params.put("FIELD3", application.getParam().getField3());
		super.update(sql, params);
	}
	
	/**
	 * 获取所有应用
	 * @return
	 */
	public List<Application> getAllApplications(){
		final String sql = "SELECT APPLICATION_ID, NAME, CODE, STORE_ID, CHANNEL_ID, CREATE_TIME, APPKEY, APPSECRET, APPURL, SESSIONKEY, VENDOR_ID, VENDOR_NAME, NICK, PRIORITY, STATUS, FIELD1, FIELD2, FIELD3 FROM APPLICATION";
		return super.queryForList(sql, new RowMapper<Application>(){
			@Override
			public Application mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractApplication(rs);
			}
		});
	}
	
	/**
	 * 更新应用信息
	 * @param application
	 */
	public void updateApplication(Application application) {
		final String sql = "UPDATE APPLICATION SET NAME = :NAME, STORE_ID = :STOREID, APPKEY = :APPKEY, APPSECRET = :APPSECRET, APPURL = :APPURL, SESSIONKEY = :SESSIONKEY, VENDOR_ID = :VENDORID, VENDOR_NAME = :VENDORNAME, NICK = :NICK, PRIORITY = :PRIORITY, STATUS = :STATUS WHERE APPLICATION_ID = :APPLICATIONID";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("NAME", application.getName());
		params.put("STOREID", application.getStoreId());
		params.put("APPKEY", application.getParam().getAppKey());
		params.put("APPSECRET", application.getParam().getAppSecret());
		params.put("APPURL", application.getParam().getAppUrl());
		params.put("SESSIONKEY", application.getParam().getSessionKey());
		params.put("VENDORID", application.getParam().getVendorId());
		params.put("VENDORNAME", application.getParam().getVendorName());
		params.put("NICK", application.getNick());
		params.put("PRIORITY", application.getPriority());
		params.put("STATUS", application.getStatus());
		params.put("APPLICATIONID", application.getId());
		super.update(sql, params);
	}
	
	/**
	 * 根据渠道id获取对应的应用列表
	 * @param channelId
	 * @return
	 */
	public List<Application> getApplicationsByChannelId(Integer channelId) {
		final String sql = "SELECT APPLICATION_ID, NAME, CODE, STORE_ID, CHANNEL_ID, CREATE_TIME, APPKEY, APPSECRET, APPURL, SESSIONKEY, VENDOR_ID, VENDOR_NAME, NICK, PRIORITY, STATUS, FIELD1, FIELD2, FIELD3 FROM APPLICATION WHERE CHANNEL_ID = :CHANNELID";
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("CHANNELID", channelId);
		return super.queryForList(sql, new RowMapper<Application>(){
			@Override
			public Application mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractApplication(rs);
			}
		}, params);
	}
	
	/**
	 * 根据渠道id获取对应的应用列表
	 * @param channelId 渠道id
	 * @param status 应用状态
	 * @return
	 */
	public List<Application> getApplicationsByChannelId(Integer channelId, Integer status) {
		final String sql = "SELECT APPLICATION_ID, NAME, CODE, STORE_ID, CHANNEL_ID, CREATE_TIME, APPKEY, APPSECRET, APPURL, SESSIONKEY, VENDOR_ID, VENDOR_NAME, NICK, PRIORITY, STATUS, FIELD1, FIELD2, FIELD3 FROM APPLICATION WHERE CHANNEL_ID = :CHANNELID AND STATUS = :STATUS";
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("CHANNELID", channelId);
		params.put("STATUS", status);
		return super.queryForList(sql, new RowMapper<Application>(){
			@Override
			public Application mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractApplication(rs);
			}
		}, params);
	}
	
	/**
	 * 根据store id获取对应的应用信息
	 * @param storeId
	 * @return
	 */
	public Application getApplicationByStoreId(Integer storeId) {
		final String sql = "SELECT APPLICATION_ID, NAME, CODE, STORE_ID, CHANNEL_ID, CREATE_TIME, APPKEY, APPSECRET, APPURL, SESSIONKEY, VENDOR_ID, VENDOR_NAME, NICK, PRIORITY, STATUS, FIELD1, FIELD2, FIELD3 "
				+ "FROM APPLICATION WHERE STORE_ID = ?";
		return super.queryForObject(sql, new RowMapper<Application>() {
			@Override
			public Application mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return extractApplication(rs);
			}
		}, storeId);
	}
	
	/**
	 * 从result中抽取Application对象需要的信息
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected Application extractApplication(ResultSet rs) throws SQLException {
		Application application = new Application();
		application.setId(rs.getInt("APPLICATION_ID"));
		application.setName(rs.getString("NAME"));
		application.setCode(rs.getString("CODE"));
		application.setStoreId(rs.getInt("STORE_ID"));
		application.setChannelId(rs.getInt("CHANNEL_ID"));
		application.setCreateTime(rs.getTimestamp("CREATE_TIME"));
		application.setNick(rs.getString("NICK"));
		application.setPriority(rs.getInt("PRIORITY"));
		application.setStatus(rs.getInt("STATUS"));
		
		//--application param
		ApplicationParam param = new ApplicationParam(
				rs.getString("APPKEY"),
				rs.getString("APPSECRET"),
				rs.getString("APPURL"),
				rs.getString("SESSIONKEY"),
				rs.getString("VENDOR_ID"),
				rs.getString("VENDOR_NAME"),
				rs.getString("FIELD1"),
				rs.getString("FIELD2"),
				rs.getInt("FIELD3")
				);
		application.setParam(param);
		return application;
	}
}
