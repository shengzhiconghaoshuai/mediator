/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderSyncDao.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class OrderSyncDao extends DAO {
	
	public List<JdpTrade> list(String sellerNick, String status, int rows) {
		final String sql = "SELECT ID, TID, STATUS, SELLER_NICK FROM JDP_TB_TRADE_TEMP WHERE SELLER_NICK = ? AND STATUS = ? AND FLAG = 0 ORDER BY JDP_MODIFIED ASC LIMIT ?";
		return super.queryForList(sql, new RowMapper<JdpTrade>() {
			@Override
			public JdpTrade mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				JdpTrade trade = new JdpTrade();
				trade.setId(rs.getInt("ID"));
				trade.setTid(rs.getLong("TID"));
				trade.setStatus(rs.getString("STATUS"));
				trade.setSellerNick(rs.getString("SELLER_NICK"));
				return trade;
			}
		}, sellerNick, status, rows);
	}
	
	/**
	 * 批量将jdpTrade数据存入临时表
	 * @param params
	 */
	public void batchSaveJdpTrades(List<Map<String, Object>> params) {
		final String sql = "INSERT INTO JDP_TB_TRADE_TEMP (TID, STATUS, SELLER_NICK, JDP_MODIFIED, FLAG) VALUES (:TID, :STATUS, :SELLER_NICK, :JDP_MODIFIED, 0)";
		super.batchUpdate(sql, params);
	}
	
	/**
	 * 更新成功标记
	 * @param flag
	 * @param id
	 * @return
	 */
	public int updateFlag(int flag, int id) {
		final String sql = "UPDATE JDP_TB_TRADE_TEMP SET FLAG = ? WHERE ID = ?";
		return super.update(sql, flag, id);
	}
}
