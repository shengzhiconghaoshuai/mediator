/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundException.java
 * 描述： 退款相关异常父类
 */
package net.chinacloud.mediator.exception.refund;

import net.chinacloud.mediator.exception.ApplicationException;
/**
 * <退款相关异常>
 * <退款相关异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class RefundException extends ApplicationException {

	private static final long serialVersionUID = 3577705387241931694L;

	protected static final String MODULE = "refund";
	
	public RefundException(String code, Object... args){
		super(MODULE, code, args);
	}
}
