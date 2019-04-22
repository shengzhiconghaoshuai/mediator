/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RegistryData.java
 * 描述： 
 */
package net.chinacloud.mediator.system.registry;

/**
 * @description 注册表数据
 * @author yejunwu123@gmail.com
 * @since 2015年7月27日 下午7:32:36
 */
public class RegistryData {
	/**key*/
	private String key;
	/**值*/
	private String value;
	/**描述*/
	private String description;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public RegistryData() {
	}
	public RegistryData(String key,String value,String description) {
		this.key = key;
		this.value = value;
		this.description = description;
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistryData other = (RegistryData) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RegistryData [key=" + key + ", value=" + value
				+ ", description=" + description + "]";
	}
	
}
