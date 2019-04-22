package net.chinacloud.mediator.jingdong.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.refund.RefundException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.jingdong.constants.JingDongConstants;
import net.chinacloud.mediator.jingdong.service.JingdongRefundService;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jd.open.api.sdk.domain.refundapply.RefundApplySoaService.RefundApplyVo;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.request.refundapply.PopAfsSoaRefundapplyQueryPageListRequest;
import com.jd.open.api.sdk.response.refundapply.PopAfsSoaRefundapplyQueryPageListResponse;
@Service("jingdongRefundServiceImpl")
public class RefundServiceImpl implements JingdongRefundService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefundServiceImpl.class);
	@Autowired
	ConnectorManager<JdRequest<?>> connectorManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	//每次请求条数
	private Integer PAGE_COUNT = 10;
	
	@Override
	public List<CommonNotifyPacket<Refund>> getRefundListByStatus(
			String status, Date startTime, Date endTime,String name) throws RefundException {
		
		return getNotifyFinishedList(status,1,startTime,endTime);
	}
	
	public List<CommonNotifyPacket<Refund>> getNotifyFinishedList(String status,int currPageIndex,
			Date startTime, Date endTime)throws RefundException{
		List<CommonNotifyPacket<Refund>> notifys = new ArrayList<CommonNotifyPacket<Refund>>();
		PopAfsSoaRefundapplyQueryPageListRequest request=new PopAfsSoaRefundapplyQueryPageListRequest();
		request.setPageIndex(currPageIndex);
		request.setPageSize(PAGE_COUNT);
		if(JingDongConstants.API_REFUND_STATUS0.equals(status)){
			request.setApplyTimeStart(DateUtil.format(startTime));
			request.setApplyTimeEnd(DateUtil.format(endTime));
		}else {
			request.setCheckTimeStart(DateUtil.format(startTime));
			request.setCheckTimeEnd(DateUtil.format(endTime));
		}
		request.setStatus(Long.valueOf(status));
		PopAfsSoaRefundapplyQueryPageListResponse response = connectorManager.getConnector().execute(request);
		
		if(response != null && response.getQueryResult().getSuccess()){
			List<RefundApplyVo> refundAppllyLists = response.getQueryResult().getResult();
			if(refundAppllyLists != null && refundAppllyLists.size() > 0){
				for(RefundApplyVo jingdongRefund : refundAppllyLists){
					Refund aRefund = null;
					try {
						aRefund = dataTranslatorManager.getDataTranslator().transformRefund(jingdongRefund);
					} catch (TranslateException e) {
						LOGGER.error("京东退款数据转换失败", e);
						LOGGER.error("jingdong refund data translate error, channel jingdongborder id:" + jingdongRefund.getOrderId());
						MailSendUtil.sendEmail("refund data translate error", 
								"jingdong refund data translate error, channel jingdongborder id:" + jingdongRefund.getOrderId());
					} 
					if(aRefund!=null){
						CommonNotifyPacket<Refund> jnp = new CommonNotifyPacket<Refund>(aRefund);
						//jnp.setMessage(aRefund);
						notifys.add(jnp);
					}
				}
				
				int totalResults =response.getQueryResult().getTotalCount().intValue();
				int lastPageIndex = totalResults % PAGE_COUNT == 0 ? totalResults
						/ PAGE_COUNT
						: totalResults / PAGE_COUNT + 1;
				int pageIndex = currPageIndex + 1;
				if (pageIndex <= lastPageIndex) {
					notifys.addAll(getNotifyFinishedList(status,pageIndex,
							startTime, endTime));
				}
			}
		}
		return notifys;
	}
	
	public Refund getRefundByRefundId(String refundId) throws RefundException,
			TranslateException {
		return null;
	}

}
