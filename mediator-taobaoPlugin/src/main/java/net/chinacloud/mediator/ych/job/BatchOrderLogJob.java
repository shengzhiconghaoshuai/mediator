package net.chinacloud.mediator.ych.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.job.CronJob;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.ych.YchClient;
import net.chinacloud.mediator.ych.YchProperties;
import net.chinacloud.mediator.ych.dao.QueryOrderDao;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BatchOrderLogJob extends CronJob{
	
	private static Logger LOGGER = LoggerFactory.getLogger(BatchOrderLogJob.class);

	private static final long serialVersionUID = -4738222633773726785L;
	
	protected static Integer PAGESIZE = 50;
	
	protected static String ntime = Long.valueOf(new Date().getTime()).toString();
	
	@Override
	protected void executeInternal(JobExecutionContext context,
			Integer channelId, Integer applicationId)
			throws JobExecutionException {
		QueryOrderDao queryOrderDao = SpringUtil.getBean(QueryOrderDao.class);
		CronLasttimeService lasttimeService = SpringUtil.getBean(CronLasttimeService.class);
		Date lastDate = lasttimeService.getLastime(applicationId, -1);
		List<Order> orders = queryOrderDao.queryOrder(DateUtil.format(lastDate));
		
		if (CollectionUtil.isNotEmpty(orders)) {
			List<String> orderIds = new ArrayList<String>();
			for (Order order : orders) {
				orderIds.add(order.getOrderId().toString());
			}
			
			List<Map<String, String>> orderParamList = constructOrderParam(orderIds);
			List<Map<String, String>> orderSendParamList = constructOrderSendParam(orderIds);
			
			excuteMethod(orderParamList,"order");
			excuteMethod(orderSendParamList,"sendOrder");
		}
		lasttimeService.insertOrUpdateLasttime(applicationId, -1, Long.valueOf(new Date().getTime()));
		LOGGER.info("ych日志上传成功!");
	}
	
	private List<Map<String, String>> constructOrderParam(List<String> orderIds) {
		List<Map<String, String>> paramList = new ArrayList<Map<String,String>>();
		int startIndex = 0;
		StringBuilder tradeIds = new StringBuilder();
		for (String orderId : orderIds) {
			startIndex ++;
			tradeIds.append(orderId).append(";");
			if (startIndex % PAGESIZE == 0 || startIndex == orderIds.size()) {
				Map<String, String> paramMap = new TreeMap<String, String>();
				paramMap.put("time",Long.valueOf(new Date().getTime()).toString());
				paramMap.put("ati", YchProperties.ati);
				paramMap.put("userId", YchProperties.userId);
				paramMap.put("userIp", YchProperties.userIp);
				paramMap.put("appName", YchProperties.appName);
				paramMap.put("url", YchProperties.url);
				paramMap.put("tradeIds", tradeIds.toString());
				paramMap.put("operation", YchProperties.operation);
				paramMap.put("topAppkey", YchProperties.topAppkey);
				paramList.add(paramMap);
				tradeIds.setLength(0);
			} 
		}
		return paramList;
	}
	
	private List<Map<String, String>> constructOrderSendParam(List<String> orderIds) {
		List<Map<String, String>> paramList = new ArrayList<Map<String,String>>();
		int startIndex = 0;
		StringBuilder tradeIds = new StringBuilder();
		for (String orderId : orderIds) {
			startIndex ++;
			tradeIds.append(orderId).append(";");
			if (startIndex % PAGESIZE == 0 || startIndex == orderIds.size()) {
				Map<String, String> paramMap = new TreeMap<String, String>();
				paramMap.put("time",Long.valueOf(new Date().getTime()).toString());
				paramMap.put("ati", YchProperties.ati);
				paramMap.put("userId",YchProperties.userId);
				paramMap.put("userIp", YchProperties.userIp);
				paramMap.put("appName", YchProperties.appName);
				paramMap.put("url", YchProperties.url);
				paramMap.put("tradeIds", tradeIds.toString());
				paramMap.put("operation", YchProperties.operation);
				paramMap.put("topAppkey", YchProperties.topAppkey);
				paramMap.put("sendTo", YchProperties.sendTo);
				paramList.add(paramMap);
				tradeIds.setLength(0);
			} 
		}
		return paramList;
	}
	
	private void excuteMethod(List<Map<String, String>> ParamList,String method){
		JSONArray jsonObj = (JSONArray) JSONArray.toJSON(ParamList);
		TreeMap<String, String> paramNew = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		paramNew.put("appkey", YchProperties.appkey);
		paramNew.put("topAppkey", YchProperties.topAppkey);
		paramNew.put("method", method);
		paramNew.put("format", "json");
		paramNew.put("time", Long.valueOf(new Date().getTime()).toString());
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
