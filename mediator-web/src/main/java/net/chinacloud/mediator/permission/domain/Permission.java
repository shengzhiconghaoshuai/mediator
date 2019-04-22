/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.permission.domain;

import java.io.Serializable;

import net.chinacloud.mediator.resource.domain.Operation;
import net.chinacloud.mediator.resource.domain.Resource;

/**
 * @description 权限实体
 * @author ywu@wuxicloud.com
 * 2015年4月30日 下午3:42:19
 */
public class Permission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 标识
	 */
	private Integer id;
	/**
	 * 权限名称,页面使用
	 */
	private String name;
	/**
	 * 标记,程序中使用
	 */
	private String identifier;
	/**
	 * 资源
	 */
	private Resource resource;
	/**
	 * 操作
	 */
	private Operation operation;

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

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
