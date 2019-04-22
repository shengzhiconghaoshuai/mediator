/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderCreateTask.java
 * 描述： 订单创建
 */
package net.chinacloud.mediator.taobao.task.order;

import java.util.Date;

import net.chinacloud.mediator.dao.RepeatOrderDao;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.RepeatOrder;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.dao.TaskDao;
import net.chinacloud.mediator.task.order.OrderTask;
import net.chinacloud.mediator.translator.ChannelOrderFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <创建征集订单类型的task>
 */
@Component
@Scope(value="prototype")
public class ZhengJiOrderSupplementTask extends OrderTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZhengJiOrderSupplementTask.class);
	protected static final String ZHENGJI_SUPPLEMENT_TYPE = "zhengjisupplement";

	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;
	@Autowired
	TaskDao taskDao;
	@Autowired
	RepeatOrderDao repeatOrderDao;
	@Override
	protected String getSubType() {
		return ZHENGJI_SUPPLEMENT_TYPE;
	}
	
	static {
		TaskManager.registTask(ORDER_TYPE, ZHENGJI_SUPPLEMENT_TYPE, ZhengJiOrderSupplementTask.class);
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		Long isRepeat = taskDao.findRepeatTask(this);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("create zhengjiorder info:" + order);
		}
		//征集预售
			if(isRepeat > 1){
				saveRepeatOrder(0);
			}
			LOGGER.info(getContext().getChannelCode() + "渠道" + getContext().getApplicationCode()
					+ "应用征集订单 " + order.getChannelOrderId());
			orderFacadeClient.payForZhengji(order, getContext(), this.id);
			return;
		}
	
	private void saveRepeatOrder(Integer type){
		RepeatOrder repeatOrder=new RepeatOrder();
		repeatOrder.setApplicationId(getContext().getApplicationId());
		repeatOrder.setStarttime(new Date());
		repeatOrder.setType(type);
		repeatOrder.setTid(getDataId());
		repeatOrderDao.saveRepeatOrder(repeatOrder);
	}
}
