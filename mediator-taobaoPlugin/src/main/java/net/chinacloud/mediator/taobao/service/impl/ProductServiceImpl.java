
/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductServiceImpl.java
 * 描述： 淘宝渠道商品接口实现
 */
package net.chinacloud.mediator.taobao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.ChannelAttributeValueMappingDao;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Attribute;
import net.chinacloud.mediator.domain.AttributeValue;
import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductAttribute;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.domain.StoreInventory;
import net.chinacloud.mediator.domain.StoreItems;
import net.chinacloud.mediator.exception.product.InventoryUpdateException;
import net.chinacloud.mediator.exception.product.MappingNotFoundException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.exception.product.PropertyRequireException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.qimen.domain.QimenStore;
import net.chinacloud.mediator.qimen.domain.QimenStoreInventory;
import net.chinacloud.mediator.qimen.domain.StoreId;
import net.chinacloud.mediator.qimen.request.ItemStoreBanDingRequest;
import net.chinacloud.mediator.qimen.request.QimenStoreinventoryIteminitialRequest;
import net.chinacloud.mediator.qimen.response.QimenStoreinventoryIteminitialResponse;
import net.chinacloud.mediator.service.ProductAttributeService;
import net.chinacloud.mediator.service.ProductPartnumberMappingService;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.taobao.constant.ProductConstants;
import net.chinacloud.mediator.taobao.dao.AttributeMappingDao;
import net.chinacloud.mediator.taobao.dao.ChannelAttributeMappingDao;
import net.chinacloud.mediator.taobao.dao.ChannelPictureMappingDao;
import net.chinacloud.mediator.taobao.dao.ProductCategoryMappingDao;
import net.chinacloud.mediator.taobao.dao.ProductJdpDao;
import net.chinacloud.mediator.taobao.domain.AttributeMapping;
import net.chinacloud.mediator.taobao.domain.ChannelAttributeMapping;
import net.chinacloud.mediator.taobao.domain.ProductCategoryMapping;
import net.chinacloud.mediator.taobao.domain.TmallItemPropValWrapper;
import net.chinacloud.mediator.taobao.domain.TmallItemPropWrapper;
import net.chinacloud.mediator.taobao.service.TmallProductService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoParser;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.internal.parser.json.ObjectJsonParser;
import com.taobao.api.request.DeliveryTemplatesGetRequest;
import com.taobao.api.request.ItemQuantityUpdateRequest;
import com.taobao.api.request.ItemSellerGetRequest;
import com.taobao.api.request.ItemSkuGetRequest;
import com.taobao.api.request.ItemSkusGetRequest;
import com.taobao.api.request.ItemUpdateDelistingRequest;
import com.taobao.api.request.ItemUpdateListingRequest;
import com.taobao.api.request.ItemcatsAuthorizeGetRequest;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.request.ItemsCustomGetRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.ProductsSearchRequest;
import com.taobao.api.request.SkusCustomGetRequest;
import com.taobao.api.request.SkusQuantityUpdateRequest;
import com.taobao.api.request.TmallItemAddSchemaGetRequest;
import com.taobao.api.request.TmallItemQuantityUpdateRequest;
import com.taobao.api.request.TmallItemQuantityUpdateRequest.UpdateSkuQuantity;
import com.taobao.api.request.TmallItemSchemaAddRequest;
import com.taobao.api.request.TmallItemSchemaUpdateRequest;
import com.taobao.api.request.TmallItemUpdateSchemaGetRequest;
import com.taobao.api.request.TmallProductAddSchemaGetRequest;
import com.taobao.api.request.TmallProductMatchSchemaGetRequest;
import com.taobao.api.request.TmallProductSchemaAddRequest;
import com.taobao.api.request.TmallProductSchemaGetRequest;
import com.taobao.api.request.TmallProductSchemaMatchRequest;
import com.taobao.api.request.TmallProductSchemaUpdateRequest;
import com.taobao.api.request.TmallProductUpdateSchemaGetRequest;
import com.taobao.api.response.DeliveryTemplatesGetResponse;
import com.taobao.api.response.ItemSellerGetResponse;
import com.taobao.api.response.ItemSkuGetResponse;
import com.taobao.api.response.ItemSkusGetResponse;
import com.taobao.api.response.ItemcatsAuthorizeGetResponse;
import com.taobao.api.response.ItemcatsGetResponse;
import com.taobao.api.response.ItemsCustomGetResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.ProductsSearchResponse;
import com.taobao.api.response.SkusCustomGetResponse;
import com.taobao.api.response.TmallItemAddSchemaGetResponse;
import com.taobao.api.response.TmallItemSchemaAddResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.taobao.api.response.TmallProductAddSchemaGetResponse;
import com.taobao.api.response.TmallProductMatchSchemaGetResponse;
import com.taobao.api.response.TmallProductSchemaAddResponse;
import com.taobao.api.response.TmallProductSchemaGetResponse;
import com.taobao.api.response.TmallProductSchemaMatchResponse;
import com.taobao.api.response.TmallProductUpdateSchemaGetResponse;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.ComplexField;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.MultiCheckField;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.MultiInputField;
import com.taobao.top.schema.field.SingleCheckField;
import com.taobao.top.schema.option.Option;
import com.taobao.top.schema.rule.Rule;
import com.taobao.top.schema.value.ComplexValue;
import com.taobao.top.schema.value.Value;

