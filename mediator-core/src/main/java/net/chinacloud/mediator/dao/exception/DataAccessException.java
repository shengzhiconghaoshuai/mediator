/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DataAccessException.java
 * 描述： 数据访问异常父类
 */
package net.chinacloud.mediator.dao.exception;

import net.chinacloud.mediator.exception.ApplicationException;

/**
 * <数据访问异常>
 * <数据访问异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class DataAccessException extends ApplicationException {

	private static final long serialVersionUID = 4048589696725663761L;

	protected static final String MODULE = "data access";
	
	public DataAccessException(String code, Object... args){
		super(MODULE, code, args, "数据访问相关操作失败");
	}
}
