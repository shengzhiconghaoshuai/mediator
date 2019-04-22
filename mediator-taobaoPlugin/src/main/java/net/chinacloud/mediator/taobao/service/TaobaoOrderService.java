/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoService.java
 * 描述： 淘宝渠道特定业务接口定义
 */
package net.chinacloud.mediator.taobao.service;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
/**
 * <淘宝渠道特定业务接口定义>
 * <淘宝渠道特定业务接口定义>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
public interface TaobaoOrderService extends OrderService {
	/**
	 * 数据推送模式获取订单数据
	 * @param applicationId 应用id
	 * @return
	 */
	public void getSyncOrders(Integer applicationId, int templateId);
	/**
	 * 抓取并转换jdp数据
	 * @param startModified
	 * @param endModified
	 * @param sellerNick
	 * @param offset
	 * @param rows
	 * @return
	 * @throws OrderException
	 */
	public List<Order> getSyncOrders(Date startModified, Date endModified, String sellerNick, int offset, int rows) throws OrderException;
	/**
	 * 抓取jdp trade id
	 * @param startModified
	 * @param endModified
	 * @param sellerNick
	 * @param offset
	 * @param rows
	 * @return
	 * @throws OrderException
	 */
	public List<Long> getSyncTradesIds(Date startModified, Date endModified, String sellerNick, int offset, int rows) throws OrderException;
	/**
	 * 获取jdp trade
	 * @param tid
	 * @return
	 */
	public Order getJdpOrderById(Long tid);
	/**
	 * 回传hold订单的结果
	 * @param order
	 * @throws OrderException
	 */
	public void toRdcAligeniusOrdermsgUpdate(Order order)throws OrderException;
	
	public List<CommonNotifyPacket<Order>> getOrderStatusListByStatus(String status,
			Date startTime, Date endTime) throws OrderException;
}
