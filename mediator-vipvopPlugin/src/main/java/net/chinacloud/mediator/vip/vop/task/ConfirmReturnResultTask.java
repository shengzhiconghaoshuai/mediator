package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.domain.VopReturnMessage;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.service.VopReturnService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("confirmReturnResultTask")
@Scope(value="prototype")
public class ConfirmReturnResultTask  extends VopCommonTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmReturnResultTask.class);
	
	private static final String RETURN_TYPE = "RETURN";
	
	private static final String RETURN_SUBTYPE = "CONFIRMRETURNRESULT";

	@Override
	protected String getType() {
		return RETURN_TYPE;
	}

	@Override
	protected String getSubType() {
		return RETURN_SUBTYPE;
	}

	static {
		TaskManager.registTask(RETURN_TYPE, RETURN_SUBTYPE, ConfirmReturnResultTask.class);
	}
	
	@Override
	public void doTask() throws Exception {
		Object data = this.getData();
		VopReturnMessage returnMessage = null;
		if (data instanceof VopReturnMessage) {
			returnMessage = (VopReturnMessage) this.getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				returnMessage = JsonUtil.jsonString2Object(strData, VopReturnMessage.class);
			} 
		}
		
		Integer appId = this.getContext().getApplicationId();
		LOGGER.info("VopOXO Confirm return - appId:" + appId);
		
		VopReturnService service = getService(VopReturnService.class, getContext().getChannelCode());
		service.confirmReturnResult(returnMessage);
	}

}
