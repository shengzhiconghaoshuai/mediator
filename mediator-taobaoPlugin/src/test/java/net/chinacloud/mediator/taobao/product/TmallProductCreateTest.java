package net.chinacloud.mediator.taobao.product;

import java.util.ArrayList;
import java.util.List;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;
import net.chinacloud.mediator.taobao.service.impl.ProductServiceImpl;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.User;
import com.taobao.api.request.ItemPriceUpdateRequest;
import com.taobao.api.request.SkusCustomGetRequest;
import com.taobao.api.request.TmallItemAddSchemaGetRequest;
import com.taobao.api.request.TmallProductAddSchemaGetRequest;
import com.taobao.api.request.TmallProductMatchSchemaGetRequest;
import com.taobao.api.request.TmallProductSchemaAddRequest;
import com.taobao.api.request.TmallProductSchemaGetRequest;
import com.taobao.api.request.TmallProductSchemaMatchRequest;
import com.taobao.api.request.UserSellerGetRequest;
import com.taobao.api.response.ItemPriceUpdateResponse;
import com.taobao.api.response.SkusCustomGetResponse;
import com.taobao.api.response.TmallItemAddSchemaGetResponse;
import com.taobao.api.response.TmallProductAddSchemaGetResponse;
import com.taobao.api.response.TmallProductMatchSchemaGetResponse;
import com.taobao.api.response.TmallProductSchemaAddResponse;
import com.taobao.api.response.TmallProductSchemaGetResponse;
import com.taobao.api.response.TmallProductSchemaMatchResponse;
import com.taobao.api.response.UserSellerGetResponse;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.ComplexField;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.SingleCheckField;
import com.taobao.top.schema.rule.Rule;
import com.taobao.top.schema.value.ComplexValue;

public class TmallProductCreateTest {
	
	//Long brandId = 31681L;		//goelia
	Long brandId = 44612L;		//思美登
	String styleNum = "SW12312311";	//款号
	
	String pruductImgUrl = "http://img02.tbsandbox.com/imgextra/i2/3638360709/TB2SypXXXXXXXX8XXXXXXXXXXXX_!!3638360709.jpg";
	@Autowired
	ProductServiceImpl productServiceImpl;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Connector<TaobaoRequest<?>> getTaobaoConnector() {
		//陆静华
		/*ApplicationParam param = new ApplicationParam("1012316299",
				"sandboxdaa5dda068b8228724fd47a74",
				"http://gw.api.tbsandbox.com/router/rest",
				"61022277ac318e75e5c34910e04a98c86ec1d019c0b7ce23651882601",
				null, null, null, null, null);*/
		//生产sm沙箱
//		ApplicationParam param = new ApplicationParam("12316299",
//				"094c5a4daa5dda068b8228724fd47a74",
//				"http://gw.api.taobao.com/router/rest",
//				"6100416877b49d34597781a310d55c0bd2c3039f9562632728274110",
		ApplicationParam param = new ApplicationParam("12339797www",
				"e015d6cf10b366ceb2a23b0955ab15d6www",
				"http://gw.api.taobao.com/router/rest",
				"61011034cb314854c63eacc327666a2edd4dcc97fa0c820723741315www",
				null, null, null, null, null);
		Connector connector = new TaobaoConnector(param);
		return connector;
	}

	@Test
	public void test() throws TopSchemaException {
		//Long categoryId = 162103L;
		Long categoryId = 50012825L;
		//Long categoryId = 60118003L;		//女装
		//Long categoryId = 162104L;		//女装-衬衫
		//Long categoryId = 50010850L;		//女装-连衣裙
		
		//1、判断用户类型,B or C
		//getSellerUser();
		
		//2、获取产品匹配的规则
		String schema = getProductMatchSchema(categoryId);
		
		//3、解析规则,填充必要的属性,根据填充后的规则搜索产品是否已经存在
		//2045449581
		String result = productSchemaMatch(categoryId, schema);
		
		//result 即为淘宝产品编号
		//4、产品不存在
		if (null == result || "".equals(result)) {
			//创建产品
			//4.1、获取产品创建规则
			String productAdddSchema = getProductAddSchema(categoryId, brandId);
			
			if (null != productAdddSchema && !"".equals(productAdddSchema)) {
				//4.2、创建产品
				// 接口暂时不可用
				//result = createProduct(categoryId, productAdddSchema);
				System.out.println("=================create product===============");
			}
		}
		//System.out.println(result);
		Long productId = Long.valueOf(result);
		
		//5、产品存在
		//5.1、获取产品状态
		boolean status = checkProductStatus(productId);
		System.out.println("product status:" + status);
		
		if (status) {
			//5.2、获取商品发布规则
			//getItemAddSchema(categoryId, productId);
			
			//5.3、创建商品
			System.out.println("===========get item add schema==========");
		}
		
	}
	
