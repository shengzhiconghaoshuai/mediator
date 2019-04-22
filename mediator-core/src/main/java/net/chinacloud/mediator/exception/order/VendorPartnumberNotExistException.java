/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：VendorPartnumberNotExistException.java
 * 描述： 子订单商家编码未填
 */
package net.chinacloud.mediator.exception.order;


/**
 * <子订单商家编码未填写异常>
 * <子订单商家编码未填写异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class VendorPartnumberNotExistException extends OrderException {
	
	private static final long serialVersionUID = 136508649067526600L;

	public VendorPartnumberNotExistException(String channelOrderId, String channelOrderItemId){
		super("exception.order.vendorpartnumber.notexist", channelOrderId, channelOrderItemId);
	}
}
