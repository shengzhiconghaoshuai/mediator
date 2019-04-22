/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：AbstractTaskAdapter.java
 * 描述： 
 */
package net.chinacloud.mediator.task;

import net.chinacloud.mediator.task.system.StartGetOrderTask;
import net.chinacloud.mediator.task.system.StopGetOrderTask;
import net.chinacloud.mediator.utils.SpringUtil;
/**
 * <生成具体的系统task类型>
 * <生成具体的系统task类型>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月16日
 * @since 2015年1月16日
 */
public abstract class AbstractTaskAdapter implements TaskAdapter {
	
	@Override
	public <T> Task generateTask(CommonNotifyPacket<T> packet) {
		Task task = generateSystemTask(packet);
		if (null == task) {
			task = generateServiceTask(packet);
		}
		return task;
	}
	
	/**
	 * 生成具体的系统task类型
	 * @param packet
	 * @return
	 */
	public <T> Task generateSystemTask(CommonNotifyPacket<T> packet) {
		Task task = null;
		T message = packet.getMessage();
		String type = packet.getType();
		if (null == message) {
			if ("StartGetOrder".equals(type)) {		//开启订单创建服务
				task = SpringUtil.getBean(StartGetOrderTask.class);
				task.setDataId(System.nanoTime() + "");
			} else if ("StopGetOrder".equals(type)) {	//停止订单创建服务
				task = SpringUtil.getBean(StopGetOrderTask.class);
				task.setDataId(System.nanoTime() + "");
			}
		}
		
		return task;
	}
	
	/**
	 * 生成业务相关的task
	 * @param packet
	 * @return
	 */
	public abstract <T> Task generateServiceTask(CommonNotifyPacket<T> packet);
}
