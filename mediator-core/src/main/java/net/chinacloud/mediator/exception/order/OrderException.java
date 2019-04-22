/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderException.java
 * 描述： 订单相关异常父类
 */
package net.chinacloud.mediator.exception.order;

import net.chinacloud.mediator.exception.ApplicationException;
/**
 * <订单相关异常>
 * <订单相关异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class OrderException extends ApplicationException {

	private static final long serialVersionUID = 8041660100313388569L;
	
	protected static final String MODULE = "order";
	
	public OrderException(String code, Object... args){
		super(MODULE, code, args, "订单相关操作失败");
	}
}
