/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RoleDao.java
 * 描述： 
 */
package net.chinacloud.mediator.role.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import net.chinacloud.mediator.dao.DAO;
import net.chinacloud.mediator.role.domain.Role;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月20日 下午4:16:04
 */
public class RoleDao extends DAO {
	
	public List<Role> getRolesByUserName(String userName) {
		final String sql = "SELECT SYS_ROLE.ID, SYS_ROLE.NAME, SYS_ROLE.IDENTIFIER, SYS_ROLE.DESCRIPTION FROM SYS_USER LEFT JOIN SYS_USER_ROLE ON SYS_USER.ID = SYS_USER_ROLE.USER_ID LEFT JOIN SYS_ROLE ON SYS_USER_ROLE.ROLE_ID = SYS_ROLE.ID WHERE SYS_USER.USERNAME = ?";
		return super.queryForList(sql, new RowMapper<Role>() {
			@Override
			public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
				if (null != rs) {
					return extractRole(rs);
				}
				return null;
			}
		}, userName);
	}
	
	private Role extractRole(ResultSet rs) throws SQLException {
		Role role = new Role();
		role.setId(rs.getInt("ID"));
		role.setName(rs.getString("NAME"));
		role.setIdentifier(rs.getString("IDENTIFIER"));
		role.setDescription(rs.getString("DESCRIPTION"));
		return role;
	}
}
