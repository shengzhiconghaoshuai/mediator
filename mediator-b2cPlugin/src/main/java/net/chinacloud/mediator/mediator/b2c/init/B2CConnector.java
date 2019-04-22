/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：B2CConnector.java
 * 描述： 
 */
package net.chinacloud.mediator.mediator.b2c.init;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.exception.ApiInvokeException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.mediator.b2c.request.B2CRequest;
import net.chinacloud.mediator.mediator.b2c.response.B2CResponse;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 下午5:56:18
 */
public class B2CConnector extends Connector<B2CRequest<B2CResponse>> {

	/**
	 * @param param
	 */
	protected B2CConnector(ApplicationParam param) {
		super(param);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected <T> T executeInternal(B2CRequest<B2CResponse> request, int currentRetryTime)
			throws ApiInvokeException {
		// TODO Auto-generated method stub
		return null;
	}

}
