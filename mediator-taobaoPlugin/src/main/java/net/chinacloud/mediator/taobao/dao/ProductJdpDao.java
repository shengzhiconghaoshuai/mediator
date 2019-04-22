package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.chinacloud.mediator.dao.DAO;

public class ProductJdpDao  extends DAO {
	public String getItemJdpResponse(long numIid) {
		final String sql = "SELECT JDP_RESPONSE FROM jdp_tb_item WHERE num_iid = ? ";
		return super.queryForObject(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("JDP_RESPONSE");
			}
		}, numIid);
	}
	public String getItemJdpResponse(long numIid,String nick) {
		final String sql = "SELECT JDP_RESPONSE FROM jdp_tb_item WHERE num_iid = ? and nick = ?";
		return super.queryForObject(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("JDP_RESPONSE");
			}
		}, numIid,nick);
	}
}
