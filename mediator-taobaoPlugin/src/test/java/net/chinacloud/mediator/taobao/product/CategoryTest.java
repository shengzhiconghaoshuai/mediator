/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CategoryTest.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.product;

import java.util.List;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.junit.Test;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.domain.Brand;
import com.taobao.api.request.ItemcatsAuthorizeGetRequest;
import com.taobao.api.response.ItemcatsAuthorizeGetResponse;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月10日 上午10:57:37
 */
public class CategoryTest {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Connector<TaobaoRequest<?>> getTaobaoConnector() {
		ApplicationParam param = new ApplicationParam("12201527",
				"88652c5476edc325476973ac71645fcd",
				"http://gw.api.taobao.com/router/rest",
				"6101c281f591cb0168836643d8929be9588713b2ef9e215133006562",
				null, null, null, null, null);
		Connector connector = new TaobaoConnector(param);
		return connector;
	}

	@Test
	public void testGetBrand() {
		ItemcatsAuthorizeGetRequest request = new ItemcatsAuthorizeGetRequest();
		request.setFields("brand.vid, brand.name");
		ItemcatsAuthorizeGetResponse response = getTaobaoConnector().execute(request);
		List<Brand> brands = response.getSellerAuthorize().getBrands();
		if (CollectionUtil.isNotEmpty(brands)) {
			for (Brand brand : brands) {
				System.out.println(brand.getName() + ":" + brand.getPid() + ":" + brand.getVid());
			}
		}
	}

}
