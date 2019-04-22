package net.chinacloud.mediator.vip.vop.domain;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2016年3月9日 上午10:30:12
 */
public class JitDeliveryConfig {

	private int id;
	
	private String key;
	
	private String value;
	
	private int parentId;
	
	private String description;
	
	private String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
