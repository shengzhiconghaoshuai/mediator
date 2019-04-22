/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DataTranslator.java
 * 描述： 数据类型转换器
 */
package net.chinacloud.mediator.init.translator;

import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Return;

/**
 * <数据类型转换器>
 * <将渠道的数据类型如Order、Return结构转换为系统的Order、Return结构>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月31日
 * @since 2014年12月31日
 */
public interface DataTranslator {
	/**
	 * 将第三方订单结构转换成内部系统的订单结构
	 * 第三方订单结构可能有多个,需要判断并转换
	 * @param source 第三方订单结构
	 * @return 内部系统订单结构
	 * @throws TranslateException
	 */
	public Order transformOrder(Object source) throws TranslateException; 
	/**
	 * 将第三方退货结构转换成内部系统的退货结构
	 * 第三方退货结构可能有多个,需要判断并转换
	 * @param source 第三方退货结构
	 * @return 内部系统退货结构
	 * @throws TranslateException
	 */
	public Return transformReturn(Object source) throws TranslateException;
	/**
	 * 将第三方退款结构转换成内部系统的退款结构
	 * 第三方退款结构可能有多个,需要判断并转换 
	 * @param source 第三方退款结构
	 * @return 内部系统退款结构
	 * @throws TranslateException
	 */
	public Refund transformRefund(Object source) throws TranslateException;
	
	/**
	 * 将第三方换货结构转换成内部系统的退款结构
	 * @param source 第三方换货结构
	 * @return 内部系统换货结构
	 * @throws TranslateException
	 */
	public Exchange translateExchange(Object source)throws TranslateException;
}
