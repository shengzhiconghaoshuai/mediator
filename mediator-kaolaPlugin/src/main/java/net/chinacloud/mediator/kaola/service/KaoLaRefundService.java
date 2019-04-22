package net.chinacloud.mediator.kaola.service;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.service.RefundService;

public interface KaoLaRefundService extends RefundService {
	
	public Refund getRefundByOrder(Order order);

}
