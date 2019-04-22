package net.chinacloud.mediator.taobao.product;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;

import org.junit.Test;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.request.TmallItemAddSchemaGetRequest;
import com.taobao.api.response.TmallItemAddSchemaGetResponse;

public class TmallItemRuleGetTest {
	
	private Long categoryId = 50008899L;
	private Long productId = 299762030L;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Connector<TaobaoRequest<?>> getTaobaoConnector() {
		//生产sm沙箱
		ApplicationParam param = new ApplicationParam("1012339797",
				"sandboxf10b366ceb2a23b0955ab15d6",
				"http://gw.api.tbsandbox.com/router/rest",
				"6102208d370a00bb18e2996121f2f44c0a06e058f47d1933638360709",
				null, null, null, null, null);
		Connector connector = new TaobaoConnector(param);
		return connector;
	}

	@Test
	public void getItemAddSchema() {
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
		System.out.println("errorCode:" + response.getErrorCode() + ",subCode:" + response.getSubCode());
		System.out.println("msg:" + response.getMsg() + ",subMsg:" + response.getSubMsg());
		//System.out.println(response.getBody());
	}
}
