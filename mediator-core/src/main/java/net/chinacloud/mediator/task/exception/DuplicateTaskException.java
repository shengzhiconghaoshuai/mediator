/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DuplicateTaskException.java
 * 描述： task重复异常
 */
package net.chinacloud.mediator.task.exception;
/**
 * <task重复异常>
 * <task重复异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
public class DuplicateTaskException extends TaskException {

	private static final long serialVersionUID = 8292297987953429113L;

	/**
	 * @param channelCode 渠道标记
	 * @param applicationCode 应用标记
	 * @param taskType task类型
	 * @param subType 子类型
	 */
	public DuplicateTaskException(String channelCode, String applicationCode, String taskType, String subType) {
		super("exception.task.duplicate", channelCode, applicationCode, taskType, subType);
	}
}