@Service
/**
 * <淘宝渠道商品接口实现>
 * <淘宝渠道商品接口实现>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
public class ProductServiceImpl implements TmallProductService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	private static final long PAGE_COUNT = 200L;
	
	@Autowired
	ConnectorManager<Object> connectorManager;
	/*@Autowired
	NumOuterDao numOuterDao;*/
	@Autowired
	ProductCategoryMappingDao productCategoryMappingDao;
	@Autowired
	ChannelAttributeMappingDao channelAttributeMappingDao;
	@Autowired
	ChannelAttributeValueMappingDao  channelAttributeValueMappingDao;
	@Autowired
	ProductAttributeService productAttributeService;
	@Autowired
	ChannelPictureMappingDao channelPictureMappingDao;
	@Autowired
	Registry registry;
	@Autowired
	AttributeMappingDao attributeMappingDao;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	ProductPartnumberMappingService productPartnumberMappingService;
	
	@Override
	public String getOuterProductIdByChannelProductId(String channelProductId)
			throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		ProductPartnumberMapping mapping = null;
		try {
			mapping = productAttributeService.getProductPartnumberMappingByChannelId(
					channelProductId, 
					applicationId, 
					0);
		} catch (Exception e1) {
			// e1.printStackTrace();
			LOGGER.error(e1.getMessage());
			mapping = null;
		}
		if (null != mapping) {
			return mapping.getOuterId();
		}
		ItemSellerGetRequest request = new ItemSellerGetRequest();
		request.setFields("outer_id");
		request.setNumIid(Long.valueOf(channelProductId));
		ItemSellerGetResponse response = connectorManager.getConnector().execute(request);
		Item item = response.getItem();
		if (null != item && StringUtils.hasText(item.getOuterId())) {
			try {
				mapping = new ProductPartnumberMapping();
				mapping.setApplicationId(applicationId);
				mapping.setChannelProductId(channelProductId);
				mapping.setOuterId(item.getOuterId().trim());
				mapping.setType(0);
				productAttributeService.saveProductPartnumberMapping(mapping);
			} catch (Exception e) {
				//e.printStackTrace();
				LOGGER.error("商品外部编码映射失败", e);
			}
			return item.getOuterId().trim();
		}
		return null;
	}

	@Override
	public String getOuterSkuIdByChannelSkuId(String channelSkuId,
			String channelProductId) throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		ProductPartnumberMapping mapping = null;
		try {
			mapping = productAttributeService.getProductPartnumberMappingByChannelId(
					channelSkuId, 
					applicationId, 
					1);
		} catch (Exception e1) {
			// e1.printStackTrace();
			LOGGER.error(e1.getMessage());
			mapping = null;
		}
		if (null != mapping) {
			return mapping.getOuterId();
		}
		ItemSkuGetRequest request = new ItemSkuGetRequest();
		request.setFields("outer_id");
		request.setSkuId(Long.valueOf(channelSkuId));
		request.setNumIid(Long.valueOf(channelProductId));
		
		ItemSkuGetResponse response = connectorManager.getConnector().execute(request);
		
		com.taobao.api.domain.Sku sku = response.getSku();
		if (null != sku && StringUtils.hasText(sku.getOuterId())) {
			try {
				mapping = new ProductPartnumberMapping();
				mapping.setApplicationId(applicationId);
				mapping.setChannelProductId(channelSkuId);
				mapping.setOuterId(sku.getOuterId().trim());
				mapping.setType(1);
				productAttributeService.saveProductPartnumberMapping(mapping);
			} catch (Exception e) {
				//e.printStackTrace();
				LOGGER.error("商品外部编码映射失败", e);
			}
			return sku.getOuterId().trim();
		}
		return null;
	}
	/**
	 * 更新库存(SKU)
	 */
	
	@Override
	public void updateSkuInventory(Sku sku) throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		ProductPartnumberMapping mapping= null;
		String numIids[] = null;
		if(StringUtils.hasText(sku.getOuterProductId())){
			 mapping = productAttributeService.getProductPartnumberMappingByOuterId(sku.getOuterProductId(), applicationId, 0);
		}else{
			throw new PropertyRequireException("商品外部编码");
		}
		if(StringUtils.hasText(mapping.getChannelProductId())){
			 numIids = mapping.getChannelProductId().split(",");
			 //numIid = Long.valueOf(mapping.getChannelProductId());
		}else{
			throw new PropertyRequireException("商品ID");
		}
		Application application =applicationService.getApplicationById(applicationId);
		String warehouseId = application.getParam().getField2();
		String itemInitialflag = "failure";
		if(StringUtils.hasLength(warehouseId)){
			StoreItems storeItems = new StoreItems();
			storeItems.setWarehouseId(warehouseId);
			storeItems.setWarehouseType("WAREHOUSE");
			StoreInventory storeInventory = new StoreInventory();
			storeInventory.setOuterProductId(sku.getOuterProductId());
			storeInventory.setOuterSkuId(sku.getOuterSkuId());
			storeInventory.setQuantity(sku.getQtyCanSell().intValue());
			storeInventory.setInventoryType("CERTAINTY");
			storeItems.addStoreInventory(storeInventory);
			itemInitialflag = this.qimenStoreInventoryItemInitial(storeItems);
		}
		if(!"success".equals(itemInitialflag)){
			if(11 == application.getChannelId()){
				taobaoUpdateSkuInventory(sku,numIids);
			}else{
				tmallUpdateSkuInventory(sku,numIids);
			}
		}
	}
	
	/**淘宝店的库存更新*/
	private void taobaoUpdateSkuInventory(Sku sku,String[] numIids) throws ProductException{
		for (int i=0; i<numIids.length; i++) {
			Long numIid = Long.valueOf(numIids[i]);
			ItemQuantityUpdateRequest req = new ItemQuantityUpdateRequest();
			if(null!=numIid){
				req.setNumIid(numIid);
			}
			if (!StringUtils.hasText(sku.getOuterSkuId())) {
				throw new PropertyRequireException("SKU外部编码");
			}
			req.setOuterId(sku.getOuterSkuId());
			//1为全量更新，2为增量更新
			if (!sku.isFull()) {
				if (null == sku.getQtyChange()) {
					throw new PropertyRequireException("SKU变化库存");
				}
				req.setQuantity(sku.getQtyChange().longValue());
				req.setType(sku.getQtyChange().longValue() != 0 ? 2L : 1L);
			}else{
				if (null == sku.getQtyCanSell()) {
					throw new PropertyRequireException("SKU总库存");
				}
				req.setQuantity(sku.getQtyCanSell().longValue());
				req.setType(1L);
				connectorManager.getConnector().execute(req);
			}
		}
	}
	
	/**天猫店的库存更新*/
	private void tmallUpdateSkuInventory(Sku sku,String[] numIids) throws ProductException{
		for (int i=0; i<numIids.length; i++) {
			Long numIid = Long.valueOf(numIids[i]);
			TmallItemQuantityUpdateRequest req = new TmallItemQuantityUpdateRequest();
			
			List<UpdateSkuQuantity> list2 = new ArrayList<UpdateSkuQuantity>();
			
			//ItemQuantityUpdateRequest req = new ItemQuantityUpdateRequest();
			if(null!=numIid){
				req.setItemId(numIid);
			}
			if (!StringUtils.hasText(sku.getOuterSkuId())) {
				throw new PropertyRequireException("SKU外部编码");
			}
			UpdateSkuQuantity obj3 = new UpdateSkuQuantity();
			obj3.setOuterId(sku.getOuterSkuId());
			com.taobao.api.request.TmallItemQuantityUpdateRequest.UpdateItemQuantityOption obj4 = new com.taobao.api.request.TmallItemQuantityUpdateRequest.UpdateItemQuantityOption();
			//1为全量更新，2为增量更新
			if (!sku.isFull()) {
				if (null == sku.getQtyChange()) {
					throw new PropertyRequireException("SKU变化库存");
				}
				obj3.setQuantity(sku.getQtyChange().longValue());
				obj4.setType(sku.getQtyChange().longValue() != 0 ? 2L : 1L);
			}else{
				if (null == sku.getQtyCanSell()) {
					throw new PropertyRequireException("SKU总库存");
				}
				obj3.setQuantity(sku.getQtyCanSell().longValue());
				obj4.setType(1L);
			}
			req.setOptions(obj4);
			list2.add(obj3);
			req.setSkuQuantities(list2);
			connectorManager.getConnector().execute(req);
		}
	}
	
	/**
	 * 根据skuOuterId获得所属商品id
	 * @param skuOuterId
	 * @return
	 * @throws OutboundRequestFailureException
	 */
	public long getNumIidBySkuOuterId(String skuOuterId) throws ProductException {
		List<com.taobao.api.domain.Sku> skus = getChannelSkusBySkuOuterId(skuOuterId);
		if (CollectionUtil.isNotEmpty(skus)) {
			/*if (skus.size() > 1) {
				throw new ProductException("exception.product.sku.duplicate.outerid", skuOuterId);
			}*/
			com.taobao.api.domain.Sku sku = skus.get(0);
			return sku.getNumIid();
		} else {
			return 0L;
		}
	}
	
	/**
	 * 根据外部sku id获取渠道sku
	 * @param skuOuterId
	 * @return
	 */
	public List<com.taobao.api.domain.Sku> getChannelSkusBySkuOuterId(String skuOuterId) {
		SkusCustomGetRequest req = new SkusCustomGetRequest();
		req.setFields("num_iid,properties,quantity,outer_id");
		req.setOuterId(skuOuterId);
		SkusCustomGetResponse response = connectorManager.getConnector().execute(req);
		return response.getSkus();
	}

	@Override
	public void updateSkuInventoryBatch(List<Sku> skus) throws ProductException {
		// do not support
	}
	/**
	 * 更新库存
	 */
	@Override
	public void updateSkuInventoryBatch(Product product) throws ProductException {
		List<Sku> skuList = product.getSkuList();
		int flag = 0;
		String errormessage = "";
		int count = product.getSkuList().size();
		int page = count%20 == 0 ? count / 20 : count / 20 + 1;
		for(int i = 1 ; i <= page ; i++){
			if(i != page){
				product.setSkuList(skuList.subList((i - 1) * 20 , i * 20));
			}else{
				product.setSkuList(skuList.subList((i - 1) * 20 , count));
			}
			try {
				updateInventory(product);
			} catch (Exception e) {
				flag = 1;
				errormessage = e.getMessage();
			}
		}
		if(flag == 1){
			throw new InventoryUpdateException(errormessage);
		}
	}
	
	public void updateInventory(Product product) throws ProductException{
		Long numiid=null;
		if(StringUtils.hasText(product.getOuterProductId())){
			 numiid=getItemId(product.getOuterProductId(),0);
		}else{
			 throw new PropertyRequireException("商品ID");
		}
		StringBuilder quantities=new StringBuilder();
		SkusQuantityUpdateRequest req=new SkusQuantityUpdateRequest();
		if(null!=numiid){
			req.setNumIid(numiid);
		}
		if(product.isFull()){
			req.setType(1L);
			for(Sku sku:product.getSkuList()){
				if (null == sku.getQtyCanSell()) {
					throw new PropertyRequireException("SKU总库存");
				}
				quantities.append(sku.getOuterSkuId()).append(":").append(sku.getQtyCanSell().longValue()).append(";");
			}
			quantities=quantities.deleteCharAt(quantities.length()-1);
		}else{
			req.setType(2L);
			for(Sku sku:product.getSkuList()){
				if (null == sku.getQtyChange()) {
					throw new PropertyRequireException("SKU变化库存");
				}
				quantities.append(sku.getOuterSkuId()).append(":").append(sku.getQtyChange().longValue()).append(";");
			}
			quantities=quantities.deleteCharAt(quantities.length()-1);
		}
		req.setOuteridQuantities(quantities.toString());
		connectorManager.getConnector().execute(req);
	}
	/**
	 * 创建商品
	 */
	@Override
	public void createProduct(Map<String, Object> product) 
			throws ProductException {
		try {
			Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
			Object categoryIdObj = product.get(ProductAttribute.CATEGORY_ID);
			if (StringUtils.isEmpty(categoryIdObj)) {
				throw new PropertyRequireException("渠道类目");
			}
			if (StringUtils.isEmpty(product.get(ProductAttribute.BRAND_ID))) {
				throw new PropertyRequireException("品牌id");
			}
			//系统类目id update by ywu 2016-06-21 此处传递过来的直接是平台的id
			//Long categoryId = Long.valueOf(String.valueOf(categoryIdObj));
			Long channelCategoryId = Long.valueOf(String.valueOf(categoryIdObj));
			/*CategoryMapping categoryMapping = productAttributeService.getCategoryMapping(categoryId, applicationId);//渠道主目录映射
			if (null == categoryMapping) {
				throw new MappingNotFoundException("category", String.valueOf(categoryIdObj));
			}*/
			/*ProductCategoryMapping productCategoryMapping = getProductCategoryMapping(categoryMapping.getChannelCategoryId(), applicationId);//天猫产品id与主目录叶子类目id映射
			if (null == productCategoryMapping) {
				throw new MappingNotFoundException("产品类目", String.valueOf(categoryMapping.getChannelCategoryId()));
			}
			Long productId = productCategoryMapping.getProductId();*/
			//Long channelCategoryId = categoryMapping.getChannelCategoryId();
			
			Long productId = null;	//天猫产品id
			
			//1、获取产品匹配规则
			String matchRule = getProductMatchSchema(channelCategoryId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("tmall match product xml rule:" + matchRule);
			}
			if (StringUtils.hasText(matchRule)) {
				List<Field> matchRuleFields = SchemaReader.readXmlForList(matchRule);
				if (CollectionUtil.isNotEmpty(matchRuleFields)) {
					for (Field field : matchRuleFields) {
						processField(channelCategoryId, field, product, false, null, null);
					}
				}
				String matchRuleData = SchemaWriter.writeParamXmlString(matchRuleFields);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("tmall match product xml data:" + matchRuleData);
				}
				//2、匹配产品
				productId = matchProduct(channelCategoryId, matchRuleData);
			} else {
				//tmall.item.schema.add 如果tmall.product.match.schema.get获取到得字段为空，这个参数传入0，否则需要通过tmall.product.schema.match查询到得可用productId 
				productId = 0L;
			}
			
			//未匹配到产品
			if (null == productId) {
				LOGGER.info("product doesn't match, create product");
				Long brandId = Long.valueOf(product.get(ProductAttribute.BRAND_ID).toString());
				//3、创建产品
				//3.1、获取产品创建规则
				String productAddRule = getProductAddSchema(channelCategoryId, brandId);
				LOGGER.info("tmall add product xml rule:" + productAddRule);
				if (StringUtils.hasText(productAddRule)) {
					List<Field> productAddFields = SchemaReader.readXmlForList(productAddRule);
					if (CollectionUtil.isNotEmpty(productAddFields)) {
						for (Field field : productAddFields) {
							processField(channelCategoryId, field, product, false, null, null);
						}
					}
					String productAddRuleData = SchemaWriter.writeParamXmlString(productAddFields);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("tmall add product xml data:" + productAddRuleData);
					}
					//3.2、创建产品
					productId = addProduct(channelCategoryId, brandId, productAddRuleData);
					
					if (null == productId) {
						// 产品创建失败
						LOGGER.error("error create product for tmall");
						throw new ProductException("exception.product.tmall.create.fail");
					}
					//add by ywu 2016-07-09 如果该类目下从来没有发布过产品,初始化产品类目关系时将初始化不到该类目,在抓取类目属性时将获取不到
					//第一次发布商品时将会失败,这里检测产品类目关系如果不存在,则初始化
					ProductCategoryMapping mapping = new ProductCategoryMapping();
					mapping.setProductId(productId);
					mapping.setChannelCategoryId(channelCategoryId);
					mapping.setApplicationId(applicationId);
					saveProductCategoryMapping(mapping);
				} else {
					//如果tmall.product.add.schema.get获取为空时,说明该类目为无关键属性类目，直接去发布商品即可
					//productId如何设值?
					//productId = 0L;
					LOGGER.error("product doesn't match, tmall add product xml rule is empty!!!");
				}
			} 
			
			//4、查看产品状态
			boolean canPublishItem = true;
			if (null != productId && productId > 0) {
				canPublishItem = canPublishItem(productId);
			}
			if (!canPublishItem) {
				LOGGER.warn("product {} cat't publish, please wait!", productId);
				throw new ProductException("exception.product.tmall.itempublish.notallow");
			}
			
			//5、创建商品
			//5.1、获取商品创建规则
			String addItemRule = getAddItemResult(channelCategoryId, productId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("tmall add item xml rule:" + addItemRule);
			}
			
			TmallItemSchemaAddRequest addItemRequest = new TmallItemSchemaAddRequest();
			addItemRequest.setProductId(productId);
			addItemRequest.setCategoryId(channelCategoryId);
			List<Field> fieldList = SchemaReader.readXmlForList(addItemRule);
			
			if (CollectionUtil.isNotEmpty(fieldList)) {
				for (Field field : fieldList) {
					processField(channelCategoryId, field, product, false, null, null);
				}
			}
			String xmlData = SchemaWriter.writeParamXmlString(fieldList);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("tmall add item xml data:" + xmlData);
			}
			addItemRequest.setXmlData(xmlData);
			
			//5.2、商品创建
			TmallItemSchemaAddResponse response = connectorManager.getConnector().execute(addItemRequest);
			
			//TODO 映射渠道商品id与外部编码关系,不知道这个是不是商品id
			String channelProductId = response.getAddItemResult();
			
			ProductPartnumberMapping productPartnumber = new ProductPartnumberMapping();
			productPartnumber.setApplicationId(applicationId);
			productPartnumber.setOuterId(product.get(ProductAttribute.OUTER_ID).toString());
			productPartnumber.setChannelProductId(channelProductId);
			// TODO 天猫不知道允不允许重复创建
	    	try {
	    		if("true".equals(product.get(ProductAttribute.REPUBLISH).toString())){
	    			LOGGER.info("republish product {} for tmall, channelProductId = {}, applicationId = {}", new Object[]{product.get(ProductAttribute.OUTER_ID), channelProductId, applicationId});
	    			productAttributeService.updateProductPartnumberMapping(productPartnumber);
	    		}else{
	    			LOGGER.info("publish product {} for tmall, channelProductId = {}, applicationId = {}", new Object[]{product.get(ProductAttribute.OUTER_ID), channelProductId, applicationId});
	    			productAttributeService.saveProductPartnumberMapping(productPartnumber);
	    		}
			} catch (Exception e) {
				//e.printStackTrace();
				LOGGER.error("", e);
				LOGGER.error("tmall product partnumber mapping error,channelProductId = {}, outerId = {}", channelProductId, product.get(ProductAttribute.OUTER_ID));
			}
	    	//渠道商品id返回后端系统 add by ywu@wuxicloud.com at 2016-05-23
	    	product.put(ProductAttribute.CHANNEL_PRODUCT_ID, channelProductId);
		} catch (TopSchemaException e) {
			//e.printStackTrace();
			LOGGER.error("天猫商品创建xml解析错误", e);
			throw new ProductException("exception.product.create.fail", "数据解析错误");
		}
	}
	
	/**
	 * 根据类目id获取产品匹配规则
	 * @param categoryId 类目id
	 * @return
	 */
	private String getProductMatchSchema(Long categoryId) {
		TmallProductMatchSchemaGetRequest request = new TmallProductMatchSchemaGetRequest();
		request.setCategoryId(categoryId);
		TmallProductMatchSchemaGetResponse response = connectorManager.getConnector().execute(request);
		return response.getMatchResult();
	}
	
	/**
	 * 匹配产品
	 * @param categoryId 类目id
	 * @param productMatchRule 填充后的产品匹配规则
	 * @return 产品id,如果为null说明未匹配到产品
	 */
	private Long matchProduct(Long categoryId, String productMatchRule) {
		TmallProductSchemaMatchRequest request = new TmallProductSchemaMatchRequest();
		request.setCategoryId(categoryId);
		request.setPropvalues(productMatchRule);
		TmallProductSchemaMatchResponse response = connectorManager.getConnector().execute(request);
		String matchResult = response.getMatchResult();
		if (StringUtils.hasText(matchResult)) {
			if (matchResult.indexOf(",") > 0) {
				String mr = matchResult.split(",")[0];
				return Long.valueOf(mr.trim());
			} else {
				return Long.valueOf(matchResult.trim());
			}
		}
		return null;
	}
	
	/**
	 * 判断产品是否能发布商品
	 * @param productId 产品id
	 * @return
	 */
	private boolean canPublishItem(Long productId) throws TopSchemaException {
		TmallProductSchemaGetRequest request = new TmallProductSchemaGetRequest();
		request.setProductId(productId);
		TmallProductSchemaGetResponse response = connectorManager.getConnector().execute(request);
		String result = response.getGetProductResult();
		try {
			List<Field> fields = SchemaReader.readXmlForList(result);
			if (CollectionUtil.isNotEmpty(fields)) {
				for (Field field : fields) {
					if ("can_publish_item".equals(field.getId())) {
						String defaultValue = ((InputField)field).getDefaultValue();
						return Boolean.valueOf(defaultValue);
					}
				}
			}
		} catch (TopSchemaException e) {
			//e.printStackTrace();
			LOGGER.error("天猫产品能否发布商品xml解析失败", e);
			throw e;
		}
		return false;
	}
	
	/**
	 * 获取产品添加规则
	 * @param categoryId
	 * @param brandId
	 * @return 产品添加规则,如果为空,则不需要创建产品,直接创建item
	 */
	private String getProductAddSchema(Long categoryId, Long brandId) {
		TmallProductAddSchemaGetRequest request = new TmallProductAddSchemaGetRequest();
		request.setCategoryId(categoryId);
		request.setBrandId(brandId);
		TmallProductAddSchemaGetResponse response = connectorManager.getConnector().execute(request);
		return response.getAddProductRule();
	}
	
	/**
	 * 创建一个产品
	 * @param categoryId 类目id
	 * @param brandId 品牌id
	 * @param productAddRule 填充后的产品创建规则
	 * @return 产品id,如果为null则表示产品创建失败
	 */
	private Long addProduct(Long categoryId, Long brandId, String productAddRule) throws TopSchemaException {
		TmallProductSchemaAddRequest request = new TmallProductSchemaAddRequest();
		request.setCategoryId(categoryId);
		request.setBrandId(brandId);
		request.setXmlData(productAddRule);
		TmallProductSchemaAddResponse response = connectorManager.getConnector().execute(request);
		String result = response.getAddProductResult();
		if (StringUtils.hasText(result)) {
			try {
				List<Field> fields = SchemaReader.readXmlForList(result);
				if (CollectionUtil.isNotEmpty(fields)) {
					for (Field field : fields) {
						if ("product_id".equals(field.getId())) {
							String value = ((InputField)field).getValue();
							return Long.valueOf(value);
						}
					}
				}
			} catch (TopSchemaException e) {
				//e.printStackTrace();
				LOGGER.error("天猫产品添加xml解析失败", e);
				throw e;
			}
		}
		return null;
	}
	
	private void processField(Long categoryId,Field field, Map<String, Object> data, boolean parentComplex, String parentFieldId, ComplexValue complexValue) {
		FieldTypeEnum fieldType = field.getType();
		String fieldId = field.getId();
		String dataid = field.getId(); 
		AttributeMapping attributeMapping = null;
		switch (fieldType) {
		case INPUT:
			if (data.containsKey(fieldId)) {
				setInputVal(complexValue, field, parentComplex , fieldId , dataid , data);
			} else {
				attributeMapping = getAttrMapping(categoryId, fieldId);
				if(null!=attributeMapping&&StringUtils.hasText(attributeMapping.getOmskey())){
					dataid=attributeMapping.getOmskey();
					setInputVal(complexValue,field,parentComplex,fieldId,dataid,data);
				}
			}
			break;
		case MULTIINPUT:
			if (data.containsKey(fieldId)) {
				setMulitInput(complexValue,field,parentComplex,parentFieldId,dataid,data);
			} else {
				attributeMapping = getAttrMapping(categoryId, fieldId);
				if(null!=attributeMapping&&StringUtils.hasText(attributeMapping.getOmskey())){
					dataid=attributeMapping.getOmskey();
					setMulitInput(complexValue,field,parentComplex,parentFieldId,dataid,data);
				}
			}
			break;
		case SINGLECHECK:
			if (data.containsKey(fieldId)) {
				setSingleCheck(complexValue,field,parentComplex,fieldId,dataid,data);
			}else{
				attributeMapping = getAttrMapping(categoryId, fieldId);
				if(null!=attributeMapping&&StringUtils.hasText(attributeMapping.getOmskey())){
					dataid=attributeMapping.getOmskey();
					setSingleCheck(complexValue,field,parentComplex,fieldId,dataid,data);
					}
			}
			break;
		case MULTICHECK:
			if (data.containsKey(fieldId)) {
				setMulitCheck(complexValue,field,parentComplex,parentFieldId,dataid,data);
			}else{
				attributeMapping = getAttrMapping(categoryId, fieldId);
				if(null!=attributeMapping&&StringUtils.hasText(attributeMapping.getOmskey())){
					dataid=attributeMapping.getOmskey();
					setMulitCheck(complexValue,field,parentComplex,parentFieldId,dataid,data);
				}
			}
			break;
		case COMPLEX:
			ComplexField complexField = (ComplexField)field;
			ComplexValue complexValue1 = new ComplexValue();
			if (parentComplex) {
				complexValue.setComplexFieldValue(complexField.getId(), complexValue1);
			}
			List<Field> complexFields = complexField.getFieldList();
			if (CollectionUtil.isNotEmpty(complexFields)) {
				for (Field perComplexField : complexFields) {
					processField(categoryId,perComplexField, data, true, fieldId, complexValue1);
				}
			}
			complexField.setComplexValue(complexValue1);
			break;
		case MULTICOMPLEX:
			MultiComplexField multiComplexField = (MultiComplexField)field;
			
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> params = (List<Map<String, Object>>)data.get(fieldId);
			if (CollectionUtil.isNotEmpty(params)) {
				for (Map<String, Object> param : params) {
					ComplexValue complexValue2 = new ComplexValue();
					
					List<Field> multiComplexFields = multiComplexField.getFieldList();
					if (CollectionUtil.isNotEmpty(multiComplexFields)) {
						for (Field multComField : multiComplexFields) {
							processField(categoryId,multComField, param, true, fieldId, complexValue2);
						}
					}
					
					multiComplexField.addComplexValue(complexValue2);
				}
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 更新商品
	 */
	@Override
	public void updateProduct(Map<String, Object> product) 
			throws ProductException {
		try {
			Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);//获取applicationId
			Object categoryIdObj = product.get(ProductAttribute.CATEGORY_ID);
			if (StringUtils.isEmpty(categoryIdObj)) {
				throw new PropertyRequireException("系统目录");
			}
			//系统类目id update by ywu 2016-06-21 此处传递过来的直接是平台的id
			//Long categoryId = Long.valueOf(String.valueOf(categoryIdObj));
			Long channelCategoryId = Long.valueOf(String.valueOf(categoryIdObj));
			/*CategoryMapping categoryMapping = productAttributeService.getCategoryMapping(categoryId, applicationId);//渠道主目录映射
			if (null == categoryMapping) {
				throw new MappingNotFoundException("类目", String.valueOf(categoryIdObj));
			}*/
			//获取渠道类目ID
			//Long channelCategoryId = categoryMapping.getChannelCategoryId();
			
			/*ProductCategoryMapping productCategoryMapping = productCategoryMappingDao.getProductCategoryMapping(channelCategoryId, applicationId);//天猫产品id与主目录叶子类目id映射
			if (null == productCategoryMapping) {
				throw new MappingNotFoundException("产品类目", String.valueOf(categoryMapping.getChannelCategoryId()));
			}
			Long productId = productCategoryMapping.getProductId();*/
			
			Object outerIdObj = product.get(ProductAttribute.OUTER_ID);
			if (StringUtils.isEmpty(outerIdObj)) {
				throw new PropertyRequireException("外部商家Id");
			}
			ProductPartnumberMapping productPartnumberMapping = productAttributeService.getProductPartnumberMappingByOuterId(outerIdObj.toString(), applicationId, 0);//映射item的outskuid
			if (null == productPartnumberMapping) {
				throw new MappingNotFoundException("商品外部编码", outerIdObj.toString());
			}
			Long itemId = Long.valueOf(productPartnumberMapping.getChannelProductId());
			//===========================================更新产品======================================
			//获取产品id
			Long productId = getProductIdByNumIid(itemId);
			if (null != productId && productId > 0) {
				//获取产品更新规则
				String updateProductRule = getProductUpdateSchema(productId);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("tmall update product xml rule:" + updateProductRule);
				}
				if (StringUtils.hasText(updateProductRule)) {
					List<Field> productUpdateFieldList = SchemaReader.readXmlForList(updateProductRule);
					if (CollectionUtil.isNotEmpty(productUpdateFieldList)) {
						for (Field field : productUpdateFieldList) {
							processField(channelCategoryId, field, product, false, null, null);
						}
					}
					String productUpdateXml = SchemaWriter.writeParamXmlString(productUpdateFieldList);
					
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("tmall update product xml data:" + productUpdateXml);
					}
					
					TmallProductSchemaUpdateRequest request = new TmallProductSchemaUpdateRequest();
					request.setProductId(productId);
					request.setXmlData(productUpdateXml);
					connectorManager.getConnector().execute(request);
				}
			}
			
			//======================================更新商品=======================================
			//获取商品全量更新规则
			String fullUpdateItemRule = getItemUpdateSchema(channelCategoryId, itemId);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("tmall full update item xml rule:" + fullUpdateItemRule);
			}
			
			//更新商品
			TmallItemSchemaUpdateRequest updateSchemaRequest = new TmallItemSchemaUpdateRequest();
			updateSchemaRequest.setItemId(itemId);
			updateSchemaRequest.setCategoryId(channelCategoryId);
			//updateSchema.setProductId(productId);
			List<Field> fieldList = SchemaReader.readXmlForList(fullUpdateItemRule);
			if (CollectionUtil.isNotEmpty(fieldList)) {
				for (Field field : fieldList) {
					processField(channelCategoryId, field, product, false, null, null);
				}
			}
			String updateXml = SchemaWriter.writeParamXmlString(fieldList);
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("tmall full update item xml data:" + updateXml);
			}
			
			updateSchemaRequest.setXmlData(updateXml);
			connectorManager.getConnector().execute(updateSchemaRequest);
		} catch (TopSchemaException e) {
			//e.printStackTrace();
			LOGGER.error("", e);
			throw new ProductException("exception.product.update.fail", "数据解析错误");
		}
	}
	
	/**
	 * 根据商品id获取天猫产品id
	 * @param numIid
	 * @return
	 */
	private Long getProductIdByNumIid(Long numIid) {
		ItemSellerGetRequest request = new ItemSellerGetRequest();
		request.setFields("product_id");
		request.setNumIid(numIid);
		ItemSellerGetResponse response = connectorManager.getConnector().execute(request);
		return response.getItem().getProductId();
	}
	
	/**
	 * 获取商品全量更新规则
	 * @param categoryId
	 * @param itemId
	 * @return
	 */
	private String getProductUpdateSchema(Long productId) {
		TmallProductUpdateSchemaGetRequest request =new TmallProductUpdateSchemaGetRequest();
		request.setProductId(productId);
		TmallProductUpdateSchemaGetResponse response = connectorManager.getConnector().execute(request);
		return response.getUpdateProductSchema();
	}
	
	/**
	 * 获取商品全量更新规则
	 * @param categoryId
	 * @param itemId
	 * @return
	 */
	private String getItemUpdateSchema(Long categoryId, Long itemId) {
		TmallItemUpdateSchemaGetRequest updateSchemaGetRequest = new TmallItemUpdateSchemaGetRequest();
		updateSchemaGetRequest.setItemId(itemId);
		updateSchemaGetRequest.setCategoryId(categoryId);
		//updateSchemaGet.setProductId(productId);	//如果没有切换产品的需求,参数可以不填写
		TmallItemUpdateSchemaGetResponse response = connectorManager.getConnector().execute(updateSchemaGetRequest);
		return response.getUpdateItemResult();
	}

	@Override
	public ProductCategoryMapping getProductCategoryMapping(
			Long channelCategoryId, Integer applicationId) {
		ProductCategoryMapping mapping = null;
		try {
			mapping = productCategoryMappingDao.getProductCategoryMapping(channelCategoryId, applicationId);
		} catch (Exception e) {
			//e.printStackTrace();
			mapping = null;
		}
		return mapping;
	}
	
	@Override
	public ChannelAttributeMapping getChannelAttributeMapping(
			Long channelCategoryId, String channelAttributeId, Integer applicationId) {
		ChannelAttributeMapping mapping = null;
		try {
			mapping = channelAttributeMappingDao.getChannelAttributeMapping(channelCategoryId, channelAttributeId, applicationId);
		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.error(e.getMessage());
			mapping = null;
		}
		return mapping;
	}
	/**
	 * 商品上架
	 */
	@Override
	public void onShelf(Product product) throws ProductException {
		Long numiid=null;
		if(StringUtils.hasText(product.getOuterProductId())){
			numiid = getItemId(product.getOuterProductId(),0);
		}else{
			throw new PropertyRequireException("商品外部编码");
		}
		Long num = 0L;
		//下架接口调用
		ItemUpdateListingRequest listingReq = new ItemUpdateListingRequest();
		if(null!=numiid){
			listingReq.setNumIid(numiid);
		}
		ItemSkusGetRequest skuGetReq = new ItemSkusGetRequest();
		skuGetReq.setFields("quantity");
		skuGetReq.setNumIids(numiid.toString());
		ItemSkusGetResponse response = connectorManager.getConnector().execute(skuGetReq);
		List<com.taobao.api.domain.Sku> skuList = response.getSkus();
		if (CollectionUtil.isNotEmpty(skuList)) {
			for (com.taobao.api.domain.Sku sku : skuList) {
				num += sku.getQuantity();
			}
			listingReq.setNum(num);
		}
		connectorManager.getConnector().execute(listingReq);
	}
	/**
	 * 商品下架
	 */
	@Override
	public void offShelf(Product product) throws ProductException {
		Long numiid=null;
		if(StringUtils.hasText(product.getOuterProductId())){
			numiid = getItemId(product.getOuterProductId(),0);
		}else{
			throw new PropertyRequireException("商品外部编码");
		}
		//下架接口调用
		ItemUpdateDelistingRequest delistingReq=new ItemUpdateDelistingRequest();
		if(null!=numiid){
			delistingReq.setNumIid(numiid);
		}
		connectorManager.getConnector().execute(delistingReq);
	}
	/**
	 * 价格更新
	 */
	@Override
	public void updatePrice(Product product) throws ProductException {
		/*Long numiid=null;
		if(StringUtils.hasText(product.getOuterProductId())){
			numiid = getItemId(product.getOuterProductId(),0);
		}else{
			throw new PropertyRequireException("商品外部编码");
		}
		List<Sku> outSkuList = product.getSkuList();
		List<UpdateSkuPrice> sku_prices = new ArrayList<UpdateSkuPrice>();
		//价格更新
		TmallItemPriceUpdateRequest priceUpdateReq = new TmallItemPriceUpdateRequest();
		if(null!=numiid){
			priceUpdateReq.setItemId(numiid);
		}
		if (product.getPrice() != null) {
			priceUpdateReq.setItemPrice(product.getPrice().toString());
		} else {
			throw new PropertyRequireException("商品的价格");
		}
		if (CollectionUtil.isNotEmpty(outSkuList)) {
			for (Sku sku : outSkuList) {
				//根据sku外部ID获取渠道ID
				UpdateSkuPrice skuPrice = new UpdateSkuPrice();
				SkusCustomGetRequest skuGetReq = new SkusCustomGetRequest();
				if(sku.getPrice() == null){
					throw new PropertyRequireException("SKU价格");
				}
				skuGetReq.setFields("sku_id");
				// TODO 测试下该查询是否有必要,以及重新发布
				SkusCustomGetResponse response = connectorManager.getConnector().execute(skuGetReq);
				skuPrice.setSkuId(response.getSkus().get(0).getSkuId());
				skuPrice.setPrice(sku.getPrice().toString());
				//skuPrice.setProperties(response.getSkus().get(0).getProperties());
				skuPrice.setOuterId(sku.getOuterSkuId());
				sku_prices.add(skuPrice);
			}
		}
		priceUpdateReq.setSkuPrices(sku_prices);
		connectorManager.getConnector().execute(priceUpdateReq);*/
	}
	
		/**
		 * 获取商品ID
		 */
	protected Long getItemId(String outid,Integer type) throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);//获取applicationId
		ProductPartnumberMapping productPartnumberMapping = productAttributeService.getProductPartnumberMappingByOuterId(outid, applicationId, type);//映射item的outskuid
		if (null != productPartnumberMapping) {
			return Long.valueOf(productPartnumberMapping.getChannelProductId());
		}
		return null;
	}

	@Override
	public List<ItemCat> getChannelCategory(long parentCid) {
		ItemcatsAuthorizeGetRequest req=new ItemcatsAuthorizeGetRequest();
		req.setFields("item_cat.cid, item_cat.parent_cid,item_cat.name,item_cat.is_parent,item_cat.status");
		ItemcatsAuthorizeGetResponse response = connectorManager.getConnector().execute(req);
		return response.getSellerAuthorize().getItemCats();
	}
	@Override
	public List<TmallItemPropWrapper> getItemPropWrappers(long categoryId) {
		try {
		List<TmallItemPropWrapper> tmList=new ArrayList<TmallItemPropWrapper>();
		Integer applicationId=ContextUtil.get(Constant.APPLICATION_ID);
		ProductCategoryMapping productCategoryMapping=productCategoryMappingDao.getProductCategoryMapping(categoryId, applicationId);//天猫产品id与主目录叶子类目id映射
		Long productId=productCategoryMapping.getProductId();
		//商品创建结构获取  开始
		TmallItemAddSchemaGetRequest schemaGetRequest=new TmallItemAddSchemaGetRequest();
		schemaGetRequest.setCategoryId(categoryId);
		schemaGetRequest.setProductId(productId);
		schemaGetRequest.setType(ProductConstants.ITEM_TYPE);
		schemaGetRequest.setIsvInit(ProductConstants.ISVINIT_FALSE);
		TmallItemAddSchemaGetResponse schemaGetResponse = connectorManager.getConnector().execute(schemaGetRequest);
		List<Field> fieldList = SchemaReader.readXmlForList(schemaGetResponse.getAddItemResult());
		for(Field field:fieldList){
			TmallItemPropWrapper itemPropWrapper=new TmallItemPropWrapper();
			//if(FieldTypeEnum.SINGLECHECK==field.getType()){
				 itemPropWrapper.setCid(categoryId);
				 itemPropWrapper.setName(field.getName());
				 itemPropWrapper.setPid(field.getId());
			//}
			List<Rule> rules = field.getRules();
			if (CollectionUtil.isNotEmpty(rules)) {
				for (Rule rule : rules) {
					if (rule.getName().equals("requiredRule")) {
						itemPropWrapper.setRequired(true);
					}
				}
			}
			tmList.add(itemPropWrapper);
		}
		return tmList;
		} catch (TopSchemaException e) {
			//e.printStackTrace();
			LOGGER.error("", e);
		}
		return null;
	}

	@Override
	public List<TmallItemPropValWrapper> getItemPropValWrappers(
			long categoryId, String pid) {
		try {
		List<TmallItemPropValWrapper> tmList=new ArrayList<TmallItemPropValWrapper>();
		Integer applicationId=ContextUtil.get(Constant.APPLICATION_ID);
		ProductCategoryMapping productCategoryMapping=productCategoryMappingDao.getProductCategoryMapping(categoryId, applicationId);//天猫产品id与主目录叶子类目id映射
		Long productId=productCategoryMapping.getProductId();
		//商品创建结构获取  开始
		TmallItemAddSchemaGetRequest schemaGetRequest=new TmallItemAddSchemaGetRequest();
		schemaGetRequest.setCategoryId(categoryId);
		schemaGetRequest.setProductId(productId);
		schemaGetRequest.setType(ProductConstants.ITEM_TYPE);
		schemaGetRequest.setIsvInit(ProductConstants.ISVINIT_FALSE);
		TmallItemAddSchemaGetResponse schemaGetResponse = connectorManager.getConnector().execute(schemaGetRequest);
		List<Field> fieldList = SchemaReader.readXmlForList(schemaGetResponse.getAddItemResult());
		for(Field field:fieldList){
			if(field.getId()==pid){
				SingleCheckField singleCheckField = (SingleCheckField) field;
				for(Option option:singleCheckField.getOptions()){
					TmallItemPropValWrapper itemPropValWrapper=new TmallItemPropValWrapper();
					itemPropValWrapper.setCid(categoryId);
					itemPropValWrapper.setPid(pid);
					itemPropValWrapper.setName(option.getDisplayName());
					itemPropValWrapper.setVid(option.getValue());
					tmList.add(itemPropValWrapper);
					}
				}
			}
		return tmList;
		} catch (TopSchemaException e) {
			//e.printStackTrace();
			LOGGER.error("", e);
		}
		return null;
	}

	@Override
	public int saveTmallAttr(ChannelAttributeMapping channelAttributeMapping) {
		return channelAttributeMappingDao.saveChannelAttributeMapping(channelAttributeMapping);
	}

	@Override
	public int updateTmallAttr(ChannelAttributeMapping channelAttributeMapping) {
		return channelAttributeMappingDao.updateChannelAttributeMapping(channelAttributeMapping);
	}

	@Override
	public int deleteTmallAttr(int attrid) {
		return channelAttributeMappingDao.deleteChannelAttributeMapping(attrid);
	}

	@Override
	public List<ChannelAttributeMapping> getChannelAttributeMappings(
			Map<String, Object> params) {
		return channelAttributeMappingDao.list(params);
	}

	@Override
	public long getChannelAttributeMappingsCount(Map<String, Object> params) {
		return channelAttributeMappingDao.count(params);
	}

	@Override
	public List<DeliveryTemplate> getDeliveryTemplates() {
		DeliveryTemplatesGetRequest request = new DeliveryTemplatesGetRequest();
		request.setFields("template_id,template_name,created,modified");
		DeliveryTemplatesGetResponse response = connectorManager.getConnector().execute(request);
		
		List<com.taobao.api.domain.DeliveryTemplate> templates = response.getDeliveryTemplates();
		List<DeliveryTemplate> deliveryTemplates = null;
		if (!CollectionUtil.isEmpty(templates)) {
			deliveryTemplates = new ArrayList<DeliveryTemplate>(templates.size());
			for (com.taobao.api.domain.DeliveryTemplate template : templates) {
				DeliveryTemplate dt = new DeliveryTemplate();
				dt.setId(template.getTemplateId());
				dt.setName(template.getName());
				dt.setCreateTime(template.getCreated());
				dt.setUpdateTime(template.getModified());
				dt.setAddress(template.getAddress());
				
				deliveryTemplates.add(dt);
			}
		}
		return deliveryTemplates;
	}

	@Override
	public String getChannelProductIdByOuterProductId(String outerProductId) {
		ItemsCustomGetRequest request = new ItemsCustomGetRequest();
		request.setOuterId(outerProductId);
		request.setFields("num_iid");
		ItemsCustomGetResponse response = connectorManager.getConnector().execute(request);
		List<Item> items = response.getItems();
		if (CollectionUtil.isNotEmpty(items)) {
			return String.valueOf(items.get(0).getNumIid());
		}
		return null;
	}

	@Override
	public String getChannelSkuIdByOuterSkuId(String outerSkuId) {
		SkusCustomGetRequest request = new SkusCustomGetRequest();
		request.setOuterId(outerSkuId);
		request.setFields("sku_id");
		SkusCustomGetResponse response = connectorManager.getConnector().execute(request);
		List<com.taobao.api.domain.Sku> skus = response.getSkus();
		if (CollectionUtil.isNotEmpty(skus)) {
			return String.valueOf(skus.get(0).getSkuId());
		}
		return null;
	}
	
	/**
	 * 获取商品添加的规则
	 * @param channelCategoryId 渠道类目id
	 * @param channelProductId 渠道产品id
	 * @return
	 */
	private String getAddItemResult(Long channelCategoryId, Long channelProductId) {
		TmallItemAddSchemaGetRequest schemaGetRequest = new TmallItemAddSchemaGetRequest();
		schemaGetRequest.setCategoryId(channelCategoryId);
		schemaGetRequest.setProductId(channelProductId);
		schemaGetRequest.setType(ProductConstants.ITEM_TYPE);
		schemaGetRequest.setIsvInit(ProductConstants.ISVINIT_FALSE);
		TmallItemAddSchemaGetResponse schemaGetResponse = connectorManager.getConnector().execute(schemaGetRequest);
		String addItemResult = schemaGetResponse.getAddItemResult();
		return addItemResult;
	}
	
	/**
	 * 获取渠道属性
	 */
	@Override
	public Category getChannelProperty(Category category)
			throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		if (StringUtils.hasText(category.getCategoryId())) {
			//系统类目id
//			CategoryMapping categoryMapping = productAttributeService.getCategoryMapping(Long.valueOf(category.getCategoryId()), applicationId);//渠道主目录映射
//			if (null == categoryMapping) {
//				throw new MappingNotFoundException("category", category.getCategoryId());
//			}
			ProductCategoryMapping productCategoryMapping = null;
			try {
				productCategoryMapping = productCategoryMappingDao.getProductCategoryMapping(Long.parseLong(category.getChannelCategoryId()), applicationId);
			} catch (NumberFormatException e1) {
				LOGGER.error("", e1);
			} catch (Exception e2) {
				LOGGER.error("", e2);
			}
			if (null == productCategoryMapping) {
				throw new MappingNotFoundException("产品类目", String.valueOf(category.getChannelCategoryId()));
			}
			Long productId = productCategoryMapping.getProductId();
			Long channelCategoryId = Long.parseLong(category.getChannelCategoryId());
			
			category.setChannelCategoryId(String.valueOf(channelCategoryId));
			
			//抓取product创建规则
			String addProductRule = getProductAddSchema(channelCategoryId, Long.valueOf(category.getBrandId()));
			
			//抓取item创建规则
			String addItemRule = getAddItemResult(channelCategoryId, productId);
			
			try {
				parseProductRule(addProductRule, category, applicationId);
				parseItemRule(addItemRule, category, applicationId);
			} catch (TopSchemaException e) {
				//e.printStackTrace();
				LOGGER.error("", e);
				return null;
			}
		}
		return category;
	}
	
	/**
	 * 解析产品添加规则
	 * @param xmlRule
	 * @param category
	 * @param applicationId
	 * @throws TopSchemaException
	 */
	private void parseProductRule(String xmlRule, Category category, Integer applicationId) throws TopSchemaException {
		if (StringUtils.hasText(xmlRule)) {
			List<Field> fieldList = SchemaReader.readXmlForList(xmlRule);
			if (CollectionUtil.isNotEmpty(fieldList)) {
				for (Field field : fieldList) {
					parseField(field, category, null, applicationId, false);
				}
			}
		}
	}
	
	/**
	 * 解析item添加规则
	 * @param xmlRule
	 * @param category
	 * @param applicationId
	 * @throws TopSchemaException
	 */
	private void parseItemRule(String xmlRule, Category category, Integer applicationId) throws TopSchemaException {
		if (StringUtils.hasText(xmlRule)) {
			List<Field> fieldList = SchemaReader.readXmlForList(xmlRule);
			
			if (CollectionUtil.isNotEmpty(fieldList)) {
				String includeFieldIds = registry.get("includeFieldIds");
				Set<String> includeFieldSet = null;
				if (StringUtils.hasText(includeFieldIds)) {
					String[] includeFieldIdArray = StringUtils.split(includeFieldIds, ",");
					includeFieldSet = new HashSet<String>(includeFieldIdArray.length);
					for (int i = 0; i < includeFieldIdArray.length; i++) {
						includeFieldSet.add(includeFieldIdArray[i].trim());
					}
				}
				String onlyRequired = registry.get("onlyRequired");
				boolean onlyParseRequiredField = false;
				if (StringUtils.hasText(onlyRequired)) {
					onlyParseRequiredField = Boolean.valueOf(onlyRequired);
				}
				for (Field field : fieldList) {
					parseField(field, category, includeFieldSet, applicationId, onlyParseRequiredField);
				}
			}
		}
	}
	
	private void parseField(Field field, Category category, Set<String> includeFieldIds, Integer applicationId, boolean onlyRequired) {
		boolean hasRequiredRule = false;
		List<Rule> rules = field.getRules();
		if (CollectionUtil.isNotEmpty(rules)) {
			for (Rule rule : rules) {
				if ("requiredRule".equals(rule.getName())) {
					hasRequiredRule = true;
					break;
				}
			}
		} 
		if (onlyRequired && !hasRequiredRule) {
			return;
		}
		FieldTypeEnum fieldType = field.getType();
		String fieldId = field.getId();
		if (CollectionUtil.isNotEmpty(includeFieldIds)) {
			if (!includeFieldIds.contains(fieldId)) {
				return;
			}
		}
		switch (fieldType) {
		case INPUT:
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_INPUT, hasRequiredRule, null);
			break;
		case MULTIINPUT:
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_INPUT, hasRequiredRule, null);
			break;
		case SINGLECHECK:
			SingleCheckField singleCheckField = (SingleCheckField)field;
			List<Option> singleCheckOptions = singleCheckField.getOptions();
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_SELECT, hasRequiredRule, singleCheckOptions);
			break;
		case MULTICHECK:
			MultiCheckField multiCheckField = (MultiCheckField)field;
			List<Option> multiCheckOptions = multiCheckField.getOptions();
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_SELECT, hasRequiredRule, multiCheckOptions);
			break;
		case COMPLEX:
			ComplexField complexField = (ComplexField)field;
			List<Field> complexFields = complexField.getFieldList();
			if (CollectionUtil.isNotEmpty(complexFields)) {
				for (Field perComplexField : complexFields) {
					parseField(perComplexField, category, includeFieldIds, applicationId, onlyRequired);
				}
			}
			break;
		case MULTICOMPLEX:
			MultiComplexField multiComplexField = (MultiComplexField)field;
			
			List<Field> multiComplexFields = multiComplexField.getFieldList();
			if (CollectionUtil.isNotEmpty(multiComplexFields)) {
				for (Field multComField : multiComplexFields) {
					parseField(multComField, category, includeFieldIds, applicationId, onlyRequired);
				}
			}
			break;
		default:
			break;
		}
	}
	
	private void setAttribute(Category category, String channelAttributeId, String channelAttributeName, 
			Integer applicationId, String attributeType, boolean hasRequiredRule, List<Option> options) {
		Attribute attribute = new Attribute();
		attribute.setAttributeId(channelAttributeId);
		attribute.setAttributeName(channelAttributeName);
		attribute.setType(attributeType);
		attribute.setRequired(hasRequiredRule);
		
		String tmallColorProperty = registry.get("tmallColorProperty");
		String tmallSizeProperty = registry.get("tmallSizeProperty");
		
		//color
		if (StringUtils.hasText(tmallColorProperty)) {
			String[] tmallColorPropertyArray = tmallColorProperty.split(",");
			for (int i = 0; i < tmallColorPropertyArray.length; i++) {
				if (channelAttributeId.equals(tmallColorPropertyArray[i])) {
					attribute.setColorProperty(true);
					attribute.setSalesProperty(true);
				}
			}
		}
		
		//size
		if (StringUtils.hasText(tmallSizeProperty)) {
			String[] tmallSizePropertyArray = tmallSizeProperty.split(",");
			for (int i = 0; i < tmallSizePropertyArray.length; i++) {
				if (channelAttributeId.equals(tmallSizePropertyArray[i])) {
					attribute.setSizeProperty(true);
					attribute.setSalesProperty(true);
				}
			}
		}
		
		//TODO fuck,不想说什么了
		if ("material_prop_name".equals(channelAttributeId) || "material_prop_content".equals(channelAttributeId)) {
			attribute.setParentAttributeId("material_prop_149422948");
			attribute.setParentType("2");
			attribute.setRequired(true);
		}
		
		if (CollectionUtil.isNotEmpty(options)) {
			for (Option option : options) {
				AttributeValue attributeValue = new AttributeValue(option.getValue(), option.getDisplayName());
				attribute.addAttributeValue(attributeValue);
			}
		}
		
		category.addAttribute(attribute);
	}

	@Override
	public Long getChannelProductId(Long categoryId, String properties) {
		ProductsSearchRequest request = new ProductsSearchRequest();
		request.setFields("product_id");
		request.setCid(categoryId);
		request.setProps(properties);
		//request.setStatus("3");
		request.setPageNo(1L);
		request.setPageSize(1L);
		ProductsSearchResponse response = connectorManager.getConnector().execute(request);
		List<com.taobao.api.domain.Product> products = response.getProducts();
		if (CollectionUtil.isNotEmpty(products)) {
			return products.get(0).getProductId();
		}
		return null;
	}

	@Override
	public void saveProductCategoryMapping(ProductCategoryMapping mapping) {
		try {
			productCategoryMappingDao.getProductCategoryMapping(mapping.getChannelCategoryId(), mapping.getApplicationId());
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error(e.getMessage());
			productCategoryMappingDao.insertProductCategoryMapping(mapping);
		}
	}

	@Override
	public List<Product> getTotalProducts(Map<String, Object> params) {
		List<Product> productInfos = new ArrayList<Product>();
		productInfos.addAll(getOnSaleProducts(params));
		productInfos.addAll(getInstockProducts(params));
		return productInfos;
	}

	@Override
	public List<Product> getOnSaleProducts(Map<String, Object> params) {
		String optionFields = "num_iid, outer_id";
		List<Product> productInfos = new ArrayList<Product>();
		productInfos.addAll(getOnSaleProducts(optionFields, 1L, params));
		return productInfos;
	}
	
	private List<Product> getOnSaleProducts(String optionFields, Long currPageIndex, Map<String, Object> params) {
		Date startTime = (Date) params.get("startDate");
		Date endTime = (Date) params.get("endDate");
		List<Product> productInfos = new ArrayList<Product>();
		
		ItemsOnsaleGetRequest request = new ItemsOnsaleGetRequest();
		if (StringUtils.hasText(optionFields)) {
			request.setFields(optionFields);
		} else {
			request.setFields("cid,pic_url,num,modified,num_iid,title,type,outer_id,price");
		}
		//request.setCid(1512L);
		request.setPageNo(currPageIndex);
		//request.setOrderBy("list_time:desc");
		request.setPageSize(PAGE_COUNT);
		request.setStartModified(startTime);
		request.setEndModified(endTime);
		ItemsOnsaleGetResponse response = connectorManager.getConnector().execute(request);
		if (CollectionUtil.isNotEmpty(response.getItems())) {
			for (Item item : response.getItems()) {
				//将淘宝订单结构转换成系统订单结构
				Product productInfo = new Product();
				//order = dataTranslatorManager.getDataTranslator().transformOrder(trade);
				productInfo.setChannelProductId(item.getNumIid().toString());
				productInfo.setOuterProductId(item.getOuterId());
				productInfo.putParam("shelfStatus", "onShelf");
				productInfos.add(productInfo);
			}
			Long totalResults = response.getTotalResults();
			long lastPageIndex = (totalResults % PAGE_COUNT == 0) ? (totalResults / PAGE_COUNT) : (totalResults / PAGE_COUNT + 1);
			long pageIndex = currPageIndex + 1;
			if (pageIndex <= lastPageIndex) {
				productInfos.addAll(getOnSaleProducts(optionFields, pageIndex, params));
			}
		}
		return productInfos;
	}

	@Override
	public List<Product> getInstockProducts(Map<String, Object> params) {
		String optionFields = "num_iid, outer_id";
		List<Product> productInfos = new ArrayList<Product>();
		productInfos.addAll(getInstockProducts(optionFields, 1L, params, "for_shelved"));
		//productInfos.addAll(getInstockProducts(optionFields, 1L, params, "sold_out"));
		return productInfos;
	}

	private List<Product> getInstockProducts(String optionFields, Long currPageIndex, Map<String, Object> params, String banner) {
		Date startTime = (Date) params.get("startDate");
		Date endTime = (Date) params.get("endDate");
		List<Product> productInfos = new ArrayList<Product>();
		
		ItemsInventoryGetRequest request = new ItemsInventoryGetRequest();
		if (StringUtils.hasText(optionFields)) {
			request.setFields(optionFields);
		} else {
			request.setFields("cid,pic_url,num,modified,num_iid,title,type,outer_id,price");
		}
		
		request.setPageNo(currPageIndex);
		request.setPageSize(PAGE_COUNT);
		request.setStartModified(startTime);
		request.setEndModified(endTime);
		request.setBanner(banner);
		
		ItemsInventoryGetResponse response = connectorManager.getConnector().execute(request);
		if (CollectionUtil.isNotEmpty(response.getItems())) {
			for (Item item : response.getItems()) {
				//将淘宝订单结构转换成系统订单结构
				Product productInfo = new Product();
				//order = dataTranslatorManager.getDataTranslator().transformOrder(trade);
				productInfo.setChannelProductId(item.getNumIid().toString());
				productInfo.setOuterProductId(item.getOuterId());
				productInfo.putParam("shelfStatus", "inStock");
				productInfos.add(productInfo);
			}
			Long totalResults = response.getTotalResults();
			long lastPageIndex = (totalResults % PAGE_COUNT == 0) ? (totalResults / PAGE_COUNT) : (totalResults / PAGE_COUNT + 1);
			long pageIndex = currPageIndex + 1;
			if (pageIndex <= lastPageIndex) {
				productInfos.addAll(getInstockProducts(optionFields, pageIndex, params ,banner));
			}
		}
		return productInfos;
	}

	@Override
	public Map<String,String> isRepeatOuterId(Product productInfo) {
		ItemsCustomGetRequest request = new ItemsCustomGetRequest();
		request.setOuterId(productInfo.getOuterProductId());
		request.setFields("outer_id,num_iid");
		ItemsCustomGetResponse response = connectorManager.getConnector().execute(request);
		List<Item> items = response.getItems();
		Map<String,String> map = null;
		if (CollectionUtil.isNotEmpty(items)) {
			StringBuilder number_iids = new StringBuilder();
			map = new HashMap<String, String>();
			for (Item i : items) {
				number_iids.append(i.getNumIid()).append(",");
			}
			map.put("OuterProductId", productInfo.getOuterProductId());
			map.put("ChannelProductId", number_iids.substring(0, number_iids.length()-1));
		}
		return map;
	}
	
	public void setInputVal(ComplexValue complexValue,Field field,Boolean parentComplex,String fieldId,String dataid,Map<String, Object> data){
		if (parentComplex) {
			InputField inputField = new InputField();
			inputField.setId(fieldId);
			inputField.setValue(data.get(dataid).toString());
			complexValue.put(inputField);
		} else {
			InputField inputField = (InputField)field;
			inputField.setValue(data.get(dataid).toString());
		}
	}
	
	public void setMulitInput(ComplexValue complexValue,Field field,Boolean parentComplex,String parentFieldId,String dataid,Map<String, Object> data){
		if (parentComplex) {
			@SuppressWarnings("unchecked")
			List<Object> listValues = (List<Object>)data.get(dataid);
			List<String> multiInputFieldValues = new ArrayList<String>(listValues.size());
			for (Object listValue : listValues) {
				multiInputFieldValues.add(listValue.toString());
			}
			complexValue.setMultiInputFieldValues(parentFieldId, multiInputFieldValues);
		} else {
			MultiInputField multiInputField = (MultiInputField)field;
			@SuppressWarnings("unchecked")
			List<Object> listValues = (List<Object>)data.get(dataid);
			if (CollectionUtil.isNotEmpty(listValues)) {
				multiInputField.addValue(listValues.toString());
			}
		}
	}
	
	public void setSingleCheck(ComplexValue complexValue,Field field,Boolean parentComplex,String fieldId,String dataid,Map<String, Object> data){
		if (parentComplex) {
			SingleCheckField singleCheckField = new SingleCheckField();
			singleCheckField.setId(fieldId);
			singleCheckField.setValue(data.get(dataid).toString());
			complexValue.put(singleCheckField);
		} else {
			SingleCheckField singleCheckField = (SingleCheckField)field;
			singleCheckField.setValue(data.get(dataid).toString());
		}
	}
	
	public void setMulitCheck(ComplexValue complexValue,Field field,Boolean parentComplex,String parentFieldId,String dataid,Map<String, Object> data){
		if (parentComplex) {
			@SuppressWarnings("unchecked")
			List<Object> listValues = (List<Object>)data.get(dataid);
			List<Value> multiCheckFieldValues = new ArrayList<Value>(listValues.size());
			if (CollectionUtil.isNotEmpty(listValues)) {
				for (Object listValue : listValues) {
					multiCheckFieldValues.add(new Value(listValue.toString()));
				}
			}
			complexValue.setMultiCheckFieldValues(parentFieldId, multiCheckFieldValues);
		} else {
			MultiCheckField multiCheckField = (MultiCheckField)field;
			/*@SuppressWarnings("unchecked")
			List<Object> listValues = (List<Object>)data.get(dataid);
			if (CollectionUtil.isNotEmpty(listValues)) {
				for (Object listValue : listValues) {
					multiCheckField.addValue(listValue.toString());
				}
			}*/
			//TODO 方案限制,暂时对于多选的,暂时只能支持选择一个
			//update by ywu 2016-07-08
			String listValues = (String)data.get(dataid);
			if (StringUtils.hasText(listValues)) {
					multiCheckField.addValue(listValues.toString());
			}
		}
	}

	@Override
	public AttributeMapping getAttrMapping(Long cid, String channelKey) {
		    AttributeMapping attributeMapping = null;
		try {
			 attributeMapping=attributeMappingDao.getAttrMapping(cid, channelKey);
		} catch (Exception e) {
			attributeMapping=null;
		}
		return attributeMapping;
	}

	@Override
	public List<CategoryList> getAllCategory() throws ProductException {
		ItemcatsAuthorizeGetRequest req = new ItemcatsAuthorizeGetRequest();
		req.setFields("item_cat.cid,item_cat.name,item_cat.parent_cid,item_cat.is_parent");
		ItemcatsAuthorizeGetResponse response = connectorManager.getConnector().execute(req);
		List<CategoryList> categoryLists = new ArrayList<CategoryList>();
		for(ItemCat itemcat : response.getSellerAuthorize().getItemCats()){
			CategoryList category = new CategoryList();
			setValue(category,itemcat);
			categoryLists.add(category);
		}
		return categoryLists;
	}
	
	public void setValue(CategoryList category , ItemCat itemcat){
		List<CategoryList> categoryLists = new ArrayList<CategoryList>();
		category.setCategoryId(itemcat.getCid());
		category.setParent_cid(itemcat.getParentCid());
		category.setName(itemcat.getName());
		if(itemcat.getIsParent()){
			ItemcatsGetRequest req = new ItemcatsGetRequest();
			req.setFields("cid,parent_cid,name,is_parent");
			req.setParentCid(itemcat.getCid());
			ItemcatsGetResponse response = connectorManager.getConnector().execute(req);
			for(ItemCat itemcats : response.getItemCats()){
					CategoryList categoryList = new CategoryList();
					if(itemcats.getIsParent()){
						setValue(categoryList, itemcats);
					}else{
						categoryList.setCategoryId(itemcats.getCid());
						categoryList.setParent_cid(itemcats.getParentCid());
						categoryList.setName(itemcats.getName());
					}
					categoryLists.add(categoryList);
			}
		}
		category.setChildrens(categoryLists);
	}

	@Override
	public void initProductCat() {
		try {
			List<CategoryList> categoryLists = getAllCategory();
			if (!CollectionUtils.isEmpty(categoryLists)) {
				doInitProductCat(categoryLists);
			}
		} catch (ProductException e) {
			LOGGER.error("", e);
		}
	}
	
	private void doInitProductCat(List<CategoryList> categoryLists) {
		for (CategoryList categoryList : categoryLists) {
			if (!CollectionUtils.isEmpty(categoryList.getChildrens())) {
				doInitProductCat(categoryList.getChildrens());
			} else {
				//insert
				String brandPVid = registry.get("brandPVid");
				if (StringUtils.hasText(brandPVid)) {
					try {
						Long channelProductId = getChannelProductId(categoryList.getCategoryId(), brandPVid);
						if (null != channelProductId) {
							ProductCategoryMapping mapping = new ProductCategoryMapping();
							mapping.setProductId(channelProductId);
							mapping.setChannelCategoryId(categoryList.getCategoryId());
							Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
							mapping.setApplicationId(applicationId);
							saveProductCategoryMapping(mapping);
						}
					} catch (Exception e) {
						LOGGER.error("产品类目" + categoryList.getName() + "-" + categoryList.getCategoryId() + "出错", e);
					}
				}
			}
		}
	}

	@Override
	public void BindItemStore(Product product) throws ProductException {
		if (product != null) {
			Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
			String outerProductId = product.getOuterProductId();
			ProductPartnumberMapping mapping = productAttributeService.getProductPartnumberMappingByOuterId(outerProductId, applicationId, 0);
			if (null == mapping) {
				throw new MappingNotFoundException("商品外部编码", outerProductId);
			}
			String storeList = product.getParam("storeList");
			String [] strs = storeList.split(",");
			List<Long> storeIds = new ArrayList<Long>();
			for (int i=0; i<strs.length; i++) {
				storeIds.add(Long.valueOf(strs[i]));
			}
			StoreId storeId = new StoreId();
			storeId.setStoreId(storeIds);
			
			ItemStoreBanDingRequest request= new ItemStoreBanDingRequest();
			request.setItemId(Long.valueOf(mapping.getChannelProductId()));
			request.setActionType("ADD");
			request.setStoreIds(storeId);
			request.setRemark("绑定门店");
			connectorManager.getConnector().execute(request);
			
		}
		
	}

	public String getOuterIdbySkuIdandNick(String skuId) throws ProductException {
		String outerId = null;
		if(skuId != null){
			Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
			String nick =applicationService.getApplicationById(applicationId).getNick();
			ItemSkuGetRequest req = new ItemSkuGetRequest();
			req.setFields("outer_id");
			req.setSkuId(Long.valueOf(skuId));
			req.setNick(nick);
			ItemSkuGetResponse rsp = connectorManager.getConnector().execute(req);
			if( rsp.isSuccess() && rsp.getSku()!=null ){
				outerId = rsp.getSku().getOuterId();
			}else {
				throw new ProductException(rsp.getMsg());
			}
		}
		return outerId;
	}


	@Override
	public String qimenStoreInventoryItemInitial(StoreItems storeItems) throws ProductException {
		String result = "failure";
		QimenStoreinventoryIteminitialRequest req = new QimenStoreinventoryIteminitialRequest();
		req.setOperationTime(DateUtil.format(new Date()));
		List<QimenStore> stores = new ArrayList<QimenStore>();
		QimenStore store = new QimenStore();
		stores.add(store);
		store.setWarehouseType(storeItems.getWarehouseType());
		store.setWarehouseId(storeItems.getWarehouseId());
		List<QimenStoreInventory> qimenStoreInventorylist = new ArrayList<QimenStoreInventory>();
		List<StoreInventory> si = storeItems.getStoreInventory();
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		for(StoreInventory sitem : si ){
			String outerProductId = sitem.getOuterProductId();
			ProductPartnumberMapping mapping = productAttributeService.getChannelProductIdByOuterId(outerProductId, applicationId, 0);
			if (null == mapping) {
				continue;
			}
			String [] numIids = mapping.getChannelProductId().split(",");
			for(int i = 0;i < numIids.length;i++){
				QimenStoreInventory qimenStoreInventory = new QimenStoreInventory();
				qimenStoreInventory.setBillNum(String.valueOf(new Date().getTime()));
				qimenStoreInventory.setItemId(Long.valueOf(numIids[i]));
				String outerSkuId = sitem.getOuterSkuId();
				qimenStoreInventory.setOuterId(sitem.getOuterSkuId());
				qimenStoreInventory.setSkuId(this.getSkuIdByOuterId(Long.valueOf(numIids[i]), outerSkuId,applicationId));
				qimenStoreInventory.setInventoryType(sitem.getInventoryType());
				qimenStoreInventory.setQuantity(sitem.getQuantity());
				qimenStoreInventorylist.add(qimenStoreInventory);
			}
		}
		if(qimenStoreInventorylist != null && qimenStoreInventorylist.size()>0){
			Application application = applicationService.getApplicationById(applicationId);
			store.setStoreInventories(qimenStoreInventorylist);
			req.setStores(stores);
			req.setUserId(Long.valueOf(application.getParam().getVendorId()));
			QimenStoreinventoryIteminitialResponse response = connectorManager.getConnector().execute(req);
			result = response.getFlag();
		}
		return result;
	}
	
	public Long getSkuIdByOuterId(Long channelProductId,String outerSkuId,Integer applicationId){
		Long returnSkuid = null;
		ProductPartnumberMapping mapping = productAttributeService.getChannelProductIdByOuterId(outerSkuId, applicationId, 1);
		if(mapping != null){
			returnSkuid = Long.valueOf(mapping.getChannelProductId());
		}else{
			ProductJdpDao productJdpDao = SpringUtil.getBean(ProductJdpDao.class);
			String response = productJdpDao.getItemJdpResponse(channelProductId);
			TaobaoParser<ItemSellerGetResponse> parser = new ObjectJsonParser<ItemSellerGetResponse>(ItemSellerGetResponse.class);
			com.taobao.api.domain.Item jdpitem = null;
			try {
				jdpitem = parser.parse(response).getItem();
			} catch (ApiException e) {
				LOGGER.error("解析Jdp item 出错"+e.getErrMsg());
			}
			List<com.taobao.api.domain.Sku> jdpskus = jdpitem.getSkus();
			List<Product> skus = new ArrayList<Product>();
			for(com.taobao.api.domain.Sku jdpsku : jdpskus){
				Product sku = new Product();
				sku.setChannelProductId(String.valueOf(jdpsku.getSkuId()));
				sku.setOuterProductId(jdpsku.getOuterId());
				skus.add(sku);
				if(outerSkuId.equals(jdpsku.getOuterId())){
					returnSkuid = jdpsku.getSkuId();
				}
			}
			productPartnumberMappingService.saveOrUpdateProductPartnumberMapping(skus, applicationId, 1);
		}
		return returnSkuid;
	}
	
	
}