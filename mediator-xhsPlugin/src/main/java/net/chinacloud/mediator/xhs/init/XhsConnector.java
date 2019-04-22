package net.chinacloud.mediator.xhs.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.exception.ApiInvokeException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.xhs.exception.XhsException;
import net.chinacloud.mediator.xhs.request.XhsRequest;
import net.chinacloud.mediator.xhs.response.XhsResponse;

public class XhsConnector extends Connector<XhsRequest>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XhsConnector.class);
	
	private DefaultXhsClient client;

	public XhsConnector(ApplicationParam param) {
		super(param);
		client = new DefaultXhsClient(appUrl, appKey, appSecret, sessionKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T executeInternal(XhsRequest request,
			int currentRetryTime) throws ApiInvokeException {
		XhsResponse response = null;
		try {
			response = (XhsResponse) client.execute(request);
		} catch (XhsException e) {
			LOGGER.error("小红书接口调用失败", e);
		}
		if(response != null) {
			LOGGER.info("xhs error response  " + response.getError_msg());	
		}
		return (T) response;
	}

}