	/**
	 * 1、判断用户类型,B or C
	 * 沙箱环境不可用
	 */
	public User getSellerUser() {
		UserSellerGetRequest request = new UserSellerGetRequest();
		request.setFields("uid,nick,type");
		UserSellerGetResponse response = getTaobaoConnector().execute(request);
		processResponse(response);
		
		User user = response.getUser();
		System.out.println(user.getType());
		
		return user;
	}
	
	/**
	 * 2、获取产品匹配的规则
	 */
	public String getProductMatchSchema(Long categoryId) {
		TmallProductMatchSchemaGetRequest request = new TmallProductMatchSchemaGetRequest();
		request.setCategoryId(categoryId);
		TmallProductMatchSchemaGetResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		String matchResult = response.getMatchResult();
		System.out.println("product match schema:" + matchResult);
		
		return matchResult;
	}
	
	/**
	 * 3、解析规则,填充必要的属性,根据填充后的规则搜索产品是否已经存在
	 * @param categoryId 产品所属叶子目录ID
	 * @param schema 规则
	 * @return
	 * @throws TopSchemaException
	 */
	public String productSchemaMatch(Long categoryId, String schema) throws TopSchemaException {
		TmallProductSchemaMatchRequest request = new TmallProductSchemaMatchRequest();
		request.setCategoryId(categoryId);
		
		List<Field> ruleFields = SchemaReader.readXmlForList(schema);
		List<Field> paramFields = new ArrayList<Field>();
		
		if (!CollectionUtil.isEmpty(ruleFields)) {
			for (Field ruleField : ruleFields) {
				List<Rule> rules = ruleField.getRules();
				for (Rule rule : rules) {
					if ("requiredRule".equals(rule.getName())) {
						FieldTypeEnum fieldType = ruleField.getType();
						if (FieldTypeEnum.INPUT == fieldType) {
							// 这边需要定义映射规则,比如淘宝中的货号对应系统中的哪个字段
							InputField inputField = new InputField();
							inputField.setId(ruleField.getId());
							inputField.setValue(styleNum);
							
							paramFields.add(inputField);
						} else if (FieldTypeEnum.SINGLECHECK == fieldType) {
							SingleCheckField singleCheckField = new SingleCheckField();
							singleCheckField.setId(ruleField.getId());
							singleCheckField.setValue(brandId + "");
							
							paramFields.add(singleCheckField);
						}
						continue;
					}
				}
			}
		}
		String productMatchValue = SchemaWriter.writeParamXmlString(paramFields);
		request.setPropvalues(productMatchValue);
		System.out.println("productMatchValue:" + productMatchValue);
		TmallProductSchemaMatchResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		String result = response.getMatchResult();
		System.out.println("matchResult:" + result);
		
		return result;
	}
	
	/**
	 * 4.1、获取产品发布规则
	 * @param categoryId
	 * @param brandId
	 * @return
	 */
	public String getProductAddSchema(Long categoryId, Long brandId) {
		TmallProductAddSchemaGetRequest request = new TmallProductAddSchemaGetRequest();
		request.setCategoryId(categoryId);
		request.setBrandId(brandId);
		TmallProductAddSchemaGetResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		String  result = response.getAddProductRule();
		
		System.out.println("add product rule:" + result);
		
		return result;
	}
	
	/**
	 * 4.2、创建产品
	 * @throws TopSchemaException 
	 */
	public String createProduct(Long categoryId, String xmlData) throws TopSchemaException {
		TmallProductSchemaAddRequest request = new TmallProductSchemaAddRequest();
		request.setCategoryId(categoryId);
		request.setBrandId(brandId);
		
		List<Field> fields = SchemaReader.readXmlForList(xmlData);
		//List<Field> paramFields = new ArrayList<Field>();
		if (null != fields && !fields.isEmpty()) {
			for (Field field : fields) {
				if (FieldTypeEnum.COMPLEX == field.getType()) {
					/*ComplexField complexField = (ComplexField)field;
					List<Field> tempFields = complexField.getFieldList();
					if (null != tempFields && !tempFields.isEmpty()) {
						for (Field tempField : tempFields) {
							processCreateProductRule(tempField);
						}
					}*/
					if ("产品图片".equals(field.getName())) {
						ComplexValue complexValue = new ComplexValue();
						complexValue.setInputFieldValue("product_image_0", pruductImgUrl);
						((ComplexField)field).setComplexValue(complexValue);
					}
				} else {
					processCreateProductRule(field);
				}
			}
		}
		String paramData = SchemaWriter.writeParamXmlString(fields);
		System.out.println("xxxxxxxxxx:" + paramData);
		request.setXmlData(paramData);
		
		TmallProductSchemaAddResponse response = getTaobaoConnector().execute(request);
		processResponse(response);
		
		String result = response.getAddProductResult();
		System.out.println("add product result:" + result);
		
		return result;
	}
	
	private void processCreateProductRule(Field field) {
		List<Rule> rules = field.getRules();
		if (null != rules && !rules.isEmpty()) {
			for (Rule rule : rules) {
				if ("requiredRule".equals(rule.getName())) {
					if (field.getName().equals("品牌")) {
						SingleCheckField singleCheckField = (SingleCheckField)field;
						singleCheckField.setValue(brandId + "");
					}
					if (field.getName().equals("货号")) {
						InputField inputField = (InputField)field;
						inputField.setValue(styleNum);;
					}
					if (field.getName().equals("产品图片")) {
						InputField inputField = (InputField)field;
						inputField.setValue(pruductImgUrl);
					}
					break;
				}
			}
		}
	}
	
