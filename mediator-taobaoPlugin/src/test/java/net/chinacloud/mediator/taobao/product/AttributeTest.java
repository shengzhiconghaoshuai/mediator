/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：AttributeTest.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.product;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.domain.Attribute;
import net.chinacloud.mediator.domain.AttributeValue;
import net.chinacloud.mediator.domain.Category;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.domain.ItemProp;
import com.taobao.api.domain.PropValue;
import com.taobao.api.request.ItempropsGetRequest;
import com.taobao.api.response.ItempropsGetResponse;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.ComplexField;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiCheckField;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.SingleCheckField;
import com.taobao.top.schema.option.Option;
import com.taobao.top.schema.rule.Rule;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月30日 上午9:51:14
 */
public class AttributeTest {
	
	private Long channelCategoryId = 50010850L;

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Connector<TaobaoRequest<?>> getTaobaoConnector() {
		ApplicationParam param = new ApplicationParam("12316299",
				"094c5a4daa5dda068b8228724fd47a74",
				"http://gw.api.taobao.com/router/rest",
				"6100416877b49d34597781a310d55c0bd2c3039f9562632728274110",
				null, null, null, null, null);
		Connector connector = new TaobaoConnector(param);
		return connector;
	}

	@Test
	public void getchannelAttr(){
		Category category=new Category();
		category.setChannelCategoryId(String.valueOf(channelCategoryId));
		category.setCategoryId("325");
		ItempropsGetRequest req = new ItempropsGetRequest();
		req.setFields("pid,name,must,is_key_prop,is_sale_prop,is_color_prop,is_enum_prop,prop_values");
		req.setCid(channelCategoryId);
		ItempropsGetResponse response=getTaobaoConnector().execute(req);
		List<Attribute> attributes=new ArrayList<Attribute>();
		for(ItemProp itemProp:response.getItemProps()){
			if (itemProp.getMust() && !itemProp.getIsSaleProp()) {
				List<AttributeValue> attributeValues=new ArrayList<AttributeValue>();
				Attribute attribute=new Attribute();
				attribute.setAttributeId(itemProp.getPid().toString());
				attribute.setAttributeName(itemProp.getName());
				if(itemProp.getIsColorProp()){
					attribute.setColorProperty(true);
				}
				if(itemProp.getIsKeyProp()){
					attribute.setKeyProperty(true);
				}
				if(itemProp.getIsSaleProp()){
					attribute.setSalesProperty(true);
				}
				if(itemProp.getMust()){
					attribute.setRequired(true);
				}
				if(itemProp.getIsEnumProp()){
					attribute.setType(Attribute.ATTRIBUTE_TYPE_SELECT);
				}else{
					attribute.setType(Attribute.ATTRIBUTE_TYPE_INPUT);
				}
				if(itemProp.getPropValues()!=null){
					for(PropValue propValue:itemProp.getPropValues()){
						AttributeValue attributeValue=new AttributeValue();
						attributeValue.setId(propValue.getVid().toString());
						attributeValue.setName(propValue.getName());
						attributeValues.add(attributeValue);
					}
				}
				attribute.setAttributeValues(attributeValues);
				attributes.add(attribute);
			}
		}
		category.setAttributes(attributes);
		MessageObject<Category> mo = new MessageObject<Category>(
				MessageActionCode.ACTION_CODE_PRODUCT_INVENTORY_UPDATE_BY_PRODUCTS, 
				1001);
		mo.setContent(category);
		System.out.println(JsonUtil.object2JsonString(mo));
	}

	@Test
	public void generateTaobao(){
		ItempropsGetRequest req = new ItempropsGetRequest();
		req.setFields("pid,name,must,is_key_prop,is_sale_prop,is_color_prop,is_enum_prop,prop_values");
		req.setCid(channelCategoryId);
		ItempropsGetResponse response=getTaobaoConnector().execute(req);
		for(ItemProp itemProp:response.getItemProps()){
			if (itemProp.getIsSaleProp()) {
				if(CollectionUtil.isNotEmpty(itemProp.getPropValues())){
					for(PropValue propValue:itemProp.getPropValues()){
						//System.out.println("propName:" + itemProp.getName() + ",valueName:" + propValue.getName() + ",pvid:" + itemProp.getPid() + ":" + propValue.getVid());
						System.out.println("insert into T_PRODUCT_MAPPING (NAME, CATALOG_ID, STORE_TYPE, VALUE) values ('" + propValue.getName() + "', 325, 2, '" + itemProp.getPid() + ":" + propValue.getVid() + "');");
					}
				}
			}
		}
	}
	
	@Test
	public void generateTmall() throws TopSchemaException, IOException {
		Resource resource = new ClassPathResource("yurongfu.txt");
		List<Field> fieldList = SchemaReader.readXmlForList(resource.getFile());
		if (CollectionUtil.isNotEmpty(fieldList)) {
			for (Field field : fieldList) {
				List<Rule> rules = field.getRules();
				if (CollectionUtil.isNotEmpty(rules)) {
					for (Rule rule : rules) {
						String ruleName = rule.getName();
						if ("requiredRule".equals(ruleName)) {
							Attribute attribute = new Attribute();
							attribute.setAttributeId(field.getId());
							attribute.setAttributeName(field.getName());
							
							FieldTypeEnum fieldType = field.getType();
							if (FieldTypeEnum.INPUT == fieldType || FieldTypeEnum.MULTIINPUT == fieldType) {
								attribute.setType(Attribute.ATTRIBUTE_TYPE_INPUT);
							} else if (FieldTypeEnum.SINGLECHECK == fieldType || FieldTypeEnum.MULTICHECK == fieldType) {
								attribute.setType(Attribute.ATTRIBUTE_TYPE_SELECT);
								
								List<Option> options = null;
								if (FieldTypeEnum.SINGLECHECK == fieldType) {
									options = ((SingleCheckField)field).getOptions();
								} else {
									options = ((MultiCheckField)field).getOptions();
								}
								if (CollectionUtil.isNotEmpty(options)) {
									for (Option option: options) {
										AttributeValue attributeValue = new AttributeValue(option.getValue(), option.getDisplayName());
										attribute.addAttributeValue(attributeValue);
									}
								}
							}
							
							break;
						}
					}
				}
			}
		}
		System.out.println(JsonUtil.object2JsonString(null));
	}
	
