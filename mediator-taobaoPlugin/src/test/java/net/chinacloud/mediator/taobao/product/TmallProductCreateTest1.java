package net.chinacloud.mediator.taobao.product;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;

import org.junit.Test;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.request.TmallItemAddSchemaGetRequest;
import com.taobao.api.request.TmallProductAddSchemaGetRequest;
import com.taobao.api.request.TmallProductMatchSchemaGetRequest;
import com.taobao.api.response.TmallItemAddSchemaGetResponse;
import com.taobao.api.response.TmallProductAddSchemaGetResponse;
import com.taobao.api.response.TmallProductMatchSchemaGetResponse;

public class TmallProductCreateTest1 {
	
	//Long brandId = 31681L;		//goelia
	//Long brandId = 44612L;		//思美登
	//Long brandId = 31681L;		//GOELIA
	Long brandId = 31888L;		//Nine West\/玖熙
	//String styleNum = "SW12312311";	//款号
	//Long categoryId = 162103L;		//毛衣
	//Long categoryId = 50010850L;		//连衣裙
	//Long categoryId = 162205L;		//牛仔裤
	Long categoryId = 50013196L;		//马甲
	
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

	/**
	 * 2、获取产品匹配的规则
	 */
	@Test
	public void getProductMatchSchema() {
		TmallProductMatchSchemaGetRequest request = new TmallProductMatchSchemaGetRequest();
		request.setCategoryId(categoryId);
		TmallProductMatchSchemaGetResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		String matchResult = response.getMatchResult();
		System.out.println("product match schema:" + matchResult);
	}
	
	/**
	 * 4.1、获取产品发布规则
	 * @param categoryId
	 * @param brandId
	 * @return
	 */
	@Test
	public void getProductAddSchema() {
		TmallProductAddSchemaGetRequest request = new TmallProductAddSchemaGetRequest();
		request.setCategoryId(categoryId);
		request.setBrandId(brandId);
		TmallProductAddSchemaGetResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		String result = response.getAddProductRule();
		
		System.out.println("add product rule:" + result);
	}
	
	/**
	 * 6、获取商品发布的规则
	 */
	@Test
	public void getItemAddSchema() {
		Long productId = 347156571L;
		TmallItemAddSchemaGetRequest request = new TmallItemAddSchemaGetRequest();
		request.setCategoryId(categoryId);
		request.setProductId(productId);
		request.setType("b");		//发布商品类型，一口价填“b”，拍卖填"a"
		
		TmallItemAddSchemaGetResponse response = getTaobaoConnector().execute(request);
		processResponse(response);
		
		String addItemRule = response.getAddItemResult();
		System.out.println("item add rule:" + addItemRule);
	}
	
	public void processResponse(TaobaoResponse response) {
		/*System.out.println("errorCode:" + response.getErrorCode() + ",subCode:" + response.getSubCode());
		System.out.println("msg:" + response.getMsg() + ",subMsg:" + response.getSubMsg());
		System.out.println(response.getBody());*/
	}
}
