/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderServiceImpl.java
 * 描述： 淘宝业务实现
 */
package net.chinacloud.mediator.taobao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.OrderItem;
import net.chinacloud.mediator.domain.QimenEventProduce;
import net.chinacloud.mediator.domain.QimenOrderAllocation;
import net.chinacloud.mediator.exception.order.OrderException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.qimen.domain.OrderCode;
import net.chinacloud.mediator.qimen.request.SyncOrderRequest;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.schedule.service.CronLasttimeService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.dao.OrderSyncDao;
import net.chinacloud.mediator.taobao.dao.OrderSyncJdpDao;
import net.chinacloud.mediator.taobao.domain.JdpTrade;
import net.chinacloud.mediator.taobao.service.TaobaoOrderService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoParser;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.parser.json.ObjectJsonParser;
import com.taobao.api.request.LogisticsConsignResendRequest;
import com.taobao.api.request.LogisticsDummySendRequest;
import com.taobao.api.request.LogisticsOfflineSendRequest;
import com.taobao.api.request.QimenEventProduceRequest;
import com.taobao.api.request.RdcAligeniusOrdermsgUpdateRequest;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.request.TradesSoldIncrementvGetRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;
import com.taobao.api.response.TradesSoldIncrementvGetResponse;
/**
 * <淘宝业务实现>
 * <淘宝业务实现>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
@Service
public class OrderServiceImpl implements TaobaoOrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	//private static Logger STATICLOGGER = LoggerFactory.getLogger("taskStatics");
	
	@Autowired
	ConnectorManager<Object> connectorManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	OrderSyncDao orderSyncDao;
	//@Autowired
	//OrderSyncJdpDao orderSyncJdpDao;
	
	//每次请求条数
	private long PAGE_COUNT = 40L;

	public Order getOrderById(String channelOrderId) throws OrderException, TranslateException {
		TradeFullinfoGetRequest request = new TradeFullinfoGetRequest();
		request.setTid(Long.valueOf(channelOrderId));
		request.setFields("seller_nick, buyer_nick, title, type,promotion, created, tid, seller_rate,buyer_flag, buyer_rate, status, payment, discount_fee,adjust_fee, post_fee, total_fee, trade_from,pay_time, end_time, modified, consign_time, buyer_obtain_point_fee, point_fee, real_point_fee, received_payment, commission_fee, buyer_memo, seller_memo, alipay_id,alipay_no, buyer_message, num_iid, num, price, buyer_alipay_no, receiver_name, receiver_state, receiver_city, receiver_district, receiver_address, receiver_zip, receiver_mobile, receiver_phone, buyer_email,seller_flag, seller_alipay_no, seller_mobile, seller_phone, seller_name, seller_email, available_confirm_fee, has_post_fee, timeout_action_time, snapshot_url, cod_fee, cod_status, shipping_type, trade_memo, is_3D,buyer_memo,buyer_email,orders.title, orders.pic_path, orders.price, orders.num, orders.num_iid, orders.sku_id, orders.refund_status, orders.status, orders.oid, orders.total_fee, orders.payment, orders.discount_fee, orders.adjust_fee, orders.sku_properties_name, orders.item_meal_name, orders.outer_sku_id, orders.outer_iid, orders.buyer_rate, orders.seller_rate,orders.is_oversold, orders.part_mjz_discount, promotion_details,end_time,step_trade_status,step_paid_fee,is_brand_sale,omnichannel_param");
		
		TradeFullinfoGetResponse response = connectorManager.getConnector().execute(request);
		Trade trade = response.getTrade();
		Order order = dataTranslatorManager.getDataTranslator().transformOrder(trade);
		return order;
	}

	public void deliverOrder(Order order) throws OrderException {
		//update by ywu 2016-06-16 增加无需物流发货功能
		if (order.isDummy()) {	//虚拟发货
			LOGGER.info("order {} 无需物流发货", order.getChannelOrderId());
			LogisticsDummySendRequest request = new LogisticsDummySendRequest();
			request.setTid(Long.valueOf(order.getChannelOrderId()));
			connectorManager.getConnector().execute(request);
		} else {
			// 淘宝/天猫需要支持子订单发货
			LogisticsOfflineSendRequest request = new LogisticsOfflineSendRequest();
			request.setTid(Long.valueOf(order.getChannelOrderId()));
			if (order.isSplit()) {
				//拆单发货
				request.setIsSplit(1L);
				if (!CollectionUtil.isEmpty(order.getOrderItems())) {
					StringBuilder sb = new StringBuilder();
					for (OrderItem orderItem : order.getOrderItems()) {
						sb.append(orderItem.getChannelOrderItemId()).append(",");
					}
					sb.deleteCharAt(sb.lastIndexOf(","));
					request.setSubTid(sb.toString());
				}
			}
			request.setOutSid(order.getShippingId());
			request.setCompanyCode(order.getShippingCompany());
			Map<String, Object> params = order.getAdditionalParams();
			if (!params.isEmpty()) {
				if (params.containsKey("retailStoreId")) {
					String retailStoreId = (String) params.get("retailStoreId");
					if (StringUtils.hasText(retailStoreId)) {
						String retailStoreType = (String) params.get("retailStoreType");
						String feature = "retailStoreId="+retailStoreId+";retailStoreType="+retailStoreType;
						request.setFeature(feature);
					}
				}
			}
			connectorManager.getConnector().execute(request);
		}
	}

	public List<CommonNotifyPacket<Order>> getOrderListByStatus(String status,
			Date startTime, Date endTime) throws OrderException {
		List<CommonNotifyPacket<Order>> orders = new ArrayList<CommonNotifyPacket<Order>>();
		//淘宝规定startModified endModified必须是同一天
		List<String[]> list = DateUtil.getDateSegment(startTime, endTime);
		for(String[] str : list){
			orders.addAll(getOrderListByStatus(status, 1L, DateUtil.parse(str[0]), DateUtil.parse(str[1])));
		}
		return orders;
	}
	
	protected List<CommonNotifyPacket<Order>> getOrderListByStatus(String status, Long currPageIndex,
			Date startTime, Date endTime) throws OrderException {
		List<CommonNotifyPacket<Order>> orders = new ArrayList<CommonNotifyPacket<Order>>();

		TradesSoldIncrementvGetRequest req = new TradesSoldIncrementvGetRequest();
		req.setFields("tid,status,orders.zhengji_status,is_o2o_passport");
		req.setStartCreate(startTime);
		req.setEndCreate(endTime);
		req.setStatus(status); //只扫描注册了的类型
		req.setPageNo(currPageIndex);
		//req.setType("step");
		req.setPageSize(PAGE_COUNT);
		
		TradesSoldIncrementvGetResponse response = connectorManager.getConnector().execute(req);

		if (CollectionUtil.isNotEmpty(response.getTrades())) {
			for (Trade trade : response.getTrades()) {
				//将淘宝订单结构转换成系统订单结构
				Order order = null;
				//order = dataTranslatorManager.getDataTranslator().transformOrder(trade);
				if(trade.getIsO2oPassport()!=null&&trade.getIsO2oPassport()){
					//如果是true的，忽略掉
					continue;
				}
				order = new Order();
				order.setChannelOrderId(trade.getTid() + "");
				order.setStatus(status);
				List<com.taobao.api.domain.Order> orderList = trade.getOrders();
				String zhengji_status = "";
				if (CollectionUtil.isNotEmpty(orderList)) {
					zhengji_status = orderList.get(0).getZhengjiStatus();
				}
				order.addAdditionalParam(TaobaoConstant.KEY_ORDER_ZHENGJI, zhengji_status);
				CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
				orders.add(packet);
			}
			Long totalResults = response.getTotalResults();
			long lastPageIndex = (totalResults % PAGE_COUNT == 0) ? (totalResults / PAGE_COUNT) : (totalResults / PAGE_COUNT + 1);
			long pageIndex = currPageIndex + 1;
			if (pageIndex <= lastPageIndex) {
				orders.addAll(getOrderListByStatus(status, pageIndex, startTime, endTime));
			}
		}
		return orders;
	}

	@Override
	public void reDeliverOrder(Order order) throws OrderException {
		LogisticsConsignResendRequest request = new LogisticsConsignResendRequest();
		request.setTid(Long.valueOf(order.getChannelOrderId()));
		if (order.isSplit()) {
			//拆单发货
			request.setIsSplit(1L);
			if (!CollectionUtil.isEmpty(order.getOrderItems())) {
				StringBuilder sb = new StringBuilder();
				for (OrderItem orderItem : order.getOrderItems()) {
					sb.append(orderItem.getChannelOrderItemId()).append(",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
				request.setSubTid(sb.toString());
			}
		}
		request.setOutSid(order.getShippingId());
		request.setCompanyCode(order.getShippingCompany());
		connectorManager.getConnector().execute(request);
	}

	@Override
	public void getSyncOrders(Integer applicationId, int templateId) {
		CronLasttimeService lasttimeService = SpringUtil.getBean(CronLasttimeService.class);
		Date startModified = lasttimeService.getLastime(applicationId, templateId);
		Date endModified = new Date();
		
		Application application = applicationService.getApplicationById(applicationId);
		OrderSyncJdpDao orderSyncJdpDao = SpringUtil.getBean(OrderSyncJdpDao.class);
		// 分页倒序从后往前翻页抓取同步数据
		int totalCount = orderSyncJdpDao.count(startModified, endModified, application.getNick()).intValue();
		int totalPage = getTotalPage(totalCount);
		getSyncOrders(totalPage, startModified, endModified, application.getNick());
		
		lasttimeService.insertOrUpdateLasttime(applicationId, templateId, endModified.getTime());
	}
	
	private void getSyncOrders(int currentPage, Date startModified, Date endModified, String sellerNick) {
		int offset = (currentPage - 1) * TaobaoConstant.ORDEY_SYNC_PAGE_SIZE;
		int rows = TaobaoConstant.ORDEY_SYNC_PAGE_SIZE;
		
		OrderSyncJdpDao orderSyncJdpDao = SpringUtil.getBean(OrderSyncJdpDao.class);
		// 将获取的每一页数据存入临时表
		List<Map<String, Object>> jdpTrades = orderSyncJdpDao.listByMap(startModified, endModified, sellerNick, offset, rows);
		if (CollectionUtil.isNotEmpty(jdpTrades)) {
			orderSyncDao.batchSaveJdpTrades(jdpTrades);
		}
		
		if (currentPage <= 1) {		// 从后往前翻页,已经处理到最后一页,不再处理
			return;
		} else {
			//int newCount = orderSyncJdpDao.count(startModified, endModified, sellerNick);
			getSyncOrders(--currentPage, startModified, endModified, sellerNick);
		}
	}
	
	/**
	 * 计算总页数
	 * @param totalCount
	 * @return
	 */
	private int getTotalPage(int totalCount) {
		return ((totalCount % TaobaoConstant.ORDEY_SYNC_PAGE_SIZE == 0) ? (totalCount / TaobaoConstant.ORDEY_SYNC_PAGE_SIZE) 
				: (totalCount / TaobaoConstant.ORDEY_SYNC_PAGE_SIZE + 1));
	}
	
	public List<Order> getSyncOrders(Date startModified, Date endModified, String sellerNick, int offset, int rows) throws OrderException {
		List<Order> orders = new ArrayList<Order>();

		OrderSyncJdpDao orderSyncJdpDao = SpringUtil.getBean(OrderSyncJdpDao.class);
		//Date startGetTradeTime = new Date();
		//STATICLOGGER.info("start get jdp trade data at " + DateUtil.format(startGetTradeTime) + " with offset " + offset);
		List<JdpTrade> jdpTrades = orderSyncJdpDao.list(startModified, endModified, sellerNick, offset, rows);
		//Date endGetTradeTime = new Date();
		//STATICLOGGER.info("end get jdp trade data at " + DateUtil.format(endGetTradeTime) + " with offset " + offset + " total time " + (endGetTradeTime.getTime() - startGetTradeTime.getTime()));

		if (CollectionUtil.isNotEmpty(jdpTrades)) {
			TaobaoParser<TradeFullinfoGetResponse> parser = new ObjectJsonParser<TradeFullinfoGetResponse>(TradeFullinfoGetResponse.class);
			for (JdpTrade jdpTrade : jdpTrades) {
				String tradeStatus = jdpTrade.getStatus();
				if ("WAIT_SELLER_SEND_GOODS".equals(tradeStatus)) {
					//将淘宝订单结构转换成系统订单结构
					Trade trade = null;
					try {
						TradeFullinfoGetResponse response = parser.parse(jdpTrade.getJdpResponse());
						trade = response.getTrade();
						Order order = dataTranslatorManager.getDataTranslator().transformOrder(trade);
						orders.add(order);
					} catch (TranslateException e) {
						//e.printStackTrace();
						LOGGER.error("淘宝订单数据转换失败", e);
						// 这边catch住异常,这个订单就会丢失,不捕获这个异常,会导致剩余的订单未处理
						LOGGER.error("taobao order data translate error, channel order id:" + jdpTrade.getTid());
						MailSendUtil.sendEmail("order data translate error", 
								"taobao order data translate error, channel order id:" + jdpTrade.getTid());
					} catch (ApiException e) {
						//e.printStackTrace();
						LOGGER.error("淘宝订单数据解析失败", e);
						// 这边catch住异常,这个订单就会丢失,不捕获这个异常,会导致剩余的订单未处理
						LOGGER.error("taobao trade data parse error, channel order id:" + jdpTrade.getTid());
						MailSendUtil.sendEmail("trade data parse error", 
								"taobao trade data parse error, channel order id:" + jdpTrade.getTid());
					} catch (Exception e) {
						LOGGER.error("未知异常", e);
						MailSendUtil.sendEmail("trade data process error", 
								"taobao trade data process error, channel order id:" + jdpTrade.getTid());
					}
				}
			}
		}
		return orders;
	}
	
	public List<Long> getSyncTradesIds(Date startModified, Date endModified, String sellerNick, int offset, int rows) throws OrderException {
		List<Long> tids = new ArrayList<Long>();

		OrderSyncJdpDao orderSyncJdpDao = SpringUtil.getBean(OrderSyncJdpDao.class);
		//Date startGetTradeTime = new Date();
		//STATICLOGGER.info("start get jdp trade data at " + DateUtil.format(startGetTradeTime) + " with offset " + offset);
		List<JdpTrade> jdpTrades = orderSyncJdpDao.listTid(startModified, endModified, sellerNick, offset, rows);
		//Date endGetTradeTime = new Date();
		//STATICLOGGER.info("end get jdp trade data at " + DateUtil.format(endGetTradeTime) + " with offset " + offset + " total time " + (endGetTradeTime.getTime() - startGetTradeTime.getTime()));

		if (CollectionUtil.isNotEmpty(jdpTrades)) {
			for (JdpTrade jdpTrade : jdpTrades) {
				String tradeStatus = jdpTrade.getStatus();
				if ("WAIT_SELLER_SEND_GOODS".equals(tradeStatus) || "WAIT_BUYER_PAY".equals(tradeStatus)) {
					tids.add(jdpTrade.getTid());
				}
			}
		}
		return tids;
	}

	@Override
	public Order getJdpOrderById(Long tid) {
		OrderSyncJdpDao orderSyncJdpDao = SpringUtil.getBean(OrderSyncJdpDao.class);
		try {
			//Date startGetJdpDetail = new Date();
			//STATICLOGGER.info("start get jdp trade detail at " + DateUtil.format(startGetJdpDetail) + " with tid " + tid);
			JdpTrade jdpTrade = orderSyncJdpDao.getJdpTrade(tid);
			//Date endGetJdpDetail = new Date();
			//STATICLOGGER.info("end get jdp trade detail at " + DateUtil.format(endGetJdpDetail) + " with tid " + tid + ", total time " + (endGetJdpDetail.getTime() - startGetJdpDetail.getTime()));
			
			TaobaoParser<TradeFullinfoGetResponse> parser = new ObjectJsonParser<TradeFullinfoGetResponse>(TradeFullinfoGetResponse.class);
			TradeFullinfoGetResponse response = parser.parse(jdpTrade.getJdpResponse());
			Trade trade = response.getTrade();
			Order order = dataTranslatorManager.getDataTranslator().transformOrder(trade);
			return order;
		} catch (ApiException e) {
			//e.printStackTrace();
			LOGGER.error("淘宝订单数据解析失败", e);
			// 这边catch住异常,这个订单就会丢失,不捕获这个异常,会导致剩余的订单未处理
			LOGGER.error("taobao trade data parse error, channel order id:" + tid);
			MailSendUtil.sendEmail("trade data parse error", 
					"taobao trade data parse error, channel order id:" + tid);
		} catch (TranslateException e) {
			//e.printStackTrace();
			LOGGER.error("淘宝订单数据转换失败", e);
			// 这边catch住异常,这个订单就会丢失,不捕获这个异常,会导致剩余的订单未处理
			LOGGER.error("taobao order data translate error, channel order id:" + tid);
			MailSendUtil.sendEmail("order data translate error", 
					"taobao order data translate error, channel order id:" + tid);
		} catch (Exception e) {
			LOGGER.error("no jdp trade found for tid " + tid, e);
			MailSendUtil.sendEmail("no jdp trade found", "tid:" + tid);
		}
		return null;
	}

	@Override
	public void AllocationNotifiedOrder(Order order) throws OrderException {
		QimenOrderAllocation orderAllocation = order.getQimenOrderAllocation();
		if (orderAllocation != null) {
			Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
			Application application = applicationService.getApplicationById(applicationId);
			OrderCode orderCode = new OrderCode();
			orderCode.setOrderCode(orderAllocation.getChannelOrderitemIds().get(0));
			SyncOrderRequest request = new SyncOrderRequest();
			request.setParentOrderCode(Long.valueOf(orderAllocation.getChannelOrderId()));
			request.setActionTime(orderAllocation.getActionTime());
			request.setType(orderAllocation.getType());
			request.setOrderCodes(orderCode);
			request.setOperator(orderAllocation.getOperator());
			request.setStatus(orderAllocation.getStatus());
			request.setStoreId(Long.valueOf(orderAllocation.getShopId()));
			request.setSellerCode(application.getParam().getVendorId());
			connectorManager.getConnector().execute(request);
		}
	}

	@Override
	public void ShopHandledOrder(Order order) throws OrderException {
		QimenOrderAllocation orderAllocation = order.getQimenOrderAllocation();
		if (orderAllocation != null) {
			Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
			Application application = applicationService.getApplicationById(applicationId);
			OrderCode orderCode = new OrderCode();
			orderCode.setOrderCode(orderAllocation.getChannelOrderitemIds().get(0));
			SyncOrderRequest request = new SyncOrderRequest();
			request.setParentOrderCode(Long.valueOf(orderAllocation.getChannelOrderId()));
			request.setActionTime(orderAllocation.getActionTime());
			request.setType(orderAllocation.getType());
			request.setOrderCodes(orderCode);
			request.setOperator(orderAllocation.getOperator());
			request.setStatus(orderAllocation.getStatus());
			request.setStoreId(Long.valueOf(orderAllocation.getShopId()));
			request.setSellerCode(application.getParam().getVendorId());
			connectorManager.getConnector().execute(request);
		}

	}

	@Override
	public void ShopAllocationOrder(Order order) throws OrderException {
		QimenOrderAllocation orderAllocation = order.getQimenOrderAllocation();
		if (orderAllocation != null) {
			Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
			Application application = applicationService.getApplicationById(applicationId);
			OrderCode orderCode = new OrderCode();
			orderCode.setOrderCode(orderAllocation.getChannelOrderitemIds().get(0));
			SyncOrderRequest request = new SyncOrderRequest();
			request.setParentOrderCode(Long.valueOf(orderAllocation.getChannelOrderId()));
			request.setActionTime(orderAllocation.getActionTime());
			request.setType(orderAllocation.getType());
			request.setOrderCodes(orderCode);
			request.setOperator(orderAllocation.getOperator());
			request.setStatus(orderAllocation.getStatus());
			request.setStoreId(Long.valueOf(orderAllocation.getShopId()));
			request.setSellerCode(application.getParam().getVendorId());
			connectorManager.getConnector().execute(request);
		}
	}

	@Override
	public void deliverOrderEventToQimen(QimenEventProduce qimenEventProduce)throws OrderException {
		QimenEventProduceRequest req = new QimenEventProduceRequest();
		req.setStatus(qimenEventProduce.getQimemStatus());//必填
		req.setTid(qimenEventProduce.getChannelOrderId());//必填
		if(StringUtils.hasText(qimenEventProduce.getExt())){
			req.setExt(qimenEventProduce.getExt());
		}
		if(qimenEventProduce.getCreate() != null){
			req.setCreate(qimenEventProduce.getCreate());;
		}
		if(StringUtils.hasText(qimenEventProduce.getPlatform())){
			req.setPlatform(qimenEventProduce.getPlatform());
			req.setNick(qimenEventProduce.getNick());
		}
		connectorManager.getConnector().execute(req);
	}
	
	public void toRdcAligeniusOrdermsgUpdate(Order order)throws OrderException{
		RdcAligeniusOrdermsgUpdateRequest req = new RdcAligeniusOrdermsgUpdateRequest();
		req.setOid(Long.valueOf(order.getOrderItems().get(0).getChannelOrderItemId()));
		req.setStatus(order.getAdvanceStatus());
		req.setTid(Long.valueOf(order.getChannelOrderId()));
		connectorManager.getConnector().execute(req);
	}
	
	/**天猫订单状态更新*/
	public List<CommonNotifyPacket<Order>> getOrderStatusListByStatus(String status,
			Date startTime, Date endTime) throws OrderException {
		List<CommonNotifyPacket<Order>> orders = new ArrayList<CommonNotifyPacket<Order>>();
		//淘宝规定startModified endModified必须是同一天
		List<String[]> list = DateUtil.getDateSegment(startTime, endTime);
		for(String[] str : list){
			orders.addAll(geNotifyOrderStatusListByStatus(status, 1L, DateUtil.parse(str[0]), DateUtil.parse(str[1])));
		}
		return orders;
	}
	
	protected List<CommonNotifyPacket<Order>> geNotifyOrderStatusListByStatus(String status, Long currPageIndex,
			Date startTime, Date endTime) throws OrderException {
		List<CommonNotifyPacket<Order>> orders = new ArrayList<CommonNotifyPacket<Order>>();
		TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
		req.setFields("tid,status,modified,is_o2o_passport");
		req.setStartModified(startTime);
		req.setEndModified(endTime);
		req.setStatus(status);//只扫描注册了的类型
		req.setUseHasNext(true);
		req.setPageNo(currPageIndex);
		req.setPageSize(PAGE_COUNT);
		
		TradesSoldIncrementGetResponse response = connectorManager.getConnector().execute(req);

		if (CollectionUtil.isNotEmpty(response.getTrades())) {
			for (Trade trade : response.getTrades()) {
				//将淘宝订单结构转换成系统订单结构
				Order order = null;
				if(trade.getIsO2oPassport() != null && trade.getIsO2oPassport()){
					//如果是true的，忽略掉
					continue;
				}
				order = new Order();
				order.setChannelOrderId(trade.getTid() + "");
				order.setModified(trade.getModified());
				order.setStatus(trade.getStatus());
				CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
				packet.setType("saveOrderStatus");
				orders.add(packet);
			}
			boolean has_next = response.getHasNext();
			if(has_next){
				Long pageIndex = currPageIndex + 1;
				orders.addAll(geNotifyOrderStatusListByStatus(status, pageIndex, startTime, endTime));
			}
		}
		return orders;
	}
}
