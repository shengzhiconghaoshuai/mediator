package net.chinacloud.mediator.vip.vop.task;

import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.vip.vop.client.ChannelVIPGetInventoryDeductOrdersClient;
import net.chinacloud.mediator.vip.vop.domain.PickBean;
import net.chinacloud.mediator.vip.vop.service.VopProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("getInventoryDeductOrdersTask")
@Scope(value="prototype")
public class GetInventoryDeductOrdersTask extends VopCommonTask{
	
	private static final String ORDER_TYPE = "ORDER";
	
	private static final String GET_GETINVENTORYDEDUCTORDERS = "getInventoryDeductOrders";

	@Override
	protected String getType() {
		return ORDER_TYPE;
	}

	@Override
	protected String getSubType() {
		return GET_GETINVENTORYDEDUCTORDERS;
	}
	
	@Autowired
	ChannelVIPGetInventoryDeductOrdersClient client;

	@Override
	public void doTask() throws Exception {
		Object data = this.getData();
		PickBean pick = null;
		if (data instanceof PickBean) {
			pick = (PickBean) this.getData();
		}else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			if ('{' == first) {
				pick = JsonUtil.jsonString2Object(strData, PickBean.class);
			} 
		}
		VopProductService service = getService(VopProductService.class, getContext().getChannelCode());
		PickBean pickBean = service.getInventoryDeductOrders(pick);
		client.postPick(pickBean, context, this.id);
	}

}
