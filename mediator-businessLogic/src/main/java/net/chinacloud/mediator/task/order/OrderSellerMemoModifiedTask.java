/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderMemoModifiedTask.java
 * 描述： 订单卖家备注修改
 */
package net.chinacloud.mediator.task.order;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <订单卖家备注修改>
 * <订单目前暂时没有整体的修改功能,否则可以直接使用订单修改功能;
 * 现目前的做法是卖家备注单独拿出来,目前仅淘宝渠道使用>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月14日
 * @since 2015年1月14日
 */
@Component
@Scope(value="prototype")
public class OrderSellerMemoModifiedTask extends OrderTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderSellerMemoModifiedTask.class);

	protected static final String ORDER_SELLER_MEMO_MODIFY_TYPE = "sellerMemoModify";
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_SELLER_MEMO_MODIFY_TYPE, OrderSellerMemoModifiedTask.class);
	}
	
	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;
	
	@Override
	protected String getSubType() {
		//修改备注
		return ORDER_SELLER_MEMO_MODIFY_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("seller memo update order info:" + order);
		}
		
		String sellerMemo = order.getSellerMemo();
		if(!StringUtils.hasText(sellerMemo)) {
			//如果卖家备注为空,则不进行下一步处理
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("卖家备注为空!");
			}
			return;
		}
		
		orderFacadeClient.saveSellerMemo(order, getContext(), this.id);
	}

}
