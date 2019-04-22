/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ReturnCreateTask.java
 * 描述： 退货创建task
 */
package net.chinacloud.mediator.task.returns;

import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.translator.ChannelReturnFacadeClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <退货单创建>
 * <退货单创建>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月21日
 * @since 2015年1月21日
 */
@Component("returnCreateTask")
@Scope(value="prototype")
public class ReturnCreateTask extends ReturnTask {
	
	@Autowired
	ChannelReturnFacadeClient returnFacadeClient;

	@Override
	protected String getSubType() {
		// 退货创建
		return "create";
	}

	@Override
	public void doTask() throws ApplicationException {
		Return returns = getReturn();
		
		returnFacadeClient.submitChannelReturn(returns, getContext());
	}

}
