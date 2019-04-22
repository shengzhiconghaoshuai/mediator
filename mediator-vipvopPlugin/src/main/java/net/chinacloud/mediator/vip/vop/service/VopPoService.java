package net.chinacloud.mediator.vip.vop.service;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.vip.vop.domain.PoBean;
import net.chinacloud.mediator.vip.vop.domain.PoMessage;
import net.chinacloud.mediator.vip.vop.domain.PoSku;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;

public interface VopPoService{
	
	public List<CommonNotifyPacket<PoBean>> getPoListByStatus(String status,
			Date startTime, Date endTime) throws VopJitException;
	
	public List<PoSku> getPoSkuByPosn(String po_sn, String status)
			throws VopJitException;
	
	public PoBean getPoByPoNo(String po_no)throws VopJitException;
	
	public List<PoMessage> getPoOrders(Date startTime, Date endTime) throws VopJitException;
}
