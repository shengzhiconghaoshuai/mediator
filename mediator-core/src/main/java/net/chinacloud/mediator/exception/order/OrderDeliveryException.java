/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderDeliveryException.java
 * 描述： 
 */
package net.chinacloud.mediator.exception.order;

/**
 * @description 订单发货失败异常
 * @author yejunwu123@gmail.com
 * @since 2015年7月8日 下午3:06:58
 */
public class OrderDeliveryException extends OrderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7673900402513966208L;

	/**
	 * @param code
	 * @param args
	 */
	public OrderDeliveryException(String errorMessage) {
		super("exception.order.deliver.err", errorMessage);
	}
}
