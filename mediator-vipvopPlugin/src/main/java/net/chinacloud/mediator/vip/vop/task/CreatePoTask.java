/**
 * 文件名：CreatePoTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.vip.vop.client.ChannelVIPpoFacadeClient;
import net.chinacloud.mediator.vip.vop.domain.PoBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("createPoTask")
@Scope(value="prototype")
public class CreatePoTask extends VopCommonTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(CreatePoTask.class);
	private static final String ORDER_TYPE = "ORDER";
	private static final String CREATE_PO="createPo";
	@Autowired
	ChannelVIPpoFacadeClient client;
	
	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		return CREATE_PO;
	}

	@Override
	public void doTask() throws ApplicationException {
		PoBean po = (PoBean)this.getData();
		int appId = this.getContext().getApplicationId();
		LOGGER.info("CreatePo...appid:"+appId+" Po_num:"+po.po_no);
		
		try {
			client.postPo(po,getContext(),this.id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("消息po发送失败");
		}
	}

}
