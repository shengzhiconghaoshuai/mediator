package net.chinacloud.mediator.vip.vop.service;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.VopOrderReturn;
import net.chinacloud.mediator.domain.VopReturnMessage;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;

public interface VopReturnService {
	
	public void confirmRefuseResult(VopReturnMessage returnMessage) throws VopJitException;
	
	public void confirmReturnResult(VopReturnMessage returnMessage) throws VopJitException;
	
	
	/**获取拒收订单列表
	 * @throws VopJitException */
	public List<VopOrderReturn> getOrderReturnList(Date startTime, Date endTime,
			String status) throws VopJitException;
}
