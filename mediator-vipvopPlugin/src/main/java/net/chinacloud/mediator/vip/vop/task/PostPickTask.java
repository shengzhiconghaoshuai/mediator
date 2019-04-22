/**
 * 文件名：PostPickTask.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.client.ChannelVIPpickFacadeClient;
import net.chinacloud.mediator.vip.vop.domain.CreatePick;
import net.chinacloud.mediator.vip.vop.domain.PickBean;
import net.chinacloud.mediator.vip.vop.service.VopPickService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("postPickTask")
@Scope(value="prototype")
public class PostPickTask extends VopCommonTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(PostPickTask.class);
	private static final String ORDER_TYPE = "ORDER";
	private static final String VIP_PICK="postPick";
	@Autowired
	ChannelVIPpickFacadeClient client;
	
	static {
		TaskManager.registTask(ORDER_TYPE, VIP_PICK, PostPickTask.class);
	}
	
	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		return VIP_PICK;
	}

	@Override
	public void doTask() throws ApplicationException {
		Integer appId = this.getContext().getApplicationId();
		LOGGER.info("Post JIT-Pick  - appId:" + appId);
		CreatePick cp = null;
		Object data = this.getData();
		if (data instanceof CreatePick) {
			cp =  (CreatePick)this.getData();
		} else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				cp = JsonUtil.jsonString2Object(strData, CreatePick.class);
			} 
		}
		VopPickService pickService = getService(VopPickService.class, getContext().getChannelCode());
		PickBean pd=null;
		if(cp!=null){
		     pd = pickService.getPickDetail(cp);
		}
		if(pd !=null){
			LOGGER.info("PostPick start..."+this.getContext().getStoreId());
			try {
				client.postPick(pd,getContext(),this.id);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApplicationException("消息pick发送失败");
			}
		}
	}

}
