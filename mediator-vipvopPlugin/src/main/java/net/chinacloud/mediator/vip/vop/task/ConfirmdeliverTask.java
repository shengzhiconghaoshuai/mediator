/**
 * 文件名：ConfirmdeliverTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.vip.vop.domain.ConfirmdeliverMsg;
import net.chinacloud.mediator.vip.vop.service.VopPickService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <TODO simple description>
 * <TODO detail description>
 * @author mwu@wuxicloud.com
 * @version 0.0.0,2015年3月2日
 * @since 2015年3月2日
 */
@Component("confirmdeliverTask")
@Scope(value="prototype")
public class ConfirmdeliverTask extends VopCommonTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmdeliverTask.class);
	private static final String ORDER_TYPE = "ORDER";
	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		// TODO Auto-generated method stub
		return "confirmDelivery";
	}

	@Override
	public void doTask() throws ApplicationException {
		ConfirmdeliverMsg msg = (ConfirmdeliverMsg)this.getData();
		Integer appId = this.getContext().getApplicationId();
		LOGGER.info("JIT Confirm deliver - appId:" + appId);
		VopPickService pickService = getService(VopPickService.class, getContext().getChannelCode());
		pickService.confirmDeliver(msg);
		
	}

}
