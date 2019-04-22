/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ReturnException.java
 * 描述： 退货相关异常父类
 */
package net.chinacloud.mediator.exception.returns;

import net.chinacloud.mediator.exception.ApplicationException;
/**
 * <退货相关异常>
 * <退货相关异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class ReturnException extends ApplicationException {

	private static final long serialVersionUID = -8918195028166819666L;
	
	protected static final String MODULE = "return";
	
	public ReturnException(String code, Object... args){
		super(MODULE, code, args);
	}
}
