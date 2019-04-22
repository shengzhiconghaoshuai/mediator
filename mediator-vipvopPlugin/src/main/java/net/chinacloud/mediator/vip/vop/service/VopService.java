package net.chinacloud.mediator.vip.vop.service;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import net.chinacloud.mediator.vip.vop.init.VopConnector;
import net.chinacloud.mediator.vip.vop.request.VopRequest;

import org.springframework.beans.factory.annotation.Autowired;

public class VopService {
	@Autowired
	ConnectorManager<VopRequest>  connectorManager;
	
	
	public VopConnector getVopJITConnector()throws VopJitException{
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		VopConnector  aVipShopConnector = (VopConnector)connectorManager.getConnector();
		if(aVipShopConnector == null){
			throw new VopJitException("VopJitConnector is null ,appid = "+appId);
		}
		 return  aVipShopConnector;
	}
}
