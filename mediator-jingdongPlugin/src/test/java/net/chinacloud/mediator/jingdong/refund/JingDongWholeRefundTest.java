package net.chinacloud.mediator.jingdong.refund;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.refund.RefundException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.jingdong.init.JingdongDataTranslator;
import net.chinacloud.mediator.jingdong.service.impl.RefundServiceImpl;
import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.DateUtil;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.refundapply.RefundApplySoaService.RefundApplyVo;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.request.refundapply.PopAfsSoaRefundapplyQueryPageListRequest;
import com.jd.open.api.sdk.response.refundapply.PopAfsSoaRefundapplyQueryPageListResponse;

public class JingDongWholeRefundTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundServiceImpl.class);
	@Autowired
	ConnectorManager<JdRequest<?>> connectorManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	//每次请求条数
	private Integer PAGE_COUNT = 10;
	

	public static JdClient getJingdongConnector(){
		JdClient client=new DefaultJdClient("https://api.jd.com/routerjson","f6475d59-f6d3-4035-b3f6-e7fac50a38f7",
				"75045C7937E4C9B52B9BFFC5FCECA07A","a79cca2d94884a91955fd9251fc97895");
		return client;
	}
	
	@Test
	public void getRefundListByStatus(
			) throws RefundException, JdException {
		
		List<CommonNotifyPacket<Refund>> list= getNotifyFinishedList("0",1,DateUtil.parse("2017-07-25 0:0:0"),DateUtil.parse("2017-07-25 21:53:52"));
		System.out.println(JSON.toJSON(list));
	}
	



	public List<CommonNotifyPacket<Refund>> getNotifyFinishedList(String status,int currPageIndex,
			Date startTime, Date endTime)throws RefundException, JdException{
		List<CommonNotifyPacket<Refund>> notifys = new ArrayList<CommonNotifyPacket<Refund>>();
		PopAfsSoaRefundapplyQueryPageListRequest request=new PopAfsSoaRefundapplyQueryPageListRequest();
		request.setPageIndex(currPageIndex);
		request.setPageSize(PAGE_COUNT);
		request.setApplyTimeStart(DateUtil.format(startTime));
		request.setApplyTimeEnd(DateUtil.format(endTime));
		request.setStatus(Long.valueOf(status));
		PopAfsSoaRefundapplyQueryPageListResponse response = getJingdongConnector().execute(request);
		
		if(response != null && response.getQueryResult().getSuccess()){
			List<RefundApplyVo> refundAppllyLists = response.getQueryResult().getResult();
			if(refundAppllyLists != null && refundAppllyLists.size() > 0){
				for(RefundApplyVo jingdongRefund : refundAppllyLists){
					Refund aRefund = null;
					try {
						aRefund = new JingdongDataTranslator().transformRefund(jingdongRefund);
					} catch (TranslateException e) {
						LOGGER.error("京东退款数据转换失败", e);
						LOGGER.error("jingdong refund data translate error, channel jingdongborder id:" + jingdongRefund.getOrderId());
						MailSendUtil.sendEmail("refund data translate error", 
								"jingdong refund data translate error, channel jingdongborder id:" + jingdongRefund.getOrderId());
					} 
					CommonNotifyPacket<Refund> jnp = new CommonNotifyPacket<Refund>(aRefund);
					notifys.add(jnp);
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
	
	
	
}
