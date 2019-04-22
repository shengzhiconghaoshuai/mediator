/**
 * 文件名：ImportDeliverDetailTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.client.ChannelVIPimportdeliverFacadeClient;
import net.chinacloud.mediator.vip.vop.domain.ImportDeliverDetailMsg;
import net.chinacloud.mediator.vip.vop.service.VopPickService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("importDeliverDetailTask")
@Scope(value="prototype")
public class ImportDeliverDetailTask extends VopCommonTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportDeliverDetailTask.class);
	private static final String ORDER_TYPE = "ORDER";
	@Autowired
	ChannelVIPimportdeliverFacadeClient client;
	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		return "importDeliver";
	}
	static {
		TaskManager.registTask(ORDER_TYPE, "importDeliver", ImportDeliverDetailTask.class);
	}

	@Override
	public void doTask() throws ApplicationException {
		Integer appId = this.getContext().getApplicationId();
		LOGGER.info("JIT Import Deliver Detail - appId:" + appId);
		VopPickService pickService = getService(VopPickService.class, getContext().getChannelCode());
		Object data = this.getData();
		ImportDeliverDetailMsg msg = null;
		if (data instanceof ImportDeliverDetailMsg) {
			msg = (ImportDeliverDetailMsg)this.getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				msg = JsonUtil.jsonString2Object(strData, ImportDeliverDetailMsg.class);
			} 
		}
		try {
			pickService.importDeliverDetail(msg,0);
			client.postImportDeliver(msg, context, this.id, true, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("回传明细失败!");
		}
	}

}
