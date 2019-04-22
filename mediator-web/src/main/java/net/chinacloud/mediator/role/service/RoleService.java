/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RoleService.java
 * 描述： 
 */
package net.chinacloud.mediator.role.service;

import java.util.List;

import net.chinacloud.mediator.role.dao.RoleDao;
import net.chinacloud.mediator.role.domain.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description 角色业务类
 * @author yejunwu123@gmail.com
 * @since 2015年8月20日 下午4:14:49
 */
@Service
public class RoleService {
	
	@Autowired
	RoleDao roleDao;
	
	public List<Role> getRolesByUserName(String userName) {
		return roleDao.getRolesByUserName(userName);
	}
}
