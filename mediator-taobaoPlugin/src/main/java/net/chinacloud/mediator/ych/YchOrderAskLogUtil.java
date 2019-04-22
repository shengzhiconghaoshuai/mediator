package net.chinacloud.mediator.ych;

import java.util.Date;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author 张宇 zhangyu@wuxicloud.com
 * @description
 * @since 2017年7月20日 下午3:28:05
 */
public class YchOrderAskLogUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(YchOrderAskLogUtil.class);

    public static void uploadYchOrderAskLog(String tradeId,String userName,String ati){
        TreeMap<String, String> paramMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        paramMap.put("time",Long.valueOf(new Date().getTime()).toString());
        paramMap.put("ati", ati);
        paramMap.put("userId", "江苏太湖云计算:"+userName);
        paramMap.put("userIp", YchProperties.userIp);
        paramMap.put("appName", YchProperties.appName);
        paramMap.put("url", YchProperties.url);
        paramMap.put("tradeIds", tradeId);
        paramMap.put("operation", YchProperties.operation);
        paramMap.put("topAppkey", YchProperties.topAppkey);
        paramMap.put("appkey", YchProperties.appkey);
        YchClient sdk = new YchClient(YchProperties.appkey, YchProperties.secret);
        String rsp = sdk.send(YchProperties.askOrderUrl, paramMap);
        LOGGER.info(JSON.toJSONString(paramMap));
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
