/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SystemTask.java
 * 描述： 系统级任务
 */
package net.chinacloud.mediator.task.system;

import net.chinacloud.mediator.task.Task;
/**
 * <系统级任务>
 * <比如:停止抓单、开启抓单、清理缓存、task扫尾等>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
public abstract class SystemTask extends Task {
	
	protected static final String SYSTEM_TYPE = "SYSTEM";
	
	@Override
	protected String getType() {
		//系统级任务
		return "SYSTEM";
	}
	
}
