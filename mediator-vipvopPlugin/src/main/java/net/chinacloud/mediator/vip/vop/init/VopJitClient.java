package net.chinacloud.mediator.vip.vop.init;

import com.vip.vop.omni.logistics.OmniLogisticsService;

import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import vip.vop.omni.inventory.OmniInventoryServiceHelper.OmniInventoryServiceClient;
import vipapis.delivery.JitDeliveryService;
import vipapis.delivery.DvdDeliveryServiceHelper.DvdDeliveryServiceClient;
import vipapis.finance.FastServiceHelper.FastServiceClient;
import vipapis.inventory.InventoryServiceHelper.InventoryServiceClient;
import vipapis.order.OmniOrderServiceHelper.OmniOrderServiceClient;

public interface VopJitClient {
	public abstract JitDeliveryService getConnector()throws VopJitException;
	
	public abstract InventoryServiceClient getInventoryServiceClient();
	
	public abstract JitDeliveryService getJitDeliveryServiceClient();
	
	public abstract OmniOrderServiceClient getOmniOrderServiceClient();
	
	public abstract OmniInventoryServiceClient getOmniInventoryServiceClient();
	
	public abstract DvdDeliveryServiceClient getDvdDeliveryServiceClient();
	
	public abstract FastServiceClient getFastServiceClient();

	public abstract OmniLogisticsService getOmniLogisticsServiceClient();
}
