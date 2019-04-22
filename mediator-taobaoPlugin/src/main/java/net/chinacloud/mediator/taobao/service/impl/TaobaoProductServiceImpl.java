/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoProductServiceImpl.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.service.impl;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Attribute;
import net.chinacloud.mediator.domain.AttributeValue;
import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.domain.ChannelPictureMapping;
import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductAttribute;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.domain.StoreItems;
import net.chinacloud.mediator.exception.product.ImageUploadException;
import net.chinacloud.mediator.exception.product.MappingNotFoundException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.exception.product.PropertyRequireException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.service.ProductAttributeService;
import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.dao.ChannelPictureMappingDao;
import net.chinacloud.mediator.taobao.dao.TaobaoChannelAttributeMappingDao;
import net.chinacloud.mediator.taobao.domain.AttributeMapping;
import net.chinacloud.mediator.taobao.domain.ChannelAttributeMapping;
import net.chinacloud.mediator.taobao.domain.ItemPropWrapper;
import net.chinacloud.mediator.taobao.domain.ProductCategoryMapping;
import net.chinacloud.mediator.taobao.domain.TaobaoChannelAttributeMapping;
import net.chinacloud.mediator.taobao.domain.TmallItemPropValWrapper;
import net.chinacloud.mediator.taobao.domain.TmallItemPropWrapper;
import net.chinacloud.mediator.taobao.service.TaobaoProductService;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.ImageUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.taobao.api.FileItem;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.ItemProp;
import com.taobao.api.domain.PropValue;
import com.taobao.api.request.ItemAddRequest;
import com.taobao.api.request.ItemJointImgRequest;
import com.taobao.api.request.ItemJointPropimgRequest;
import com.taobao.api.request.ItemPriceUpdateRequest;
import com.taobao.api.request.ItemUpdateRequest;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.request.ItempropsGetRequest;
import com.taobao.api.response.ItemAddResponse;
import com.taobao.api.response.ItemJointImgResponse;
import com.taobao.api.response.ItemcatsGetResponse;
import com.taobao.api.response.ItempropsGetResponse;

/**
 * @description 淘宝渠道的商品业务实现类
 * @author yejunwu123@gmail.com
 * @since 2015年7月11日 上午11:08:00
 */
