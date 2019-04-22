package net.chinacloud.mediator.kaola.util;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.chinacloud.mediator.utils.StringUtils;

public class KopUtils {

	public static final String UTF8 = "UTF-8";

	/**
	 * 使用secret对paramValues按以下算法进行签名： 

	 * uppercase(hex(sha1(secretkey1value1key2value2...secret))
	 *
	 * @param sysParams
	 *            参数列表
	 * @param secret
	 * @return
	 */
	public static String sign(Map<String, Object> sysParams, String secret) {
		return sign(sysParams, null, secret);
	}

	/**
	 * 对paramValues进行签名，其中ignoreParamNames这些参数不参与签名
	 * 
	 * @param paramValues
	 * @param ignoreParamNames
	 * @param secret
	 * @return
	 */
	public static String sign(Map<String,Object> paramValues, List<String> ignoreParamNames, String secret) {
		try {
			StringBuilder sb = new StringBuilder();
			List<String> paramNames = new ArrayList<String>(paramValues.size());
			paramNames.addAll(paramValues.keySet());
			if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
				for (String ignoreParamName : ignoreParamNames) {
					paramNames.remove(ignoreParamName);
				}
			}
			Collections.sort(paramNames);

			sb.append(secret);
			for (String paramName : paramNames) {
				sb.append(paramName).append(paramValues.get(paramName));
			}
			sb.append(secret);
			byte[] sha1Digest = getSHA1Digest(sb.toString());
			return byte2hex(sha1Digest);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String utf8Encoding(String value, String sourceCharsetName) {
		try {
			return new String(value.getBytes(sourceCharsetName), UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static byte[] getSHA1Digest(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes(UTF8));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse.getMessage());
		}
		return bytes;
	}

	/**
	 * 二进制转十六进制字符串
	 *
	 * @param bytes
	 * @return
	 */
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
	
	public static String buildQuery(Map<String, String> params, String charset)
			throws Exception {
		if ((params == null) || (params.isEmpty())) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> en = params.entrySet();
		boolean hasParam = false;
		for ( Map.Entry entry : en) {
			String name = (String) entry.getKey();
			String value = (String) entry.getValue();

			if (StringUtils.areNotEmpty(new String[]{name, value})) {
				if (hasParam)
					query.append("&");
				else {
					hasParam = true;
				}
				query.append(name).append("=")
						.append(URLEncoder.encode(value, charset));
			}

		}

		return query.toString();
	}
	
	

}