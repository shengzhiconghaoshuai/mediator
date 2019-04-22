/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoConnector.java
 * 描述： 淘宝渠道连接器
 */
package net.chinacloud.mediator.taobao.init;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.exception.ApiInvokeException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.qimen.domain.QimenSign;
import net.chinacloud.mediator.qimen.request.QimenRequest;
import net.chinacloud.mediator.qimen.response.QimenResponse;
import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;
import net.chinacloud.mediator.utils.XStreamUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.api.ApiException;
import com.taobao.api.BaseTaobaoRequest;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.thoughtworks.xstream.XStream;
/**
 * <淘宝渠道连接器>
 * <淘宝渠道连接器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月15日
 * @since 2014年12月15日
 */
public class TaobaoConnector extends Connector<Object> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaobaoConnector.class);
	
	private DefaultTaobaoClient tbclient;
	
	private QimenClient qimenClient;
	
	private String sessionKey;
	
	private String customerId;
	
	private Integer tbEagleeyext;
	/**允许重跑的错误码*/
	private static Set<String> allowedRetrySubCode = new HashSet<String>();
	
	static {
		//isp.***-service-unavailable 暂时没加入
		allowedRetrySubCode.add("isp.remote-service-error");
		allowedRetrySubCode.add("isp.remote-service-timeout");
		allowedRetrySubCode.add("isp.remote-connection-error");
		allowedRetrySubCode.add("isp.top-remote-connection-timeout");
		allowedRetrySubCode.add("isp.top-remote-connection-error");
		allowedRetrySubCode.add("isp.unknown-error");
	}
	/**
	 * @param appKey
	 * @param appSecret
	 * @param sessionKey
	 * @param appUrl
	 */
	public TaobaoConnector(ApplicationParam param) {
		super(param);
		tbclient = new DefaultTaobaoClient(appUrl, appKey, appSecret);
		qimenClient = new QimenClient(appUrl,appKey,appSecret);
		this.sessionKey = param.getSessionKey();
		this.retry = true;
		this.customerId = param.getVendorId();
		this.tbEagleeyext = param.getField3();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T executeInternal(Object request, int currentRetryTime) throws ApiInvokeException{
		if(request instanceof BaseTaobaoRequest && tbEagleeyext == 1){
			return this.executeBaseTaobaoInternal((BaseTaobaoRequest<TaobaoResponse>) request,currentRetryTime);
		}else if (request instanceof TaobaoRequest) {
			return this.executeTaobaoInternal((TaobaoRequest<TaobaoResponse>) request,currentRetryTime);
		} else if (request instanceof QimenRequest) {
			return this.executeQimenInternal((QimenRequest<QimenResponse>) request, currentRetryTime);
		} 
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T> T executeTaobaoInternal(TaobaoRequest<TaobaoResponse> request, int currentRetryTime)
			throws ApiInvokeException {
		//LOGGER.info("taobao request " + request.getApiMethodName() + " retry " + currentRetryTime + " API with parameters " + request.getTextParams());
		LOGGER.info("taobao request {} retry {} with parameters {}", new Object[]{request.getApiMethodName(), currentRetryTime, request.getTextParams()});
		if(this.retry && currentRetryTime > 1){
			try {
				//暂停
				Thread.sleep(currentRetryTime * this.retryInterval);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				LOGGER.error("淘宝连接器初始化失败", e);
			} 
		}
		T response = null;
		try {
			response = (T)tbclient.execute(request, this.sessionKey);
		} catch (ApiException e) {
			//e.printStackTrace();
			LOGGER.error("淘宝接口调用失败", e);
			throw new ApiInvokeException("exception.api.connect.error",e.getMessage());
		}
		if (!((TaobaoResponse)response).isSuccess()) {
			TaobaoResponse taobaoResponse = (TaobaoResponse)response;
			StringBuilder sb = new StringBuilder();
			sb.append("淘宝response返回失败,")
				.append("errorCode = ").append(taobaoResponse.getErrorCode()).append(",")
				.append("msg = ").append(taobaoResponse.getMsg()).append(",")
				.append("subCode = ").append(taobaoResponse.getSubCode()).append(",")
				.append("subMsg = ").append(taobaoResponse.getSubMsg());
				
			LOGGER.error(sb.toString());
			if (currentRetryTime == this.retryTime) {
				String errorMsg = (!StringUtils.hasText(taobaoResponse.getSubMsg()) || "null".equals(taobaoResponse.getSubMsg())) ? taobaoResponse.getMsg() : taobaoResponse.getSubMsg();
				errorMsg = replaceSpecialString(errorMsg);
				throw new ApiInvokeException("exception.api.response.notsuccess", errorMsg);
			}
			if (this.retry && allowedRetrySubCode.contains(taobaoResponse.getSubCode())) {
				return executeInternal(request, ++currentRetryTime);
			} else {
				String errorMsg = (!StringUtils.hasText(taobaoResponse.getSubMsg()) || "null".equals(taobaoResponse.getSubMsg())) ? taobaoResponse.getMsg() : taobaoResponse.getSubMsg();
				errorMsg = replaceSpecialString(errorMsg);
				throw new ApiInvokeException("exception.api.response.notsuccess", errorMsg);
			}
		}
		LOGGER.info("taobao response " + ((TaobaoResponse)response).getBody());
		return response;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T executeBaseTaobaoInternal(BaseTaobaoRequest<TaobaoResponse> request, int currentRetryTime)
			throws ApiInvokeException {
		//LOGGER.info("taobao request " + request.getApiMethodName() + " retry " + currentRetryTime + " API with parameters " + request.getTextParams());
		LOGGER.info("BaseTaobao request {} retry {} with parameters {}", new Object[]{request.getApiMethodName(), currentRetryTime, request.getTextParams()});
		if(this.retry && currentRetryTime > 1){
			try {
				//暂停
				Thread.sleep(currentRetryTime * this.retryInterval);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				LOGGER.error("淘宝连接器初始化失败", e);
			} 
		}
		T response = null;
		try {
			request.putOtherTextParam("tb_eagleeyex_t", "1");
			response = (T)tbclient.execute(request, this.sessionKey);
		} catch (ApiException e) {
			//e.printStackTrace();
			LOGGER.error("淘宝接口调用失败", e);
			throw new ApiInvokeException("exception.api.connect.error",e.getMessage());
		}
		if (!((TaobaoResponse)response).isSuccess()) {
			TaobaoResponse taobaoResponse = (TaobaoResponse)response;
			StringBuilder sb = new StringBuilder();
			sb.append("淘宝压测response返回失败,")
				.append("errorCode = ").append(taobaoResponse.getErrorCode()).append(",")
				.append("msg = ").append(taobaoResponse.getMsg()).append(",")
				.append("subCode = ").append(taobaoResponse.getSubCode()).append(",")
				.append("subMsg = ").append(taobaoResponse.getSubMsg());
				
			LOGGER.error(sb.toString());
			if (currentRetryTime == this.retryTime) {
				String errorMsg = (!StringUtils.hasText(taobaoResponse.getSubMsg()) || "null".equals(taobaoResponse.getSubMsg())) ? taobaoResponse.getMsg() : taobaoResponse.getSubMsg();
				errorMsg = replaceSpecialString(errorMsg);
				throw new ApiInvokeException("exception.api.response.notsuccess", errorMsg);
			}
			if (this.retry && allowedRetrySubCode.contains(taobaoResponse.getSubCode())) {
				return executeInternal(request, ++currentRetryTime);
			} else {
				String errorMsg = (!StringUtils.hasText(taobaoResponse.getSubMsg()) || "null".equals(taobaoResponse.getSubMsg())) ? taobaoResponse.getMsg() : taobaoResponse.getSubMsg();
				errorMsg = replaceSpecialString(errorMsg);
				throw new ApiInvokeException("exception.api.response.notsuccess", errorMsg);
			}
		}
		LOGGER.info("taobao tb_eagleeyex_t response " + ((TaobaoResponse)response).getBody());
		return response;
	}
	
	private String replaceSpecialString(String source) {
		if (StringUtils.hasText(source)) {
			Registry registry = SpringUtil.getBean(Registry.class);
			String specialString = registry.get(TaobaoConstant.KEY_SPECIAL_STRING_LIST);
			if (StringUtils.hasText(specialString)) {
				String specialStr[] = specialString.split(",");
				int length = specialStr.length;
				String[] replacementList = new String[length];
				for (int i = 0; i < length; i++) {
					replacementList[i] = "";
				}
				return org.apache.commons.lang3.StringUtils.replaceEach(source, specialStr, replacementList);
			}
			return source;
		}
		return source;
	}
	
	@SuppressWarnings("unchecked")
	protected  <T> T executeQimenInternal(QimenRequest<QimenResponse> request,int currentRetryTime) throws ApiInvokeException  {
		String xmlBody = XStreamUtils.transformObjectToXMLStr(request);
		String apiMethod = request.getApiMethodName();
		
		String url = generateSignitureURL(apiMethod,xmlBody);
		LOGGER.info("qimen request url:" + url+",xmlBody:"+xmlBody);
    	HttpResponse response =  qimenClient.execute(url, xmlBody);
    	String result = null;
    	if(response != null){
    		int code = response.getStatusLine().getStatusCode();
    		if (code == HttpStatus.SC_OK) {
    			HttpEntity resEntity = response.getEntity();
    			if (resEntity != null) {
    				 try {
    					result = EntityUtils.toString(resEntity, "utf-8");
    					result = result.replaceAll("[\\t\\n\\r\\s]", "");//将内容区域的回车换行去除
    				} catch (ParseException e) {
    					LOGGER.error(e.getMessage(),e);
    			        throw new ApiInvokeException("EntityUtils ParseException",e);
    				} catch (IOException e) {
    					LOGGER.error(e.getMessage(),e);
    			        throw new ApiInvokeException("EntityUtils IOException",e);
    				}
    				 LOGGER.info("response "+result);
    			}
    		}else{
    			LOGGER.error("HttpStatus: "+code);
    			throw new ApiInvokeException("HttpStatus: "+code);
    		}	
    	}else{
    		LOGGER.error("HttpResponse response is null");
	        throw new ApiInvokeException("HttpResponse response is null");
    	}
    	
    	QimenResponse qimenResponse = null;
		try {
			qimenResponse = request.getResponseClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	XStream xstream = qimenResponse.getXStream();
    	return (T) xstream.fromXML(result);
	}
	
	private String generateSignitureURL(String method, String body){
    	StringBuffer sb = new StringBuffer();
    	sb.append("http://qimen.api.taobao.com/router/qimen/service").append("?");
    	sb.append("method="+method).append("&");
    	sb.append("timestamp="+DateUtil.format(new Date(),"yyyy-MM-dd'%20'HH:mm:ss")).append("&");
    	sb.append("format=").append(QimenClient.format).append("&");
    	sb.append("app_key=").append(qimenClient.getAppKey()).append("&");
    	sb.append("v=").append(QimenClient.v).append("&");
    	sb.append("sign_method=").append(QimenClient.sign_method).append("&");
    	sb.append("customerId=").append(customerId);
    	QimenSign qimenSign = new QimenSign();
    	String md5Sign = qimenSign.sign(sb.toString(), body, qimenClient.getAppSecret());
    	sb.append("&sign="+md5Sign);
    	return sb.toString();	
    }
}
