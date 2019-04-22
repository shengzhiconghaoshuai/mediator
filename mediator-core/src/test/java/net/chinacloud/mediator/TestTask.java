/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TestTask.java
 * 描述： 
 */
package net.chinacloud.mediator;

import net.chinacloud.mediator.task.Task;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月11日 下午2:45:58
 */
public class TestTask extends Task {

	/* (non-Javadoc)
	 * @see net.chinacloud.mediator.task.Task#getType()
	 */
	@Override
	protected String getType() {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.chinacloud.mediator.task.Task#getSubType()
	 */
	@Override
	protected String getSubType() {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.chinacloud.mediator.task.Task#doTask()
	 */
	@Override
	public void doTask() throws Exception {

	}

}
