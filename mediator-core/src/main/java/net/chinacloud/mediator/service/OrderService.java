/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderService.java
 * 描述： 订单相关接口
 */
package net.chinacloud.mediator.service;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.QimenEventProduce;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.task.CommonNotifyPacket;

/**
 * <订单接口>
 * <订单业务相关接口>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月10日
 * @since 2014年12月10日
 */
public interface OrderService {
	
	/**
	 * 根据订单id获取订单详情
	 * @param orderId 第三方渠道(淘宝、京东)订单号
	 * @return 内部系统订单结构,非第三方渠道接口返回的订单接口
	 * @throws OrderException
	 * @throws TranslateException 
	 */
	public Order getOrderById(String channelOrderId) throws OrderException, TranslateException;
	/**
	 * 发货,将发运信息更新到第三方平台
	 * 看平台支持情况,如果平台支持子订单发货,则按子订单发货,如果不支持,则整单发货
	 * @param order 包含发运信息的订单结构
	 * @throws OrderException
	 */
	public void deliverOrder(Order order) throws OrderException;
	/**
	 * 重新发货,将发运信息更新到第三方平台
	 * 看平台支持情况,如果平台支持子订单发货,则按子订单发货,如果不支持,则整单发货
	 * @param order 包含发运信息的订单结构
	 * @throws OrderException
	 */
	public void reDeliverOrder (Order order) throws OrderException;
	/**
	 * 根据状态获取一段时间内的订单
	 * @param status 订单状态
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return CommonNotifyPacket包装过的订单结构
	 * @throws OrderException
	 */
	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,Date startTime, Date endTime) throws OrderException;
	
	/**
	 * 订单全链路 通知门店配货
	 * @param order
	 * @throws OrderException
	 */
	public void AllocationNotifiedOrder(Order order) throws OrderException;

	/**
	 * 订单全链路 门店已接单
	 * @param order
	 * @throws OrderException
	 */
	public void ShopHandledOrder(Order order) throws OrderException;
	/**
	 * 订单全链路 门店已发货
	 * @param order
	 * @throws OrderException
	 */
	public void ShopAllocationOrder(Order order) throws OrderException;
	
	/**
	 * 接入订单全链路监控
	 * @param qimenEventProduce
	 * @throws OrderException 
	 */
	public void deliverOrderEventToQimen(QimenEventProduce qimenEventProduce) throws OrderException;
}
