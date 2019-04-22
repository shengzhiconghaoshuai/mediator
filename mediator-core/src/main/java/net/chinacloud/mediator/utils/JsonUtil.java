/**
 * 版权：Copyright 2014- ChinaCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：JsonUtil.java
 * 描述： 
 */
package net.chinacloud.mediator.utils;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * <json操作相关工具类>
 * <json操作相关工具类,底层使用alibaba的fastjson>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月8日
 * @since 2014年12月8日
 */
public final class JsonUtil {
	//private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	
	/*static{
		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	}*/
	
	/**
	 * 将Object对象序列化成json字符串
	 * 默认日期格式为yyyy-MM-dd HH:mm:ss
	 * @param obj 待序列化对象
	 * @return json字符串
	 */
	public static String object2JsonString(Object obj){
		return JSON.toJSONString(obj,SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
	 * 将json格式的字符串反序列化为Object
	 * @param jsonString json字符串
	 * @param clazz 对象类型
	 * @return
	 */
	public static <T> T jsonString2Object(String jsonString,Class<T> clazz){
		return JSON.parseObject(jsonString, clazz);
	}
	
	/**
	 * 将json格式的字符串反序列化为List
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonString2List(String jsonString,Class<T> clazz) {
		return JSON.parseArray(jsonString, clazz);
	}
	
	/**
	 * 将json格式的字符串反序列化为Map
	 * Map的key为java.lang.String,value为java.lang.Object
	 * @param jsonString json字符串
	 * @return
	 */
	public static Map<String,Object> jsonString2Map(String jsonString){
		return JSON.parseObject(jsonString, new TypeReference<Map<String,Object>>(){});
	}
}
