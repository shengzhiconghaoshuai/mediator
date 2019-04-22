/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductTest.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.product;

import java.io.File;
import java.io.IOException;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;

import org.junit.Test;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.request.ItemUpdateDelistingRequest;
import com.taobao.api.request.ItemUpdateListingRequest;
import com.taobao.api.request.ItemsCustomGetRequest;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.taobao.api.response.ItemsCustomGetResponse;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年6月29日 下午4:48:47
 */
public class ProductTest {
	
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
	public void testGetItemidByOuterId() {
		ItemsCustomGetRequest request = new ItemsCustomGetRequest();
		request.setOuterId("product12334");
		request.setFields("num_iid");
		ItemsCustomGetResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		System.out.println(response.getItems().get(0).getNumIid());
	}

	@Test
	public void testOffShelf() {
		ItemUpdateDelistingRequest request = new ItemUpdateDelistingRequest();
		request.setNumIid(45643028245L);
		ItemUpdateDelistingResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		System.out.println(response.getItem().getOuterId());
	}
	
	@Test
	public void testOnShelf() {
		ItemUpdateListingRequest request = new ItemUpdateListingRequest();
		request.setNumIid(45643028245L);
		request.setNum(1L);
		ItemUpdateListingResponse response = getTaobaoConnector().execute(request);
		
		processResponse(response);
		
		System.out.println(response.getItem().getOuterId());
	}

	public void processResponse(TaobaoResponse response) {
		System.out.println("errorCode:" + response.getErrorCode() + ",subCode:" + response.getSubCode());
		System.out.println("msg:" + response.getMsg() + ",subMsg:" + response.getSubMsg());
		System.out.println(response.getBody());
	}
	
	@Test
	public void test1() throws IOException{
		File file  = new File("c:/aaa.html");
		file.createNewFile();
	}
	
}
