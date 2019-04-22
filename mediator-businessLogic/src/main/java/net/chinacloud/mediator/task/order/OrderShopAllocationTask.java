package net.chinacloud.mediator.task.order;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Octopus8 on 2017/5/17.
 */
@Component
@Scope(value="prototype")
public class OrderShopAllocationTask extends OrderTask{

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderShopAllocationTask.class);

    protected static final String Order_ShopAllocation_Type = "shopallocation";

    @Autowired
    ChannelOrderFacadeClient orderFacadeClient;

    static {
        TaskManager.registTask(ORDER_TYPE, Order_ShopAllocation_Type, OrderShopAllocationTask.class);
    }

    @Override
    protected String getSubType() {
        return Order_ShopAllocation_Type;
    }

    @Override
    public void doTask() throws Exception {
        Order order = getOrder();
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("alllcation order info:" + order);
        }
        if (order != null) {
            OrderService orderService = getService(OrderService.class,getContext().getChannelCode());
            orderService.ShopAllocationOrder(order);
        }

    }
}
