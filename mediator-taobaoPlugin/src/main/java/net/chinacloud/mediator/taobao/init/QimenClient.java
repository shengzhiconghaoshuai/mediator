package net.chinacloud.mediator.taobao.init;

import java.io.IOException;

import net.chinacloud.mediator.exception.ApiInvokeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class QimenClient {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(QimenClient.class);

	public static final String QIMEN_RESPONSE = "response";
	public static final String QIMEN_RESPONSE_OK = "success";
	public static final String QIMEN_RESPONSE_FAIL = "failure";
	public static final String QIMEN_ERROR = "error";
	public static final String QIMEN_ERRORDESCRIPTIONS = "errorDescriptions";
	private String serverUrl;
	private String appKey;
	private String appSecret;
	public static String format = "xml";
	public static String sign_method = "md5";
	public static String v = "2.0";
	public static int connectTimeout = 15000;
	public static int readTimeout = 30000;
	public static boolean needCheckRequest = true;
	public static boolean needEnableParser = true;
	public static boolean useSimplifyJson = false;
	public static boolean useGzipEncoding = true;

	public QimenClient(String serverUrl, String appKey, String appSecret) {
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.serverUrl = serverUrl;
	}

	public HttpResponse execute(String url, String body) {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/xml");
		HttpEntity entity = new StringEntity(body, "utf-8");
		post.setEntity(entity);
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			LOGGER.error(e.getMessage());
			throw new ApiInvokeException("exception.api.response.notsuccess",
					e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new ApiInvokeException("exception.api.response.notsuccess",
					e.getMessage());
		}
		return response;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

}
