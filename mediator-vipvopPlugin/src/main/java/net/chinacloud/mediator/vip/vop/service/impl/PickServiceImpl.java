package net.chinacloud.mediator.vip.vop.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.vip.vop.constants.JitConstants;
import net.chinacloud.mediator.vip.vop.domain.ConfirmdeliverMsg;
import net.chinacloud.mediator.vip.vop.domain.CreatePick;
import net.chinacloud.mediator.vip.vop.domain.CreatedeliverMsg;
import net.chinacloud.mediator.vip.vop.domain.DeliverySkuDetailBean;
import net.chinacloud.mediator.vip.vop.domain.ImportDeliverDetailMsg;
import net.chinacloud.mediator.vip.vop.domain.JITDeliveryBean;
import net.chinacloud.mediator.vip.vop.domain.PickBean;
import net.chinacloud.mediator.vip.vop.domain.PickOrderDetail;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import net.chinacloud.mediator.vip.vop.service.VopPickService;
import net.chinacloud.mediator.vip.vop.service.VopService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.vip.osp.sdk.exception.OspException;

@Service
public class PickServiceImpl extends VopService implements VopPickService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PickServiceImpl.class);
	
	@Autowired
	private ApplicationService aApplicationService;

	@Override
	public List<CommonNotifyPacket<CreatePick>> createPickListByPO(String po_no)
			throws VopJitException {
		List<CommonNotifyPacket<CreatePick>> notifys = new ArrayList<CommonNotifyPacket<CreatePick>>();
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		LOGGER.info("vipvop request {} with parameters {} ",new Object[]{"createPickListByPO",po_no});
		try {
			List<vipapis.delivery.SimplePick> pickList =getVopJITConnector().getJitDeliveryServiceClient().createPick(po_no, Integer.valueOf(venderId),null,null,null);
			LOGGER.info("vipvop response with result {"+JSON.toJSONString(pickList)+"}");
			for(vipapis.delivery.SimplePick simplePick : pickList){
				CreatePick aCreatePick = new CreatePick();
				aCreatePick.setPick_no(simplePick.getPick_no());
				//aCreatePick.setPick_type(simplePick.getPick_type());
				aCreatePick.setPo_no(po_no);
				//aCreatePick.setWarehouse(simplePick.getWarehouse());
				CommonNotifyPacket<CreatePick> aCommonNotifyPacket = new CommonNotifyPacket<CreatePick>(aCreatePick, JitConstants.STATUS_PICK_CREATE);
				notifys.add(aCommonNotifyPacket);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new VopJitException("createPick error:"+e.getMessage(),
					e);
		} catch (OspException e) {
			e.printStackTrace();
			throw new VopJitException("createPick error:"+e.getMessage(),
					e);
		}
		
		return notifys;
	}

	@Override
	public PickBean getPickDetail(CreatePick cp) throws VopJitException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		
		PickBean aPickBean = new PickBean();
		aPickBean.setPick_num(cp.getPick_no());
		//aPickBean.setPick_type( cp.getPick_type());
		List<PickOrderDetail> lists = new ArrayList<PickOrderDetail>();
		int totalResults =0;
		int pageNo = 1;
		int default_page_size =50;
		
		do {
			try {
				LOGGER.info("vipvop request {} with parameters {} ",new Object[]{"getPickDetail",cp.getPo_no()+","+cp.getPick_no()});
				vipapis.delivery.PickDetail aPickDetail = 
						getVopJITConnector().getJitDeliveryServiceClient().getPickDetail(cp.getPo_no(), Integer.valueOf(venderId), cp.getPick_no(), pageNo, default_page_size,null);
				LOGGER.info("vipvop response {"+JSON.toJSONString(aPickDetail)+"}");
				totalResults = aPickDetail.getTotal();
				List<vipapis.delivery.PickProduct> pick_product_list = aPickDetail.getPick_product_list();
				aPickBean.setPo_no(aPickDetail.getPo_no());
				aPickBean.setSell_st_time(aPickDetail.getSell_st_time());
				aPickBean.setSell_et_time(aPickDetail.getSell_et_time());
				aPickBean.setExport_time(aPickDetail.getExport_time());
				aPickBean.setExport_num(aPickDetail.getExport_num() == null ? null : aPickDetail.getExport_num().toString());
				aPickBean.setWarehouse(aPickDetail.getWarehouse());
				aPickBean.setOrder_cate(aPickDetail.getOrder_cate());
				aPickBean.setTotal(aPickDetail.getTotal() == null ? null : aPickDetail.getTotal().toString());
				for(vipapis.delivery.PickProduct aPickProduct : pick_product_list){
					PickOrderDetail aPickOrderDetail = new PickOrderDetail();
					aPickOrderDetail.setArt_no(aPickProduct.getArt_no());
					aPickOrderDetail.setPro_name(aPickProduct.getProduct_name());
					aPickOrderDetail.setSize(aPickProduct.getSize());
					aPickOrderDetail.setSku(aPickProduct.getBarcode());
					aPickOrderDetail.setStock(aPickProduct.getStock());
					lists.add(aPickOrderDetail);
				}
				
			} catch (NumberFormatException e) {
				throw new VopJitException("getPickDetail error"+e.getMessage(),e);
			} catch (OspException e) {
				e.printStackTrace();
				throw new VopJitException("getPickDetail error"+e.getMessage(),e);
			}
		}while (totalResults > default_page_size*pageNo++);
		
		if(lists.size()==0){
			throw new VopJitException("pickDetail is null");
		}
		
		aPickBean.list = lists;
		return aPickBean;
	}


	@Override
	public JITDeliveryBean createDelivery(CreatedeliverMsg createdeliverData)
			throws VopJitException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		JITDeliveryBean aJITDeliveryBean =  new JITDeliveryBean();
		aJITDeliveryBean.setPo_no(createdeliverData.getPo_no());
		aJITDeliveryBean.setDelivery_no(createdeliverData.getDelivery_no());
		aJITDeliveryBean.setDelivery_warehouse(createdeliverData.getDelivery_warehouse());
		aJITDeliveryBean.setPick_no(createdeliverData.getPick_no());
		aJITDeliveryBean.setDelivery_method(createdeliverData.getDelivery_method());
		
		int totalResults =0;
		int pageNo = 1;
		int default_page_size =50;
		
		do{
			String warehouseStr  = createdeliverData.getDelivery_warehouse(); 
			vipapis.delivery.CreateDeliveryResponse resp;
			try {
//				resp = getVopJITConnector().getVopJitConnector().createDelivery(Integer.valueOf(venderId), createdeliverData.getPo_no(), createdeliverData.getDelivery_no(),
//						vipapis.common.Warehouse.valueOf(warehouseStr), null, createdeliverData.getArrival_time(),createdeliverData.getDelivery_method() , createdeliverData.getCarrier(), null, null,
//						createdeliverData.getDriver_tel(), null, pageNo, default_page_size, createdeliverData.getDelivery_method(), null,createdeliverData.getCarrierCode());
//				aJITDeliveryBean.setDelivery_id(resp.getDelivery_id());
//				aJITDeliveryBean.setStorage_no(resp.getStorage_no());

				//修改为新版sdk
				resp = getVopJITConnector().getVopJitConnector().createDelivery(
						Integer.valueOf(venderId),createdeliverData.getPo_no(),createdeliverData.getDelivery_no(),vipapis.common.Warehouse.valueOf(warehouseStr),
						null,createdeliverData.getArrival_time(),null,createdeliverData.getCarrier(),createdeliverData.getDriver_tel(),null,
						createdeliverData.getDriver_tel(),null,pageNo, default_page_size,createdeliverData.getDelivery_method(),null,createdeliverData.getCarrierCode(),null );
				aJITDeliveryBean.setDelivery_id(resp.getDelivery_id());
				aJITDeliveryBean.setStorage_no(resp.getStorage_no());


			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new VopJitException("createDelivery error:"
						+ e.getMessage(), e);
			} catch (OspException e) {
				e.printStackTrace();
				throw new VopJitException("createDelivery error:"
						+ e.getMessage(), e);
			}
		}while(totalResults > default_page_size*pageNo++);
		
		return aJITDeliveryBean;
	}


	@Override
	public void importDeliverDetail(ImportDeliverDetailMsg importDeliverMsg,int retryCount)
			throws VopJitException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		List<DeliverySkuDetailBean> delivery_list = importDeliverMsg.delivery_list;
		List<vipapis.delivery.Delivery> deliverylist = new ArrayList<vipapis.delivery.Delivery>();
		for(DeliverySkuDetailBean deB : delivery_list){
			vipapis.delivery.Delivery aDelivery = new vipapis.delivery.Delivery();
			aDelivery.setAmount(deB.getSku_num());
			aDelivery.setBarcode(deB.getSku());
			aDelivery.setBox_no(deB.getBox_no());
			aDelivery.setPick_no(deB.getPick_no());
			aDelivery.setVendor_type(deB.getVendor_type());
			deliverylist.add(aDelivery);
		}
		
		int dealNum =250;//每250个sku请求一次
		List<List<vipapis.delivery.Delivery>> deliveryskuGroup = new ArrayList<List<vipapis.delivery.Delivery>>();
		
		int totalPage = deliverylist.size()%dealNum == 0 ? deliverylist.size()/dealNum : deliverylist.size()/dealNum + 1;
		int flag = delivery_list.size()%dealNum;
		
		for(int i = 0;i<totalPage;i++){
			if(i == totalPage-1 && flag!=0){
				List<vipapis.delivery.Delivery> list = deliverylist.subList(i * dealNum,i * dealNum+flag);
				deliveryskuGroup.add(list);
			}else{
				List<vipapis.delivery.Delivery> list = deliverylist.subList(i * dealNum, (i + 1) * dealNum);
				deliveryskuGroup.add(list);		
			}
		}
		
		for(int i=0; i<deliveryskuGroup.size(); i++ ){
			try {
				LOGGER.info("vipvop request {} with parameters {} ",new Object[]{"importDeliverDetail",importDeliverMsg.po_no+","+importDeliverMsg.storage_no});
				LOGGER.info("parameters with DeliveryDetail {}",JSON.toJSONString(deliveryskuGroup.get(i)));
				String result = getVopJITConnector().getVopJitConnector().importDeliveryDetail(Integer.valueOf(venderId), importDeliverMsg.po_no, importDeliverMsg.storage_no, null,null,deliveryskuGroup.get(i));
				LOGGER.info("vipvop response {"+result+"}");
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new VopJitException("importDeliverDetail error:"
						+ e.getMessage(), e);
			} catch (OspException e) {
				e.printStackTrace();
				throw new VopJitException("importDeliverDetail error:"
						+ e.getMessage(), e);
			} catch (VopJitException e) {
				e.printStackTrace();
				throw new VopJitException("importDeliverDetail error:"
						+ e.getMessage(), e);
			} 
		}
		
	}

	public void confirmDeliver(String po_no,String storage_no)throws VopJitException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		try {
			getVopJITConnector().getVopJitConnector().confirmDelivery(Integer.valueOf(venderId), storage_no, po_no , null);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new VopJitException("confirmDeliver error:"
					+ e.getMessage(), e);
		} catch (OspException e) {
			e.printStackTrace();
			throw new VopJitException("confirmDeliver error:"
					+ e.getMessage(), e);
		} catch (VopJitException e) {
			e.printStackTrace();
			throw new VopJitException("confirmDeliver error:"
					+ e.getMessage(), e);
		}
	}
	
	@Override
	public void confirmDeliver(ConfirmdeliverMsg msg) throws VopJitException {
		confirmDeliver(msg.getPo_no(), msg.getStorage_no());
		
	}

	@Override
	public List<CommonNotifyPacket<CreatePick>> getPickList(String po_no)
			throws VopJitException {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String vendor_id = app.getParam().getVendorId();
		List<CommonNotifyPacket<CreatePick>> notifys = new ArrayList<CommonNotifyPacket<CreatePick>>();
		try {
			vipapis.delivery.GetPickListResponse resp = 
			getVopJITConnector().getVopJitConnector().getPickList(Integer.valueOf(vendor_id), po_no, null, null, null, null, null, null, null, null, null, null, null, 1, 100 , null);
			List<vipapis.delivery.Pick> pisks = resp.getPicks();
			
			for(vipapis.delivery.Pick pick :pisks){
				if(pick.getPick_no().startsWith("BH")){
					CreatePick cp = new CreatePick();
					cp.setPick_no(pick.getPick_no());
			    	cp.setPo_no(pick.getPo_no());
					CommonNotifyPacket<CreatePick> aCommonNotifyPacket = new CommonNotifyPacket<CreatePick>(cp, JitConstants.STATUS_PICK_CREATE);
					notifys.add(aCommonNotifyPacket);
				}
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new VopJitException("getPickList error:"
					+ e.getMessage(), e);
		} catch (OspException e) {
			e.printStackTrace();
			throw new VopJitException("getPickList error:"
					+ e.getMessage(), e);
		} catch (VopJitException e) {
			e.printStackTrace();
			throw new VopJitException("getPickList error:"
					+ e.getMessage(), e);
		}
		
		return notifys;
	}

	@Override
	public void updateInventory(String sku, Integer quantity, Integer syncMode) {
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String vendor_id = app.getParam().getVendorId();
		List<CommonNotifyPacket<CreatePick>> notifys = new ArrayList<CommonNotifyPacket<CreatePick>>();
		try {
			vipapis.inventory.UpdateSkuInventoryRequest param=new vipapis.inventory.UpdateSkuInventoryRequest();
			param.setVendor_id(Integer.valueOf(vendor_id));
			param.setBarcode(sku);
			param.setBatch_no(sku+(int)(Math.random()*900)+100);
			param.setQuantity(quantity);
			LOGGER.info("开始同步vip的库存更新操作");
			getVopJITConnector().getInventoryServiceClient().updateInventory(param);
			System.out.println("开始同步vip的库存更新操作end");
		} catch (OspException e) {
			LOGGER.error("开始同步vip的库存更新操作失败",e.getMessage());
			e.printStackTrace();
		} catch (VopJitException e) {
			LOGGER.error("开始同步vip的库存更新操作失败",e.getMessage());
			e.printStackTrace();
		}
	}

}