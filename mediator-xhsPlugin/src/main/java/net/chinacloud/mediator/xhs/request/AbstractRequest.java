package net.chinacloud.mediator.xhs.request;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest {


	protected String timestamp;
	protected String appKey;
	protected String sign;
	protected String urlKey;

	public AbstractRequest() {
		Calendar cal = Calendar.getInstance();
		timestamp = String.valueOf(cal.getTimeInMillis()/1000l);
	}

	public Map<Object,Object> getSysParams() {
		Map<Object,Object> sysParams = new HashMap<Object,Object>();
		sysParams.put("timestamp", timestamp);
		sysParams.put("sign", sign);
		sysParams.put("app-key", appKey);
		sysParams.put("urlKey", urlKey);
		return sysParams;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

	public String getOtherParams(){
		return null;
	}


}
