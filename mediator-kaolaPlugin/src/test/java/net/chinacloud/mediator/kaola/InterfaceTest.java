package net.chinacloud.mediator.kaola;

import java.util.Date;

import org.junit.Test;

import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.kaola.serviceImpl.KaoLaOrderServiceImpl;

public class InterfaceTest {
	
	@Test
	public void orderInterfaceTest(){
		KaoLaOrderServiceImpl kaoLaOrderServiceImpl = new KaoLaOrderServiceImpl();
		Date startTime = new Date("2018-01-01 00:00:00"); 
		Date endTime = new Date("2018-01-22 00:00:00"); 
		try {
			kaoLaOrderServiceImpl.getOrderListByStatus("1",startTime,endTime,1,1001);
		} catch (OrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
