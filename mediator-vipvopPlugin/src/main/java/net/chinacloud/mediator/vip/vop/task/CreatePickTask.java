/**
 * 文件名：CreatePickTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import java.util.ArrayList;
import java.util.List;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.vip.vop.domain.CreatePick;
import net.chinacloud.mediator.vip.vop.domain.PoBean;
import net.chinacloud.mediator.vip.vop.domain.PoMessage;
import net.chinacloud.mediator.vip.vop.service.VopPickService;
import net.chinacloud.mediator.vip.vop.service.VopPoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("createPickTask")
@Scope(value="prototype")
public class CreatePickTask extends VopCommonTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(CreatePickTask.class);
	@Autowired
	private TaskManager taskManager;
	@Override
	protected String getType() {
		return "ORDER";
	}

	@Override
	protected String getSubType() {
		return "createPick";
	}

	@Override
	public void doTask() throws ApplicationException {
		PoMessage poMsg = (PoMessage)this.getData();
		Integer appId = this.getContext().getApplicationId();
		LOGGER.info("Get JIT-Pick NotifyBatch - appId:" + appId);
		VopPoService poService = getService(VopPoService.class, getContext().getChannelCode());
		List<CommonNotifyPacket<CreatePick>> notifys = new ArrayList<CommonNotifyPacket<CreatePick>>();
		//创建pick
    	String po_no = poMsg.getPo_no();
    	boolean immediately = poMsg.isImmediately();
    	if(immediately){
    		doit(notifys, po_no);
    	}else{
    		PoBean aPoBean = poService.getPoByPoNo(po_no);
    		String not_pick = aPoBean.not_pick;
    		if(!"".equals(not_pick) && not_pick!=null){
    			int not_pickInt = Integer.valueOf(not_pick);
    			LOGGER.info("--------create pick pono="+po_no+" immediately="+immediately+" not_pick="+not_pick);
    			if(not_pickInt > 200){
    				doit(notifys, po_no);
    			}
    		}
    		
    	}
	}
	
	public void doit(List<CommonNotifyPacket<CreatePick>> notifys,String po_no) throws ApplicationException{
		VopPickService pickService = getService(VopPickService.class, getContext().getChannelCode());
		  List<CommonNotifyPacket<CreatePick>> temp = pickService.createPickListByPO(po_no);
	        if(temp!=null && temp.size()>0){
	    		notifys.addAll(temp);
	    	}
	        if (notifys.isEmpty()) {
	        	LOGGER.info("jit pick notifyBatches list is empty");
				return;
			}
	        for (CommonNotifyPacket<CreatePick> packet : notifys) {
	        	Task task = taskManager.generateTask(getContext().getChannelCode(), packet);//packet已经设置type
				
				if (null != task) {
					//传递上下文
					task.setContext(getContext());
					
					taskManager.executeTask(task);
				}
	        }
	}

}
