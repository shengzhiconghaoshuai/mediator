package net.chinacloud.mediator.xhs.task.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderTask;
import net.chinacloud.mediator.xhs.service.XhsOrderService;
@Component("orderCancelAuditingTask")
@Scope(value="prototype")
public class OrderCancelAuditingTask extends OrderTask {

private static final String ORDER_CANCEL_AUDITING_TYPE = "cancelAuditing";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_CANCEL_AUDITING_TYPE, OrderCancelAuditingTask.class);
	}
	

	@Autowired
	XhsOrderService orderService;
	
	@Override
	protected String getSubType() {
		return ORDER_CANCEL_AUDITING_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		orderService.auditingOrderCancel(order);
    }
}
