/**
 * 文件名：CreatePickTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.vip.vop.domain.CreatePick;
import net.chinacloud.mediator.vip.vop.domain.PoBean;
import net.chinacloud.mediator.vip.vop.domain.PoMessage;
import net.chinacloud.mediator.vip.vop.domain.VipInventoryMessage;
import net.chinacloud.mediator.vip.vop.service.VopPickService;
import net.chinacloud.mediator.vip.vop.service.VopPoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("vipSyscInventoryItemTask")
@Scope(value="prototype")
public class VipSyscInventoryItemTask extends VopCommonTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(VipSyscInventoryItemTask.class);
	@Autowired
	private TaskManager taskManager;
	@Override
	protected String getType() {
		return "sysc";
	}

	@Override
	protected String getSubType() {
		return "vipSyscInventoryItemTask";
	}

	@Override
	public void doTask() throws ApplicationException {
		VipInventoryMessage inventoryMessage = (VipInventoryMessage)this.getData();
		Integer appId = this.getContext().getApplicationId();
		LOGGER.info(" VipSyscInventoryItemTask- appId:" + appId);
		VopPoService poService = getService(VopPoService.class, getContext().getChannelCode());

		VopPickService pickService = getService(VopPickService.class, getContext().getChannelCode());
		//开始执行vip库存更新同步
		LOGGER.info("doTask开始执行vip库存更新同步");
		pickService.updateInventory(inventoryMessage.getSKU(),inventoryMessage.getQuantity(),inventoryMessage.getSyncMode());
	}
}
