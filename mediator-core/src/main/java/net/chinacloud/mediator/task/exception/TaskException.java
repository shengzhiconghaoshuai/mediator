/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskException.java
 * 描述： task执行相关异常父类
 */
package net.chinacloud.mediator.task.exception;

import net.chinacloud.mediator.exception.ApplicationException;
/**
 * <task执行相关异常>
 * <task执行相关异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class TaskException extends ApplicationException {

	private static final long serialVersionUID = 196080734044463508L;

	protected static final String MODULE = "task";
	
	public TaskException(String code, Object... args){
		super(MODULE, code, args, "task相关操作失败");
	}
}
