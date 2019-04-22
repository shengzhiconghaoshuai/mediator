/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：JingdongConnector.java
 * 描述： 京东渠道连接器
 */
package net.chinacloud.mediator.jingdong.init;

import java.util.Set;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.exception.ApiInvokeException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.response.AbstractResponse;

/**
 * 
 * <京东渠道连接器>
 * <京东渠道连接器>
 * @author mwu
 * @version 0.0.0,2015年3月11日
 * @since 2015年3月11日
 */
public class JingdongConnector extends Connector<JdRequest<AbstractResponse>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(JingdongConnector.class);
	private String accessToken;
	
	/**
	 * 最大重试次数
	 */
	private int maxRetryCount = 3;
	
	/**
	 * 重试等待时间
	 */
	private long retryWaitTime = 1000L;
	
	JdClient client ;

	/**
	 * @param param
	 */
	public JingdongConnector(ApplicationParam param) {
		super(param);
		accessToken = param.getSessionKey();
		client = new DefaultJdClient(appUrl, accessToken, appKey, appSecret, 10000, 60000); 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T executeInternal(JdRequest<AbstractResponse> request, int currentRetryTime)
			throws ApiInvokeException {
		AbstractResponse response = null;
		try {
			LOGGER.info("jingdong request " + request.getApiMethod() + " API with parameters " + request.getAppJsonParams() + " 重试次数 " + currentRetryTime);
			
			response = client.execute(request);
			String code = response.getCode();
			
			if(StringUtils.hasLength(code) && !"0".equals(code)){
				Set<String> set =  (Set<String>) SpringUtil.getBean("JDCode");
				if(set.contains(code) && currentRetryTime < maxRetryCount) {
					sleepWithoutInterrupt(retryWaitTime);
					currentRetryTime = currentRetryTime + 1;
					return this.executeInternal(request, currentRetryTime);
				}
				
				StringBuilder sb = new StringBuilder();
				sb.append("JD response返回失败,")
					.append("code = ").append(response.getCode()).append(",")
					.append("msg = ").append(response.getMsg()).append(",")
					.append("zhDesc = ").append(response.getZhDesc());
				LOGGER.error(sb.toString());
				throw new ApiInvokeException("exception.api.response.notsuccess", response.getZhDesc());
			}
		} catch (JdException e) {
			if (currentRetryTime < maxRetryCount) {
				sleepWithoutInterrupt(retryWaitTime);
				currentRetryTime = currentRetryTime + 1;
				return this.executeInternal(request, currentRetryTime);
			} else {
				LOGGER.error("京东接口调用失败", e);
				throw new ApiInvokeException("exception.api.connect.error",e.getMessage());				
			}
		} catch (Exception e) {
			LOGGER.error("京东接口调用失败",e);
			throw new ApiInvokeException("exception.api.connect.error",e.getMessage());
		}
		
		if(response != null) {
			LOGGER.info("jingdong response " + response.getMsg());	
		}
    	return (T)response;
	}
	
	private void sleepWithoutInterrupt(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
