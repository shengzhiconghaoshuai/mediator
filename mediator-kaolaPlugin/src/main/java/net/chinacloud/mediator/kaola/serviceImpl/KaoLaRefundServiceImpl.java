package net.chinacloud.mediator.kaola.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.refund.RefundException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.kaola.bean.KaoLaConstant;
import net.chinacloud.mediator.kaola.bean.KaoLaRefund;
import net.chinacloud.mediator.kaola.exception.KaoLaException;
import net.chinacloud.mediator.kaola.init.DefaultKaoLaClient;
import net.chinacloud.mediator.kaola.init.KaoLaClient;
import net.chinacloud.mediator.kaola.request.KaoLaRequest;
import net.chinacloud.mediator.kaola.request.RefundDetailRequest;
import net.chinacloud.mediator.kaola.request.RefundListRequest;
import net.chinacloud.mediator.kaola.response.RefundDetailReponse;
import net.chinacloud.mediator.kaola.response.RefundListResponse;
import net.chinacloud.mediator.kaola.service.KaoLaRefundService;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.DateUtil;
@Service
public class KaoLaRefundServiceImpl implements KaoLaRefundService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaRefundServiceImpl.class);
	@Autowired
	ConnectorManager<KaoLaRequest> connectorManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	//每次请求条数
	private Integer PAGE_COUNT = 10;
	
	@Override
	public List<CommonNotifyPacket<Refund>> getRefundListByStatus(
			String status, Date startTime, Date endTime,String name) throws RefundException {
		
		return getRefundListByStatus(status,1,startTime,endTime);
	}
	
	public List<CommonNotifyPacket<Refund>> getRefundListByStatus(String status,int currPageIndex,
			Date startTime, Date endTime)throws RefundException{
		List<CommonNotifyPacket<Refund>> notifys = new ArrayList<CommonNotifyPacket<Refund>>();
		RefundListRequest request = new RefundListRequest();
		request.setPage_no(currPageIndex);
		request.setPage_size(PAGE_COUNT);
		request.setStart_time(DateUtil.format(startTime));
		request.setEnd_time(DateUtil.format(endTime));
		request.setStatus(Integer.valueOf(status));
		RefundListResponse response = connectorManager.getConnector().execute(request);
		
		if(response != null ){
			List<KaoLaRefund> refundAppllyLists = response.getKaola_refund_search_response().getRefunds();
			if(refundAppllyLists != null && refundAppllyLists.size() > 0){
				for(KaoLaRefund kaolaRefund : refundAppllyLists){
					Refund aRefund = null;
					try {
						aRefund = dataTranslatorManager.getDataTranslator().transformRefund(kaolaRefund);
					} catch (TranslateException e) {
						LOGGER.error("kaola退款数据转换失败", e);
						LOGGER.error("kaola refund data translate error, channel kaola id:" + kaolaRefund.getOrder_id());
						MailSendUtil.sendEmail("refund data translate error", 
								"kaola refund data translate error, channel kaola id:" + kaolaRefund.getOrder_id());
					} 
					if(aRefund!=null){
						CommonNotifyPacket<Refund> jnp = new CommonNotifyPacket<Refund>(aRefund);
						//jnp.setMessage(aRefund);
						notifys.add(jnp);
					}
				}
				
				int totalResults =response.getKaola_refund_search_response().getTotal_count();
				int lastPageIndex = totalResults % PAGE_COUNT == 0 ? totalResults/ PAGE_COUNT: totalResults / PAGE_COUNT + 1;
				int pageIndex = currPageIndex + 1;
				if (pageIndex <= lastPageIndex) {
					notifys.addAll(getRefundListByStatus(status,pageIndex,
							startTime, endTime));
				}
			}
		}
		return notifys;
	}
	
	public Refund getRefundByRefundId(String refundId) throws RefundException,
			TranslateException {
		RefundDetailRequest request = new RefundDetailRequest();
		request.setRefund_id(refundId);
		Refund refund = null;
		RefundDetailReponse detailResponse = null;
		
		detailResponse = connectorManager.getConnector().execute(request); 
		if(detailResponse.getKaola_refund_get_response()!=null){
			refund = dataTranslatorManager.getDataTranslator().transformRefund(detailResponse.getKaola_refund_get_response().getRefundDetailInfoDTO());
		}else{
			throw new RefundException(detailResponse.getError_response().getMsg());
		}
		return refund;
	}
	
	public Refund getRefundByOrder(Order order) {
		
		Refund refund = new Refund();
		//渠道退款编号---退款单id 
		refund.setChannelRefundId(order.getChannelOrderId()+ "");
		//渠道订单号-----订单号 
		refund.setChannelOrderId(order.getChannelOrderId() + "");
		//退款金额
		refund.setRefundFee(order.getPayment());
		//退款申请时间
		refund.setCreateTime(order.getPayTime());
		//退款审核时间---退款修改时间
		if(order.getModified()!=null){
			refund.setModifyTime(order.getModified());
		}else{
			//没有审核时jingdongRefund.getCheckTime()就会是null
			refund.setModifyTime(null);
		}
		//客户帐号---买家账号
		refund.setBuyerNick(order.getShopper().getChannelUserId());
		
		//审核状态 ----退款申请单状态 
		String status = order.getStatus();
		if( status.equals(KaoLaConstant.ORDER_TO_REFUND)){
			//买家已经申请退款,等待卖家同意
			refund.setStatus(Refund.STATUS_WAIT_SELLER_AGREE);
			refund.setStatusDesc(Refund.STATUS_WAIT_SELLER_AGREE_DESC);
		}else if(status.equals(KaoLaConstant.ORDER_TO_CANCELED)){
			//同意退款       
			refund.setStatus(Refund.STATUS_SUCCESS);
			refund.setStatusDesc(Refund.STATUS_SUCCESS_DESC);
		}else if(status.equals(KaoLaConstant.ORDER_TO_REVOKE)){
			//卖家拒绝退款    
			refund.setStatus(Refund.STATUS_CLOSED);
			refund.setStatusDesc(Refund.STATUS_CLOSED_DESC);
		}
		//退款阶段
		refund.addAdditionalParam("refundPhase","onsale");
		return refund;
	}
	
	//test
	public List<CommonNotifyPacket<KaoLaRefund>> getNotifyFinishedList111(int status,int currPageIndex,
			String startTime, String endTime)throws RefundException{
		List<CommonNotifyPacket<KaoLaRefund>> notifys = new ArrayList<CommonNotifyPacket<KaoLaRefund>>();
		RefundListRequest request = new RefundListRequest();
		request.setPage_no(currPageIndex);
		request.setPage_size(PAGE_COUNT);
		request.setStart_time(startTime);
		request.setEnd_time(endTime);
		request.setStatus(Integer.valueOf(status));
		KaoLaClient kaola = new DefaultKaoLaClient("http://openapi-test.kaola.com/router", "edb6c3b9ac4847e7584c38e2b630b14f", "8200ee92ec22fcae76e2f00bc5c79247188e0593", "b6ee443b-cba6-4327-83ae-0de0afd58c95");

		RefundListResponse response = null;
		try {
			response = (RefundListResponse) kaola.execute(request);
		} catch (KaoLaException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(response != null ){
			List<KaoLaRefund> refundAppllyLists = response.getKaola_refund_search_response().getRefunds();
			if(refundAppllyLists != null && refundAppllyLists.size() > 0){
				for(KaoLaRefund kaolaRefund : refundAppllyLists){
					KaoLaRefund aRefund = kaolaRefund;
						CommonNotifyPacket<KaoLaRefund> jnp = new CommonNotifyPacket<KaoLaRefund>(aRefund);
						//jnp.setMessage(aRefund);
						notifys.add(jnp);
					}
				}
				
				int totalResults =response.getKaola_refund_search_response().getTotal_count();
				int lastPageIndex = totalResults % PAGE_COUNT == 0 ? totalResults
						/ PAGE_COUNT
						: totalResults / PAGE_COUNT + 1;
				int pageIndex = currPageIndex + 1;
				if (pageIndex <= lastPageIndex) {
					notifys.addAll(getNotifyFinishedList111(status,pageIndex,
							startTime, endTime));
				}
			}
		return notifys;
	}
	
}
