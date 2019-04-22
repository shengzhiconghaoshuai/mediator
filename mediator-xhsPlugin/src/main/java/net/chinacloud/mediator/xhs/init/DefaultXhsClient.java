package net.chinacloud.mediator.xhs.init;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.chinacloud.mediator.utils.StringUtils;
import net.chinacloud.mediator.xhs.exception.XhsException;
import net.chinacloud.mediator.xhs.request.XhsRequest;
import net.chinacloud.mediator.xhs.util.SimplehttpClient;

public class DefaultXhsClient implements XhsClient{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultXhsClient.class);

	private String serverUrl;
	private long timestamp;
	private String appKey;
	private String appSecret;
	private String sign;
	private String supplierNo;
	
	public DefaultXhsClient() {
		
	}
	public DefaultXhsClient(String serverUrl, String appKey, String appSecret, String supplierNo) {
		this.serverUrl = serverUrl;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.supplierNo = supplierNo;
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
	
	public String getSupplierNo() {
		return supplierNo;
	}
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}
	@Override
	public Object execute(XhsRequest request) throws XhsException {
		try {
			Map sysParams = request.getSysParams();
			String urlKey = (String) sysParams.get("urlKey");
			String apiMethodName = request.getApiMethodName();
			if(apiMethodName.contains("urlKey")){
				if(StringUtils.hasText(urlKey)){
					apiMethodName = apiMethodName.replaceAll("urlKey", urlKey);
				}else {
					throw new XhsException("urlKey is empty");
				}
			}

			sysParams.remove("urlKey");
			sysParams.put("app-key", appKey);
			Calendar cal = Calendar.getInstance();
			sysParams.put("timestamp", cal.getTimeInMillis()/1000);
			
			Map appJsonParams = request.getAppJsonParams();
			HashMap<String, Object> pmap = new HashMap<String, Object>();
			
			String method = request.getRequestMethod();
			if(method.equals("GET")){
				pmap.putAll(appJsonParams);
			}
			pmap.putAll(sysParams);
			String httpUrl = this.serverUrl + apiMethodName;
			String sign = makeSign(pmap, apiMethodName, appSecret);
			
			sysParams.put("sign", sign);
			String rsp = null;
			
			Iterator it = appJsonParams.entrySet().iterator();
			Map<String, String> sparams = new HashMap<String, String>();
			while (it.hasNext()) {
				Entry obj = (Entry) it.next();
				if(StringUtils.hasText(obj.getValue().toString())){
					sparams.put(obj.getKey().toString(), obj.getValue().toString());
				}
			}
			
			if(method.equals("GET")){
				LOGGER.info("==========xhs request:"+ sparams.toString() + sysParams.toString());
				rsp = SimplehttpClient.doGet(httpUrl, sparams, sysParams);
				LOGGER.info("==========xhs response:"+ rsp);
			}else{
				LOGGER.info("==========xhs request:"+ sparams.toString() + sysParams.toString());
				rsp = SimplehttpClient.doRequest(httpUrl, sparams, sysParams, method);
				LOGGER.info("==========xhs response:"+ rsp);
			}
			
			Class<?> responseClass = request.getResponseClass();

			return JSONObject.parseObject(rsp, responseClass);
		} catch (Exception e) {
			throw new XhsException(e);
		}
	}

	private static String makeSign(Map<String, Object> params, String serviceUrl, String appSecret) {
		String sig = "";
		Object[] keys = params.keySet().toArray();
		Arrays.sort(keys);
		String s1 = "";
		for (int i = 0; i < keys.length; i++) {
			if(params.get(keys[i])!=null){
				s1 += keys[i].toString() + "="
						+ params.get(keys[i]).toString() + "&";
			}
			
		}
		s1 = s1.substring(0, s1.length() - 1);
		sig = MD5(serviceUrl + "?" + s1 + appSecret);
		return sig;
	}
	
	public static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
