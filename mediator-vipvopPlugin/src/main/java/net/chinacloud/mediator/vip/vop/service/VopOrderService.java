package net.chinacloud.mediator.vip.vop.service;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.VopStoreMessage;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.service.OrderService;

public interface VopOrderService extends OrderService{
	
	public void responseOrderStore(VopStoreMessage vopStoreMessage) throws OrderException;
	
	public void confirmStoreDelivery(VopStoreMessage vopStoreMessage) throws OrderException;
	
	public Order updateOrderPrice(Order Order) throws OrderException;

	public Order getOXOLogisticsTransportNo(Order order) throws OrderException;
	

}
