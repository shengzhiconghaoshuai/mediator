/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderCreateTask.java
 * 描述： 订单创建
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
 * <创建订单类型的task>
 * <创建订单类型的task>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
@Component
@Scope(value="prototype")
public class OrderCreateTask extends OrderTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreateTask.class);
	protected static final String ORDER_CREATE_TYPE = "create";

	@Autowired
	ChannelOrderFacadeClient orderFacadeClient;
	/*@Autowired
	TaskDao taskDao;
	@Autowired
	RepeatOrderDao repeatOrderDao;*/
	@Override
	protected String getSubType() {
		return ORDER_CREATE_TYPE;
	}
	
	// 仅仅只有静态代码块注册的方式并没有什么卵用,因为每个task是prototype的,spring容器初始化通过解析读取的Class文件生成bean定义,
	// 并没有加载该类型到内存,更别说class初始化了,所以容器启动完,并不会立马执行该静态代码块,要等到task创建才会初始化,因此在扫尾时
	// 还是可能会存在问题(由于task没有注册,无法获取task的具体类型)
	// 解决方法:使用自定义注解@Task,继承spring的@Component注解,提供注解处理类,显示调用Class.forName()方法,使得类被加载并初始化,
	// 静态代码块被执行
	// task的type和subType也许可以放到@Task注解中
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_CREATE_TYPE, OrderCreateTask.class);
	}

	@Override
	public void doTask() throws Exception {
		Order order = getOrder();
		//Long isRepeat = taskDao.findRepeatTask(this);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("create order info:" + order);
		}
		//征集预售
		if(!StringUtils.isEmpty(order.getAdditionalParam("zhengji_status"))){
			LOGGER.info(getContext().getChannelCode() + "渠道" + getContext().getApplicationCode()
					+ "应用征集订单 " + order.getChannelOrderId());
			orderFacadeClient.submitChannelOrder(order, getContext(), this.id);
			return;
		}
		//预售订单
		String stepStatus = order.getStepStatus();
		if(stepStatus != null && Order.STEP_STATUS_FRONT_PAID_FINAL_PAID.equalsIgnoreCase(stepStatus)) {
        	//定金已付&尾款已付
			if(LOGGER.isInfoEnabled()){
				LOGGER.info(getContext().getChannelCode() + "渠道" + getContext().getApplicationCode()
						+ "应用预售订单 " + order.getChannelOrderId() + " 支付尾款");
			}
			orderFacadeClient.payForStepTrade(order, getContext(), this.id);
			return;
        }
		orderFacadeClient.submitChannelOrder(order, getContext(), this.id);
	}
}
