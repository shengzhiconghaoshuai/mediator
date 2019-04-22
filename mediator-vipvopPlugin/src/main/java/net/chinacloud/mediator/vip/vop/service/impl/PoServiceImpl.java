package net.chinacloud.mediator.vip.vop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.vip.vop.constants.JitConstants;
import net.chinacloud.mediator.vip.vop.domain.PoBean;
import net.chinacloud.mediator.vip.vop.domain.PoMessage;
import net.chinacloud.mediator.vip.vop.domain.PoSku;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import net.chinacloud.mediator.vip.vop.service.VopPoService;
import net.chinacloud.mediator.vip.vop.service.VopService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vipapis.delivery.GetPoListResponse;
import vipapis.delivery.GetPoOrdersResponse;
import vipapis.delivery.PoOrder;

import com.vip.osp.sdk.exception.OspException;




@Service
public class PoServiceImpl extends VopService implements VopPoService{
	
	
	@Autowired
	private ApplicationService aApplicationService;
	
	public List<CommonNotifyPacket<PoBean>> getPoListByStatus(String status,
			Date startTime, Date endTime) throws VopJitException{
		List<CommonNotifyPacket<PoBean>> notifys = new ArrayList<CommonNotifyPacket<PoBean>>();
		int page=1;
		List<CommonNotifyPacket<PoBean>> temp = getPoListByStatusPL(status, page, startTime, endTime);
		if(temp!=null && temp.size()>0){
			notifys.addAll(temp);
		}
		/*while((temp=getPoListByStatusPL(status, page, startTime, endTime))!=null&&temp.size()>0){
			notifys.addAll(temp);
			page++;
		}*/
	
		return notifys;
	}
	
	public List<CommonNotifyPacket<PoBean>> getPoListByStatusPL(String status,int pageNo,
			Date startTime, Date endTime)throws VopJitException{
		List<CommonNotifyPacket<PoBean>> notifys = new ArrayList<CommonNotifyPacket<PoBean>>();
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		int default_page_size =50;
		
		try {
			GetPoListResponse resp =  
					getVopJITConnector().getVopJitConnector().getPoList(DateUtil.format(startTime), DateUtil.format(endTime), null, null, "jit_4a", venderId, null, null, null, null, pageNo, default_page_size,null,null);
			List<vipapis.delivery.PurchaseOrder> purchase_order_list = resp.getPurchase_order_list();
			 if(purchase_order_list!=null && purchase_order_list.size()>0){
				 for(vipapis.delivery.PurchaseOrder po : purchase_order_list){
						PoBean aPoBean = new PoBean();
						aPoBean.po_no = po.getPo_no();
						aPoBean.co_mode =po.getCo_mode();
						aPoBean.sell_st_time=po.getSell_st_time();
						aPoBean.sell_et_time=po.getSell_et_time();
						aPoBean.stock=Integer.valueOf(po.getStock());
						List<PoSku> poSkuBeans
						=getPoSkuByPosn(po.getPo_no(),status); 
						System.out.println("po_no:"+po.getPo_no()+" poSkuBeans.size:"+poSkuBeans.size());
						aPoBean.poSkus = poSkuBeans;
						aPoBean.not_pick = po.getNot_pick();//未拣货数
						CommonNotifyPacket<PoBean> aCommonNotifyPacket = new CommonNotifyPacket<PoBean>(aPoBean, JitConstants.STATUS_PO_CREATE);
						
						notifys.add(aCommonNotifyPacket);
				 } 
				 return notifys;
			 }
		
		} catch (OspException e) {
			e.printStackTrace();
			throw new VopJitException("Get polist error:"+e.getMessage(),
					e);
		}
		
		return null;
	}
	
	public List<PoSku> getPoSkuByPosn(String po_sn, String status)
			throws VopJitException {
		List<PoSku> list = new ArrayList<PoSku>();
		int page = 1;
		List<PoSku> temp ;
		while((temp = getPoSkuByPosnPL( po_sn, status, page)) != null && temp.size()>0){
			list.addAll(temp);
			page++;
		}
		return list;
	}
	
