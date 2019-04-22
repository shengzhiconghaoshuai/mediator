/**
 * 版权：Copyright 2014- ChinaCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MD5Util.java
 * 描述： MD5签名工具类
 */
package net.chinacloud.mediator.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <MD5签名工具类>
 * <MD5签名工具类,提供字符串的MD5签名及获取随机MD5签名功能>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月8日
 * @since 2014年12月8日
 */
public final class MD5Util {
	private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);
	/**
	 * 获取字符串的MD5签名,source编码为UTF-8
	 * @param source 待签名字符串
	 * @return
	 */
	public static String getMD5(String source){
		return getMD5(source, "UTF-8");
	}
	/**
	 * 获取字符串的MD5签名
	 * @param source 待签名字符串
	 * @param encoding 字符编码,eg:UTF-8
	 * @return
	 */
	public static String getMD5(String source,String encoding) {
		//MessageDigest非线程安全,直接new一个,不采用加锁共享的方式了
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
		try {
			return byte2hex(md5.digest(source.getBytes(encoding)));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}
	
	/**
	 * 获取随机的MD5签名字符串
	 * @return
	 */
	public static String generateRandomSigniture(){
		return getMD5(String.valueOf(Math.random() + new Date().getTime()));
	}
}
