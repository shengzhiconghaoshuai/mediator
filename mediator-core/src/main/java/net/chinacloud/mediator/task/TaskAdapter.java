/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskAdapter.java
 * 描述： task适配器
 */
package net.chinacloud.mediator.task;
/**
 * <task适配器>
 * <映射具体的task类型>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
public interface TaskAdapter {
	/**
	 * 生成具体的task类型
	 * @param packet
	 * @return
	 */
	public <T> Task generateTask(CommonNotifyPacket<T> packet);
}
