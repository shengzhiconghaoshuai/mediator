package net.chinacloud.mediator.kaola.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.product.MappingNotFoundException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.kaola.bean.KaoLaProduct;
import net.chinacloud.mediator.kaola.bean.KaoLaSkuBasic;
import net.chinacloud.mediator.kaola.dao.KaoLaSkuMappingDao;
import net.chinacloud.mediator.kaola.exception.KaoLaException;
import net.chinacloud.mediator.kaola.init.DefaultKaoLaClient;
import net.chinacloud.mediator.kaola.init.KaoLaClient;
import net.chinacloud.mediator.kaola.request.GetSkuByStatusRequest;
import net.chinacloud.mediator.kaola.request.KaoLaRequest;
import net.chinacloud.mediator.kaola.request.UpdateInventoryRequest;
import net.chinacloud.mediator.kaola.response.GetSkuByStatusReponse;
import net.chinacloud.mediator.kaola.response.UpdateInventoryResponse;
import net.chinacloud.mediator.kaola.service.KaoLaProductService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class KaoLaProductServiceImpl implements KaoLaProductService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KaoLaProductServiceImpl.class);
	@Autowired
	ConnectorManager<KaoLaRequest> connectorManager;
	@Autowired
	private KaoLaSkuMappingDao kaoLaSkuMappingDao;
	
	private static final int PAGE_COUNT = 40;
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
			Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
			String outerSkuId = sku.getOuterSkuId();
			ProductPartnumberMapping skuPartnumberMapping = kaoLaSkuMappingDao.getSkuPartnumberMappingByOuterSkuId(outerSkuId, applicationId);
			if(skuPartnumberMapping != null){
				UpdateInventoryRequest request = new UpdateInventoryRequest();
				request.setKey(skuPartnumberMapping.getChannelProductId());
				request.setStock(sku.getQtyCanSell().intValue());
				connectorManager.getConnector().execute(request);
			}else{
				throw new MappingNotFoundException("sku的外部编码",sku.getOuterSkuId()); 
			}
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
		return null;
	}

	@Override
	public List<Product> getOnSaleProducts(Map<String, Object> params) {
		Date startDate = (Date)params.get("startDate");
		Date endDate = (Date)params.get("endDate");
		String status = (String)params.get("status");
		String [] strs = status.split("-");
		if(strs.length==1){
			return getOnSaleProducts(DateUtil.format(startDate),DateUtil.format(endDate),Integer.valueOf(status),1);
		}else{
			status = strs[0];
			return getOnSaleProducts(null,null,Integer.valueOf(status),1);
		}
		
	}
	
	public List<Product> getOnSaleProducts(String startTime,String endTime,Integer status ,int pageno) {
		List<Product> productInfos = new ArrayList<Product>();
		GetSkuByStatusRequest request = new GetSkuByStatusRequest();
		request.setPage_no(pageno);
		request.setPage_size(PAGE_COUNT);
		if(!StringUtils.isEmpty(startTime)){
			request.setStart_modified(startTime);
		}
		if(!StringUtils.isEmpty(endTime)){
			request.setEnd_modified(endTime);
		}
		request.setItem_edit_status(status);
		GetSkuByStatusReponse response = connectorManager.getConnector().execute(request);
		if (response != null && response.getKaola_item_searchBasicByCondition_response().isSuccess()) {
			List<KaoLaProduct> kaoLaProducts = response.getKaola_item_searchBasicByCondition_response().getItem_list();
			if(CollectionUtil.isNotEmpty(kaoLaProducts)){
				for (KaoLaProduct product : kaoLaProducts) {
					Product productInfo = null;
					try {
						productInfo = translateProduct(product);
						productInfos.add(productInfo);
					} catch (TranslateException e) {
						LOGGER.error("kaola 的商品装换出现出错，商品的key="+product.getItem_key()+"商品的货号为"+product.getItem_no());
						LOGGER.error("失败原因"+e.getMessage());
					}
				}	
				pageno ++;
				productInfos.addAll(getOnSaleProducts(startTime,endTime,status,pageno));
			}
		}
		return productInfos;
	}
	private Product translateProduct(Object source)  throws TranslateException{
		Product productInfo = null;
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("transform " + source + " to Product");
		}
		if (source instanceof KaoLaProduct){
			KaoLaProduct kaolaProduct = (KaoLaProduct)source;
			productInfo = new Product();
			productInfo.setChannelProductId(kaolaProduct.getItem_key());//平台自动生成商品的key
			productInfo.setOuterProductId(kaolaProduct.getItem_no());//商家填的商品货号
			for(KaoLaSkuBasic skubasic : kaolaProduct.getSku_list()){
				Sku sku = new Sku();
				try {
					String skuKey = skubasic.getSku_key();
					String skuId = skubasic.getBarcode();
					if(StringUtils.isEmpty(skuKey)|| StringUtils.isEmpty(skuId) ){//如果有一个为空直接跳过.忽略
						LOGGER.debug("跳过的商品为："+skuId+"平台对应的映射为skuKey："+skuKey);
						continue;
					}
					sku.setChannelSkuId(skuKey);//Sku的key
					sku.setOuterProductId(skubasic.getOuter_id());//上架外部唯一编码(目前不支持此字段编辑，请期待)
					sku.setOuterSkuId(skuId);
					productInfo.addSku(sku);
				} catch (Exception e) {
					LOGGER.error("转换获取sku失败,失败信息为:"+e.getMessage());
				}
			}
		}
		return productInfo;
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
