/**
 * Copyright (c) 2015-2015 yejunwu126@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package net.chinacloud.mediator.resource.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @description 资源实体
 * @author ywu@wuxicloud.com
 * 2015年4月30日 下午3:43:43
 */
public class Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4306834431650948692L;
	/**
	 * 标识
	 */
	private Integer id;
	/**
	 * 资源名称,页面中使用
	 */
	private String name;
	/**
	 * 资源标记,程序中使用
	 */
	private String identifier;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 资源图标
	 */
	private String icon;
	/**
	 * 父资源
	 */
	private Resource parent;
	/**
	 * 子资源
	 */
	private Set<Resource> children = new HashSet<Resource>(0);
	/**
     * 点击后前往的地址
     * 菜单才有
     */
    private String url;
	
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public Set<Resource> getChildren() {
		return children;
	}

	public void setChildren(Set<Resource> children) {
		this.children = children;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