	@Test
	public void getChannelProperty()
			throws ProductException, IOException {
		Resource resource = new ClassPathResource("lianyiqun-goelia.txt");
		
		String categoryId = "";
		Integer applicationId = ContextUtil.get(Constant.APPLICATION_ID);
		Category category = null;
			
		List<Field> fieldList = null;
		try {
			fieldList = SchemaReader.readXmlForList(resource.getFile());
			if (CollectionUtil.isNotEmpty(fieldList)) {
				category = new Category();
				category.setChannelCategoryId(String.valueOf(channelCategoryId));
				category.setCategoryId(categoryId);
				/*Registry registry = SpringUtil.getBean(Registry.class);
				String excludeFieldIds = registry.get("excludeFieldIds");*/
				String excludeFieldIds = "";
				Set<String> excludeFieldSet = null;
				if (StringUtils.hasText(excludeFieldIds)) {
					String[] excludeFieldIdArray = StringUtils.split(excludeFieldIds, ",");
					excludeFieldSet = new HashSet<String>(excludeFieldIdArray.length);
					for (int i = 0; i < excludeFieldIdArray.length; i++) {
						excludeFieldSet.add(excludeFieldIdArray[i].trim());
					}
				}
				//String onlyRequired = registry.get("onlyRequired");
				String onlyRequired = "false";
				boolean onlyParseRequiredField = false;
				if (StringUtils.hasText(onlyRequired)) {
					onlyParseRequiredField = Boolean.valueOf(onlyRequired);
				}
				for (Field field : fieldList) {
					parseField(field, category, excludeFieldSet, applicationId, onlyParseRequiredField);
				}
			}
		} catch (TopSchemaException e) {
			e.printStackTrace();
		}
		//System.out.println(JsonUtil.object2JsonString(category));
	}
	
	private void parseField(Field field, Category category, Set<String> excludeFieldIds, Integer applicationId, boolean onlyRequired) {
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
		if (CollectionUtil.isNotEmpty(excludeFieldIds)) {
			if (excludeFieldIds.contains(fieldId)) {
				return;
			}
		}
		switch (fieldType) {
		case INPUT:
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_INPUT, hasRequiredRule);
			break;
		case MULTIINPUT:
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_MULTI_INPUT, hasRequiredRule);
			break;
		case SINGLECHECK:
			if (fieldId.equals("prop_1627207")) {
				SingleCheckField singleCheckField = (SingleCheckField)field;
				List<Option> options = singleCheckField.getOptions();
				if (CollectionUtil.isNotEmpty(options)) {
					for (Option option : options) {
						//insert into T_PRODUCT_MAPPING (NAME, CATALOG_ID, STORE_TYPE, VALUE, PRODUCT_MAPPING_ID, LASTUPDATE, TYPE) values ('青色', 311, 4, '1627207:3455405', -3803, TIMESTAMP '2015-08-06 10:26:19', 1);

						System.out.println("insert into T_PRODUCT_MAPPING (NAME, CATALOG_ID, STORE_TYPE, VALUE, PRODUCT_MAPPING_ID, LASTUPDATE, TYPE) values ('" + field.getName() + "', 311, 4, '" + fieldId + ":" + option.getValue() + "', -3803, SYSDATE, 1);");
					}
				}
			}
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_SELECT, hasRequiredRule);
			break;
		case MULTICHECK:
			setAttribute(category, fieldId, field.getName(), applicationId, Attribute.ATTRIBUTE_TYPE_MULTI_SELECT, hasRequiredRule);
			break;
		case COMPLEX:
			ComplexField complexField = (ComplexField)field;
			List<Field> complexFields = complexField.getFieldList();
			if (CollectionUtil.isNotEmpty(complexFields)) {
				for (Field perComplexField : complexFields) {
					parseField(perComplexField, category, excludeFieldIds, applicationId, onlyRequired);
				}
			}
			break;
		case MULTICOMPLEX:
			MultiComplexField multiComplexField = (MultiComplexField)field;
			
			List<Field> multiComplexFields = multiComplexField.getFieldList();
			if (CollectionUtil.isNotEmpty(multiComplexFields)) {
				for (Field multComField : multiComplexFields) {
					parseField(multComField, category, excludeFieldIds, applicationId, onlyRequired);
				}
			}
			break;
		default:
			break;
		}
	}
	
	private void setAttribute(Category category, String channelAttributeId, String channelAttributeName, Integer applicationId, String attributeType, boolean hasRequiredRule) {
		Attribute attribute = new Attribute();
		attribute.setAttributeId(channelAttributeId);
		attribute.setAttributeName(channelAttributeName);
		attribute.setType(attributeType);
		attribute.setRequired(hasRequiredRule);
		
		/*ChannelAttributeMapping mapping = getChannelAttributeMapping(Long.valueOf(category.getChannelCategoryId()), channelAttributeId, applicationId);
		if (null != mapping) {
			attribute.setMappedAttributeCode(mapping.getAttributeCode());
		}*/
		
		category.addAttribute(attribute);
	}
}
