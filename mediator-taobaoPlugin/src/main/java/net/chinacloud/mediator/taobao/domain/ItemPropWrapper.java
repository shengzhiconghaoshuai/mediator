/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ItemPropVal.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.domain;

import com.taobao.api.domain.ItemProp;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月16日 下午2:46:23
 */
public class ItemPropWrapper {
	
	private ItemProp itemProp;
	
	public ItemPropWrapper(ItemProp itemProp) {
		this.itemProp = itemProp;
	}
	
	public Long getCid() {
		return this.itemProp.getCid();
	}

	public Boolean getIsColorProp() {
		return this.itemProp.getIsColorProp();
	}

	public Boolean getIsEnumProp() {
		return this.itemProp.getIsEnumProp();
	}

	public Boolean getIsInputProp() {
		return this.itemProp.getIsInputProp();
	}

	public Boolean getIsItemProp() {
		return this.itemProp.getIsItemProp();
	}

	public Boolean getIsKeyProp() {
		return this.itemProp.getIsKeyProp();
	}

	public Boolean getIsSaleProp() {
		return this.itemProp.getIsSaleProp();
	}

	public Boolean getMulti() {
		return this.itemProp.getMulti();
	}

	public Boolean getMust() {
		return this.itemProp.getMust();
	}

	public String getName() {
		return this.itemProp.getName();
	}

	public Long getParentPid() {
		return this.itemProp.getParentPid();
	}

	public Long getParentVid() {
		return this.itemProp.getParentVid();
	}

	public Long getPid() {
		return this.itemProp.getPid();
	}

	public Boolean getRequired() {
		return this.itemProp.getRequired();
	}

	public String getStatus() {
		return this.itemProp.getStatus();
	}

	public String getType() {
		return this.itemProp.getType();
	}
	
	public boolean getIsParent() {
		return true;
	}
}