	public List<PoSku> getPoSkuByPosnPL(String po_sn,String status,int pageNo)
			throws VopJitException {
		List<PoSku> list = new ArrayList<PoSku>();
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();

		int default_page_size =50;
		
			vipapis.delivery.GetPoSkuListResponse resp;
			try {
				resp = getVopJITConnector().getVopJitConnector().getPoSkuList(Integer.valueOf(venderId),
						po_sn, null, null, null, null, null, null, null, null, null, null, null, null,pageNo, default_page_size);
			
				List<vipapis.delivery.PurchaseOrderSku> purchase_order_sku_list = resp.getPurchase_order_sku_list();
				if(purchase_order_sku_list !=null && purchase_order_sku_list.size()>0){
					for(vipapis.delivery.PurchaseOrderSku posku : purchase_order_sku_list){
						PoSku ps = new PoSku();
						ps.setAudity_time(posku.getAudit_time());
						ps.setCreate_time(posku.getCreate_time());
						ps.setOrder_cate(posku.getOrder_cate());
						ps.setOrder_status(Integer.valueOf(posku.getOrder_status()));
						ps.setSell_site(posku.getSell_site());
						ps.setSku(posku.getBarcode());
						ps.setSku_num(posku.getAmount());
						ps.setSo_no(po_sn);
						ps.setWarehouse(posku.getWarehouse());
						list.add(ps);
					}
					return list;
				}else{
					return null;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new VopJitException("getPoByPoNoError,po_no:"+po_sn+",msg:"+e.getMessage(),e);
			} catch (OspException e) {
				e.printStackTrace();
				throw new VopJitException("getPoByPoNoError,po_no:"+po_sn+",msg:"+e.getMessage(),e);
			}
		}


	@Override
	public PoBean getPoByPoNo(String po_no) throws VopJitException {

		PoBean aPoBean =  new PoBean();
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		try {
			GetPoListResponse resp =
			getVopJITConnector().getVopJitConnector().getPoList(null, null, null, po_no, "jit_4a", venderId, null, null, null, null, 1, 50, null, null);
			List<vipapis.delivery.PurchaseOrder> purchase_order_list = resp.getPurchase_order_list();
			 if(purchase_order_list!=null && purchase_order_list.size()>0){
				 vipapis.delivery.PurchaseOrder po = purchase_order_list.get(0);
					aPoBean.po_no = po.getPo_no();
					aPoBean.co_mode =po.getCo_mode();
					aPoBean.sell_st_time=po.getSell_st_time();
					aPoBean.sell_et_time=po.getSell_et_time();
					aPoBean.stock=Integer.valueOf(po.getStock());
					aPoBean.not_pick = po.getNot_pick();//未拣货数	 
			 }
		} catch (OspException e) {
			e.printStackTrace();
			throw new VopJitException("getPoByPoNoError,po_no:"+po_no+",msg:"+e.getMessage(),e);
		} catch (VopJitException e) {
			e.printStackTrace();
			throw new VopJitException("getPoByPoNoError,po_no:"+po_no+",msg:"+e.getMessage(),e);
		}
		return aPoBean;
	
	}

	@Override
	public List<PoMessage> getPoOrders(Date startTime, Date endTime)
			throws VopJitException {
		List<PoMessage> pobeans = new ArrayList<PoMessage>();
		Integer appId  = ContextUtil.get(Constant.APPLICATION_ID);
		Application app = aApplicationService.getApplicationById(appId);
		String venderId = app.getParam().getVendorId();
		try {
			GetPoOrdersResponse response = getVopJITConnector().getJitDeliveryServiceClient().getPoOrders(null, null, null, venderId, DateUtil.format(startTime), DateUtil.format(endTime), 1, 50,null,null);
			List<PoOrder> porders = response.getPo_orders();
			if (CollectionUtil.isNotEmpty(porders)) {
				for (PoOrder po : porders) {
					PoMessage poBean = new PoMessage();
					poBean.setPo_no(po.getPo_no());
					poBean.setSell_st_time(po.getSell_st_time());
					poBean.setSell_et_time(po.getSell_et_time());
					poBean.setFlag(String.valueOf(po.getNormality_flag()));
					pobeans.add(poBean);
				}
			}
		} catch (OspException e) {
			e.printStackTrace();
			throw new VopJitException("getPoOrdersError,msg:"+e.getMessage(),e);
		}
		return pobeans;
	}

}