package net.chinacloud.mediator.jingdong.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.CategoryMappingDao;
import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.domain.CategoryList;
import net.chinacloud.mediator.domain.DeliveryTemplate;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductAttribute;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.product.ImageUploadException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.exception.product.PropertyRequireException;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.jingdong.dao.ChannelAttributeMappingDao;
import net.chinacloud.mediator.jingdong.domain.ChannelAttributeMapping;
import net.chinacloud.mediator.jingdong.service.JingdongProductService;
import net.chinacloud.mediator.service.ProductAttributeService;
import net.chinacloud.mediator.system.application.dao.ApplicationDao;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.DateUtil;
import net.chinacloud.mediator.utils.ImageUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.open.api.sdk.FileItem;
import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.domain.ware.WarePropimg;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeSearchRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeValueSearchRequest;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.request.ware.SkuCustomGetRequest;
import com.jd.open.api.sdk.request.ware.WareAddRequest;
import com.jd.open.api.sdk.request.ware.WareDelistingGetRequest;
import com.jd.open.api.sdk.request.ware.WareGetRequest;
import com.jd.open.api.sdk.request.ware.WareListingGetRequest;
import com.jd.open.api.sdk.request.ware.WarePropimgAddRequest;
import com.jd.open.api.sdk.request.ware.WarePropimgDeleteRequest;
import com.jd.open.api.sdk.request.ware.WarePropimgsGetRequest;
import com.jd.open.api.sdk.request.ware.WareSkuGetRequest;
import com.jd.open.api.sdk.request.ware.WareSkuPriceUpdateRequest;
import com.jd.open.api.sdk.request.ware.WareSkuStockUpdateRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateDelistingRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateListingRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateRequest;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse.Attribute;
import com.jd.open.api.sdk.response.category.CategoryAttributeValueSearchResponse;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;
import com.jd.open.api.sdk.response.ware.SkuCustomGetResponse;
import com.jd.open.api.sdk.response.ware.WareAddResponse;
import com.jd.open.api.sdk.response.ware.WareDelistingGetResponse;
import com.jd.open.api.sdk.response.ware.WareGetResponse;
import com.jd.open.api.sdk.response.ware.WareListingGetResponse;
import com.jd.open.api.sdk.response.ware.WarePropimgAddResponse;
import com.jd.open.api.sdk.response.ware.WarePropimgDeleteResponse;
import com.jd.open.api.sdk.response.ware.WarePropimgsGetResponse;
import com.jd.open.api.sdk.response.ware.WareSkuGetResponse;
import com.jd.open.api.sdk.response.ware.WareSkuPriceUpdateResponse;
import com.jd.open.api.sdk.response.ware.WareSkuStockUpdateResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateDelistingResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateListingResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateResponse;

