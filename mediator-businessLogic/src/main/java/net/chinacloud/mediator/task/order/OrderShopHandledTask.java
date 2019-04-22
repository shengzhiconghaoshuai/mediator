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
public class OrderShopHandledTask extends OrderTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderShopHandledTask.class);

    protected static final String Order_ShopHandled_Type = "shophandled";

    @Autowired
    ChannelOrderFacadeClient orderFacadeClient;

    @Override
    protected String getSubType() {
        return Order_ShopHandled_Type;
    }

    static {
        TaskManager.registTask(ORDER_TYPE, Order_ShopHandled_Type, OrderShopHandledTask.class);
    }

    @Override
    public void doTask() throws Exception {
        Order order = getOrder();
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("shophandler order info:" + order);
        }
        if (order != null) {
            OrderService orderService = getService(OrderService.class,getContext().getChannelCode());
            orderService.ShopHandledOrder(order);
        }
    }
}
