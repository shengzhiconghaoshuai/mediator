package net.chinacloud.mediator.ych;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YchProperties {

	private static Logger LOGGER = LoggerFactory.getLogger(YchProperties.class);

	public static String isVerifyPassedUrl;

	public static String getVerifyUrl;

	public static String batchLogUrl;

	public static String loginUrl;

	public static String topUrl;

	public static String computeRiskUrl;

	public static String ati;

	public static String userIp;

	public static String userId;

	public static String appName;

	public static String url;

	public static String operation;

	public static String appkey;

	public static String secret;

	public static String sendTo;

	public static String topAppkey;

	public static String db;

	public static String mobile;

	public static String redirectURL;

	public static String sendOrderUrl;

	public static String askOrderUrl;

	static {
		try {
			LOGGER.info("初始化ych参数...");
			PropertiesUtil p = new PropertiesUtil("ych.properties");
			sendOrderUrl = p.readValue("ych.sendOrderUrl");
			batchLogUrl = p.readValue("ych.batchLogUrl");
			loginUrl = p.readValue("ych.loginUrl");
			computeRiskUrl = p.readValue("ych.computeRiskUrl");
			userIp = p.readValue("ych.userIp");
			appName = p.readValue("ych.appName");
			url = p.readValue("ych.url");
			operation = p.readValue("ych.operation");
			appkey = p.readValue("ych.appkey");
			secret = p.readValue("ych.secret");
			sendTo = p.readValue("ych.sendTo");
			topAppkey = p.readValue("ych.topAppkey");
			db = p.readValue("ych.db");
			mobile = p.readValue("ych.mobile");
			redirectURL = p.readValue("ych.redirectURL");
			getVerifyUrl = p.readValue("ych.getVerifyUrl");
			isVerifyPassedUrl = p.readValue("ych.isVerifyPassed");
			topUrl = p.readValue("ych.topUrl");
			askOrderUrl = p.readValue("ych.askOrderUrl");
			ati = p.readValue("ych.ati");
			userId = p.readValue("ych.userId");
		} catch (IOException e) {
			LOGGER.error("初始化ych参数错误...");
		}
	}

}
