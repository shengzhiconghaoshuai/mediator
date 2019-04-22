package net.chinacloud.mediator.ych.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.domain.Order;

import org.springframework.jdbc.core.RowMapper;

public class QueryOrderDao extends DAO{
	
	public List<Order> queryOrder(String starttime){
		final String sql = "SELECT DATAID FROM TASK WHERE STARTTIME > ? AND TEMPLATE_ID = 2 ";
		return super.queryForList(sql,new RowMapper<Order>() {
			@Override
			public Order mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				if (null != rs) {
					return extractOrder(rs);
				}
				return null;
			}},starttime);
	}

	protected Order extractOrder(ResultSet rs) throws SQLException {
		Order order = new Order();
		order.setOrderId(Long.valueOf(rs.getString("DATAID")));
		return order;
	}

}
