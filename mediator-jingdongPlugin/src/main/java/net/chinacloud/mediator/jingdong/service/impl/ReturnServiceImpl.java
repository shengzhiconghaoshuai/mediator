/**
 * 文件名：ReturnServiceImpl.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.jingdong.service.impl;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.exception.returns.ReturnException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.init.translator.cache.DataTranslatorManager;
import net.chinacloud.mediator.jingdong.service.JingdongReturnService;
import net.chinacloud.mediator.task.CommonNotifyPacket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.open.api.sdk.request.JdRequest;
//import com.jd.open.api.sdk.request.afsservice.AfsserviceFreightmessageGetRequest;
//import com.jd.open.api.sdk.request.afsservice.AfsserviceReceivetaskGetRequest;
//import com.jd.open.api.sdk.request.afsservice.AfsserviceServicedetailListRequest;
//import com.jd.open.api.sdk.response.afsservice.AfsFreightOut;
//import com.jd.open.api.sdk.response.afsservice.AfsServiceDetail;
//import com.jd.open.api.sdk.response.afsservice.AfsServiceMessage;
//import com.jd.open.api.sdk.response.afsservice.AfsserviceFreightmessageGetResponse;
//import com.jd.open.api.sdk.response.afsservice.AfsserviceReceivetaskGetResponse;
//import com.jd.open.api.sdk.response.afsservice.AfsserviceServicedetailListResponse;
//import com.jd.open.api.sdk.response.afsservice.PageReceiveTask;
//import com.jd.open.api.sdk.response.afsservice.PublicResultListAfsServiceDetail;
//import com.jd.open.api.sdk.response.afsservice.PublicResultObjectAfsFreight;
//import com.jd.open.api.sdk.response.afsservice.PublicResultObjectReceiveTask;

/**
 * <退货业务实现类>
 * <退货业务实现类>
 * @author mwu@wuxicloud.com
 * @version 0.0.0,2015年4月8日
 * @since 2015年4月8日
 */
@Service("jingdongReturnServiceImpl")
public class ReturnServiceImpl implements JingdongReturnService{
	
	@Autowired
	ConnectorManager<JdRequest<?>> connectorManager;
	@Autowired
	DataTranslatorManager dataTranslatorManager;

	@Override
	public Return getReturnByReturnId(String returnId) throws ReturnException,
			TranslateException {
		return null;
	}

	@Override
	public List<CommonNotifyPacket<Return>> getReturnListByStatus(
			String status, Date startTime, Date endTime) throws ReturnException {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public List<CommonNotifyPacket<Return>> getReturnListByStatus(
//			String status, Date startTime, Date endTime) throws ReturnException {
//		return getReturnListByStatus(status, 1, startTime, endTime);
//	}
	
//	public List<CommonNotifyPacket<Return>> getReturnListByStatus(String status,int currPageIndex,
//			Date startTime, Date endTime)throws ReturnException{
//		int PAGE_COUNT = 10;
//		List<CommonNotifyPacket<Return>> notifys = new ArrayList<CommonNotifyPacket<Return>>();
//		AfsserviceReceivetaskGetRequest request = new AfsserviceReceivetaskGetRequest();
//		request.setAfsApplyTimeBegin(startTime);
//		request.setAfsApplyTimeEnd(endTime);
//		request.setPageNumber(currPageIndex);
//		request.setPageSize(PAGE_COUNT);
//		
//		AfsserviceReceivetaskGetResponse response = connectorManager.getConnector().execute(request);
//		if(null != response){
//			PublicResultObjectReceiveTask returnGoods = response.getPublicResultObject();
//			int resultCode = returnGoods.getResultCode();
//			if(resultCode == 100){
//				PageReceiveTask pageReceiveTask = returnGoods.getWaitReceiveAfsService();
//				List<AfsServiceMessage> afsServiceMessages = pageReceiveTask.getResult();
//				if(null != afsServiceMessages && afsServiceMessages.size() > 0){
//					for(AfsServiceMessage message : afsServiceMessages){
//						//判断有无发运信息
//						AfsFreightOut out = getFreightMessage(message.getAfsServiceId());
//						if(null == out){
//							continue;
//						}
//						if(null == out.getExpressCode() || "".equals(out.getExpressCode())){
//							continue;
//						}
//						if(null == out.getExpressCompany() || "".equals(out.getExpressCompany())){
//							continue;
//						}
//						//获取服务单详情
//						AfsServiceDetail detail = getServiceDetail(message.getAfsServiceId());
//						message.setWareId(detail.getWareId());
//						message.setWareName(detail.getWareName());
//						JingdongDataTranslator jdTranslator = new JingdongDataTranslator();
//						Return aAFSReturn=null;
//						try {
//							aAFSReturn = jdTranslator.transformReturn(message);
//						} catch (TranslateException e) {
//							throw new ReturnException("Translate  error" + e.getMessage());
//						}
//						
//						CommonNotifyPacket<Return> jnp = new CommonNotifyPacket<Return>(aAFSReturn, JingDongConstants.STATUS_RETURN_CREATE);
//						notifys.add(jnp);
//					}
//					
//					int totalResults = pageReceiveTask.getTotalCount();
//					int lastPageIndex = totalResults % PAGE_COUNT == 0 ? totalResults
//							/ PAGE_COUNT
//							: totalResults / PAGE_COUNT + 1;
//					int pageIndex = currPageIndex + 1;
//					if (pageIndex <= lastPageIndex) {
//						notifys.addAll(getReturnListByStatus(status, pageIndex,
//								startTime, endTime));
//					}
//				}
//			}else{
//				
//				throw new ReturnException("jd return list error,resultCode=" + resultCode);
//			}
//		}
//		return notifys;
//	}
//	
//	/**
//	 * 根据京东服务单号获取发运信息
//	 * @param afsServiceId
//	 * @return
//	 * @throws OutboundRequestFailureException
//	 */
//	public AfsFreightOut getFreightMessage(int afsServiceId) throws ReturnException {
//		AfsserviceFreightmessageGetRequest request = new AfsserviceFreightmessageGetRequest();
//		request.setAfsServiceId(afsServiceId);
//		
//			AfsserviceFreightmessageGetResponse response = connectorManager.getConnector().execute(request);
//			if(null != response){
//				PublicResultObjectAfsFreight result = response.getPublicResultObject();
//				if(100 == result.getResultCode()){
//					return result.getResult();
//				}else{
//					throw new ReturnException("get jing dong Freight Message error with service id " + afsServiceId);
//				}
//			}
//	
//		return null;
//	}
//	
//	/**
//	 * 获取京东服务单详细信息
//	 * @param afsServiceId
//	 * @return
//	 * @throws OutboundRequestFailureException
//	 */
//	private AfsServiceDetail getServiceDetail(int afsServiceId) throws ReturnException {
//		AfsserviceServicedetailListRequest request = new AfsserviceServicedetailListRequest();
//		request.setAfsServiceId(afsServiceId);
//		AfsserviceServicedetailListResponse response =  connectorManager.getConnector().execute(request);
//		if(null != response){
//			PublicResultListAfsServiceDetail publicResultListAfsServiceDetail = response.getPublicResultList();
//			List<AfsServiceDetail> serviceDetails = publicResultListAfsServiceDetail.getModelList();
//			if(null != serviceDetails && serviceDetails.size() > 0){
//				return serviceDetails.get(0);
//			}
//		}
//		return null;
//	}

}
