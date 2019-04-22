package net.chinacloud.mediator.kaola.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.exception.ApiInvokeException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.kaola.exception.KaoLaException;
import net.chinacloud.mediator.kaola.request.KaoLaRequest;
import net.chinacloud.mediator.kaola.response.KaoLaResponse;

public class KaoLaConnector extends Connector<KaoLaRequest>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaConnector.class);
	
	private DefaultKaoLaClient client;

	public KaoLaConnector(ApplicationParam param) {
		super(param);
		client = new DefaultKaoLaClient(appUrl, appKey, appSecret, sessionKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T executeInternal(KaoLaRequest request,
			int currentRetryTime) throws ApiInvokeException {
		KaoLaResponse response = null;
		try {
			response = (KaoLaResponse) client.execute(request);
		} catch (KaoLaException e) {
			LOGGER.error("kaola接口调用失败", e);
		}
		if(response != null) {
			LOGGER.info("KaoLa error response  " + response.getError_response());	
		}
		return (T) response;
	}

}
