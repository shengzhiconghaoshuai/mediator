package net.chinacloud.mediator.jingdong.domain;

/**
 * 京东属性值映射加载ztree的自定义实体类
 * @author user
 *
 */
public class JdAttValue {
	private long id;
	private long fid;
	private String name;
	private String status;
	private long indexId;
	private String features;
	private String identify;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFid() {
		return fid;
	}

	public void setFid(long fid) {
		this.fid = fid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getIndexId() {
		return indexId;
	}

	public void setIndexId(long indexId) {
		this.indexId = indexId;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

}
