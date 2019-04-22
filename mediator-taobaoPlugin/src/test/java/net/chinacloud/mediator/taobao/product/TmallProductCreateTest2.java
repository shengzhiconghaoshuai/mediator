/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TmallProductCreateTest2.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.product;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.domain.Attribute;
import net.chinacloud.mediator.domain.AttributeValue;
import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.request.TmallProductAddSchemaGetRequest;
import com.taobao.api.request.TmallProductMatchSchemaGetRequest;
import com.taobao.api.request.TmallProductSchemaAddRequest;
import com.taobao.api.request.TmallProductSchemaGetRequest;
import com.taobao.api.request.TmallProductSchemaMatchRequest;
import com.taobao.api.response.TmallProductAddSchemaGetResponse;
import com.taobao.api.response.TmallProductMatchSchemaGetResponse;
import com.taobao.api.response.TmallProductSchemaAddResponse;
import com.taobao.api.response.TmallProductSchemaGetResponse;
import com.taobao.api.response.TmallProductSchemaMatchResponse;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.ComplexField;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.MultiCheckField;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.SingleCheckField;
import com.taobao.top.schema.option.Option;
import com.taobao.top.schema.rule.Rule;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月18日 下午4:21:05
 */
public class TmallProductCreateTest2 {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Connector<TaobaoRequest<?>> getTaobaoConnector() {
		//陆静华
		/*ApplicationParam param = new ApplicationParam("1012316299",
				"sandboxdaa5dda068b8228724fd47a74",
				"http://gw.api.tbsandbox.com/router/rest",
				"61022277ac318e75e5c34910e04a98c86ec1d019c0b7ce23651882601",
				null, null, null, null, null);*/
		//生产sm沙箱
		ApplicationParam param = new ApplicationParam("12339797www",
				"e015d6cf10b366ceb2a23b0955ab15d6www",
				"http://gw.api.taobao.com/router/rest",
				"6102713345c9b6ac7889a1b644e23009ac06bb3063baf061770004653www",
				null, null, null, null, null);
		Connector connector = new TaobaoConnector(param);
		return connector;
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	/**
	 * 根据类目id获取产品匹配规则
	 * @param categoryId 类目id
	 * @return
	 */
	private String getProductMatchSchema(Long categoryId) {
		TmallProductMatchSchemaGetRequest request = new TmallProductMatchSchemaGetRequest();
		request.setCategoryId(categoryId);
		TmallProductMatchSchemaGetResponse response = getTaobaoConnector().execute(request);
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
		TmallProductSchemaMatchResponse response = getTaobaoConnector().execute(request);
		String matchResult = response.getMatchResult();
		if (StringUtils.hasText(matchResult)) {
			if (matchResult.indexOf(",") > 0) {
				String mr = matchResult.split(",")[0];
				return Long.valueOf(mr.trim());
			}
		} else {
			return Long.valueOf(matchResult.trim());
		}
		return null;
	}
	
	/**
	 * 判断产品是否能发布商品
	 * @param productId 产品id
	 * @return
	 */
	private boolean canPublishItem(Long productId) {
		TmallProductSchemaGetRequest request = new TmallProductSchemaGetRequest();
		request.setProductId(productId);
		TmallProductSchemaGetResponse response = getTaobaoConnector().execute(request);
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
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取产品添加规则
	 * @param categoryId
	 * @param brandId
	 * @return 产品添加规则,如果为空,则不需要创建产品,直接创建item
	 */
	public String getProductAddSchema(Long categoryId, Long brandId) {
		TmallProductAddSchemaGetRequest request = new TmallProductAddSchemaGetRequest();
		request.setCategoryId(categoryId);
		request.setBrandId(brandId);
		TmallProductAddSchemaGetResponse response = getTaobaoConnector().execute(request);
		return response.getAddProductRule();
	}
	
	/**
	 * 创建一个产品
	 * @param categoryId 类目id
	 * @param brandId 品牌id
	 * @param productAddRule 填充后的产品创建规则
	 * @return 产品id,如果为null则表示产品创建失败
	 */
	public Long addProduct(Long categoryId, Long brandId, String productAddRule) {
		TmallProductSchemaAddRequest request = new TmallProductSchemaAddRequest();
		request.setCategoryId(categoryId);
		request.setBrandId(brandId);
		request.setXmlData(productAddRule);
		TmallProductSchemaAddResponse response = getTaobaoConnector().execute(request);
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
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	
	
	@Test
	public void getChannelProperty()
			throws ProductException {
		Category category = new Category();
		category.setCategoryId("315");
		if (StringUtils.hasText(category.getCategoryId())) {
			category.setChannelCategoryId("162103");
			
			List<Field> fieldList = null;
			try {
				Resource resource = new FileSystemResource("E:\\Goelia\\workspace\\mediator-taobaoPlugin\\src\\test\\resources\\lianyiqun.txt");
				try {
					fieldList = SchemaReader.readXmlForList(resource.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (CollectionUtil.isNotEmpty(fieldList)) {
					String includeFieldIds = null;
					Set<String> includeFieldSet = null;
					if (StringUtils.hasText(includeFieldIds)) {
						String[] includeFieldIdArray = StringUtils.split(includeFieldIds, ",");
						includeFieldSet = new HashSet<String>(includeFieldIdArray.length);
						for (int i = 0; i < includeFieldIdArray.length; i++) {
							includeFieldSet.add(includeFieldIdArray[i].trim());
						}
					}
					String onlyRequired = null;
					boolean onlyParseRequiredField = false;
					if (StringUtils.hasText(onlyRequired)) {
						onlyParseRequiredField = Boolean.valueOf(onlyRequired);
					}
					for (Field field : fieldList) {
						parseField(field, category, includeFieldSet, null, onlyParseRequiredField);
					}
				}
			} catch (TopSchemaException e) {
				e.printStackTrace();
			}
		}
		System.out.println(JsonUtil.object2JsonString(category));
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
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_MULTI_INPUT, hasRequiredRule, null);
			break;
		case SINGLECHECK:
			SingleCheckField singleCheckField = (SingleCheckField)field;
			List<Option> singleCheckOptions = singleCheckField.getOptions();
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_SELECT, hasRequiredRule, singleCheckOptions);
			break;
		case MULTICHECK:
			MultiCheckField multiCheckField = (MultiCheckField)field;
			List<Option> multiCheckOptions = multiCheckField.getOptions();
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_MULTI_SELECT, hasRequiredRule, multiCheckOptions);
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
		
		String tmallColorProperty = "";
		String tmallSizeProperty = "";
		
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
		
		/*ChannelAttributeMapping mapping = getChannelAttributeMapping(Long.valueOf(category.getChannelCategoryId()), channelAttributeId, applicationId);
		if (null != mapping) {
			attribute.setMappedAttributeCode(mapping.getAttributeCode());
		}*/
		
		if (CollectionUtil.isNotEmpty(options)) {
			for (Option option : options) {
				AttributeValue attributeValue = new AttributeValue(option.getValue(), option.getDisplayName());
				attribute.addAttributeValue(attributeValue);
			}
		}
		
		category.addAttribute(attribute);
	}
}
