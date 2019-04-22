/**
 * 文件名：CreatedeliverTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.vip.vop.client.ChannelVIPdeliverFacadeClient;
import net.chinacloud.mediator.vip.vop.domain.CreatedeliverMsg;
import net.chinacloud.mediator.vip.vop.domain.JITDeliveryBean;
import net.chinacloud.mediator.vip.vop.service.VopPickService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("createdeliverTask")
@Scope(value="prototype")
public class CreatedeliverTask extends VopCommonTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(CreatedeliverTask.class);
	private static final String ORDER_TYPE = "ORDER";
	private static final String VIP_DELIVER="vipdeliver";
	@Autowired
	ChannelVIPdeliverFacadeClient client;
	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		return VIP_DELIVER;
	}
	static {
		TaskManager.registTask(ORDER_TYPE, VIP_DELIVER, CreatedeliverTask.class);
	}
	@Override
	public void doTask() throws ApplicationException {
		CreatedeliverMsg msg = (CreatedeliverMsg)this.getData();
		Integer appId = this.getContext().getApplicationId();
		LOGGER.info("JIT createdeliver - appId:" + appId);
		VopPickService pickService = getService(VopPickService.class, getContext().getChannelCode());
		JITDeliveryBean jdb = pickService.createDelivery(msg);
		//发送bean
		try {
			client.postCreateDeliver(jdb,getContext(),this.id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("消息jit-deliver发送失败");
		}
				
	}

}
