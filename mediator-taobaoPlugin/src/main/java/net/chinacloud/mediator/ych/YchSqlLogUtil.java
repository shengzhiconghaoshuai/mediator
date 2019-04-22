package net.chinacloud.mediator.ych;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class YchSqlLogUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(YchSqlLogUtil.class);
	
	public static void uploadYchSqlLog(String sql){
		String ntime = Long.valueOf(new Date().getTime()).toString();
		TreeMap<String, String> param = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		param.put("time",ntime);
		param.put("ati", YchProperties.ati);
		param.put("userId",YchProperties.userId);
		param.put("userIp", YchProperties.userIp);
		param.put("appName", YchProperties.appName);
		param.put("operation", YchProperties.operation);
		param.put("topAppkey", YchProperties.topAppkey);
		param.put("url",YchProperties.url);
		param.put("db",YchProperties.db);
		param.put("sql",sql);
		List<Map<String, String>> paramList = new ArrayList<Map<String,String>>();
		paramList.add(param);
		JSONArray jsonObj = (JSONArray) JSONArray.toJSON(paramList);
		TreeMap<String, String> paramNew = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		paramNew.put("appkey", YchProperties.appkey);
		paramNew.put("topAppkey", YchProperties.topAppkey);
		paramNew.put("method", "sql");
		paramNew.put("format", "json");
		paramNew.put("time", ntime);
		paramNew.put("data", jsonObj.toString());
		YchClient sdk = new YchClient(YchProperties.appkey, YchProperties.secret);
		String rsp = sdk.send(YchProperties.batchLogUrl, paramNew);
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
