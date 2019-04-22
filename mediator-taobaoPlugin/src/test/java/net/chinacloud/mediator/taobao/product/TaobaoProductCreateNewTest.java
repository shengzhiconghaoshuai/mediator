package net.chinacloud.mediator.taobao.product;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;

import org.junit.Test;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.domain.User;
import com.taobao.api.request.UserSellerGetRequest;
import com.taobao.api.response.UserSellerGetResponse;
import com.taobao.top.schema.exception.TopSchemaException;

public class TaobaoProductCreateNewTest {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Connector<TaobaoRequest<?>> getTaobaoConnector() {
		ApplicationParam param = new ApplicationParam("12316299",
				"094c5a4daa5dda068b8228724fd47a74",
				"http://gw.api.tbsandbox.com/router/rest",
				"6100416877b49d34597781a310d55c0bd2c3039f9562632728274110",
				null, null, null, null, null);
		Connector connector = new TaobaoConnector(param);
		return connector;
	}

	@Test
	public void test() throws TopSchemaException {
		//Long categoryId = 50008901L;
		//Long categoryId = 60118003L;
		//1、判断用户类型,B or C
		//getSellerUser();
		
		//2、获取淘宝商品发布规则
		//String schema = getProductMatchSchema(categoryId);
		
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
	
	public void getItemAddSchema(Long categoryId) {
		//ItemAddSchemaGetRequest request = 
	}
	
	public void processResponse(TaobaoResponse response) {
		System.out.println("errorCode:" + response.getErrorCode() + ",subCode:" + response.getSubCode());
		System.out.println("msg:" + response.getMsg() + ",subMsg:" + response.getSubMsg());
		System.out.println(response.getBody());
	}
}
