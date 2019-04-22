/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Connector.java
 * 描述： api调用连接器封装
 */
package net.chinacloud.mediator.init.connector;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.exception.ApiInvokeException;

/**
 * < api调用连接器封装>
 * < api调用连接器封装>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
public abstract class Connector<E> {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger("taskStatics");
	
	protected String appKey;
	protected String appSecret;
	protected String sessionKey;
	protected String appUrl;
	protected String venderId;
	
	/**是否重试*/
	protected boolean retry = false;
	/**重试次数*/
	protected Integer retryTime = 5;
	/**重试间隔,默认一秒*/
	protected Integer retryInterval = 1000;
	
	protected Connector(ApplicationParam param, boolean retry, Integer retryTime, Integer retryInterval){
		this.appKey = param.getAppKey();
		this.appSecret = param.getAppSecret();
		this.sessionKey = param.getSessionKey();
		this.appUrl = param.getAppUrl();
		this.venderId = param.getVendorId();
		this.retry = retry;
		if(this.retry){
			if(null != retryInterval){
				this.retryInterval = retryInterval;
			}
			if(null != retryTime){
				this.retryTime = retryTime;
			}
		}
	}
	
	protected Connector(ApplicationParam param, boolean retry){
		this(param, retry, null, null);
	}
	
	protected Connector(ApplicationParam param){
		this(param, false);
	}
	
	/**
	 * 执行请求
	 * @param request 请求对象
	 * @return 响应
	 * @throws ApiInvokeException
	 */
	public <T> T execute(E request) throws ApiInvokeException{
		//if(retry) {
			return execute(request,0);
		/*} else {
			return executeInternal(request);
		}*/
	}
	
	/**
	 * 执行请求
	 * @param request 请求对象
	 * @param retry 重试当前次数
	 * @return 响应
	 * @throws ApiInvokeException
	 */
	protected <T> T execute(E request, int retry) throws ApiInvokeException {
		/*if(retry > 0){
			try {
				//暂停
				Thread.sleep(retry * this.retryInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}*/
		T response = null;
		/*if(retry == this.retryTime){
			if(null == response){
				throw new ApiInvokeException("exception.api.invoke.response.null");
			}
		}
		if(retry == this.retryTime){
			return response;
		}*/
		//try {
			//long startTime = new Date().getTime();
			response = executeInternal(request, 1);
			//LOGGER.debug("request execution time " + (new Date().getTime() - startTime));
		/*} catch (ApiInvokeException e) {
			e.printStackTrace();
			//response = execute(request,retry++);
		}*/
		if(null != response){
			return response;
		}else{
			throw new ApiInvokeException("exception.api.invoke.response.null");
		}
	}
	
	/**
	 * 各平台实现
	 * @param request 请求对象,需要强制转换类型,这点不是很好
	 * @return 响应
	 * @throws ApiInvokeException
	 */
	protected abstract <T> T executeInternal(E request, int currentRetryTime) throws ApiInvokeException;
}
