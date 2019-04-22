/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundServiceImpl.java
 * 描述： 淘宝渠道退款接口实现
 */
package net.chinacloud.mediator.taobao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.refund.RefundException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.taobao.dao.RefundDao;
import net.chinacloud.mediator.taobao.service.TaobaoRefundService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.SpringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.request.NextoneLogisticsWarehouseUpdateRequest;
import com.taobao.api.request.RdcAligeniusSendgoodsCancelRequest;
import com.taobao.api.request.RdcAligeniusSendgoodsCancelRequest.CancelGoodsDto;
import com.taobao.api.request.RefundGetRequest;
import com.taobao.api.response.NextoneLogisticsWarehouseUpdateResponse;
import com.taobao.api.response.RdcAligeniusSendgoodsCancelResponse;
import com.taobao.api.response.RefundGetResponse;
/**
 * <淘宝渠道退款接口实现>
 * <淘宝渠道退款接口实现>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月20日
 * @since 2015年1月20日
 */
@Service
public class RefundServiceImpl implements TaobaoRefundService {
	
	@Autowired
	ConnectorManager<TaobaoRequest<?>> connectorManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;

	@Override
	public Refund getRefundByRefundId(String refundId) throws RefundException, TranslateException {
		RefundGetRequest request = new RefundGetRequest();
		request.setFields("refund_id, alipay_no, tid, oid, buyer_nick, seller_nick, total_fee, status, created, refund_fee, good_status, has_good_return, payment, reason, desc, num_iid, title, price, num, good_return_time, company_name, sid, address, shipping_type, refund_remind_timeout,order_status, modified, refund_phase");
		request.setRefundId(Long.valueOf(refundId));
		Refund refund = null;
		
		RefundGetResponse response = connectorManager.getConnector().execute(request);
		if (response.isSuccess()) {
			refund = dataTranslatorManager.getDataTranslator().transformRefund(response.getRefund());
		} else {
			throw new RefundException("exception.api.response.invalid", response.isSuccess());
		}
		return refund;
	}

	@Override
	public List<CommonNotifyPacket<Refund>> getRefundListByStatus(
			String status, Date startTime, Date endTime , String name) throws RefundException {
		List<CommonNotifyPacket<Refund>> refunds = new ArrayList<CommonNotifyPacket<Refund>>();
		RefundDao refundDao = SpringUtil.getBean(RefundDao.class);
		List<Long> refundids = refundDao.getRefundIdList(startTime, endTime, name, status);
		if(CollectionUtil.isNotEmpty(refundids)){
			for(long id : refundids){
				Refund refund = new Refund();
				refund.setChannelRefundId(String.valueOf(id));
				refund.setStatus(status);
				CommonNotifyPacket<Refund> packet = new CommonNotifyPacket<Refund>(refund);
				refunds.add(packet);
			}
		}
		return refunds;
	}
	
	@Override
	public void AgWareHouseUpdate(Refund refund) throws RefundException {
		NextoneLogisticsWarehouseUpdateRequest request = new NextoneLogisticsWarehouseUpdateRequest();
		request.setRefundId(Long.valueOf(refund.getChannelRefundId()));
		request.setWarehouseStatus(1L);
		NextoneLogisticsWarehouseUpdateResponse response = connectorManager.getConnector().execute(request);
//		boolean bool = response.getSucceed();
//		if (bool) {
//			RdcAligeniusRefundsCheckRequest rcrequest = new RdcAligeniusRefundsCheckRequest();
//			RefundCheckDto dto = new RefundCheckDto();
//			dto.setRefundId(Long.valueOf(refund.getChannelRefundId()));
//			dto.setStatus("SUCCESS");
//			dto.setMsg("取消发货成功");
//			rcrequest.setParam(dto);
//			RdcAligeniusRefundsCheckResponse rdcResponse = connectorManager.getConnector().execute(rcrequest);
//			if (!rdcResponse.getResult().getSuccess()) {
//				throw new RefundException("exception.api.response.invalid",bool);
//			}
//		} else {
//			throw new RefundException("exception.api.response.invalid",bool);
//		}
		
	}

	@Override
	public void AgSendGoodsCancel(Refund refund) throws RefundException {
		RdcAligeniusSendgoodsCancelRequest request = new RdcAligeniusSendgoodsCancelRequest();
		CancelGoodsDto dto = new CancelGoodsDto();
		dto.setRefundId(Long.valueOf(refund.getChannelRefundId()));
		dto.setStatus("SUCCESS");
		dto.setTid(Long.valueOf(refund.getChannelOrderId()));
		request.setParam(dto);
		RdcAligeniusSendgoodsCancelResponse response = connectorManager.getConnector().execute(request);
//		boolean bool = response.getResult().getSuccess();
//		if (bool) { 
//			RdcAligeniusRefundsCheckRequest rcrequest = new RdcAligeniusRefundsCheckRequest();
//			RefundCheckDto rcdto = new RefundCheckDto();
//			dto.setRefundId(Long.valueOf(refund.getChannelRefundId()));
//			dto.setStatus("SUCCESS");
//			dto.setMsg("取消发货成功");
//			rcrequest.setParam(rcdto);
//			RdcAligeniusRefundsCheckResponse rdcResponse = connectorManager.getConnector().execute(rcrequest);
//			if (!rdcResponse.getResult().getSuccess()) {
//				throw new RefundException("exception.api.response.invalid",bool);
//			}
//		} else {
//			throw new RefundException("exception.api.response.invalid",bool);
//		}
	}

}
