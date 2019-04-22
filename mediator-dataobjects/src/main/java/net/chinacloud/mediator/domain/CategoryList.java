package net.chinacloud.mediator.domain;

import java.util.List;

/**
 * @description 类目结构
 */
public class CategoryList {
	private Long categoryId;
	private Long parent_cid;
	private String name;
	private List<CategoryList> childrens;
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getParent_cid() {
		return parent_cid;
	}
	public void setParent_cid(Long parent_cid) {
		this.parent_cid = parent_cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<CategoryList> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<CategoryList> childrens) {
		this.childrens = childrens;
	}
	
	
}
