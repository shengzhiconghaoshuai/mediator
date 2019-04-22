package net.chinacloud.mediator.kaola.init;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.chinacloud.mediator.kaola.exception.KaoLaException;
import net.chinacloud.mediator.kaola.request.KaoLaRequest;
import net.chinacloud.mediator.kaola.util.KopUtils;
import net.chinacloud.mediator.kaola.util.SimplehttpClient;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class DefaultKaoLaClient implements KaoLaClient{


		private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKaoLaClient.class);

		private String serverUrl;
		private long timestamp;
		private String appKey;
		private String appSecret;
		private String sign;
		private String sessionKey;
		private String method;
		
		public DefaultKaoLaClient() {
			
		}
		public DefaultKaoLaClient(String serverUrl, String appKey, String appSecret, String supplierNo) {
			this.serverUrl = serverUrl;
			this.appKey = appKey;
			this.appSecret = appSecret;
			this.sessionKey = supplierNo;
		}
		
	
		@Override
		public Object execute(KaoLaRequest request) throws KaoLaException {
			try {
				String url = buildUrl(request);
				String rsp = null;
				String method = request.getRequestMethod();
				Map<String, Object> appJsonParams = request.getAppJsonParams();
				Iterator it = appJsonParams.entrySet().iterator();
				Map<String, String> sparams = new HashMap<String, String>();
				while (it.hasNext()) {
					Entry obj = (Entry) it.next();
					if(StringUtils.hasText(obj.getValue().toString())){
						sparams.put(obj.getKey().toString(), obj.getValue().toString());
					}
				}
				
				if(method.equals("GET")){
					LOGGER.info("==========kaola request:"+ url+sparams.toString());
					rsp = SimplehttpClient.doGet(url, sparams, null);
					LOGGER.info("==========kaola response:"+ rsp);
				}else{
					LOGGER.info("==========kaola request:" + url + sparams.toString());
					rsp = SimplehttpClient.doRequest(url, sparams, null, method);
					LOGGER.info("==========kaola response:"+ rsp);
				}
				
				Class<?> responseClass = request.getResponseClass();
				return JSONObject.parseObject(rsp, responseClass);
			} catch (Exception e) {
				
				throw new KaoLaException(e);
			}
		}
		
		

		private java.lang.String buildUrl(KaoLaRequest request)
				throws Exception {
			Map<String,String> sysParams = request.getSysParams();
			String apiMethodName = request.getApiMethodName();
			sysParams.put("app_key", appKey);
			sysParams.put("method",apiMethodName);
			sysParams.put("access_token",sessionKey);
			Map<String, Object> pmap = new TreeMap<String, Object>();
			Map<String, Object> appJsonParams = request.getAppJsonParams();
			String method = request.getRequestMethod();
			if(method.equals("GET")){
				pmap.putAll(appJsonParams);
			}
			pmap.putAll(sysParams);
			String sign = KopUtils.sign(pmap, appSecret);
			
			sysParams.put("sign", sign);
			sysParams.put("secret", appSecret);
			StringBuilder sb = new StringBuilder(this.serverUrl);
			sb.append("?");
			sb.append(KopUtils.buildQuery(sysParams, "UTF-8"));
			return sb.toString();
		}
		
		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
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

		public String getServerUrl() {
			return serverUrl;
		}
		public void setServerUrl(String serverUrl) {
			this.serverUrl = serverUrl;
		}
		
		public String getAppSecret() {
			return appSecret;
		}
		public void setAppSecret(String appSecret) {
			this.appSecret = appSecret;
		}
		
		public String getSessionKey() {
			return sessionKey;
		}
		public void setSessionKey(String sessionKey) {
			this.sessionKey = sessionKey;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
	}
