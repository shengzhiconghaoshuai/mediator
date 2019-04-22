/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.role.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.chinacloud.mediator.permission.domain.Permission;

/**
 * @description 角色实体
 * @author ywu@wuxicloud.com
 * 2015年4月30日 下午3:00:40
 */
public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 947718668664872149L;
	/**
	 * 标识
	 */
	private Integer id;
	/**
	 * 名称,页面中使用
	 */
	private String name;
	/**
	 * 标记,代码中使用
	 */
	private String identifier;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 权限列表
	 */
	private Set<Permission> permissions = new HashSet<Permission>(0);

	public Integer getId() {
		return id;
	}

	public boolean exist() {
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Permission> getPermissions() {
		return Collections.unmodifiableSet(permissions);
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	/**
	 * 添加权限
	 * @param permission
	 */
	public void addPermission(Permission permission) {
		this.permissions.add(permission);
	}
}