	/**
	 * 5、查询产品状态,能发布商品则返回true,否则返回false
	 * @throws TopSchemaException 
	 */
	public boolean checkProductStatus(Long productId) throws TopSchemaException {
		TmallProductSchemaGetRequest request = new TmallProductSchemaGetRequest();
		request.setProductId(productId);
		TmallProductSchemaGetResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		boolean status = true;
		
		String result = response.getGetProductResult();
		List<Field> fields = SchemaReader.readXmlForList(result);
		if (!CollectionUtil.isEmpty(fields)) {
			for (Field field : fields) {
				if ("can_publish_item".equals(field.getId())) {
					String defaultValue = ((InputField)field).getDefaultValue();
					if (Boolean.valueOf(defaultValue)) {
						status = true;
					} else {
						status = false;
					}
				}
			}
		}
		
		//System.out.println("product status:" + result);
		return status;
	}
	
	/**
	 * 6、获取商品发布的规则
	 */
	public String getItemAddSchema(Long categoryId, Long productId) {
		TmallItemAddSchemaGetRequest request = new TmallItemAddSchemaGetRequest();
		request.setCategoryId(categoryId);
		request.setProductId(productId);
		request.setType("b");		//发布商品类型，一口价填“b”，拍卖填"a"
		
		TmallItemAddSchemaGetResponse response = getTaobaoConnector().execute(request);
		processResponse(response);
		
		String addItemRule = response.getAddItemResult();
		System.out.println("item add rule:" + addItemRule);
		
		return addItemRule;
	}
	
	/*private Field processRuleField(Field ruleField) {
		FieldTypeEnum fieldType = ruleField.getType();
		Field field = null;
		switch (fieldType) {
		case INPUT: {
			field = new InputField();
			break;
		}
		case MULTIINPUT:
			field = new InputField();
			break;
		default:
			break;
		}
		return field;
	}*/
	
	public void xxx() throws ProductException{
		Product product=new Product();
		Sku channelsku=new Sku();
		channelsku.setOuterSkuId("5DJ6E13AW03M");
		channelsku.setPrice(2999D);
		product.addSku(channelsku);
		Sku channelsku1=new Sku();
		channelsku1.setOuterSkuId("102181080012S");
		channelsku1.setPrice(2999D);
		product.addSku(channelsku1);
		ItemPriceUpdateRequest updateReq=new ItemPriceUpdateRequest();
		updateReq.setNumIid(521414406198L);
		updateReq.setPrice("2999");
			StringBuilder skuPrices= new StringBuilder();
			StringBuilder skuProps=new StringBuilder();
			StringBuilder skuquantities=new StringBuilder();
			StringBuilder skuoutids=new StringBuilder();
			for(Sku sku:product.getSkuList()){
				SkusCustomGetRequest req = new SkusCustomGetRequest();
				req.setFields("num_iid,properties,quantity,outer_id");
				req.setOuterId(sku.getOuterSkuId());
				SkusCustomGetResponse response = getTaobaoConnector().execute(req);
				List<com.taobao.api.domain.Sku> skuList=response.getSkus();
				if(CollectionUtil.isEmpty(skuList)){
					throw new ProductException("exception.product.sku.duplicate.outerid",sku.getOuterSkuId());
				}
				skuPrices.append(sku.getPrice()).append(",");
				for(int i=0;i<skuList.size();i++){
					if(skuList.get(i).getNumIid()==521414406198L){
						skuProps.append(skuList.get(i).getProperties()).append(",");
						skuquantities.append(skuList.get(i).getQuantity()).append(",");
						skuoutids.append(skuList.get(i).getOuterId()).append(",");
					}
				}
			}
			updateReq.setSkuQuantities(skuquantities.substring(0, skuquantities.length()-1));
			updateReq.setSkuOuterIds(skuoutids.substring(0, skuoutids.length()-1));
			updateReq.setSkuPrices(skuPrices.substring(0, skuPrices.length()-1));
			updateReq.setSkuProperties(skuProps.substring(0, skuProps.length()-1));
			ItemPriceUpdateResponse response = getTaobaoConnector().execute(updateReq);
			System.out.println(response);
	}
	public static void main(String[] args) throws ProductException  {
		TmallProductCreateTest t = new TmallProductCreateTest();
		t.xxx();
	//	t.onShelf();
		   //t.getchannel();
		//t.updateSkuInventoryBatch();
	}
	
	public void processResponse(TaobaoResponse response) {
		/*System.out.println("errorCode:" + response.getErrorCode() + ",subCode:" + response.getSubCode());
		System.out.println("msg:" + response.getMsg() + ",subMsg:" + response.getSubMsg());
		System.out.println(response.getBody());*/
	}
}
