package net.chinacloud.mediator.xhs.serviceImpl;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.xhs.request.UpdateInventoryRequest;
import net.chinacloud.mediator.xhs.service.XhsProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XhsProductServiceImpl implements XhsProductService{
	
	@Autowired
	ConnectorManager<Object> connectorManager;

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
		if (sku != null) {
			UpdateInventoryRequest request = new UpdateInventoryRequest();
			request.setUrlKey(sku.getOuterSkuId());
			request.setQty(sku.getQtyCanSell().intValue());
			request.setType(1);
			connectorManager.getConnector().execute(request);
			//UpdateInventoryResponse response = 
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
	public void BindItemStore(Product product) throws ProductException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getOuterIdbySkuIdandNick(String skuId) {
		// TODO Auto-generated method stub
		return null;
	}

}
