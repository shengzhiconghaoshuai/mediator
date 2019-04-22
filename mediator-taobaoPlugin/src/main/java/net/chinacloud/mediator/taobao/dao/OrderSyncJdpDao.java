/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSyncDao.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.taobao.domain.JdpTrade;

import org.springframework.jdbc.core.RowMapper;

/**
 * @description 数据推送DAO
 * @author yejunwu123@gmail.com
 * @since 2015年7月11日 下午5:34:09
 */
public class OrderSyncJdpDao extends DAO {
	
	public List<JdpTrade> list(Date startModified, Date endModified, String sellerNick, int offset, int rows) {
		final String sql = "SELECT TID, STATUS, SELLER_NICK, JDP_MODIFIED, JDP_RESPONSE "
						+ "FROM JDP_TB_TRADE WHERE JDP_MODIFIED >= ? AND JDP_MODIFIED < ? AND SELLER_NICK = ? "
						+ "ORDER BY JDP_MODIFIED DESC LIMIT ?, ?";
		return super.queryForList(sql, new RowMapper<JdpTrade>() {
			@Override
			public JdpTrade mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				JdpTrade trade = new JdpTrade();
				trade.setTid(rs.getLong("TID"));
				trade.setStatus(rs.getString("STATUS"));
				trade.setSellerNick(rs.getString("SELLER_NICK"));
				trade.setJdpResponse(rs.getString("JDP_RESPONSE"));
				return trade;
			}
		}, startModified, endModified, sellerNick, offset, rows);
	}
	
	public JdpTrade getJdpTrade(Long tid) {
		final String sql = "SELECT TID, STATUS, SELLER_NICK, JDP_MODIFIED, JDP_RESPONSE FROM JDP_TB_TRADE WHERE TID = ?";
		return super.queryForObject(sql, new RowMapper<JdpTrade>() {
			@Override
			public JdpTrade mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				JdpTrade trade = new JdpTrade();
				trade.setTid(rs.getLong("TID"));
				trade.setStatus(rs.getString("STATUS"));
				trade.setSellerNick(rs.getString("SELLER_NICK"));
				trade.setJdpResponse(rs.getString("JDP_RESPONSE"));
				return trade;
			}
		}, tid);
	}
	
	public List<JdpTrade> listTid(Date startModified, Date endModified, String sellerNick, int offset, int rows) {
		final String sql = "SELECT TID, STATUS "
						+ "FROM JDP_TB_TRADE WHERE JDP_MODIFIED >= ? AND JDP_MODIFIED < ? AND SELLER_NICK = ? "
						+ "ORDER BY JDP_MODIFIED DESC LIMIT ?, ?";
		return super.queryForList(sql, new RowMapper<JdpTrade>() {
			@Override
			public JdpTrade mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				JdpTrade trade = new JdpTrade();
				trade.setTid(rs.getLong("TID"));
				trade.setStatus(rs.getString("STATUS"));
				return trade;
			}
		}, startModified, endModified, sellerNick, offset, rows);
	}
	
	public List<Map<String, Object>> listByMap(Date startModified, Date endModified, String sellerNick, int offset, int rows) {
		final String sql = "SELECT TID, STATUS, SELLER_NICK, JDP_MODIFIED, JDP_RESPONSE "
						+ "FROM JDP_TB_TRADE WHERE JDP_MODIFIED > ? AND JDP_MODIFIED <= ? AND SELLER_NICK = ? "
						+ "ORDER BY JDP_MODIFIED DESC LIMIT ?, ?";
		return super.queryForList(sql, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> trade = new HashMap<String, Object>();
				trade.put("TID", rs.getLong("TID"));
				trade.put("STATUS", rs.getString("STATUS"));
				trade.put("SELLER_NICK", rs.getString("SELLER_NICK"));
				trade.put("JDP_RESPONSE", rs.getString("JDP_RESPONSE"));
				return trade;
			}
		}, startModified, endModified, sellerNick, offset, rows);
	}
	
	public Long count(Date startModified, Date endModified, String sellerNick) {
		final String sql = "SELECT COUNT(1) FROM JDP_TB_TRADE WHERE JDP_MODIFIED >= ? AND JDP_MODIFIED < ? AND SELLER_NICK = ?";
		return ((Number)super.queryForBasicType(sql, startModified, endModified, sellerNick)).longValue();
	}
	
	public String getJdpResponse(long tid) {
		final String sql = "SELECT JDP_RESPONSE FROM JDP_TB_TRADE WHERE TID = ?";
		return super.queryForObject(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("JDP_RESPONSE");
			}
		}, tid);
	}
}
