package net.chinacloud.mediator.xhs.service;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.task.CommonNotifyPacket;

public interface XhsOrderService extends OrderService{

	public List<CommonNotifyPacket<Order>> getOrderCancelListByStatus(String trim, Date startTime, Date endTime) throws OrderException;

	public void auditingOrderCancel(Order order)throws OrderException;

}
