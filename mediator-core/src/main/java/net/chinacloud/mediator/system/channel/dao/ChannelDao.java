/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ChannelDao.java
 * 描述： 渠道DAO类
 */
package net.chinacloud.mediator.system.channel.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.domain.Channel;

import org.springframework.jdbc.core.RowMapper;

/**
 * <渠道相关DAO操作>
 * <渠道相关DAO操作>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
//@Repository
public class ChannelDao extends DAO {
	/**
	 * 根据渠道标记获取渠道信息
	 * @param code 渠道标记
	 * @return 渠道
	 */
	public Channel getChannelByCode(String code){
		final String sql = "SELECT CHANNEL_ID,CODE,NAME,PRIORITY,STATUS,CREATE_TIME FROM CHANNEL WHERE CODE = :CODE";
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("CODE", code);
		return super.queryForObject(sql, params, new RowMapper<Channel>() {
			@Override
			public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
				if(null != rs){
					return extractChannel(rs);
				}
				return null;
			}
		});
	}
	
	/**
	 * 根据渠道标记获取渠道信息
	 * @param code 渠道标记
	 * @return 渠道
	 */
	public Channel getChannelById(Integer channelId){
		final String sql = "SELECT CHANNEL_ID,CODE,NAME,PRIORITY,STATUS,CREATE_TIME FROM CHANNEL WHERE CHANNEL_ID = :CHANNELID";
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("CHANNELID", channelId);
		return super.queryForObject(sql, params, new RowMapper<Channel>() {
			@Override
			public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
				if(null != rs){
					return extractChannel(rs);
				}
				return null;
			}
		});
	}
	
	/***
	 * 根据状态获取渠道信息
	 * @return 渠道列表
	 */
	public List<Channel> getChannelsByStatus(Integer status){
		final String sql = "SELECT CHANNEL_ID, CODE, NAME, PRIORITY, STATUS, CREATE_TIME FROM CHANNEL WHERE STATUS = ? ORDER BY PRIORITY DESC";
		return super.queryForList(sql, new RowMapper<Channel>(){
			@Override
			public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return extractChannel(rs);
			}
		}, status);
	}
	
	/***
	 * 获取所有渠道
	 * @return 所有渠道列表
	 */
	public List<Channel> getAllChannels(){
		final String sql = "SELECT CHANNEL_ID,CODE,NAME,PRIORITY,STATUS,CREATE_TIME FROM CHANNEL ORDER BY CREATE_TIME ASC";
		return super.queryForList(sql, new RowMapper<Channel>(){
			@Override
			public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return extractChannel(rs);
			}
		});
	}
	
	/**
	 * 保存渠道
	 * @param channel
	 */
	public void save(Channel channel){
		final String sql = "INSERT INTO CHANNEL(CODE, NAME, PRIORITY, STATUS, CREATE_TIME) values (:CODE, :NAME, :PRIORITY, :STATUS, :CREATETIME)";
		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put("CODE", channel.getCode());
		params.put("NAME", channel.getName());
		params.put("PRIORITY", channel.getPriority());
		params.put("STATUS", channel.getStatus());
		params.put("CREATETIME", channel.getCreateTime());
		super.update(sql, params);
	}
	
	/**
	 * 更新渠道信息
	 * @param channel
	 */
	public void updateChannel(Channel channel){
		final String sql = "UPDATE CHANNEL SET NAME = :NAME, PRIORITY = :PRIORITY, STATUS = :STATUS WHERE CHANNEL_ID = :CHANNELID";
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("NAME", channel.getName());
		params.put("PRIORITY", channel.getPriority());
		params.put("STATUS", channel.getStatus());
		params.put("CHANNELID", channel.getId());
		super.update(sql, params);
	}
	
	/**
	 * 从result set中抽取Channel需要的数据
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected Channel extractChannel(ResultSet rs) throws SQLException {
		Channel channel = new Channel();
		channel.setId(rs.getInt("CHANNEL_ID"));
		channel.setCode(rs.getString("CODE"));
		channel.setName(rs.getString("NAME"));
		channel.setPriority(rs.getInt("PRIORITY"));
		channel.setStatus(rs.getInt("STATUS"));
		channel.setCreateTime(rs.getTimestamp("CREATE_TIME"));
		return channel;
	}
}