@Service("jingdongProductServiceImpl")
public class ProductServiceImpl implements JingdongProductService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProductServiceImpl.class);
	@Autowired
	ConnectorManager<JdRequest<?>> connectorManager;
	@Autowired
	ChannelAttributeMappingDao channelAttributeMappingDao;
	@Autowired
	ProductAttributeService productAttributeServiceImpl;
	@Autowired
	ApplicationDao applicationDao;
	@Autowired
	CategoryMappingDao categoryMappingDao;
	@Autowired
	ProductAttributeService productAttributeService;

	// 每次请求40条
	private Integer PAGE_COUNT = 40;

	@Override
	public String getOuterProductIdByChannelProductId(String channelProductId)
			throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		ProductPartnumberMapping mapping = null;
		try {
			mapping = productAttributeService
					.getProductPartnumberMappingByChannelId(channelProductId,
							applicationId, 0);
		} catch (Exception e1) {
			// e1.printStackTrace();
			LOGGER.error(e1.getMessage());
			mapping = null;
		}
		if (null != mapping) {
			return mapping.getOuterId();
		}
		WareGetRequest request = new WareGetRequest();
		request.setWareId(channelProductId);
		request.setFields("item_num");
		WareGetResponse response = connectorManager.getConnector().execute(
				request);
		Ware ware = response.getWare();
		if (null != ware && StringUtils.hasText(ware.getItemNum())) {
			try {
				mapping = new ProductPartnumberMapping();
				mapping.setApplicationId(applicationId);
				mapping.setChannelProductId(channelProductId);
				mapping.setOuterId(ware.getItemNum().trim());
				mapping.setType(0);
				productAttributeService.saveProductPartnumberMapping(mapping);
			} catch (Exception e) {
				// e.printStackTrace();
				LOGGER.error("京东商品外部编码映射失败", e);
			}
			return ware.getItemNum().trim();
		}
		return null;
	}

	@Override
	public String getOuterSkuIdByChannelSkuId(String channelSkuId,
			String channelProductId) throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		ProductPartnumberMapping mapping = productAttributeService
				.getProductPartnumberMappingByChannelId(channelSkuId,
						applicationId, 1);
		if (null != mapping) {
			return mapping.getOuterId();
		}
		com.jd.open.api.sdk.domain.ware.Sku sku = getSkuBySkuId(channelSkuId);
		if (null != sku && StringUtils.hasText(sku.getOuterId())) {
			try {
				mapping = new ProductPartnumberMapping();
				mapping.setApplicationId(applicationId);
				mapping.setChannelProductId(channelSkuId);
				mapping.setOuterId(sku.getOuterId().trim());
				mapping.setType(1);
				productAttributeService.saveProductPartnumberMapping(mapping);
			} catch (Exception e) {
				// e.printStackTrace();
				LOGGER.error("京东商品外部编码映射失败", e);
			}
			return sku.getOuterId().trim();
		}
		return null;
	}

	/**
	 * 根据skuId获得sku详细信息
	 * 
	 * @param skuId
	 * @return
	 * @throws OutboundRequestFailureException
	 */
	public com.jd.open.api.sdk.domain.ware.Sku getSkuBySkuId(String skuId)
			throws ProductException {
		WareSkuGetRequest request = new WareSkuGetRequest();
		request.setSkuId(skuId);
		// req.setFields("sku_id,ware_id,status,stock_num,jd_price,outer_id,created,color_value,size_value");
		request.setFields("sku_id,ware_id,status,stock_num,jd_price,outer_id,created");
		WareSkuGetResponse response = connectorManager.getConnector().execute(
				request);
		com.jd.open.api.sdk.domain.ware.Sku sku = response.getSku();
		return sku;
	}

	@Override
	public void updateSkuInventory(Sku sku) throws ProductException {
		WareSkuStockUpdateRequest request = new WareSkuStockUpdateRequest();
		String outId = sku.getOuterSkuId();
		if (StringUtils.hasText(outId)) {
			request.setOuterId(outId);
		} else {
			throw new PropertyRequireException("外部商品编号");
		}
		Double qtyCanSell = sku.getQtyCanSell();
		if (qtyCanSell == null) {
			throw new PropertyRequireException("全量库存");
		} else {
			request.setQuantity(String.valueOf(qtyCanSell.intValue()));
		}
		WareSkuStockUpdateResponse response = this.connectorManager
				.getConnector().execute(request);
		LOGGER.info("sku库存更新成功,更新时间:" + response.getModified() + "sku外部商品编号:"
				+ response.getOuterId());
	}

	@Override
	public void updateSkuInventoryBatch(List<Sku> skus) throws ProductException {
		// do not support
	}

	@Override
	public void updateSkuInventoryBatch(Product product)
			throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID); // 渠道的applicationId
		List<Sku> skus = product.getSkuList();
		String outerId = product.getOuterProductId(); // 外部商品编号
		if (!StringUtils.hasText(outerId)) {
			throw new PropertyRequireException("外部商品编号");
		}
		if (CollectionUtil.isEmpty(skus)) {
			throw new PropertyRequireException("sku列表信息");
		}
		ProductPartnumberMapping ppmapping = this.productAttributeServiceImpl
				.getProductPartnumberMappingByOuterId(outerId, applicationId, 0);
		String wareId = ppmapping.getChannelProductId(); // jd渠道的商品id
		WareUpdateRequest request = new WareUpdateRequest();
		request.setWareId(wareId); // jd渠道的商品id 必设条件
		StringBuilder sku_stocks = new StringBuilder();
		StringBuilder outer_id = new StringBuilder();
		StringBuilder sku_prices = new StringBuilder();
		StringBuilder sku_property = new StringBuilder();
		for (Sku s : skus) {
			/**
			 * 调用京东商品更新接口去更新sku的库存，由于该接口要求ku_prices、sku_properties、
			 * sku_stocks和outer_id四个参数的sku组数必须保持一致
			 * 所以调用了SkuCustomRequest去获取相关的sku_stock和sku_property数据
			 */
			String outSkuId = s.getOuterSkuId();
			Double qtyCanSell = s.getQtyCanSell();
			if (!StringUtils.hasLength(outSkuId)) {
				throw new PropertyRequireException("SKU外部ID");
			}
			if (qtyCanSell == null) {
				throw new PropertyRequireException("SKU价格");
			}
			SkuCustomGetRequest scgRequest = new SkuCustomGetRequest(); // 根据商家设定的sku的外部id获取所对应的sku数据，一个sku的外部id对应一个sku数据
			scgRequest.setOuterId(outSkuId);
			SkuCustomGetResponse scgResponse = this.connectorManager
					.getConnector().execute(scgRequest);
			com.jd.open.api.sdk.domain.ware.Sku sku = scgResponse.getSku();

			sku_stocks.append(s.getQtyAll().intValue()).append("|");
			outer_id.append(s.getOuterSkuId()).append("|");
			sku_prices.append(sku.getJdPrice()).append("|"); // 需要更新的价格
			sku_property.append(sku.getAttributes()).append("|"); // 通过接口获取的sku销售属性组合字符串
		}
		request.setOuterId(outer_id.substring(0, outer_id.length() - 1));
		request.setSkuStocks(sku_stocks.substring(0, sku_stocks.length() - 1));
		request.setSkuProperties(sku_property.substring(0,
				sku_property.length() - 1));
		request.setSkuPrices(sku_prices.substring(0, sku_prices.length() - 1));
		WareUpdateResponse response = this.connectorManager.getConnector()
				.execute(request);
		LOGGER.info("商品更新库存成功,商品的id:" + response.getWareId() + "外部商品编号id:"
				+ outerId + "更新时间:" + response.getModified());
	}

	@Override
	public void createProduct(Map<String, Object> product)
			throws ProductException {
		LOGGER.info("----------商品创建开始------------");
		WareAddRequest request = new WareAddRequest(); // 新增商品的方法名
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID); // 渠道的applicationId
		//CategoryMapping categoryMapping = null;
		//update by ywu 2016-06-21 这里传过来的直接是平台的类目id,无需再转换
		if (!product.containsKey(ProductAttribute.CATEGORY_ID)) {
			throw new PropertyRequireException("平台目录");
		}
		String channelCategoryId = product.get(ProductAttribute.CATEGORY_ID).toString();
		if (!StringUtils.hasText(channelCategoryId)) {
			throw new PropertyRequireException("平台目录");
		}
		/*categoryMapping = productAttributeServiceImpl.getCategoryMapping(
				Long.parseLong(categoryId), applicationId); // 通过系统id获取渠道类目id
		if (categoryMapping == null) {
			throw new MappingNotFoundException("类目", categoryId);
		} else {
			channelCategoryId = categoryMapping.getChannelCategoryId()
					.toString();// 获取渠道类目的id
			request.setCid(channelCategoryId); // 类目id
		}*/
		request.setCid(channelCategoryId); // 类目id

		if (!product.containsKey(ProductAttribute.TITLE)) {
			throw new PropertyRequireException("商品标题");
		}
		String title = product.get(ProductAttribute.TITLE).toString(); // 商品标题
		if (!StringUtils.hasText(title)) {
			throw new PropertyRequireException("商品标题");
		}
		request.setTitle(title);

		if (!product.containsKey(ProductAttribute.QUANTIRY)) {
			throw new PropertyRequireException("商品库存");
		}
		String stockNum = product.get(ProductAttribute.QUANTIRY).toString(); // 商品库存
		if (!StringUtils.hasText(stockNum)) {
			throw new PropertyRequireException("商品库存");
		}
		request.setStockNum(stockNum);

		if (!product.containsKey(ProductAttribute.LENGTH)) {
			throw new PropertyRequireException("长");
		}
		String length = product.get(ProductAttribute.LENGTH).toString(); // 长
		if (!StringUtils.hasText(length)) {
			throw new PropertyRequireException("长");
		}
		request.setLength(length);

		if (!product.containsKey(ProductAttribute.WIDTH)) {
			throw new PropertyRequireException("宽");
		}
		String wide = product.get(ProductAttribute.WIDTH).toString(); // 宽
		if (!StringUtils.hasText(wide)) {
			throw new PropertyRequireException("宽");
		}
		request.setWide(wide);

		if (!product.containsKey(ProductAttribute.HEIGHT)) {
			throw new PropertyRequireException("高");
		}
		String high = product.get(ProductAttribute.HEIGHT).toString(); // 高
		if (!StringUtils.hasText(high)) {
			throw new PropertyRequireException("高");
		}
		request.setHigh(high);

		if (!product.containsKey(ProductAttribute.WEIGHT)) {
			throw new PropertyRequireException("重量");
		}
		String weight = product.get(ProductAttribute.WEIGHT).toString(); // 重量
		if (!StringUtils.hasText(weight)) {
			throw new PropertyRequireException("重量");
		}
		request.setWeight(weight);

		if (!product.containsKey(ProductAttribute.LIST_PRICE)) {
			throw new PropertyRequireException("市场价");
		}
		String marketPrice = product.get(ProductAttribute.LIST_PRICE)
				.toString(); // 市场价
		if (!StringUtils.hasText(marketPrice)) {
			throw new PropertyRequireException("市场价");
		}
		request.setMarketPrice(marketPrice);

		if (!product.containsKey(ProductAttribute.SALES_PRICE)) {
			throw new PropertyRequireException("京东价");
		}
		String jdPrice = product.get(ProductAttribute.SALES_PRICE).toString(); // 京东价
		if (!StringUtils.hasText(jdPrice)) {
			throw new PropertyRequireException("京东价");
		}
		request.setJdPrice(jdPrice);

		if (!product.containsKey(ProductAttribute.DESCRIPTION)) {
			throw new PropertyRequireException("描述(最多支持3万个英文字符)");
		}
		String notes = product.get(ProductAttribute.DESCRIPTION).toString(); // 描述（最多支持3万个英文字符）
		if (!StringUtils.hasText(notes)) {
			throw new PropertyRequireException("描述(最多支持3万个英文字符)");
		}
		request.setNotes(notes);

		if (!product.containsKey(ProductAttribute.MAIN_IMAGE)) {
			throw new PropertyRequireException(
					"主图 (图片尺寸为800*800，单张大小不超过 1024K)");
		}
		String wareImg = product.get(ProductAttribute.MAIN_IMAGE).toString(); // 主图
																				// 图片尺寸为800*800，单张大小不超过
																				// 1024K
		if (!StringUtils.hasText(wareImg)) {
			throw new PropertyRequireException(
					"主图 (图片尺寸为800*800，单张大小不超过 1024K)");
		}
		try {
			request.setWareImage(ImageUtil.image2Bytes(wareImg));
		} catch (Exception e) {
			LOGGER.error("", e);
			throw new ProductException("exception.product.img.error",
					product.get(ProductAttribute.OUTER_ID));
		}

		if (!product.containsKey(ProductAttribute.PROPERTIES)) {
			throw new PropertyRequireException("商品属性列表");
		}
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)product.get(ProductAttribute.PROPERTIES);
		StringBuilder sb = new StringBuilder();
		if (map.isEmpty()) {
			throw new PropertyRequireException("商品属性列表");
		}
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey()).append(":").append(entry.getValue())
					.append("|");
		}
		String attribute = sb.toString();
		attribute = attribute.substring(0, attribute.length() - 1);
		request.setAttributes(attribute); // 商品属性列表
		// -------------------------新增商品的非必要属性----------------------------------------------------------------------
		if (product.containsKey(ProductAttribute.OUTER_ID)) {
			String itemNum = product.get(ProductAttribute.OUTER_ID).toString();
			if (StringUtils.hasText(itemNum)) {
				request.setItemNum(itemNum); // 外部商品编号，对应商家后台货号
			}
		}

		if (product.containsKey(ProductAttribute.APPROVE_STATUS)) {
			String optionType = product.get(ProductAttribute.APPROVE_STATUS)
					.toString();
			if ("onsale".equals(optionType)) {
				request.setOptionType(optionType); // offsale 或onsale,默认为下架状态
			}
		}

		Map<String, List<String>> colorMap = new HashMap<String, List<String>>();
		if (product.containsKey(ProductAttribute.SKU)) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> skuInfos = (List<Map<String, Object>>)product.get(ProductAttribute.SKU);
			// 获取Sku信息
			StringBuilder sbuff = new StringBuilder();
			StringBuilder price = new StringBuilder();
			StringBuilder stock = new StringBuilder();
			StringBuilder out_id = new StringBuilder();
			StringBuilder propertie_Alias = new StringBuilder();
			Set<String> set = new HashSet<String>();
			for (Map<String, Object> skuInfo : skuInfos) {
				String color = skuInfo.get(ProductAttribute.SKU_COLOR).toString();
				String size = skuInfo.get(ProductAttribute.SKU_SIZE).toString();
				String skuPrice = skuInfo.get(ProductAttribute.SKU_PRICE).toString();
				String skuQuantity = skuInfo.get(ProductAttribute.SKU_QUANTITY).toString();
				String skuOuterId = skuInfo.get(ProductAttribute.SKU_OUTER_ID).toString();
				if (skuInfo.containsKey(ProductAttribute.COLOR_ALIAS)) {
					String colorAlias = skuInfo.get(ProductAttribute.COLOR_ALIAS).toString();
					if (StringUtils.hasText(colorAlias)) {
						propertie_Alias.append(color).append(":").append(colorAlias);
						set.add(propertie_Alias.toString());
						propertie_Alias.setLength(0); // 清空
					}
				}

				if (skuInfo.containsKey(ProductAttribute.SIZE_ALIAS)) {
					String sizeAlias = skuInfo.get(ProductAttribute.SIZE_ALIAS).toString();
					if (StringUtils.hasText(sizeAlias)) {
						propertie_Alias.append(size).append(":").append(sizeAlias);
						set.add(propertie_Alias.toString());
						propertie_Alias.setLength(0);
					}
				}

				if (!StringUtils.hasText(color)) {
					throw new PropertyRequireException("SKU颜色");
				}
				if (!StringUtils.hasText(size)) {
					throw new PropertyRequireException("SKU尺码");
				}
				if (!StringUtils.hasText(skuPrice)) {
					throw new PropertyRequireException("SKU价格");
				}
				if (!StringUtils.hasText(skuQuantity)) {
					throw new PropertyRequireException("SKU库存");
				}
				if (!StringUtils.hasText(skuOuterId)) {
					throw new PropertyRequireException("SKU外部Id");
				}
				sbuff.append(color).append("^").append(size).append("|");
				price.append(skuPrice).append("|");
				stock.append(skuQuantity).append("|");
				out_id.append(skuOuterId).append("|");
				@SuppressWarnings("unchecked")
				List<String> skuImgs = (List<String>)skuInfo.get(ProductAttribute.PROPERTY_IMAGE); // 获取SKU的图片
				if (!colorMap.containsKey(color)) {
					colorMap.put(color, skuImgs);
				}
			}
			request.setSkuPrices(price.substring(0, price.length() - 1)); // sku价格
			request.setSkuStocks(stock.substring(0, stock.length() - 1)); // sku库存
			request.setSkuProperties(sbuff.substring(0, sbuff.length() - 1)); // sku属性
			request.setOuterId(out_id.substring(0, out_id.length() - 1)); // sku外部id

			for (String e : set) {
				propertie_Alias.append(e).append("^");
			}
			if (StringUtils.hasText(propertie_Alias.toString())) {
				request.setPropertyAlias(propertie_Alias.substring(0, propertie_Alias.length() - 1)); // 自定义属性别名
			}
		}
		WareAddResponse response = connectorManager.getConnector().execute(request);
		long ware_id = response.getWareId(); // 商品ID
		LOGGER.info("商品创建成功,商品id为" + ware_id + "创建时间:" + response.getCreated());
		ProductPartnumberMapping productPartnumber = new ProductPartnumberMapping();
		productPartnumber.setApplicationId(applicationId);
		productPartnumber.setChannelProductId(String.valueOf(ware_id));
		productPartnumber.setOuterId(product.get(ProductAttribute.OUTER_ID).toString());
		productPartnumber.setType(0);
		try {
			productAttributeService.saveProductPartnumberMapping(productPartnumber);// 返回的结构存到product_partnumber_mapping表中
		} catch (Exception e) {
			LOGGER.error("京东商品外部编码映射失败", e);
		}
		String imgUrl = null;
		try {
			for (String color : colorMap.keySet()) {
				String clr = color;
				String[] str = clr.split(":");
				List<String> imageList = colorMap.get(clr);
				for (int i = 0; i < imageList.size(); i++) {
					WarePropimgAddRequest wpaRequwest = new WarePropimgAddRequest(); // 根据skuId上传图片接口
					wpaRequwest.setWareId(String.valueOf(ware_id));
					wpaRequwest.setAttributeValueId(str[1]);
					imgUrl = imageList.get(i).toString();
					wpaRequwest.setImage(new FileItem(ImageUtil.toFileName(imageList.get(i).toString()), ImageUtil.image2Bytes(imgUrl)));
					if (i == 0) {
						wpaRequwest.setMainPic(true); // 第一张设置成主图
					} else {
						wpaRequwest.setMainPic(false);
					}
					WarePropimgAddResponse wpaResponse = this.connectorManager.getConnector().execute(wpaRequwest);
					LOGGER.info("图片上传成功,商品id为:" + wpaResponse.getWareId()
							+ "颜色值id为:" + wpaResponse.getAttributeValueId()
							+ "图片id为:" + wpaResponse.getId() + "上传时间为:"
							+ wpaResponse.getCreated());
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
			throw new ImageUploadException(imgUrl, e.getMessage());
		}

	}

	/**
	 * 更新商品操作必须输入的字段为商品的ware_id,其余字段均为非必须输入
	 * 
	 * @param product
	 * @throws ProductException
	 */
	@Override
	public void updateProduct(Map<String, Object> product)
			throws ProductException {
		LOGGER.info("----------商品更新开始------------");
		WareUpdateRequest request = new WareUpdateRequest(); // 新增商品的方法名
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID); // 渠道的applicationId
		if (!product.containsKey(ProductAttribute.OUTER_ID)) {
			throw new PropertyRequireException("外部商家Id");
		}
		String itemNum = product.get(ProductAttribute.OUTER_ID).toString(); // 外部商家id
		if (!StringUtils.hasText(itemNum)) {
			throw new PropertyRequireException("外部商家Id");
		}
		request.setItemNum(itemNum);
		ProductPartnumberMapping ppMapping = productAttributeService.getProductPartnumberMappingByOuterId(itemNum, applicationId, 0); // 根据外部商家id获取商品的渠道id
		String wareId = ppMapping.getChannelProductId();
		request.setWareId(wareId); // 必填属性
		if (product.containsKey(ProductAttribute.TITLE)) {
			String title = product.get(ProductAttribute.TITLE).toString(); // 商品标题
			if (StringUtils.hasText(title)) {
				request.setTitle(title);
			}
		}
		if (product.containsKey(ProductAttribute.QUANTIRY)) {
			String stockNum = product.get(ProductAttribute.QUANTIRY).toString(); // 商品库存
			if (StringUtils.hasText(stockNum)) {
				request.setStockNum(stockNum);
			}
		}
		if (product.containsKey(ProductAttribute.LENGTH)) {
			String length = product.get(ProductAttribute.LENGTH).toString(); // 长
			if (StringUtils.hasText(length)) {
				request.setLength(length);
			}
		}
		if (product.containsKey(ProductAttribute.WIDTH)) {
			String wide = product.get(ProductAttribute.WIDTH).toString(); // 宽
			if (StringUtils.hasText(wide)) {
				request.setWide(wide);
			}
		}
		if (product.containsKey(ProductAttribute.HEIGHT)) {
			String high = product.get(ProductAttribute.HEIGHT).toString(); // 高
			if (StringUtils.hasText(high)) {
				request.setHigh(high);
			}
		}
		if (product.containsKey(ProductAttribute.WEIGHT)) {
			String weight = product.get(ProductAttribute.WEIGHT).toString(); // 重量
			if (StringUtils.hasText(weight)) {
				request.setWeight(weight);
			}
		}
		if (product.containsKey(ProductAttribute.LIST_PRICE)) {
			String marketPrice = product.get(ProductAttribute.LIST_PRICE)
					.toString(); // 市场价
			if (StringUtils.hasText(marketPrice)) {
				request.setMarketPrice(marketPrice);
			}
		}
		if (product.containsKey(ProductAttribute.SALES_PRICE)) {
			String jdPrice = product.get(ProductAttribute.SALES_PRICE)
					.toString(); // 京东价
			if (StringUtils.hasText(jdPrice)) {
				request.setJdPrice(jdPrice);
			}
		}
		if (product.containsKey(ProductAttribute.DESCRIPTION)) {
			String notes = product.get(ProductAttribute.DESCRIPTION).toString(); // 描述（最多支持3万个英文字符）
			if (StringUtils.hasText(notes)) {
				request.setNotes(notes);
			}
		}
		if (product.containsKey(ProductAttribute.PROPERTIES)) {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>)product.get(ProductAttribute.PROPERTIES);
			if (!map.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					sb.append(entry.getKey()).append(":").append(entry.getValue()).append("|");
				}
				String attribute = sb.toString();
				attribute = attribute.substring(0, attribute.length() - 1);
				request.setAttributes(attribute); // 商品属性列表

			}
		}
		if (product.containsKey(ProductAttribute.APPROVE_STATUS)) {
			String optionType = product.get(ProductAttribute.APPROVE_STATUS).toString();
			if ("onsale".equals(optionType)) {
				request.setOptionType(optionType); // offsale 或onsale,默认为下架状态
			}
		}

		Map<String, List<String>> colorMap = new HashMap<String, List<String>>();
		if (product.containsKey(ProductAttribute.SKU)) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> skuInfos = (List<Map<String, Object>>) product.get(ProductAttribute.SKU);
			// 获取Sku信息
			StringBuilder sbuff = new StringBuilder();
			StringBuilder price = new StringBuilder();
			StringBuilder stock = new StringBuilder();
			StringBuilder out_id = new StringBuilder();
			StringBuilder propertie_Alias = new StringBuilder();
			Set<String> set = new HashSet<String>();
			for (Map<String, Object> skuInfo : skuInfos) {
				String color = skuInfo.get(ProductAttribute.SKU_COLOR).toString();
				String size = skuInfo.get(ProductAttribute.SKU_SIZE).toString();
				String skuPrice = skuInfo.get(ProductAttribute.SKU_PRICE).toString();
				String skuQuantity = skuInfo.get(ProductAttribute.SKU_QUANTITY).toString();
				String skuOuterId = skuInfo.get(ProductAttribute.SKU_OUTER_ID).toString();
				if (skuInfo.containsKey(ProductAttribute.COLOR_ALIAS)) {
					String colorAlias = skuInfo.get(ProductAttribute.COLOR_ALIAS).toString();
					if (StringUtils.hasText(colorAlias)) {
						propertie_Alias.append(color).append(":").append(colorAlias);
						set.add(propertie_Alias.toString());
						propertie_Alias.setLength(0);
					}
				}

				if (skuInfo.containsKey(ProductAttribute.SIZE_ALIAS)) {
					String sizeAlias = skuInfo.get(ProductAttribute.SIZE_ALIAS).toString();
					if (StringUtils.hasText(sizeAlias)) {
						propertie_Alias.append(size).append(":").append(sizeAlias);
						set.add(propertie_Alias.toString());
						propertie_Alias.setLength(0);
					}
				}

				if (!StringUtils.hasText(color)) {
					throw new PropertyRequireException("SKU颜色");
				}
				if (!StringUtils.hasText(size)) {
					throw new PropertyRequireException("SKU尺码");
				}
				if (!StringUtils.hasText(skuPrice)) {
					throw new PropertyRequireException("SKU价格");
				}
				if (!StringUtils.hasText(skuQuantity)) {
					throw new PropertyRequireException("SKU库存");
				}
				if (!StringUtils.hasText(skuOuterId)) {
					throw new PropertyRequireException("SKU外部Id");
				}
				sbuff.append(color).append("^").append(size).append("|");
				price.append(skuPrice).append("|");
				stock.append(skuQuantity).append("|");
				out_id.append(skuOuterId).append("|");
				@SuppressWarnings("unchecked")
				List<String> skuImgs = (List<String>)skuInfo.get(ProductAttribute.PROPERTY_IMAGE);
				if (!colorMap.containsKey(color)) {
					colorMap.put(color, skuImgs);
				}
			}
			request.setSkuPrices(price.substring(0, price.length() - 1)); // sku价格
			request.setSkuStocks(stock.substring(0, stock.length() - 1)); // sku库存
			request.setSkuProperties(sbuff.substring(0, sbuff.length() - 1)); // sku属性
			request.setOuterId(out_id.substring(0, out_id.length() - 1)); // sku外部id

			for (String e : set) {
				propertie_Alias.append(e).append("^");
			}
			if (StringUtils.hasText(propertie_Alias.toString())) {
				request.setPropertyAlias(propertie_Alias.substring(0, propertie_Alias.length() - 1)); // 自定义属性别名
			}
		}
		WareUpdateResponse response = connectorManager.getConnector().execute(request);
		LOGGER.info("商品修改成功,商品id为" + response.getWareId() + "修改时间:"
				+ response.getModified());
		String imgUrl = null;
		try {
			for (String color : colorMap.keySet()) {
				String str[] = color.split(":");
				WarePropimgsGetRequest wpgRequest = new WarePropimgsGetRequest();
				wpgRequest.setWareId(wareId);
				wpgRequest.setAttributeValueId(str[1]);
				WarePropimgsGetResponse wpgResponse = this.connectorManager.getConnector().execute(wpgRequest);
				List<WarePropimg> warePropimgs = wpgResponse.getWarePropimg();
				Map<String, String> colorImg = new HashMap<String, String>();
				for (WarePropimg w : warePropimgs) {
					if ("否".equals(w.getMain())) {
						WarePropimgDeleteRequest wpdRequest = new WarePropimgDeleteRequest();
						wpdRequest.setAttributeValueId(w.getColorId());
						wpdRequest.setImageId(w.getImgId().toString());
						wpdRequest.setWareId(wareId);
						WarePropimgDeleteResponse wpdResponse = this.connectorManager.getConnector().execute(wpdRequest);
						LOGGER.info("图片删除成功,图片的id为" + wpdResponse.getImageId()
								+ "操作时间为:" + wpdResponse.getCreated());
					} else {
						colorImg.put("color", w.getColorId());
						colorImg.put("Img", w.getImgId().toString());
					}
				}
				List<String> colorMaps = colorMap.get(color);
				for (int i = 0; i < colorMaps.size(); i++) {
					WarePropimgAddRequest wpaRequwest = new WarePropimgAddRequest();
					wpaRequwest.setWareId(wareId);
					wpaRequwest.setAttributeValueId(str[1]);
					imgUrl = colorMaps.get(i).toString();
					wpaRequwest.setImage(new FileItem(ImageUtil.toFileName(colorMaps.get(i).toString()), ImageUtil.image2Bytes(imgUrl)));
					if (i == 0) {
						wpaRequwest.setMainPic(true); // 第一张设置成主图
						WarePropimgAddResponse wpaResponse = this.connectorManager.getConnector().execute(wpaRequwest);
						LOGGER.info("图片上传成功,商品id为:" + wpaResponse.getWareId()
								+ "颜色值id为:" + wpaResponse.getAttributeValueId()
								+ "图片id为:" + wpaResponse.getId() + "上传时间为:"
								+ wpaResponse.getCreated());
						WarePropimgDeleteRequest wpdRequest = new WarePropimgDeleteRequest();
						wpdRequest.setAttributeValueId(colorImg.get("color"));
						wpdRequest.setImageId(colorImg.get("Img"));
						wpdRequest.setWareId(wareId);
						WarePropimgDeleteResponse wpdResponse = this.connectorManager.getConnector().execute(wpdRequest);
						LOGGER.info("图片删除成功,图片的id为" + wpdResponse.getImageId()
								+ "操作时间为:" + wpdResponse.getCreated());

					} else {
						wpaRequwest.setMainPic(false);
						WarePropimgAddResponse wpaResponse = this.connectorManager.getConnector().execute(wpaRequwest);
						LOGGER.info("图片上传成功,商品id为:" + wpaResponse.getWareId()
								+ "颜色值id为:" + wpaResponse.getAttributeValueId()
								+ "图片id为:" + wpaResponse.getId() + "上传时间为:"
								+ wpaResponse.getCreated());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
			throw new ImageUploadException(imgUrl, e.getMessage());
		}
	}

	@Override
	public List<ChannelAttributeMapping> getKeyPropertyMappings(
			Long channelCategoryId, Integer applicationId) {
		return channelAttributeMappingDao.getChannelAttributeMappings(channelCategoryId, applicationId, true);
	}

	@Override
	public List<ChannelAttributeMapping> getSalePropertyMappings(
			Long channelCategoryId, Integer applicationId) {
		return channelAttributeMappingDao.getChannelAttributeMappings(channelCategoryId, applicationId, false);
	}

	@Override
	public void onShelf(Product product) throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID); // 渠道的applicationId
		String outerProductId = product.getOuterProductId();
		if (!StringUtils.hasText(outerProductId)) {
			throw new PropertyRequireException("外部id");
		}
		// 根据商家的外部编码获得ware_id,商品级别 所以type=0
		ProductPartnumberMapping ppmapping = productAttributeServiceImpl.getProductPartnumberMappingByOuterId(outerProductId, applicationId, 0);
		WareUpdateListingRequest request = new WareUpdateListingRequest();
		request.setWareId(ppmapping.getChannelProductId()); // 商品id
		// request.setTradeNo("tradeNo"); //新版api必须设，但是旧版的京东api可以不设，具体要不要设等待测试
		WareUpdateListingResponse response = this.connectorManager.getConnector().execute(request);
		String code = response.getCode();
		long ware_id = response.getWareId(); // 商品id
		String modified = response.getModified(); // 更改时间
		LOGGER.debug("状态代码:" + code + "商品的id:" + ware_id + "更改时间：" + modified);
	}

	@Override
	public void offShelf(Product product) throws ProductException {
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID); // 渠道的applicationId
		String outerProductId = product.getOuterProductId();
		if (!StringUtils.hasText(outerProductId)) {
			throw new PropertyRequireException("外部id");
		}
		// 根据商家的外部编码获得ware_id,商品级别 所以type=0
		ProductPartnumberMapping ppmapping = productAttributeServiceImpl.getProductPartnumberMappingByOuterId(outerProductId, applicationId, 0);
		WareUpdateDelistingRequest request = new WareUpdateDelistingRequest();
		request.setWareId(ppmapping.getChannelProductId()); // 商品id
		// request.setTradeNo("tradeNo");
		// //新版api必须设，但是旧版的京东api可以不设，具体要不要设等待测试,我觉得可以不设
		WareUpdateDelistingResponse response = this.connectorManager
				.getConnector().execute(request);
		String code = response.getCode();
		long ware_id = response.getWareId(); // 商品id
		String modified = response.getModified(); // 更改时间
		LOGGER.debug("状态代码:" + code + "商品的id:" + ware_id + "更改时间：" + modified);
	}

	@Override
	public void updatePrice(Product product) throws ProductException {
		/*
		 * Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		 * //渠道的applicationId String outerProductId =
		 * product.getOuterProductId();
		 * if(!StringUtils.hasText(outerProductId)){ throw new
		 * PropertyRequireException("商品的外部编码"); } //根据商家的外部编码获得ware_id,商品级别
		 * 所以type=0 1246013634 ProductPartnumberMapping ppmapping =
		 * productAttributeServiceImpl
		 * .getProductPartnumberMappingByOuterId(outerProductId, applicationId,
		 * 0); String wareId = ppmapping.getChannelProductId(); //京东渠道的商品Id
		 * WareUpdateRequest request = new WareUpdateRequest();
		 * request.setWareId(wareId); //更新商品必设属性 Double jdPrice =
		 * product.getPrice(); //product的价格 if(jdPrice!=null){
		 * request.setJdPrice(String.valueOf(jdPrice)); }else{ throw new
		 * PropertyRequireException("商品的京东价"); } List<Sku> skus =
		 * product.getSkuList(); //获取sku信息 if(CollectionUtil.isNotEmpty(skus)){
		 * //如果skus不为空,更新sku的价格信息,如果为空，则只更新商品的京东价 StringBuilder sku_prices = new
		 * StringBuilder(); StringBuilder outer_id = new StringBuilder();
		 * StringBuilder sku_stock = new StringBuilder(); StringBuilder
		 * sku_property = new StringBuilder(); for(Sku s : skus){
		 *//**
		 * 调用京东商品更新接口去更新sku的价格，由于该接口要求ku_prices、sku_properties、
		 * sku_stocks和outer_id四个参数的sku组数必须保持一致
		 * 所以调用了SkuCustomRequest去获取相关的sku_stock和sku_property数据
		 */
		/*
		 * String outSkuId = s.getOuterSkuId(); Double skuPrice = s.getPrice();
		 * if(!StringUtils.hasLength(outSkuId)){ throw new
		 * PropertyRequireException("SKU外部ID"); } if(skuPrice==null){ throw new
		 * PropertyRequireException("SKU价格"); } SkuCustomGetRequest scgRequest =
		 * new SkuCustomGetRequest();
		 * //根据商家设定的sku的外部id获取所对应的sku数据，一个sku的外部id对应一个sku数据
		 * scgRequest.setOuterId(outSkuId); SkuCustomGetResponse scgResponse =
		 * this.connectorManager.getConnector().execute(scgRequest);
		 * com.jd.open.api.sdk.domain.ware.Sku sku = scgResponse.getSku();
		 * if(sku == null) { continue; }
		 * sku_prices.append(skuPrice).append("|"); //需要更新的价格
		 * outer_id.append(outSkuId).append("|"); //该sku的外部id
		 * sku_stock.append(sku.getStockNum()).append("|"); //通过接口获取的sku库存
		 * sku_property.append(sku.getAttributes()).append("|");
		 * //通过接口获取的sku销售属性组合字符串 }
		 * request.setSkuPrices(sku_prices.substring(0,sku_prices.length()-1));
		 * request.setOuterId(outer_id.substring(0,outer_id.length()-1));
		 * request.setSkuStocks(sku_stock.substring(0,sku_stock.length()-1));
		 * request
		 * .setSkuProperties(sku_property.substring(0,sku_property.length()-1));
		 * } WareUpdateResponse response =
		 * this.connectorManager.getConnector().execute(request);
		 * LOGGER.info("商品价格更新成功,商品的id:"
		 * +response.getWareId()+"价格更改时间:"+response.getModified());
		 */

		List<Sku> skus = product.getSkuList();
		Double jdPrice = product.getPrice(); // product的价格
		if (jdPrice == null) {
			throw new PropertyRequireException("商品的京东价");
		}

		List<String> errorOuterId = new ArrayList<String>();

		boolean bool = false;

		for (Sku s : skus) {
			String outSkuId = s.getOuterSkuId();
			Double skuPrice = s.getPrice();
			try {
				if (!StringUtils.hasLength(outSkuId)) {
					bool = true;
					throw new PropertyRequireException("SKU外部ID");
				}
				if (skuPrice == null) {
					bool = true;
					throw new PropertyRequireException("SKU价格");
				}
				WareSkuPriceUpdateRequest request = new WareSkuPriceUpdateRequest();
				request.setOuterId(outSkuId);
				request.setPrice(skuPrice.toString());
				request.setJdPrice(jdPrice.toString());
				WareSkuPriceUpdateResponse response = this.connectorManager
						.getConnector().execute(request);
				LOGGER.info("商品价格更新成功,商品的外部id:" + outSkuId + ",价格更改时间:"
						+ response.getModified());
			} catch (Exception e) {
				LOGGER.error("", e);
				bool = true;
				errorOuterId.add(outSkuId);
				LOGGER.info("商品价格更新失败，失败原因:" + e.getMessage());
			}
		}
		if (bool) {
			StringBuilder sb = new StringBuilder();
			for (String errorid : errorOuterId) {
				sb.append(errorid).append(",");
			}
			throw new ProductException("exception.product.price.update.fail",
					"失败的sku外部id为:" + sb.substring(0, sb.length() - 1));
		}
	}

	@Override
	public int saveChannelAttributeMapping(
			ChannelAttributeMapping channelAttributeMapping) {
		return channelAttributeMappingDao
				.saveChannelAttributeMapping(channelAttributeMapping);
	}

	@Override
	public List<ChannelAttributeMapping> loadjdChannelAttrMapping(
			Map<String, Object> params) {
		return channelAttributeMappingDao.getChannelAttrMappings(params);
	}

	@Override
	public int deletejdChannelAttrMapping(Integer id) {
		return channelAttributeMappingDao.deletejdChannelAttrMapping(id);
	}

	@Override
	public int updatejdChannelAttrMapping(
			ChannelAttributeMapping channelAttributeMapping) {
		return channelAttributeMappingDao
				.updateChannelAttributeMapping(channelAttributeMapping);
	}

	@Override
	public List<DeliveryTemplate> getDeliveryTemplates() {
		return null;
	}

	@Override
	public Long countChannelAttributeMapping(Map<String, Object> params) {
		return channelAttributeMappingDao.count(params);
	}

	@Override
	public String getChannelProductIdByOuterProductId(String outerProductId) {
		// 京东不支持根据外部商品商家编码获取京东商品编号
		return null;
	}

	@Override
	public String getChannelSkuIdByOuterSkuId(String OuterSkuId) {
		SkuCustomGetRequest request = new SkuCustomGetRequest();
		request.setOuterId(OuterSkuId);
		request.setFields("sku_id");
		SkuCustomGetResponse response = connectorManager.getConnector()
				.execute(request);
		com.jd.open.api.sdk.domain.ware.Sku sku = response.getSku();
		if (null != sku) {
			return String.valueOf(sku.getSkuId());
		}
		return null;
	}

	@Override
	public List<ChannelAttributeMapping> getChannelAttributeMapping(
			Long channelCategoryId, Integer applicationId) {
		return channelAttributeMappingDao.getChannelAttributeMapping(
				channelCategoryId, applicationId);
	}

	@Override
	public Category getChannelProperty(Category category)
			throws ProductException {
		//Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID); // 渠道的applicationId
		// if(!StringUtils.hasText(category.getCategoryId())){
		// throw new PropertyRequireException("系统类目id");
		// }
		// CategoryMapping categoryMapping =
		// this.productAttributeServiceImpl.getCategoryMapping(Long.parseLong(category.getCategoryId()),
		// applicationId);
		// if(categoryMapping == null){
		// throw new MappingNotFoundException("系统类目id",
		// category.getCategoryId());
		// }
		// long channelCategoryId = categoryMapping.getChannelCategoryId();
		// //渠道类目id
		// category.setChannelCategoryId(String.valueOf(channelCategoryId));
		CategoryAttributeSearchRequest request = new CategoryAttributeSearchRequest();
		request.setCid(category.getChannelCategoryId()); // 类目id
		request.setFields("aid,name,is_key_prop,is_sale_prop,index_id,status,att_type,input_type,is_req,is_fet,is_nav,cid,is_color_prop,is_size_prop");
		CategoryAttributeSearchResponse response = this.connectorManager
				.getConnector().execute(request);
		List<Attribute> attr = response.getAttributes(); // 根据类目id获取属性
		for (Attribute a : attr) {
			net.chinacloud.mediator.domain.Attribute attr1 = new net.chinacloud.mediator.domain.Attribute();
			String channelAttributeId = String.valueOf(a.getAid());
			attr1.setAttributeId(channelAttributeId);
			attr1.setAttributeName(String.valueOf(a.getName()));
			attr1.setColorProperty(a.isColorProp());
			attr1.setSizeProperty(a.isSizeProp());
			attr1.setKeyProperty(a.getKeyProp());
			attr1.setRequired(Boolean.parseBoolean(a.getReq()));
			attr1.setSalesProperty(a.getSaleProp());
			if (1 == a.getInputType()) {
				attr1.setType(net.chinacloud.mediator.domain.Attribute.ATTRIBUTE_TYPE_SELECT);
			} else if (2 == a.getInputType()) {
				attr1.setType(net.chinacloud.mediator.domain.Attribute.ATTRIBUTE_TYPE_SELECT);
			} else if (3 == a.getInputType()) {
				attr1.setType(net.chinacloud.mediator.domain.Attribute.ATTRIBUTE_TYPE_INPUT);
			}

			/*
			 * ChannelAttributeMapping mapping =
			 * getChannelAttributeMapping(channelCategoryId, channelAttributeId,
			 * applicationId); if (null != mapping) {
			 * attr1.setMappedAttributeCode(mapping.getAttributeCode()); }
			 */

			CategoryAttributeValueSearchRequest cavsrequest = new CategoryAttributeValueSearchRequest();
			cavsrequest.setAvs(String.valueOf(a.getAid())); // 根据属性查询该属性下的属性值
			CategoryAttributeValueSearchResponse cavsresponse = this.connectorManager
					.getConnector().execute(cavsrequest);
			List<AttValue> attValues = cavsresponse.getAttValues(); // 属性值
			for (AttValue a1 : attValues) {
				net.chinacloud.mediator.domain.AttributeValue attValue = new net.chinacloud.mediator.domain.AttributeValue();
				attValue.setId(String.valueOf(a1.getVid()));
				attValue.setName(a1.getName());
				attr1.addAttributeValue(attValue);
			}
			category.addAttribute(attr1);
		}
		return category;
	}

	@Override
	public ChannelAttributeMapping getChannelAttributeMapping(
			Long channelCategoryId, String channelAttributeId,
			Integer applicationId) {
		ChannelAttributeMapping mapping = null;
		try {
			mapping = channelAttributeMappingDao.getChannelAttributeMappings(
					channelCategoryId, channelAttributeId, applicationId);
		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.error("", e);
			mapping = null;
		}
		return mapping;
	}

	@Override
	public List<Product> getTotalProducts(Map<String, Object> params) {
		List<Product> onSaleProducts = new ArrayList<Product>();
		List<Product> inStockProducts = new ArrayList<Product>();
		List<Product> totalProducts = new ArrayList<Product>();

		onSaleProducts = getOnSaleProducts(params);
		inStockProducts = getInstockProducts(params);

		totalProducts.addAll(onSaleProducts);
		totalProducts.addAll(inStockProducts);
		return totalProducts;
	}

	@Override
	public List<Product> getOnSaleProducts(Map<String, Object> params) {
		List<Product> productInfoList = new ArrayList<Product>();
		productInfoList = getOnSaleProducts(1, productInfoList, params);
		return productInfoList;
	}

	private List<Product> getOnSaleProducts(int currentPage,
			List<Product> productInfoList, Map<String, Object> params) {
		Date startTime = (Date) params.get("startDate");
		Date endTime = (Date) params.get("endDate");

		WareListingGetRequest request = new WareListingGetRequest();
		request.setPage(String.valueOf(currentPage));
		request.setPageSize(String.valueOf(PAGE_COUNT));
		request.setFields("ware_id,item_num");
		request.setStartModified(DateUtil.format(startTime));
		request.setEndModified(DateUtil.format(endTime));
		WareListingGetResponse response = connectorManager.getConnector()
				.execute(request);
		List<Ware> ware = response.getWareInfos();
		int totalResults = response.getTotal();
		for (Ware w : ware) {
			Product product = new Product();
			product.setChannelProductId(String.valueOf(w.getWareId()));
			product.setOuterProductId(w.getItemNum());
			product.putParam("shelfStatus", "onShelf");
			productInfoList.add(product);
		}
		int lastPageIndex = totalResults % PAGE_COUNT == 0 ? totalResults
				/ PAGE_COUNT : totalResults / PAGE_COUNT + 1;
		int pageIndex = currentPage + 1;
		if (pageIndex <= lastPageIndex) {
			getOnSaleProducts(pageIndex, productInfoList, params);
		}
		return productInfoList;
	}

	@Override
	public List<Product> getInstockProducts(Map<String, Object> params) {
		List<Product> productInfoList = new ArrayList<Product>();
		productInfoList = getInstockProducts(1, productInfoList, params);
		return productInfoList;
	}

	private List<Product> getInstockProducts(int currentPage,
			List<Product> productInfoList, Map<String, Object> params) {
		Date startTime = (Date) params.get("startDate");
		Date endTime = (Date) params.get("endDate");

		WareDelistingGetRequest request = new WareDelistingGetRequest();
		request.setPage(String.valueOf(currentPage));
		request.setPageSize(String.valueOf(PAGE_COUNT));
		request.setFields("ware_id,item_num,ware_status");
		request.setStartModified(DateUtil.format(startTime));
		request.setEndModified(DateUtil.format(endTime));
		WareDelistingGetResponse response = connectorManager.getConnector()
				.execute(request);
		List<Ware> ware = response.getWareInfos();
		int totalResults = response.getTotal();
		for (Ware w : ware) {
			Product productInfo = new Product();
			productInfo.setChannelProductId(String.valueOf(w.getWareId()));
			productInfo.setOuterProductId(w.getItemNum());
			productInfo.putParam("shelfStatus", "inStock");
			productInfoList.add(productInfo);
		}
		int lastPageIndex = totalResults % PAGE_COUNT == 0 ? totalResults
				/ PAGE_COUNT : totalResults / PAGE_COUNT + 1;
		int pageIndex = currentPage + 1;
		if (pageIndex <= lastPageIndex) {
			getInstockProducts(pageIndex, productInfoList, params);
		}
		return productInfoList;
	}

	@Override
	public Map<String, String> isRepeatOuterId(Product productInfo) {
		// 京东不去做处理，直接返回1,不要问我为什么，业务这样定的
		return null;
	}

	@Override
	public List<CategoryList> getAllCategory() throws ProductException {
		CategorySearchRequest crequest = new CategorySearchRequest();
		CategorySearchResponse cresponse = connectorManager.getConnector()
				.execute(crequest);
		List<CategoryList> categoryLists = new ArrayList<CategoryList>();
		for (com.jd.open.api.sdk.domain.category.Category category : cresponse
				.getCategory()) {
			CategoryList catList = new CategoryList();
			catList.setCategoryId(Long.valueOf(category.getId()));
			catList.setName(category.getName());
			catList.setParent_cid(Long.valueOf(category.getFid()));
			categoryLists.add(catList);
		}
		List<CategoryList> treeCateList = new ArrayList<CategoryList>();
		//int i = 0;
		for (CategoryList catelogory : categoryLists) {
			if (catelogory.getParent_cid() == 0) {
				//i++;
				treeCateList.add(catelogory);
			}
			for (CategoryList cate : categoryLists) {
				if (cate.getParent_cid().longValue() == catelogory
						.getCategoryId().longValue()) {
					if (catelogory.getChildrens() == null) {
						List<CategoryList> myChildrens = new ArrayList<CategoryList>();
						myChildrens.add(cate);
						catelogory.setChildrens(myChildrens);
					} else {
						catelogory.getChildrens().add(cate);
					}
				}
			}
		}
		return treeCateList;
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
