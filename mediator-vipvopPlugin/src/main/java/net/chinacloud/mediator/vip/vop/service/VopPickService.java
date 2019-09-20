package net.chinacloud.mediator.vip.vop.service;

import java.util.List;

import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.vip.vop.domain.ConfirmdeliverMsg;
import net.chinacloud.mediator.vip.vop.domain.CreatePick;
import net.chinacloud.mediator.vip.vop.domain.CreatedeliverMsg;
import net.chinacloud.mediator.vip.vop.domain.ImportDeliverDetailMsg;
import net.chinacloud.mediator.vip.vop.domain.JITDeliveryBean;
import net.chinacloud.mediator.vip.vop.domain.PickBean;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;

public interface VopPickService {
	public List<CommonNotifyPacket<CreatePick>> createPickListByPO(String po_no) throws VopJitException;
	
	public PickBean getPickDetail(CreatePick createPick) throws VopJitException;
	
	public JITDeliveryBean createDelivery(CreatedeliverMsg createdeliverData) throws VopJitException;
	
	public void importDeliverDetail(ImportDeliverDetailMsg importDeliverMsg,int retryCount)throws VopJitException;
	
	public void confirmDeliver(ConfirmdeliverMsg msg)throws VopJitException;
	
	public List<CommonNotifyPacket<CreatePick>> getPickList(String po_no) throws VopJitException;

	//同步vip库存
	public void updateInventory(String sku,Integer quantity,Integer syncMode);
}