@Service
public class TaobaoProductServiceImpl implements
		TaobaoProductService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaobaoProductServiceImpl.class);
	@Autowired
	TaobaoChannelAttributeMappingDao taobaoChannelAttributeMappingDao;
	@Autowired
	ProductAttributeService productAttributeService;
	@Autowired
	ConnectorManager<TaobaoRequest<?>> connectorManager;
	@Autowired
	ProductServiceImpl productServiceImpl;
	@Autowired
	ChannelPictureMappingDao channelPictureMappingDao;
	@Autowired
	Registry registry;
	
	@Override
	public void createProduct(Map<String,Object> product)
			throws ProductException {
		LOGGER.info("==============start:" + DateUtil.format(new Date()) + "================");
		Long picId = 0L;
		ItemAddRequest request = new ItemAddRequest();
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID); //渠道的applicationId
		//CategoryMapping categoryMapping = null ; //主目录
		Long cid = 0L;
		if("false".equals(product.get(ProductAttribute.REPUBLISH).toString())){
			if(!StringUtils.isEmpty(product.get(ProductAttribute.OUTER_ID))){
				ProductPartnumberMapping partnumberMapping=null;
				try{
					partnumberMapping=productAttributeService.getProductPartnumberMappingByOuterId(product.get(ProductAttribute.OUTER_ID).toString(), applicationId, 0);
				}catch(Exception e){
					
				}
				if(partnumberMapping!=null){
					throw new ProductException("exception.product.exist", product.get(ProductAttribute.OUTER_ID).toString());
				}
			}
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.CATEGORY_ID))) {
			//update by ywu 2016-06-21 传过来的直接是平台id,无需再转换
			String categoryId = product.get(ProductAttribute.CATEGORY_ID).toString();
			/*categoryMapping = productAttributeService.getCategoryMapping(Long.parseLong(categoryId), applicationId);
			if (categoryMapping == null) {
				throw new MappingNotFoundException("类目",categoryId);
			} else {
				cid = categoryMapping.getChannelCategoryId();//获取渠道类目的id
				request.setCid(cid); //类目id
			}*/
			cid = Long.valueOf(categoryId);//获取渠道类目的id
			request.setCid(cid); //类目id
		} else {
			 throw new PropertyRequireException("系统目录");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.QUANTIRY))) {	//数量
			String quantity = product.get(ProductAttribute.QUANTIRY).toString();
			request.setNum(Long.parseLong(quantity));
		} else {
			throw new PropertyRequireException("商品库存");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.SALES_PRICE)))  {	// 价格
			String price = product.get(ProductAttribute.SALES_PRICE).toString();
			request.setPrice(price.toString());
		} else {
			 throw new PropertyRequireException("商品价格");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.STUFF_STATUS))) {	// 新旧程度
			String stuffStatus = product.get(ProductAttribute.STUFF_STATUS).toString();
			request.setStuffStatus(stuffStatus);
		} else {
			throw new PropertyRequireException("新旧程度");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.TYPE))) {	//  发布类型
			String type = product.get(ProductAttribute.TYPE).toString();
			request.setType(type);
		} else {
			throw new PropertyRequireException("发布类型");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.TITLE))) {		//商品标题
			String title = product.get(ProductAttribute.TITLE).toString();
			request.setTitle(title);
		} else {
			throw new PropertyRequireException("商品标题");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.APPROVE_STATUS))) {		//商品状态
			String approvestatus=product.get(ProductAttribute.APPROVE_STATUS).toString();
			request.setApproveStatus(approvestatus);
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.DESCRIPTION)))  {		//描述字数要大于5个字符，小于25000个字符，受违禁词控制
			String description = product.get(ProductAttribute.DESCRIPTION).toString();
			request.setDesc(description);
		} else {
			throw new PropertyRequireException("描述(大于5个字符)");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.LOCATION_STATE))) {	//省
			String province = product.get(ProductAttribute.LOCATION_STATE).toString();
			request.setLocationState(province);
		} else {
			throw new PropertyRequireException("省份");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.LOCATION_CITY))) {	//市
			String city = product.get(ProductAttribute.LOCATION_CITY).toString();
			request.setLocationCity(city);
		} else {
			throw new PropertyRequireException("城市");
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.FREIGHT_PAYER))) {	//运费承担方式
			String freightPayer = product.get(ProductAttribute.FREIGHT_PAYER).toString();
			request.setFreightPayer(freightPayer);
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.POSTAGE_ID))) {
			String postageId = product.get(ProductAttribute.POSTAGE_ID).toString();
			request.setPostageId(Long.parseLong(postageId));
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.SUB_STOCK))) {
			String substock = product.get(ProductAttribute.SUB_STOCK).toString();//拍下减库存
			request.setSubStock(Long.parseLong(substock));
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.OUTER_ID))) {		// 外部商家编码ID
			String outerId = product.get(ProductAttribute.OUTER_ID).toString();
			request.setOuterId(outerId);
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.SHOWCASE))) {		// 是否橱窗推荐
			request.setHasShowcase(Boolean.getBoolean(product.get(ProductAttribute.SHOWCASE).toString()));
		}
		if (!StringUtils.isEmpty(product.get(ProductAttribute.MAIN_IMAGE))) {		// 主图
			String image = product.get(ProductAttribute.MAIN_IMAGE).toString();
			if (StringUtils.hasText(image)) {
				try {
					LOGGER.info("==============start translate main img:" + DateUtil.format(new Date()) + "================");
					byte[] imgBytes = ImageUtil.image2Bytes(image);
					LOGGER.info("==============end translate main img:" + DateUtil.format(new Date()) + "================");
					FileItem fItem = new FileItem(ImageUtil.toFileName(image), imgBytes);
					request.setImage(fItem);
				} catch (Exception e) {
					LOGGER.error("", e);
					throw new ProductException("exception.product.img.error", product.get(ProductAttribute.OUTER_ID).toString(), image);
				}
			}
		}
		//库存扣减方式
		if (!StringUtils.isEmpty(product.get(ProductAttribute.SUB_STOCK))) {
			String subStock = product.get(ProductAttribute.SUB_STOCK).toString();
			request.setSubStock(Long.valueOf(subStock));
		}
		Map<String,String> propPicture=new HashMap<String, String>();
		Map<String,String> alias=new HashMap<String, String>();
		//查询sku销售属性及对应的值
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> skuInfos = (List<Map<String, Object>>)product.get(ProductAttribute.SKU);
		StringBuilder skuProperties = new StringBuilder();
		StringBuilder propAlias=new StringBuilder();
		if (CollectionUtil.isNotEmpty(skuInfos)) {
			StringBuilder skuPrice = new StringBuilder();
		    StringBuilder skuQuantitys = new StringBuilder();
		    StringBuilder skuOuterIds = new StringBuilder();
	        for (Map<String,Object> skuInfo : skuInfos) {
	        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_PRICE)))){
	        		skuPrice.append(skuInfo.get(ProductAttribute.SKU_PRICE).toString()).append(",");
	        	}else{
	        		throw new PropertyRequireException("SKU价格");
	        	}
	        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_QUANTITY)))){
	        		skuQuantitys.append(skuInfo.get(ProductAttribute.SKU_QUANTITY).toString()).append(",");
	        	}else{
	        		throw new PropertyRequireException("SKU库存");
	        	}
	        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_OUTER_ID)))){
	        		skuOuterIds.append(skuInfo.get(ProductAttribute.SKU_OUTER_ID).toString()).append(",");
	        	}else{
	        		throw new PropertyRequireException("SKU外部Id");
	        	}
	        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_COLOR)))){
	        		skuProperties.append(skuInfo.get(ProductAttribute.SKU_COLOR).toString()).append(";");
	        	}else{
	        		throw new PropertyRequireException("SKU颜色");
	        	}
	        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_SIZE)))){
	        		skuProperties.append(skuInfo.get(ProductAttribute.SKU_SIZE).toString()).append(";");
	        	}
	        	skuProperties.deleteCharAt(skuProperties.lastIndexOf(";")).append(",");
	        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.PROPERTY_IMAGE)))){
	        		@SuppressWarnings("unchecked")
					List<String> picPath = (List<String>)skuInfo.get(ProductAttribute.PROPERTY_IMAGE);
		        	propPicture.put(skuInfo.get(ProductAttribute.SKU_COLOR).toString(), picPath.get(0));
	        	}
	        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.COLOR_ALIAS)))){
	        		alias.put(skuInfo.get(ProductAttribute.SKU_COLOR).toString(), skuInfo.get(ProductAttribute.COLOR_ALIAS).toString());
	        	}
	        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SIZE_ALIAS)))){
	        		alias.put(skuInfo.get(ProductAttribute.SKU_SIZE).toString(), skuInfo.get(ProductAttribute.SIZE_ALIAS).toString());
	        	}
	        }
	        for (Map.Entry<String, String> entry : alias.entrySet()) {
	        	propAlias.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
			}
	        if(StringUtils.hasText(propAlias)){
	        	 request.setPropertyAlias(propAlias.toString());
	        }
	        request.setSkuPrices(skuPrice.substring(0, skuPrice.length() - 1));
	        request.setSkuOuterIds(skuOuterIds.substring(0, skuOuterIds.length() - 1));
	        request.setSkuQuantities(skuQuantitys.substring(0, skuQuantitys.length() - 1));
	        skuProperties.deleteCharAt(skuProperties.lastIndexOf(","));
	        LOGGER.info("sku property:" + skuProperties.toString());
	        request.setSkuProperties(skuProperties.toString());
		}
		
		// 商品属性
		@SuppressWarnings("unchecked")
		Map<String, String> keyProperties = (Map<String, String>)product.get(ProductAttribute.PROPERTIES);
		if (!CollectionUtils.isEmpty(keyProperties)) {
			StringBuilder propertyBuilder = new StringBuilder();
			for (Map.Entry<String, String> entry : keyProperties.entrySet()) {
				propertyBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
			}
			propertyBuilder.append(skuProperties.toString().replace(',', ';'));
			LOGGER.info("product property:" + propertyBuilder.toString());
			request.setProps(propertyBuilder.toString());
		}
		LOGGER.info("==============start create product:" + DateUtil.format(new Date()) + "================");
		ItemAddResponse response = connectorManager.getConnector().execute(request);
		Long channelProductId = response.getItem().getNumIid();
		uploadPropPic(channelProductId, propPicture);
		LOGGER.info("==============end create product:" + DateUtil.format(new Date()) + "================");
		
		String channelProductIdStr = channelProductId.toString();
		
		//渠道商品id返回后端系统 add by ywu@wuxicloud.com at 2016-06-17
    	product.put(ProductAttribute.CHANNEL_PRODUCT_ID, channelProductIdStr);
		
		ProductPartnumberMapping productPartnumber = new ProductPartnumberMapping();
		productPartnumber.setApplicationId(applicationId);
		productPartnumber.setOuterId(product.get(ProductAttribute.OUTER_ID).toString());
		productPartnumber.setChannelProductId(channelProductIdStr);
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
			LOGGER.error("淘宝 product partnumber mapping error,channelProductId = {}, outerId = {}", channelProductIdStr, product.get(ProductAttribute.OUTER_ID).toString());
		}
     	Boolean flag = true;
     	String errorStr=null;
     	String errorPicPath = null;
    	if (!StringUtils.isEmpty(product.get(ProductAttribute.IMAGE_0))) {
    		String image0 = product.get(ProductAttribute.IMAGE_0).toString();
    		try {
    			picId = uploadPicture(channelProductId, image0, 1L, -1L);
    			insertPicProp(applicationId, channelProductIdStr, picId.toString(),1);
			} catch (Exception e) {
				LOGGER.error("", e);
				flag=false;
				errorStr=e.getMessage();
				errorPicPath = image0;
				insertPicProp(applicationId, channelProductIdStr, null,1);
			}
		}
    	if (!StringUtils.isEmpty(product.get(ProductAttribute.IMAGE_1))) {
    		String image1 = product.get(ProductAttribute.IMAGE_1).toString();
    		try {
				picId = uploadPicture(channelProductId, image1, 2L, -1L);
				insertPicProp(applicationId, channelProductIdStr, picId.toString(),2);
    		}catch (Exception e) {
    			LOGGER.error("", e);
    			flag=false;
    			errorStr=e.getMessage();
    			errorPicPath = image1;
    			insertPicProp(applicationId, channelProductIdStr, null,2);
    		}
		}
    	if (!StringUtils.isEmpty(product.get(ProductAttribute.IMAGE_2))) {
    		String image2 = product.get(ProductAttribute.IMAGE_2).toString();
    		try {
				picId = uploadPicture(channelProductId, image2, 3L, -1L);
				insertPicProp(applicationId, channelProductIdStr, picId.toString(),3);
    		}catch (Exception e) {
    			LOGGER.error("", e);
    			flag=false;
    			errorStr=e.getMessage();
    			errorPicPath = image2;
    			insertPicProp(applicationId, channelProductIdStr,null,3);
    		}
		}
    	if (!StringUtils.isEmpty(product.get(ProductAttribute.IMAGE_3))) {
    		String image3 = product.get(ProductAttribute.IMAGE_3).toString();
    		try {
				picId = uploadPicture(channelProductId, image3, 4L, -1L);
				insertPicProp(applicationId, channelProductIdStr, picId.toString(),4);
    		}catch (Exception e) {
    			LOGGER.error("", e);
    			flag=false;
    			errorStr=e.getMessage();
    			errorPicPath = image3;
    			insertPicProp(applicationId, channelProductIdStr,null,4);
    		}
		}
    	if(!flag){
    		throw new ImageUploadException(errorPicPath,errorStr);
    	}
    	LOGGER.info("==============end:" + DateUtil.format(new Date()) + "================");
	}
	
	@Override
	public void updateProduct(Map<String,Object> product)
			throws ProductException {
		LOGGER.info("==============start:" + DateUtil.format(new Date()) + "================");
		if (!product.containsKey(ProductAttribute.OUTER_ID)) {
			throw new PropertyRequireException("外部商家Id");
		}
		Long numiid=getItemId(product.get(ProductAttribute.OUTER_ID).toString(),0);
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID); //渠道的applicationId
		//CategoryMapping categoryMapping = null ; //主目录
		 Long cid=0L;
		 ItemUpdateRequest request=new ItemUpdateRequest();
		 if (null == numiid) {
			 throw new MappingNotFoundException("商品ID", product.get(ProductAttribute.OUTER_ID).toString());
		 }
		 request.setNumIid(numiid);
		 if (!StringUtils.isEmpty(product.get(ProductAttribute.CATEGORY_ID))) {
			 	//update by ywu 2016-06-21 传过来的直接是平台id,无需再转换
				String categoryId = product.get(ProductAttribute.CATEGORY_ID).toString();
				/*categoryMapping = productAttributeService.getCategoryMapping(Long.parseLong(categoryId), applicationId);
				if (categoryMapping == null) {
					throw new MappingNotFoundException("类目",categoryId);
				} else {
					cid = categoryMapping.getChannelCategoryId();//获取渠道类目的id
					request.setCid(cid); //类目id
				}*/
				cid = Long.valueOf(categoryId);//获取渠道类目的id
				request.setCid(cid); //类目id
			} else {
				 throw new PropertyRequireException("系统目录");
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.QUANTIRY))) {	//数量
				String quantity = product.get(ProductAttribute.QUANTIRY).toString();
				request.setNum(Long.parseLong(quantity));
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.SALES_PRICE)))  {	// 价格
				String price = product.get(ProductAttribute.SALES_PRICE).toString();
				request.setPrice(price.toString());
			} 
			if (!StringUtils.isEmpty(product.get(ProductAttribute.STUFF_STATUS))) {	// 新旧程度
				String stuffStatus = product.get(ProductAttribute.STUFF_STATUS).toString();
				request.setStuffStatus(stuffStatus);
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.TITLE))) {		//商品标题
				String title = product.get(ProductAttribute.TITLE).toString();
				request.setTitle(title);
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.APPROVE_STATUS))) {		//商品状态
				String approvestatus=product.get(ProductAttribute.APPROVE_STATUS).toString();
				request.setApproveStatus(approvestatus);
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.DESCRIPTION)))  {		//描述字数要大于5个字符，小于25000个字符，受违禁词控制
				String description = product.get(ProductAttribute.DESCRIPTION).toString();
				request.setDesc(description);
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.LOCATION_STATE))) {	//省
				String province = product.get(ProductAttribute.LOCATION_STATE).toString();
				request.setLocationState(province);
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.LOCATION_CITY))) {	//市
				String city = product.get(ProductAttribute.LOCATION_CITY).toString();
				request.setLocationCity(city);
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.FREIGHT_PAYER))) {	//运费承担方式
				String freightPayer = product.get(ProductAttribute.FREIGHT_PAYER).toString();
				request.setFreightPayer(freightPayer);
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.POSTAGE_ID))) {
				String postageId = product.get(ProductAttribute.POSTAGE_ID).toString();
				request.setPostageId(Long.parseLong(postageId));
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.SUB_STOCK))) {
				String substock = product.get(ProductAttribute.SUB_STOCK).toString();//拍下减库存
				request.setSubStock(Long.parseLong(substock));
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.OUTER_ID))) {		// 外部商家编码ID
				String outerId = product.get(ProductAttribute.OUTER_ID).toString();
				request.setOuterId(outerId);
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.SHOWCASE))) {		// 是否橱窗推荐
				request.setHasShowcase(Boolean.getBoolean(product.get(ProductAttribute.SHOWCASE).toString()));
			}
			if (!StringUtils.isEmpty(product.get(ProductAttribute.MAIN_IMAGE))) {		// 主图
				String image = product.get(ProductAttribute.MAIN_IMAGE).toString();
				if (StringUtils.hasText(image)) {
					try {
						LOGGER.info("==============start translate main img:" + DateUtil.format(new Date()) + "================");
						byte[] imgBytes = ImageUtil.image2Bytes(image);
						LOGGER.info("==============end translate main img:" + DateUtil.format(new Date()) + "================");
						FileItem fItem = new FileItem(ImageUtil.toFileName(image), imgBytes);
						request.setImage(fItem);
					} catch (Exception e) {
						LOGGER.error("", e);
						throw new ProductException("exception.product.img.error", product.get(ProductAttribute.OUTER_ID).toString(), image);
					}
				}
			}
			//库存扣减方式
			if (!StringUtils.isEmpty(product.get(ProductAttribute.SUB_STOCK))) {
				String subStock = product.get(ProductAttribute.SUB_STOCK).toString();
				request.setSubStock(Long.valueOf(subStock));
			}
			Map<String,String> propPicture=new HashMap<String, String>();
			Map<String,String> alias=new HashMap<String, String>();
			//查询sku销售属性及对应的值
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> skuInfos = (List<Map<String, Object>>)product.get(ProductAttribute.SKU);
			StringBuilder skuProperties = new StringBuilder();
			if (CollectionUtil.isNotEmpty(skuInfos)) {
				StringBuilder skuPrice = new StringBuilder();
			    StringBuilder skuQuantitys = new StringBuilder();
			    StringBuilder skuOuterIds = new StringBuilder();
		        for (Map<String,Object> skuInfo : skuInfos) {
		        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_PRICE)))){
		        		skuPrice.append(skuInfo.get(ProductAttribute.SKU_PRICE).toString()).append(",");
		        	}else{
		        		throw new PropertyRequireException("SKU价格");
		        	}
		        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_QUANTITY)))){
		        		skuQuantitys.append(skuInfo.get(ProductAttribute.SKU_QUANTITY).toString()).append(",");
		        	}else{
		        		throw new PropertyRequireException("SKU库存");
		        	}
		        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_OUTER_ID)))){
		        		skuOuterIds.append(skuInfo.get(ProductAttribute.SKU_OUTER_ID).toString()).append(",");
		        	}else{
		        		throw new PropertyRequireException("SKU外部Id");
		        	}
		        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_COLOR)))){
		        		skuProperties.append(skuInfo.get(ProductAttribute.SKU_COLOR).toString()).append(";");
		        	}else{
		        		throw new PropertyRequireException("SKU颜色");
		        	}
		        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SKU_SIZE)))){
		        		skuProperties.append(skuInfo.get(ProductAttribute.SKU_SIZE).toString()).append(";");
		        	}
		        	skuProperties.deleteCharAt(skuProperties.lastIndexOf(";")).append(",");
		        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.PROPERTY_IMAGE)))){
		        		@SuppressWarnings("unchecked")
						List<String> picPath = (List<String>)skuInfo.get(ProductAttribute.PROPERTY_IMAGE);
			        	propPicture.put(skuInfo.get(ProductAttribute.SKU_COLOR).toString(), picPath.get(0));
		        	}
		        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.COLOR_ALIAS)))){
		        		alias.put(skuInfo.get(ProductAttribute.SKU_COLOR).toString(), skuInfo.get(ProductAttribute.COLOR_ALIAS).toString());
		        	}
		        	if(!StringUtils.isEmpty((skuInfo.get(ProductAttribute.SIZE_ALIAS)))){
		        		alias.put(skuInfo.get(ProductAttribute.SKU_SIZE).toString(), skuInfo.get(ProductAttribute.SIZE_ALIAS).toString());
		        	}
		        }
		        StringBuilder propAlias=new StringBuilder();
		        for (Map.Entry<String, String> entry : alias.entrySet()) {
		        	propAlias.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
				}
		        if(StringUtils.hasText(propAlias)){
		        	 request.setPropertyAlias(propAlias.toString());
		        }
		        request.setSkuPrices(skuPrice.substring(0, skuPrice.length() - 1));
		        request.setSkuOuterIds(skuOuterIds.substring(0, skuOuterIds.length() - 1));
		        request.setSkuQuantities(skuQuantitys.substring(0, skuQuantitys.length() - 1));
		        skuProperties.deleteCharAt(skuProperties.lastIndexOf(","));
		        LOGGER.info("sku property:" + skuProperties.toString());
		        request.setSkuProperties(skuProperties.toString());
			}
			
			// 商品属性
			@SuppressWarnings("unchecked")
			Map<String, String> keyProperties = (Map<String, String>)product.get(ProductAttribute.PROPERTIES);
			if (!CollectionUtils.isEmpty(keyProperties)) {
				StringBuilder propertyBuilder = new StringBuilder();
				for (Map.Entry<String, String> entry : keyProperties.entrySet()) {
					propertyBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
				}
				propertyBuilder.append(skuProperties.toString().replace(',', ';'));
				LOGGER.info("product property:" + propertyBuilder.toString());
				request.setProps(propertyBuilder.toString());
			}
			LOGGER.info("==============start create product:" + DateUtil.format(new Date()) + "================");
			connectorManager.getConnector().execute(request);
			uploadPropPic(numiid,propPicture);
			LOGGER.info("==============end create product:" + DateUtil.format(new Date()) + "================");
			List<ChannelPictureMapping> channelPictureMappings = this.getPictureList(numiid.toString(), applicationId,true);
			Long[] ids = new Long[]{-1L,-1L,-1L,-1L};
			if(channelPictureMappings.size() > 0){
				for(ChannelPictureMapping channelPictureMapping:channelPictureMappings){
					if(!StringUtils.hasText(channelPictureMapping.getImageId())){
						ids[channelPictureMapping.getSort()-1]=-1L;
					}else{
						ids[channelPictureMapping.getSort()-1]=Long.parseLong(channelPictureMapping.getImageId());
					}
				}
			}
			Long picId = 0L;
			Boolean flag = true;
			String errorStr=null;
			String errorPicPath = null;
		    if (!StringUtils.isEmpty(product.get(ProductAttribute.IMAGE_0))) {
		    	String image0 = product.get(ProductAttribute.IMAGE_0).toString();
		    	try {
		    		 picId=uploadPicture(numiid, image0,1L,ids[0]);
		    		 updatePicProp(applicationId, numiid.toString(), picId.toString(),1);
				} catch (Exception e) {
					LOGGER.error("", e);
					flag=false;
					errorStr=e.getMessage();
					errorPicPath = image0;
				}
			} 
		     if (!StringUtils.isEmpty(product.get(ProductAttribute.IMAGE_1))) {
		    	 String image1 =product.get(ProductAttribute.IMAGE_1).toString();
		    	 try {
		    		 picId=uploadPicture(numiid,image1,2L,ids[1]);
		    		 updatePicProp(applicationId, numiid.toString(), picId.toString(),2);
		    	 } catch (Exception e) {
		    		 LOGGER.error("", e);
					 flag=false;
					 errorStr=e.getMessage();
					 errorPicPath = image1;
				}
			}
		    if (!StringUtils.isEmpty(product.get(ProductAttribute.IMAGE_2))) {
		    	String image2 = product.get(ProductAttribute.IMAGE_2).toString();
		    	 try {
		    		 picId=uploadPicture(numiid, image2,3L,ids[2]);
		    		 updatePicProp(applicationId, numiid.toString(), picId.toString(),3);
		    	 } catch (Exception e) {
		    		 LOGGER.error("", e);
		    		 flag=false;
		    		 errorStr=e.getMessage();
		    		 errorPicPath = image2;
		    	 }
			}
		    if (!StringUtils.isEmpty(product.get(ProductAttribute.IMAGE_3))) {
		    	String image3 = product.get(ProductAttribute.IMAGE_3).toString();
		    	 try {
		    		 picId=uploadPicture(numiid, image3,4L,ids[3]);
		    		 updatePicProp(applicationId, numiid.toString(), picId.toString(),4);
		    	 } catch (Exception e) {
		    		LOGGER.error("", e);
					flag=false;
					errorStr=e.getMessage();
					errorPicPath = image3;
		    	 }
			}
		    if(!flag){
	    		throw new ImageUploadException(errorPicPath,errorStr);
	    	}
	}
	/**
	 * 更新价格
	 */
	@Override
	public void updatePrice(Product product) throws ProductException {
		Long numiid=null;
		if(StringUtils.hasText(product.getOuterProductId())){
			numiid = getItemId(product.getOuterProductId(),0);
		}else{
			throw new PropertyRequireException("商品外部编码");
		}
		ItemPriceUpdateRequest updateReq=new ItemPriceUpdateRequest();
		if(null!=numiid){
			updateReq.setNumIid(numiid);
		}
		if (product.getPrice() != null) {
			updateReq.setPrice(product.getPrice().toString());
		} else {
			throw new PropertyRequireException("商品的价格");
		}
		if(!CollectionUtil.isEmpty(product.getSkuList())){
			StringBuilder skuPrices= new StringBuilder();
			StringBuilder skuProps=new StringBuilder();
			StringBuilder skuquantities=new StringBuilder();
			StringBuilder skuoutids=new StringBuilder();
			for(Sku sku:product.getSkuList()){
				if(sku.getPrice() == null){
					throw new PropertyRequireException("SKU价格");
				}
				List<com.taobao.api.domain.Sku> skuList=productServiceImpl.getChannelSkusBySkuOuterId(sku.getOuterSkuId());
//				if(CollectionUtil.isEmpty(skuList)){
//					throw new ProductException("exception.product.sku.duplicate.outerid",sku.getOuterSkuId());
//				}
				if(!CollectionUtil.isEmpty(skuList)){
					for(int i=0;i<skuList.size();i++){ 
						if(skuList.get(i).getNumIid().longValue() == numiid.longValue()){
							skuPrices.append(sku.getPrice()).append(",");
							skuProps.append(skuList.get(i).getProperties()).append(",");
							skuquantities.append(skuList.get(i).getQuantity()).append(",");
							skuoutids.append(skuList.get(i).getOuterId()).append(",");
						}
					}
				}else{
					LOGGER.info("==============not existed skuOutid:" + sku.getOuterSkuId());
				}
			}
			if(!StringUtils.hasText(skuquantities)||!StringUtils.hasText(skuoutids)||!StringUtils.hasText(skuProps)){
				throw new ProductException("exception.product.notexist", numiid.toString());
			}
			updateReq.setSkuQuantities(skuquantities.substring(0, skuquantities.length()-1));
			updateReq.setSkuOuterIds(skuoutids.substring(0, skuoutids.length()-1));
			updateReq.setSkuPrices(skuPrices.substring(0, skuPrices.length()-1));
			updateReq.setSkuProperties(skuProps.substring(0, skuProps.length()-1));
		}
	    connectorManager.getConnector().execute(updateReq);
	}
	
	/**
	 * 获取商品ID
	 */
	private Long getItemId(String outid, Integer type) throws ProductException {
		return productServiceImpl.getItemId(outid, type);
	}
	
	@Override
	public List<ItemCat> getChannelCategory(long parentCid) {
		ItemcatsGetRequest request =new ItemcatsGetRequest();
		request.setFields("cid, parent_cid, name, is_parent, status");
		request.setParentCid(parentCid);
		ItemcatsGetResponse response = connectorManager.getConnector().execute(request);
		return response.getItemCats();
	}

	/**
	 * 将图片ID插入本地库
	 */
	public void insertPicProp(Integer applicationId,String itemId,String picId,Integer sort) throws ProductException{
		try{
			 ChannelPictureMapping cMapping=new ChannelPictureMapping();
			 cMapping.setApplicationId(applicationId);
			 cMapping.setChannelProductId(itemId);
			 cMapping.setImageId(picId);
			 cMapping.setSort(sort);
			 this.insertPicture(cMapping);
		}catch(Exception e){
			throw new ProductException("exception.product.imgfail");
		}
    	
	}
	/**
	 * 更新本地图片信息
	 */
	public void updatePicProp(Integer applicationId,String itemId,String picId,Integer sort) throws ProductException{
		try{
    	 ChannelPictureMapping cMapping=new ChannelPictureMapping();
		 cMapping.setApplicationId(applicationId);
		 cMapping.setChannelProductId(itemId);
		 cMapping.setImageId(picId);
		 cMapping.setSort(sort);
		 this.insertPicture(cMapping);
		}catch(Exception e){
			throw new ProductException("exception.product.imgfail");
		}
	}
	/**
	 * 上传图片
	 */
	private Long uploadPicture(Long numiid,String path,Long sort,Long id){
			ItemJointImgRequest req=new ItemJointImgRequest();
			req.setNumIid(numiid);
			URI uri=URI.create(path);
			path=uri.getPath();
			req.setPicPath(path.substring(10,path.length()));
			req.setPosition(sort);
			if(id!=-1L){
				req.setId(id);	
			}
			ItemJointImgResponse response =connectorManager.getConnector().execute(req);
			return response.getItemImg().getId();
 	}

	@Override
	public List<TaobaoChannelAttributeMapping> getTaobaoChannelAttributeMappings(
			Map<String, Object> params) {
		return taobaoChannelAttributeMappingDao.list(params);
	}

	@Override
	public long getTaobaoChannelAttributeMappingsCount(Map<String, Object> params) {
		return taobaoChannelAttributeMappingDao.count(params);
	}

	@Override
	public List<ItemPropWrapper> getChannelAttribute(long categoryId) {
		ItempropsGetRequest request = new ItempropsGetRequest();
		request.setFields("pid,cid,is_key_prop,is_sale_prop,is_color_prop,is_enum_prop,is_item_prop,name,must,multi,status,is_input_prop,type,required");
		request.setCid(categoryId);
		ItempropsGetResponse response = connectorManager.getConnector().execute(request);
		List<ItemProp> itemProps = response.getItemProps();
		List<ItemPropWrapper> itemPropsWrapper = new ArrayList<ItemPropWrapper>(itemProps.size());
		for (ItemProp itemProp : itemProps) {
			itemProp.setCid(categoryId);
			itemPropsWrapper.add(new ItemPropWrapper(itemProp));
		}
		return itemPropsWrapper;
	}
	
	@Override
	public List<PropValue> getChannelAttributeValue(long cid,long pid) {
		ItempropsGetRequest request = new ItempropsGetRequest();
		request.setFields("prop_values");
		request.setCid(cid);
		request.setPid(pid);
		ItempropsGetResponse response = connectorManager.getConnector().execute(request);
		List<ItemProp> itemProps = response.getItemProps();
		if (!CollectionUtil.isEmpty(itemProps)) {
			ItemProp itemProp = itemProps.get(0);
			return itemProp.getPropValues();
		}
		return null;
	}

	@Override
	public ProductCategoryMapping getProductCategoryMapping(
			Long channelCategoryId, Integer applicationId) {
		return productServiceImpl.getProductCategoryMapping(channelCategoryId, applicationId);
	}

	@Override
	public ChannelAttributeMapping getChannelAttributeMapping(
			Long channelCategoryId, String attributeCode, Integer applicationId) {
		return productServiceImpl.getChannelAttributeMapping(channelCategoryId, attributeCode, applicationId);
	}

	@Override
	public String getOuterProductIdByChannelProductId(String channelProductId)
			throws ProductException {
		return productServiceImpl.getOuterProductIdByChannelProductId(channelProductId);
	}

	@Override
	public String getOuterSkuIdByChannelSkuId(String channelSkuId,
			String channelProductId) throws ProductException {
		return productServiceImpl.getOuterSkuIdByChannelSkuId(channelSkuId, channelProductId);
	}

	@Override
	public void updateSkuInventory(Sku sku) throws ProductException {
		productServiceImpl.updateSkuInventory(sku);
	}

	@Override
	public void updateSkuInventoryBatch(List<Sku> skus) throws ProductException {
		productServiceImpl.updateSkuInventoryBatch(skus);
	}

	@Override
	public void updateSkuInventoryBatch(Product product)
			throws ProductException {
		productServiceImpl.updateSkuInventoryBatch(product);
	}

	@Override
	public void onShelf(Product product) throws ProductException {
		productServiceImpl.onShelf(product);
	}

	@Override
	public void offShelf(Product product) throws ProductException {
		productServiceImpl.offShelf(product);
	}

	@Override
	public int saveChannelAttributeMapping(
			TaobaoChannelAttributeMapping attributeMapping) {
		return taobaoChannelAttributeMappingDao.saveChannelAttributeMapping(attributeMapping);
	}

	@Override
	public int updateChannelAttributeMapping(
			TaobaoChannelAttributeMapping attributeMapping) {
		return taobaoChannelAttributeMappingDao.updateChannelAttributeMapping(attributeMapping);
	}

	@Override
	public int deleteChannelAttributeMapping(int id) {
		return taobaoChannelAttributeMappingDao.deleteChannelAttributeMapping(id);
	}

	@Override
	public List<TmallItemPropWrapper> getItemPropWrappers(long categoryId) {
		return null;
	}

	@Override
	public List<TmallItemPropValWrapper> getItemPropValWrappers(
			long categoryId, String pid) {
		return null;
	}

	@Override
	public int saveTmallAttr(ChannelAttributeMapping channelAttributeMapping) {
		return 0;
	}

	@Override
	public int updateTmallAttr(ChannelAttributeMapping channelAttributeMapping) {
		return 0;
	}

	@Override
	public int deleteTmallAttr(int attrid) {
		return 0;
	}

	@Override
	public List<ChannelAttributeMapping> getChannelAttributeMappings(
			Map<String, Object> params) {
		return null;
	}

	@Override
	public long getChannelAttributeMappingsCount(Map<String, Object> params) {
		return 0;
	}

	@Override
	public List<DeliveryTemplate> getDeliveryTemplates() {
		return productServiceImpl.getDeliveryTemplates();
	}

	@Override
	public String getChannelProductIdByOuterProductId(String outerProductId) {
		return productServiceImpl.getChannelProductIdByOuterProductId(outerProductId);
	}

	@Override
	public String getChannelSkuIdByOuterSkuId(String outerSkuId) {
		return productServiceImpl.getChannelSkuIdByOuterSkuId(outerSkuId);
	}

	/**
	 * 获取渠道属性
	 */
	@Override
	public Category getChannelProperty(Category category)
			throws ProductException {
		//Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
//		CategoryMapping categoryMapping = productAttributeService.getCategoryMapping(Long.parseLong(category.getCategoryId()), applicationId);//渠道主目录映射
//		if (categoryMapping == null) {
//			throw new MappingNotFoundException("类目",category.getCategoryId()); 
//		}
//		category.setChannelCategoryId(categoryMapping.getChannelCategoryId().toString());
		ItempropsGetRequest req = new ItempropsGetRequest();
		req.setFields("pid,name,must,is_key_prop,is_sale_prop,is_color_prop,is_enum_prop,prop_values");
		req.setCid(Long.parseLong(category.getChannelCategoryId()));
		ItempropsGetResponse response = connectorManager.getConnector().execute(req);
		String taobaoSizeProperty = registry.get("taobaoSizeProperty");
		String brandkey = registry.get("brandKey");
		String brandvalue = registry.get("brandValue");
		String[] brandvalues=null;
		if(StringUtils.hasText(brandvalue)){
			brandvalues=brandvalue.split(",");
		}
		if(!CollectionUtil.isEmpty(response.getItemProps())){
			for (ItemProp itemProp : response.getItemProps()) {
				Attribute attribute = new Attribute();
				String channelAttributeId = itemProp.getPid().toString();
				attribute.setAttributeId(channelAttributeId);
				attribute.setAttributeName(itemProp.getName());
				if (itemProp.getIsColorProp()) {
					attribute.setColorProperty(true);
				}
				if (itemProp.getIsKeyProp()) {
					attribute.setKeyProperty(true);
				}
				if (itemProp.getIsSaleProp()) {
					attribute.setSalesProperty(true);
				}
				if (itemProp.getMust()) {
					attribute.setRequired(true);
				}
				if (itemProp.getIsEnumProp()) {
					attribute.setType(Attribute.ATTRIBUTE_TYPE_SELECT);
				} else {
					attribute.setType(Attribute.ATTRIBUTE_TYPE_INPUT);
				}
				//size
				if (StringUtils.hasText(taobaoSizeProperty)) {
					String[] taobaoSizePropertyArray = taobaoSizeProperty.split(",");
					for (int i = 0; i < taobaoSizePropertyArray.length; i++) {
						if (channelAttributeId.equals(taobaoSizePropertyArray[i])) {
							attribute.setSizeProperty(true);
							attribute.setSalesProperty(true);
						}
					}
				}
				if (CollectionUtil.isNotEmpty(itemProp.getPropValues())) {
					if(StringUtils.hasText(brandkey)&&null!=brandvalues&&brandkey.equals(channelAttributeId)){
						for (PropValue propValue : itemProp.getPropValues()) {
							for(int i=0;i<brandvalues.length;i++){
								if(propValue.getVid().toString().equals(brandvalues[i])){
									AttributeValue attributeValue = new AttributeValue();
									attributeValue.setId(propValue.getVid().toString());
									attributeValue.setName(propValue.getName());
									attribute.addAttributeValue(attributeValue);
								}
							}
						}
					}else{
						for (PropValue propValue : itemProp.getPropValues()) {
							AttributeValue attributeValue = new AttributeValue();
							attributeValue.setId(propValue.getVid().toString());
							attributeValue.setName(propValue.getName());
							attribute.addAttributeValue(attributeValue);
						}
					}
				}
				category.addAttribute(attribute);
			}
		}
		return category;
	}
	/**
	 * 上传属性图片
	 */
	public void uploadPropPic(Long numiid,Map<String,String> propMap) throws ImageUploadException {
		for (Map.Entry<String, String> entry : propMap.entrySet()) {
			try {
				ItemJointPropimgRequest req = new ItemJointPropimgRequest();
				req.setProperties(entry.getKey());
				URI uri = URI.create(entry.getValue());
				String path = uri.getPath();
				req.setPicPath(path.substring(10,path.length()));
				req.setNumIid(numiid);
				connectorManager.getConnector().execute(req);
			} catch (Exception e) {
				//e.printStackTrace();
				LOGGER.error("淘宝图片关联失败", e);
				throw new ImageUploadException(entry.getValue(),e.getMessage());
			}
		}
	}
	
	@Override
	public void insertPicture(ChannelPictureMapping cMapping)
			throws ProductException {
		try {
			channelPictureMappingDao.insertPicture(cMapping);
		} catch (SQLException e) {
			//e.printStackTrace();
			LOGGER.error("图片插入失败", e);
		}
	}

	@Override
	public List<ChannelPictureMapping> getPictureList(String productId,
			Integer applicationId,Boolean uploadSucess) {
		return channelPictureMappingDao.getPictureList(productId, applicationId, uploadSucess);
	}

	@Override
	public void updatePicture(ChannelPictureMapping cMapping)
			throws ProductException {
		try {
			channelPictureMappingDao.updatePicture(cMapping);
		} catch (SQLException e) {
			//e.printStackTrace();
			LOGGER.error("图片更新失败", e);
		}
	}

	@Override
	public Long getChannelProductId(Long categoryId, String properties) {
		return null;
	}

	@Override
	public void saveProductCategoryMapping(ProductCategoryMapping mapping) {
		
	}

	@Override
	public TaobaoChannelAttributeMapping getTaobaoChannelAttributeMapping(
			Long channelCategoryId, String channelAttrId, Integer applicationId) {
		TaobaoChannelAttributeMapping mapping = null;
		try {
			mapping = taobaoChannelAttributeMappingDao.getTaobaoChannelAttributeMapping(channelCategoryId, channelAttrId, applicationId);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error("淘宝渠道属性映射失败", e);
		}
		return mapping;
	}

	@Override
	public List<Product> getTotalProducts(Map<String, Object> params) {
		return productServiceImpl.getTotalProducts(params);
	}

	@Override
	public List<Product> getOnSaleProducts(Map<String, Object> params) {
		return productServiceImpl.getOnSaleProducts(params);
	}

	@Override
	public List<Product> getInstockProducts(Map<String, Object> params) {
		return productServiceImpl.getInstockProducts(params);
	}

	@Override
	public Map<String,String> isRepeatOuterId(Product productInfo) {
		return productServiceImpl.isRepeatOuterId(productInfo);
	}

	@Override
	public AttributeMapping getAttrMapping(Long cid, String channelKey) {
		return null;
	}

	@Override
	public List<CategoryList> getAllCategory() throws ProductException {
		ItemcatsGetRequest req = new ItemcatsGetRequest();
		List<CategoryList> categoryLists = new ArrayList<CategoryList>();
		req.setFields("cid,parent_cid,name,is_parent");
		//只抓指定目录
		String taobaoIncludeCatStr = registry.get(TaobaoConstant.KEY_TAOBAO_INCLUDE_CAT);
		List<Long> taobaoIncludeCatIds = null;
		if (StringUtils.hasText(taobaoIncludeCatStr)) {
			String[] taobaoIncludeCatArray = taobaoIncludeCatStr.split(",");
			taobaoIncludeCatIds = new ArrayList<Long>(taobaoIncludeCatArray.length);
			for (String taobaoIncludeCat : taobaoIncludeCatArray) {
				try {
					taobaoIncludeCatIds.add(Long.valueOf(taobaoIncludeCat.trim()));
				} catch (NumberFormatException e) {
					LOGGER.error("", e);
				}
			}
		}
		req.setParentCid(0L);
		ItemcatsGetResponse response = connectorManager.getConnector().execute(req);
		for(ItemCat itemcat : response.getItemCats()){
			if (!CollectionUtils.isEmpty(taobaoIncludeCatIds)) {
				for (Long taobaoIncludeCatId : taobaoIncludeCatIds) {
					if (itemcat.getCid().longValue() == taobaoIncludeCatId.longValue()) {
						CategoryList category = new CategoryList();
						setValue(category,itemcat);
						categoryLists.add(category);
					}
				}
			} else {
				CategoryList category = new CategoryList();
				setValue(category,itemcat);
				categoryLists.add(category);
			}
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
		throw new RuntimeException("illegal call");
	}

	@Override
	public void BindItemStore(Product product) throws ProductException {
		productServiceImpl.BindItemStore(product);
	}

	@Override
	public String getOuterIdbySkuIdandNick(String skuId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String qimenStoreInventoryItemInitial(StoreItems storeItems) throws ProductException {
		return productServiceImpl.qimenStoreInventoryItemInitial(storeItems);
	}


	
}
