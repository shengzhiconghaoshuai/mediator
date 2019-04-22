package net.chinacloud.mediator.service;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.ExchangeMessage;
import net.chinacloud.mediator.domain.ExchangeReason;
import net.chinacloud.mediator.exception.exchange.ExchangeException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.task.CommonNotifyPacket;

public interface ExchangeService {
	/**
	 * 根换货单号id获取换货单详情
	 * @param ExchangeId 第三方渠道(淘宝、京东)换货单号
	 * @return 内部系统Exchange结构,非第三方渠道接口返回的换货接口
	 * @throws TranslateException 
	 * @throws ExchangeException 
	 */
	public Exchange getExchangeById(String channelExchangeId) throws TranslateException, ExchangeException ;
	
	/**
	 * 根据状态获取一段时间内的订单
	 * @param startTime 查询修改时间段的开始时间点
	 * @param endTime 查询修改时间段的结束时间点
	 * @return CommonNotifyPacket包装过的换货结构
	 * @throws ExchangeException
	 */
	public List<CommonNotifyPacket<Exchange>> getExchangeListByTime(Date startTime, Date endTime) throws ExchangeException;
	
	/**
	 * 卖家发货,将发运信息更新到第三方平台
	 * @throws ExchangeException 
	 * @throws ExchangeException
	 */
	public void deliverExchange(Exchange exchange) throws ExchangeException;
	
	/**
	 * 卖家确认收货
	 */
	public void agreeExchangeReturngoods(String channelExchangeId) throws ExchangeException;
	
	/**
	 * 卖家创建留言
	 */
	public void addExchangeMessage(ExchangeMessage exchangeMessage) throws ExchangeException;
	
	/**
	 * 查询换货订单留言列表
	 */
	public Exchange exchangeMessageByExchangeId(String channelExchangeId)throws ExchangeException;
	
	/**
	 *卖家同意换货申请 
	 *只用传卖家addressId和换货单号
	 */
	public void agreeExchange(Exchange exchange) throws ExchangeException;

	
	/**
	 *tmall.exchange.refuse (卖家拒绝换货申请)
	 */
	public void refuseExchange(Exchange exchange) throws ExchangeException;
	
	/**卖家拒绝确认收货
	 * */
	public void refuseExchangeReturngoods(Exchange exchange) throws ExchangeException;
	/**
	 * 取拒绝换货原因列表
	 * @param channelExchange
	 * @return ExchangeReason
	 */
	public ExchangeReason getExchangeRefusereason(String channelExchange)throws ExchangeException;
	
}
