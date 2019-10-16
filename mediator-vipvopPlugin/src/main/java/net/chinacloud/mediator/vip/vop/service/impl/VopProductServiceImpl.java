package net.chinacloud.mediator.vip.vop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.vip.vop.domain.PickBean;
import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import net.chinacloud.mediator.vip.vop.service.VopProductService;
import net.chinacloud.mediator.vip.vop.service.VopService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vipapis.inventory.InventoryDeductOrdersRequest;
import vipapis.inventory.InventoryDeductOrdersResponse;
import vipapis.inventory.InventoryOccupiedOrdersRequest;
import vipapis.inventory.OccupiedOrder;
import vipapis.inventory.OccupiedOrderResponse;

import com.vip.osp.sdk.exception.OspException;
import com.vip.vop.omni.inventory.InventoryUpdateRequest;

@Service
public class VopProductServiceImpl extends VopService implements VopProductService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VopProductServiceImpl.class);
	
	@Autowired
	ApplicationService applicationService;

	@Override
	public String getOuterProductIdByChannelProductId(String channelProductId)
			throws ProductException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelProductIdByOuterProductId(String outerProductId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOuterSkuIdByChannelSkuId(String channelSkuId,
			String channelProductId) throws ProductException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelSkuIdByOuterSkuId(String OuterSkuId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void updateSkuInventory(Sku sku) throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		Application application = applicationService.getApplicationById(applicationId);
		List<String> areaCodes = new ArrayList<String>();
		areaCodes.add("101");
		vipapis.inventory.UpdateSkuInventoryRequest request=new 	vipapis.inventory.UpdateSkuInventoryRequest();
		//InventoryUpdateRequest request = new InventoryUpdateRequest();
		request.setBarcode(sku.getOuterSkuId());
	//	request.setCooperation_no(11424004);
		request.setQuantity(sku.getQtyCanSell().intValue());
		request.setVendor_id(Integer.valueOf(application.getParam().getVendorId()));
		request.setBatch_no(UUID.randomUUID().toString().substring(0, 20));
		//request.setArea_codes(areaCodes);
//		LOGGER.info("暂停更新唯品会库存....");

		try {
			//修改新的请求更新库存接口
			//getVopJITConnector().getOmniInventoryServiceClient().updateStoreInventory(request);
			getVopJITConnector().getInventoryServiceClient().updateInventory(request);
		} catch (VopJitException e) {
			throw new ProductException(e.getMessage());
		} catch (OspException e) {
			throw new ProductException("exception.product.inventory.update.fail",e.getMessage());
		}
	}

	@Override
	public void updateSkuInventoryBatch(List<Sku> skus) throws ProductException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSkuInventoryBatch(Product product)
			throws ProductException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createProduct(Map<String, Object> product)
			throws ProductException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduct(Map<String, Object> product)
			throws ProductException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShelf(Product product) throws ProductException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offShelf(Product product) throws ProductException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePrice(Product product) throws ProductException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DeliveryTemplate> getDeliveryTemplates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category getChannelProperty(Category category)
			throws ProductException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CategoryList> getAllCategory() throws ProductException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getTotalProducts(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getOnSaleProducts(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getInstockProducts(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> isRepeatOuterId(Product productInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PickBean getInventoryDeductOrders(PickBean pickBean) throws ProductException {
		List<String> orders = getInventoryDeductOrders(pickBean,1);
		PickBean bean = new PickBean();
		bean.setPo_no(pickBean.getPo_no());
		bean.setOrders(orders);
		return bean;
	}
	
	
	public List<String> getInventoryDeductOrders(PickBean pickBean,Integer page) throws ProductException {
		List<String> orders = new ArrayList<String>();
		
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		Application application = applicationService.getApplicationById(applicationId);
		
		InventoryDeductOrdersRequest request = new InventoryDeductOrdersRequest();
		request.setPo_no(pickBean.getPo_no());
		request.setPick_no(pickBean.getPick_num());
		request.setVendor_id(Long.valueOf(application.getParam().getVendorId()));
		request.setPage(page);
		request.setLimit(200);
		try {
			InventoryDeductOrdersResponse response = getVopJITConnector().getInventoryServiceClient().getInventoryDeductOrders(request);
			orders.addAll(response.getOrders());
			if (response.getHas_next()) {
				int pageIndex = page + 1;
				orders.addAll(getInventoryDeductOrders(pickBean,pageIndex));
			}
			
		} catch (VopJitException e) {
			throw new ProductException(e.getMessage());
		} catch (OspException e) {
			LOGGER.error(e.getMessage()+","+e.getReturnMessage());
			throw new ProductException(e.getReturnMessage());
		}
		return orders;
	}

	@Override
	public List<OccupiedOrder> getInventoryOccupiedOrders(Long st_query_time,
			Long et_query_time) throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		Application application = applicationService.getApplicationById(applicationId);
		Long venderId = Long.valueOf(application.getParam().getVendorId());
		List<OccupiedOrder> orders = getInventoryOccupiedOrders(st_query_time/1000,et_query_time/1000,1,venderId);
		return orders;
	}
	
	public List<OccupiedOrder> getInventoryOccupiedOrders(Long st_query_time,Long et_query_time,Integer currentPage,Long venderId) throws ProductException{
		List<OccupiedOrder> occupiedOrders = new ArrayList<OccupiedOrder>();
		
		InventoryOccupiedOrdersRequest request = new InventoryOccupiedOrdersRequest();
		request.setSt_query_time(st_query_time);
		request.setEt_query_time(et_query_time);
		request.setPage(currentPage);
		request.setLimit(200);
		request.setVendor_id(venderId);
		try {
			OccupiedOrderResponse response = getVopJITConnector().getInventoryServiceClient().getInventoryOccupiedOrders(request);
			List<OccupiedOrder> orders = response.getOccupied_orders();
			if (CollectionUtil.isNotEmpty(orders)) {
				occupiedOrders.addAll(orders);
			}
			if (response.getHas_next()) {
				int pageIndex = currentPage + 1;
				occupiedOrders.addAll(getInventoryOccupiedOrders(st_query_time,et_query_time,pageIndex,venderId));
			}
		} catch (VopJitException e) {
			throw new ProductException(e.getMessage());
		} catch (OspException e) {
			LOGGER.error(e.getMessage()+","+e.getReturnMessage());
			throw new ProductException(e.getReturnMessage());
		}
		return occupiedOrders;
	}

	@Override
	public void BindItemStore(Product product) throws ProductException {
		
	}

	@Override
	public String getOuterIdbySkuIdandNick(String skuId)
			throws ProductException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
