package net.chinacloud.mediator.vip.vop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vip.osp.sdk.exception.OspException;

import vipapis.delivery.ConfirmRefuseResultRequest;
import vipapis.delivery.ConfirmReturnResultRequest;
import vipapis.delivery.OrderReturn;
import vipapis.delivery.OrderReturnResponse;
import vipapis.delivery.ReturnGoods;
import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.RefuseGoods;
import net.chinacloud.mediator.domain.VopReturnMessage;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import net.chinacloud.mediator.vip.vop.service.VopReturnService;
import net.chinacloud.mediator.vip.vop.service.VopService;
import net.chinacloud.mediator.domain.VopOrderReturn;

@Service
public class VopReturnServiceImpl extends VopService implements VopReturnService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VopReturnServiceImpl.class);
	
	@Autowired
	private ApplicationService aApplicationService;
	
	@Autowired
	DataTranslatorManager dataTranslatorManager;
	
	int PAGE_COUNT = 50;

	@Override
	public void confirmRefuseResult(VopReturnMessage returnMessage) throws VopJitException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		
		ConfirmRefuseResultRequest request = new ConfirmRefuseResultRequest();
		request.setOrder_id(returnMessage.getOrder_id());
		request.setCarrier_code(returnMessage.getCarrier_code());
		request.setCarrier_name(returnMessage.getCarrier_name());
		request.setDelivery_no(returnMessage.getDelivery_no());
		request.setVendor_id(Integer.valueOf(venderId));
		
		List<RefuseGoods> refuseGoods = returnMessage.getRefuseGoods();
		List<vipapis.delivery.RefuseGoods> voprefuseGoods = new ArrayList<vipapis.delivery.RefuseGoods>();
		
		for (RefuseGoods rg : refuseGoods) {
			vipapis.delivery.RefuseGoods voprefuseGood = new vipapis.delivery.RefuseGoods();
			voprefuseGood.setAmount(rg.getAmount());
			voprefuseGood.setBarcode(rg.getBarcode());
			voprefuseGoods.add(voprefuseGood);
		}
		request.setGoods_list(voprefuseGoods);
		try {
			getVopJITConnector().getDvdDeliveryServiceClient().confirmRefuseResult(request);
		} catch (VopJitException e) {
			throw new VopJitException(e.getMessage());
		} catch (OspException e) {
			throw new VopJitException(e.getMessage());
		}
		
	}

	@Override
	public void confirmReturnResult(VopReturnMessage returnMessage) throws VopJitException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		
		ConfirmReturnResultRequest request = new ConfirmReturnResultRequest();
		request.setOrder_id(returnMessage.getOrder_id());
		request.setCarrier_code(returnMessage.getCarrier_code());
		request.setCarrier_name(returnMessage.getCarrier_name());
		request.setDelivery_no(returnMessage.getDelivery_no());
		request.setVendor_id(Integer.valueOf(venderId));
		List<RefuseGoods> refuseGoods = returnMessage.getRefuseGoods();
		
		List<ReturnGoods> returnGoods = new ArrayList<ReturnGoods>();
		for (RefuseGoods rg : refuseGoods) {
			ReturnGoods returnGood = new ReturnGoods();
			returnGood.setAmount(rg.getAmount());
			returnGood.setBarcode(rg.getBarcode());
			returnGood.setProduct_name(rg.getProduct_name());
			returnGoods.add(returnGood);
		}
		request.setGoods_list(returnGoods);
		
		try {
			getVopJITConnector().getDvdDeliveryServiceClient().confirmReturnResult(request);
		} catch (VopJitException e) {
			throw new VopJitException(e.getMessage());
		} catch (OspException e) {
			throw new VopJitException(e.getMessage());
		}
		
	}

	@Override
	public List<VopOrderReturn> getOrderReturnList(Date startTime, Date endTime,String status) throws VopJitException {
		return getOrderReturnList(status,1,startTime,endTime);
	}

	public List<VopOrderReturn> getOrderReturnList(String status,
			int currPageIndex, Date startTime, Date endTime) throws VopJitException{
		List<VopOrderReturn> orderRetrun = new ArrayList<VopOrderReturn>();
		VopOrderReturn refund = null;
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		OrderReturnResponse response;
		try {
			response = getVopJITConnector().getDvdDeliveryServiceClient().getOrderReturnList(DateUtil.format(startTime), DateUtil.format(endTime), Long.valueOf(venderId), null, Integer.valueOf(status), currPageIndex, PAGE_COUNT);
			List<vipapis.delivery.OrderReturn> vopOrderRturns= response.getOrder_return_list();
			if (CollectionUtil.isNotEmpty(vopOrderRturns)) {
				for (OrderReturn vopOrderRturn : vopOrderRturns) {
					refund = new VopOrderReturn();
					refund.setChannelOrderId(vopOrderRturn.getOrder_id());
					refund.setReturnTime(vopOrderRturn.getReturn_time());
					refund.setReturnType(vopOrderRturn.getReturn_type());
					orderRetrun.add(refund);
				}
				int totalResults = response.getTotal();
				int lastPageIndex = (totalResults % PAGE_COUNT == 0) ? (totalResults / PAGE_COUNT) : (totalResults / PAGE_COUNT + 1);
				int pageIndex = currPageIndex + 1;
				if (pageIndex <= lastPageIndex) {
					orderRetrun.addAll(getOrderReturnList(status, pageIndex, startTime, endTime));
				}
			}
		} catch (VopJitException e) {
			throw new VopJitException(e.getMessage());
		} catch (OspException e) {
			throw new VopJitException(e.getMessage());
		}
		return orderRetrun;
	}
}
