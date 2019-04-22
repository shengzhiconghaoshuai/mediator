/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.user.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.chinacloud.mediator.role.domain.Role;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @description 登陆用户实体
 * @author ywu@wuxicloud.com
 * 2015年4月30日 下午4:52:22
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4998157448363739147L;
	/**
	 * 标识
	 */
	private Integer id;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 盐
	 */
	private String salt;
	/**
	 * 创建时间
	 */
	private Date createTime = new Date();
	/**
	 * 更新时间
	 */
	private Date lastUpdate = new Date();
	/**
	 * 是否是管理员
	 */
	private boolean admin = false;
	/**
	 * 是否已删除
	 */
	private Integer deleted;
	/**
	 * 角色列表
	 */
	private Set<Role> roles = new HashSet<Role>(0);
	
	public User() {
		
	}
	
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public Integer getId() {
		return id;
	}
	
	public boolean exist() {
		if (null == getId()) {
			return false;
		}
		return true;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Role> getRoles() {
		return Collections.unmodifiableSet(roles);
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	/**
	 * 添加角色
	 * @param role
	 */
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	/**
	 * 设置随机盐值
	 */
	public void randomSalt() {
		setSalt(RandomStringUtils.randomAlphanumeric(10));
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
}
