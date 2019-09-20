/**
 * 文件名：BatchCreatePickTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.vip.vop.domain.PoList;
import net.chinacloud.mediator.vip.vop.domain.PoMessage;
import net.chinacloud.mediator.vip.vop.domain.VipInventoryMessage;
import net.chinacloud.mediator.vip.vop.domain.VipInventoryUpList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("vipSyscInventoryTask")
@Scope(value="prototype")
public class VipSyscInventoryTask extends Task{
	@Autowired
	private TaskManager taskManager;
	@Override
	protected String getType() {
		return "Sysc";
	}

	@Override
	protected String getSubType() {
		return "syscInventoryItemTask";
	}
// TODO ch
	@Override
	public void doTask() throws ApplicationException {
		VipInventoryUpList list=(VipInventoryUpList)this.getData();
		List<VipInventoryMessage> vipInventoryMessageList = list.getVipInventoryMessageList();
		for(VipInventoryMessage msg : vipInventoryMessageList){
			CommonNotifyPacket<VipInventoryMessage> packet = new CommonNotifyPacket<VipInventoryMessage>(msg);
			packet.setType("VIP_ITEM_SYNC_INVENTORY");
			
			Task task = null;
			task = taskManager.generateTask(this.context.getChannelCode(), packet);
			if(null == task){
				return;
			}
			//task context
			task.getContext().setApplicationId(this.context.getApplicationId());
			task.getContext().setApplicationCode(this.context.getApplicationCode());
			task.getContext().setChannelId(this.context.getChannelId());
			task.getContext().setChannelCode(this.context.getChannelCode());
			task.getContext().setStoreId(this.context.getStoreId());
			try {
				Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			taskManager.executeTask(task);
		}
	}

}
