/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：UserDao.java
 * 描述： 
 */
package net.chinacloud.mediator.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.user.domain.User;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月19日 下午3:41:35
 */
public class UserDao extends DAO {
	/**
	 * 创建用户
	 * @param user
	 */
	public void save(User user) {
		final String sql = "INSERT INTO SYS_USER (USERNAME, PASSWORD, EMAIL, SALT, CREATETIME, LASTUPDATE, ADMIN, DELETED) "
       		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		super.update(sql, user.getUserName(), user.getPassword(), user.getEmail(), user.getSalt(), user.getCreateTime(), user.getLastUpdate(), user.isAdmin(), user.getDeleted());
	}
	
	public User findByUserName(String userName) {
		final String sql = "SELECT ID, USERNAME, PASSWORD, EMAIL, SALT, CREATETIME, LASTUPDATE, ADMIN, DELETED FROM SYS_USER WHERE USERNAME = ?";
		return super.queryForObject(sql, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				if (null != rs) {
					return extractUser(rs);
				}
				return null;
			}
		}, userName);
	}
	
	private User extractUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("ID"));
		user.setCreateTime(rs.getDate("CREATETIME"));
		user.setDeleted(rs.getInt("DELETED"));
		user.setEmail(rs.getString("EMAIL"));
		user.setAdmin(rs.getBoolean("ADMIN"));
		user.setLastUpdate(rs.getDate("LASTUPDATE"));
		user.setPassword(rs.getString("PASSWORD"));
		user.setSalt(rs.getString("SALT"));
		user.setUserName(rs.getString("USERNAME"));
		return user;
	}
}
