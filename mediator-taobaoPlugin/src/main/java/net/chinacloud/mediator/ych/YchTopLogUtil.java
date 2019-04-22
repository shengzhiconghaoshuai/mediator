package net.chinacloud.mediator.ych;

import java.util.Date;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;

public class YchTopLogUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(YchTopLogUtil.class);
	
	public static void uploadYchTopLog(TaobaoRequest<TaobaoResponse> request){
		String ntime = Long.valueOf(new Date().getTime()).toString();
		TreeMap<String, String> param = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		param.put("time",ntime);
		param.put("ati", YchProperties.ati);
		param.put("userId",YchProperties.userId);
		param.put("userIp", YchProperties.userIp);
		param.put("appName", YchProperties.appName);
		param.put("operation", YchProperties.operation);
		param.put("topAppkey", YchProperties.topAppkey);
		param.put("appKey", YchProperties.appkey);
		param.put("url", request.getApiMethodName()+request.getTextParams().toString());
		YchClient sdk = new YchClient(YchProperties.appkey, YchProperties.secret);
		String rsp = sdk.send(YchProperties.topUrl, param);
		parseResult(rsp);
	}
	
	
	private static void parseResult(String rsponse) {
    	String result = "";	
		String errMsg = "";
		if (rsponse != null) {
			JSONObject obj = null;
			try {
				obj = (JSONObject) JSON.parse(rsponse);
			} catch (Exception e) {			
			}
			
			if (obj != null) {
				result = obj.getString("result");
				errMsg = obj.getString("errMsg");
			}
		}
		if ("success".equals(result) && (errMsg == null || errMsg.isEmpty())) {
			LOGGER.info("Ych Success...");
		} else {
			LOGGER.error("Ych Error:" + rsponse);
		}
    }
	
}
