package net.chinacloud.mediator.vip.vop.init;



import com.vip.osp.sdk.context.InvocationContext;
import com.vip.vop.omni.logistics.OmniLogisticsService;

import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import vip.vop.omni.inventory.OmniInventoryServiceHelper.OmniInventoryServiceClient;
import vipapis.delivery.DvdDeliveryServiceHelper.DvdDeliveryServiceClient;
import vipapis.delivery.JitDeliveryService;
import vipapis.inventory.InventoryServiceHelper.InventoryServiceClient;
import vipapis.order.OmniOrderServiceHelper.OmniOrderServiceClient;
import vipapis.delivery.JitDeliveryServiceHelper.JitDeliveryServiceClient;
import vipapis.finance.FastServiceHelper.FastServiceClient;


public class DefaultVopClient implements VopJitClient{
	//private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVopClient.class);
	
	private String appKey;
	private String appSecret;
	private String appUrl;
	private String token;
	
	public DefaultVopClient(String appKey,String appSecret,String appUrl,String token){
		this.appKey =  appKey;
		this.appSecret = appSecret;
		this.appUrl =  appUrl;
		this.token = token;
	}
	
	@Override
	public JitDeliveryService getConnector()
			throws VopJitException {
		InvocationContext instance = InvocationContext.Factory.getInstance();
		instance.setAppKey(this.appKey);
		instance.setAppSecret(this.appSecret);
		instance.setAppURL(this.appUrl);
		instance.setAccessToken(this.token);
		JitDeliveryService jit = new JitDeliveryServiceClient();
		return jit;
	}
	
	@Override
	public InventoryServiceClient getInventoryServiceClient() {
		InventoryServiceClient client = new InventoryServiceClient();
		InvocationContext instance = InvocationContext.Factory.getInstance();
		instance.setAppKey(this.appKey);
		instance.setAppSecret(this.appSecret);
		instance.setAppURL(this.appUrl);
		instance.setAccessToken(this.token);
		return client;
	}
	
	@Override
	public JitDeliveryService getJitDeliveryServiceClient() {
		InvocationContext instance = InvocationContext.Factory.getInstance();
		instance.setAppKey(this.appKey);
		instance.setAppSecret(this.appSecret);
		instance.setAppURL(this.appUrl);
		instance.setAccessToken(this.token);
		JitDeliveryService client = new JitDeliveryServiceClient();
		return client;
	}
	
	@Override
	public OmniOrderServiceClient getOmniOrderServiceClient() {
		OmniOrderServiceClient client=new OmniOrderServiceClient();
		InvocationContext instance = InvocationContext.Factory.getInstance();
		instance.setAppKey(this.appKey);
		instance.setAppSecret(this.appSecret);
		instance.setAppURL(this.appUrl);
		instance.setAccessToken(this.token);
		return client;
	}
	
	@Override
	public OmniInventoryServiceClient getOmniInventoryServiceClient() {
		OmniInventoryServiceClient client = new OmniInventoryServiceClient();
		InvocationContext instance = InvocationContext.Factory.getInstance();
		instance.setAppKey(this.appKey);
		instance.setAppSecret(this.appSecret);
		instance.setAppURL(this.appUrl);
		instance.setAccessToken(this.token);
		return client;
	}
	
	@Override
	public DvdDeliveryServiceClient getDvdDeliveryServiceClient(){
		DvdDeliveryServiceClient client = new DvdDeliveryServiceClient();
		InvocationContext instance = InvocationContext.Factory.getInstance();
		instance.setAppKey(this.appKey);
		instance.setAppSecret(this.appSecret);
		instance.setAppURL(this.appUrl);
		instance.setAccessToken(this.token);
		return client;
	}
	
	@Override
	public FastServiceClient getFastServiceClient() {
		vipapis.finance.FastServiceHelper.FastServiceClient client = new vipapis.finance.FastServiceHelper.FastServiceClient();
	    com.vip.osp.sdk.context.InvocationContext instance = com.vip.osp.sdk.context.InvocationContext.Factory.getInstance();
	    instance.setAppKey(this.appKey);
		instance.setAppSecret(this.appSecret);
		instance.setAppURL(this.appUrl);
		instance.setAccessToken(this.token);
	    return client;
	}
	
	@Override
	public OmniLogisticsService getOmniLogisticsServiceClient() {
		com.vip.vop.omni.logistics.OmniLogisticsServiceHelper.OmniLogisticsServiceClient client=new com.vip.vop.omni.logistics.OmniLogisticsServiceHelper.OmniLogisticsServiceClient();
	    com.vip.osp.sdk.context.InvocationContext instance=com.vip.osp.sdk.context.InvocationContext.Factory.getInstance();
	    instance.setAppKey(this.appKey);
		instance.setAppSecret(this.appSecret);
		instance.setAppURL(this.appUrl);
		instance.setAccessToken(this.token);
		return client;
	}

	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
	
}
