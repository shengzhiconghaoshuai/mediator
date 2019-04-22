package net.chinacloud.mediator.taobao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.taobao.api.request.TmallExchangeAgreeRequest;
import com.taobao.api.request.TmallExchangeConsigngoodsRequest;
import com.taobao.api.request.TmallExchangeGetRequest;
import com.taobao.api.request.TmallExchangeMessagesGetRequest;
import com.taobao.api.request.TmallExchangeReceiveGetRequest;
import com.taobao.api.request.TmallExchangeRefuseRequest;
import com.taobao.api.request.TmallExchangeReturngoodsAgreeRequest;
import com.taobao.api.request.TmallExchangeReturngoodsRefuseRequest;
import com.taobao.api.response.TmallExchangeAgreeResponse;
import com.taobao.api.response.TmallExchangeConsigngoodsResponse;
import com.taobao.api.response.TmallExchangeGetResponse;
import com.taobao.api.response.TmallExchangeMessagesGetResponse;
import com.taobao.api.response.TmallExchangeMessagesGetResponse.RefundMessage;
import com.taobao.api.response.TmallExchangeReceiveGetResponse;
import com.taobao.api.response.TmallExchangeRefuseResponse;
import com.taobao.api.response.TmallExchangeReturngoodsAgreeResponse;
import com.taobao.api.response.TmallExchangeReturngoodsRefuseResponse;

import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.ExchangeMessage;
import net.chinacloud.mediator.domain.ExchangeReason;
import net.chinacloud.mediator.exception.exchange.ExchangeException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.service.TmallExchangeService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.StringUtils;

