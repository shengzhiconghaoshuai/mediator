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
 * @since 2017年7月20日 下午3:18:38
 */
public class YchOrderLogUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(YchOrderLogUtil.class);

    public static void uploadOrderLog(String tradeId,String userName,String ati){
        TreeMap<String, String> paramNew = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        paramNew.put("ati", ati);
        paramNew.put("userId","江苏太湖云计算:"+userName);
        paramNew.put("userIp", YchProperties.userIp);
        paramNew.put("topAppkey", YchProperties.topAppkey);
        paramNew.put("appName", YchProperties.appName);
        paramNew.put("url", YchProperties.url);
        paramNew.put("tradeIds",tradeId.concat(";"));
        paramNew.put("sendTo", YchProperties.sendTo);
        paramNew.put("appkey", YchProperties.appkey);
        paramNew.put("time", Long.valueOf(new Date().getTime()).toString());
        YchClient sdk = new YchClient(YchProperties.appkey, YchProperties.secret);
        String rsp = sdk.send(YchProperties.sendOrderUrl, paramNew);
        LOGGER.info(JSON.toJSONString(paramNew));
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
