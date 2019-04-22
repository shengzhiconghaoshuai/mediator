package net.chinacloud.mediator.taobao.task.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.taobao.service.TaobaoOrderService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.order.OrderTask;
/**
 * 预约退款，hold订单的结果
 * @author 
 *
 */
@Component
@Scope(value="prototype")
public class OrderUpdateResponseMsgTask extends OrderTask {

	private static final String HOLD_ORDER_RESPONSE_TYPE = "responseMsgOrderUpdate";
	
	static {
		TaskManager.registTask(ORDER_TYPE, HOLD_ORDER_RESPONSE_TYPE, OrderUpdateResponseMsgTask.class);
	}

	@Autowired
	TaobaoOrderService orderService;
	
	@Override
	protected String getSubType() {
		return HOLD_ORDER_RESPONSE_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		orderService.toRdcAligeniusOrdermsgUpdate(order);
		
    }

}