@Service
public class ExchangeServiceImpl implements TmallExchangeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeServiceImpl.class);
	//private static Logger STATICLOGGER = LoggerFactory.getLogger("taskStatics");
	
	@Autowired
	ConnectorManager<Object> connectorManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	@Autowired
	ApplicationService applicationService;
	
	//每次请求条数
	private Long PAGE_COUNT = 30L;
		
	
	public Exchange getExchangeById(String channelExchangeId) throws TranslateException, ExchangeException {
		TmallExchangeGetRequest req = new TmallExchangeGetRequest();
		req.setDisputeId(Long.valueOf(channelExchangeId));
		req.setFields(TaobaoConstant.FIELDS_EXCHANGE_GET);
		TmallExchangeGetResponse response= connectorManager.getConnector().execute(req);
		Exchange exchange = null;
		if(response != null && response.getResult().getSuccess()){
			com.taobao.api.response.TmallExchangeGetResponse.Exchange tmallExchange = response.getResult().getExchange();
		    exchange = dataTranslatorManager.getDataTranslator().translateExchange(tmallExchange);
		}else if(!response.getResult().getSuccess()){
			throw new ExchangeException(response.getResult().getMessage());
		}
		return exchange;
	}
	

	@Override
	public List<CommonNotifyPacket<Exchange>> getExchangeListByTime(
			Date startTime, Date endTime) throws ExchangeException{
		return getNotifyExchangeList(1L,startTime,endTime);
	}
	


	
	public void deliverExchange(Exchange exchange) throws ExchangeException {
		TmallExchangeConsigngoodsRequest req = new TmallExchangeConsigngoodsRequest();
		req.setDisputeId(Long.valueOf(exchange.getDisputeId()));
		req.setLogisticsNo(exchange.getSellerLogisticNo());
//		卖家发货的物流类型，100表示平邮，200表示快递
		req.setLogisticsType(200L);
		req.setLogisticsCompanyName(exchange.getSellerLogisticName());
		req.setFields(TaobaoConstant.FIELDS_EXCHANGE_CONSIGNGOODS);
		TmallExchangeConsigngoodsResponse rsp = connectorManager.getConnector().execute(req);
		if(!rsp.getResult().getSuccess()){
			throw new ExchangeException(rsp.getResult().getMessage());
		}
	}
	
	
	/**卖家确认收货*/
	public void agreeExchangeReturngoods(String channelExchangeId) throws ExchangeException{
		TmallExchangeReturngoodsAgreeRequest req = new TmallExchangeReturngoodsAgreeRequest();
		req.setDisputeId(Long.valueOf(channelExchangeId));
		req.setFields(TaobaoConstant.FIELDS_EXCHANGE_RETURNGOODS_AGREE);
		TmallExchangeReturngoodsAgreeResponse rsp = connectorManager.getConnector().execute(req);
		if(!rsp.getResult().getSuccess()){
			throw new ExchangeException(rsp.getResult().getMessage());
		}
		
	}

	@Override
	public void addExchangeMessage(ExchangeMessage exchangeMessage)throws ExchangeException {
		
		
	}

	
	
	public Exchange exchangeMessageByExchangeId(String channelExchangeId)throws ExchangeException {
		Exchange exchangeMessage = new Exchange();
		TmallExchangeMessagesGetRequest req = new TmallExchangeMessagesGetRequest();
		req.setDisputeId(Long.valueOf(channelExchangeId));
		req.setFields(TaobaoConstant.FIELDS_EXCHANGE_MESSAGE_ADD);
		TmallExchangeMessagesGetResponse rsp = connectorManager.getConnector().execute(req);
		if(rsp != null ){
			List<RefundMessage> refundMessages =rsp.getResult().getResults();
			for(int i=0;i < refundMessages.size();i++){
				if(StringUtils.isEmpty(exchangeMessage.getSellerMessage())){
					if(refundMessages.get(i).getOwnerRole().contains("卖家")){
						if(!StringUtils.isEmpty(refundMessages.get(i).getContent())){
						String[] contents = refundMessages.get(i).getContent().split("\\\\n");
						exchangeMessage.setSellerMessage(contents[contents.length-1]);
						}
					}
				}
				if(StringUtils.isEmpty(exchangeMessage.getBuyerMessage())){
					if(refundMessages.get(i).getOwnerRole().contains("买家")){
						if(!StringUtils.isEmpty(refundMessages.get(i).getContent())){
							String[] contents = refundMessages.get(i).getContent().split("\\\\n");
							exchangeMessage.setBuyerMessage(contents[contents.length-1]);
						}
						
					}
				}
				if(!StringUtils.isEmpty(exchangeMessage.getSellerMessage()) && !StringUtils.isEmpty(exchangeMessage.getBuyerMessage())){
					break;
				}
			}
		}
		return exchangeMessage;
	}

	
	public void agreeExchange(Exchange exchange) throws ExchangeException{
		TmallExchangeAgreeRequest req = new TmallExchangeAgreeRequest();
		req.setLeaveMessage(exchange.getSellerMessage());
		req.setAddressId(exchange.getSellerAddressId());
		req.setDisputeId(Long.valueOf(exchange.getDisputeId()));
		req.setFields(TaobaoConstant.FIELDS_EXCHANGE_AGREE_REFUSE);
		TmallExchangeAgreeResponse rsp = connectorManager.getConnector().execute(req);
		if(!rsp.getResult().getSuccess()){
			throw new ExchangeException(rsp.getResult().getMessage());
		}
	}

	
	public void refuseExchange(Exchange exchange)throws ExchangeException {
		TmallExchangeRefuseRequest req = new TmallExchangeRefuseRequest();
//		req.setLeaveMessage(exchange.getSellerMessage());//拒绝换货申请时的留言  可选
		req.setDisputeId(Long.valueOf(exchange.getDisputeId()));
		req.setSellerRefuseReasonId(exchange.getSellerRefuseReasonID());
		req.setFields(TaobaoConstant.FIELDS_EXCHANGE_AGREE_REFUSE);
		TmallExchangeRefuseResponse rsp = connectorManager.getConnector().execute(req);
		if(!rsp.getResult().getSuccess()){
			throw new ExchangeException(rsp.getMsg());
		}
	}

	public void refuseExchangeReturngoods(Exchange exchange) throws ExchangeException {
		TmallExchangeReturngoodsRefuseRequest req = new TmallExchangeReturngoodsRefuseRequest();
//		req.setLeaveMessage(exchange.getSellerMessage());//留言说明   可选
		req.setDisputeId(Long.valueOf(exchange.getDisputeId()));
		req.setSellerRefuseReasonId(exchange.getSellerRefuseReasonID());
		TmallExchangeReturngoodsRefuseResponse rsp =connectorManager.getConnector().execute(req);
		if(!rsp.getResult().getSuccess()){
			throw new ExchangeException(rsp.getResult().getMessage());
		}
	}

	@Override
	public ExchangeReason getExchangeRefusereason(String channelExchangeId)throws ExchangeException {
		return null;
	}
	
	public List<CommonNotifyPacket<Exchange>> getNotifyExchangeList(long currPageIndex,
			Date startTime, Date endTime)throws ExchangeException{
		List<CommonNotifyPacket<Exchange>> notifys = new ArrayList<CommonNotifyPacket<Exchange>>();
		TmallExchangeReceiveGetRequest req = new TmallExchangeReceiveGetRequest();
		req.setEndGmtModifedTime(endTime);
		req.setStartGmtModifiedTime(startTime);
		req.setFields(TaobaoConstant.FIELDS_EXCHANGE_GET);
		req.setPageSize(PAGE_COUNT);
		req.setPageNo(currPageIndex);
		TmallExchangeReceiveGetResponse response = connectorManager.getConnector().execute(req);
		
		if(response != null && response.isSuccess()){
			List<com.taobao.api.response.TmallExchangeReceiveGetResponse.Exchange> tmallExchanges = response.getResults();
			if(tmallExchanges != null && tmallExchanges.size() > 0){
				for(com.taobao.api.response.TmallExchangeReceiveGetResponse.Exchange tmallExc : tmallExchanges){
					Exchange exchange = null;
					try {
						exchange = dataTranslatorManager.getDataTranslator().translateExchange(tmallExc);
					} catch (TranslateException e) {
						LOGGER.error("天猫换货数据转换失败", e);
						LOGGER.error("tmall exchange data translate error, channel tmallborder id:" + tmallExc.getDisputeId());
						MailSendUtil.sendEmail("exchange data translate error", 
								"tmall exchange data translate error, channel tmallborder id:" +  tmallExc.getDisputeId());
					} 
					if(exchange!=null){
						CommonNotifyPacket<Exchange> jnp = new CommonNotifyPacket<Exchange>(exchange);
						notifys.add(jnp);
					}
				}
				boolean totalResults =response.getHasNext();
				if(totalResults){
					Long pageIndex = currPageIndex + 1;
					notifys.addAll(getNotifyExchangeList(pageIndex,
							startTime, endTime));
				}
			}
		}
		return notifys;
	}
}
