package net.chinacloud.mediator.vip.vop.service;

import java.util.List;

import vipapis.inventory.OccupiedOrder;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.vip.vop.domain.PickBean;

public interface VopProductService extends ProductService{
	
	public PickBean getInventoryDeductOrders(PickBean pickBean) throws ProductException;
	
	public List<OccupiedOrder> getInventoryOccupiedOrders(Long st_query_time,Long et_query_time) throws ProductException;

}
