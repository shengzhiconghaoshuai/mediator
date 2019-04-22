package net.chinacloud.mediator.kaola.request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest {
	private final DateFormat sdf;
	protected String timestamp;
	protected String appKey;
	protected String sign;
	protected String sessionKey;
	protected String v;
	

	public AbstractRequest() {
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.timestamp = this.sdf.format(new Date());
		this.v = "1.0";
	}

	public Map<String,String> getSysParams() {
		Map<String,String> sysParams = new HashMap<String,String>();
		sysParams.put("timestamp", timestamp);
		sysParams.put("app_key", appKey);
		sysParams.put("access_token", sessionKey);
		sysParams.put("v", v);
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

	
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getOtherParams(){
		return null;
	}


}
