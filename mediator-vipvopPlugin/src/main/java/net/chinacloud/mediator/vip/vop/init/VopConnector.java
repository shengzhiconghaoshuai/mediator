package net.chinacloud.mediator.vip.vop.init;

import com.vip.vop.omni.logistics.OmniLogisticsService;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.exception.ApiInvokeException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import net.chinacloud.mediator.vip.vop.request.VopRequest;
import vip.vop.omni.inventory.OmniInventoryServiceHelper.OmniInventoryServiceClient;
import vipapis.delivery.JitDeliveryService;
import vipapis.delivery.DvdDeliveryServiceHelper.DvdDeliveryServiceClient;
import vipapis.finance.FastService;
import vipapis.inventory.InventoryServiceHelper.InventoryServiceClient;
import vipapis.order.OmniOrderServiceHelper.OmniOrderServiceClient;

public class VopConnector  extends Connector<VopRequest>{

	VopJitClient client;
	
	public VopConnector(ApplicationParam param){
		super(param);
		client = new DefaultVopClient(appKey,appSecret,appUrl,sessionKey);
	}
	
	public JitDeliveryService getVopJitConnector() throws VopJitException{
		return client.getConnector();
	}
	
	public InventoryServiceClient getInventoryServiceClient() throws VopJitException{
		return client.getInventoryServiceClient();
	}

	public JitDeliveryService getJitDeliveryServiceClient() {
		return client.getJitDeliveryServiceClient();
	}
	
	public OmniOrderServiceClient getOmniOrderServiceClient(){
		return client.getOmniOrderServiceClient();
	};
	
	public OmniInventoryServiceClient getOmniInventoryServiceClient() {
		return client.getOmniInventoryServiceClient();
	}
	
	public DvdDeliveryServiceClient getDvdDeliveryServiceClient(){
		return client.getDvdDeliveryServiceClient();
	}
	
	
	public FastService getFastServiceClient(){
		return client.getFastServiceClient();
	}
	
	public OmniLogisticsService getOmniLogisticsServiceClient(){
		return client.getOmniLogisticsServiceClient();
	}
	
	@Override
	protected <T> T executeInternal(VopRequest request, int currentRetryTime)
			throws ApiInvokeException {
		return null;
	}

}
