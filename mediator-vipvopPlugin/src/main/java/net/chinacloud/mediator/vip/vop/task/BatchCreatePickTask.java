/**
 * 文件名：BatchCreatePickTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import java.util.List;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.vip.vop.domain.PoList;
import net.chinacloud.mediator.vip.vop.domain.PoMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("batchCreatePickTask")
@Scope(value="prototype")
public class BatchCreatePickTask extends Task{
	@Autowired
	private TaskManager taskManager;
	@Override
	protected String getType() {
		return "ORDER";
	}

	@Override
	protected String getSubType() {
		return "batchPickCreate";
	}

	@Override
	public void doTask() throws ApplicationException {
		PoList list=(PoList)this.getData();
		List<PoMessage> po_nos= list.getPo_nos();
		for(PoMessage po : po_nos){
			CommonNotifyPacket<PoMessage> packet = new CommonNotifyPacket<PoMessage>(po);
			packet.setType("STATUS_PICK_BATCHCREATE");
			
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
