/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TmallAttributeTest.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.product;

import java.util.List;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.dao.ProductCategoryMappingDao;
import net.chinacloud.mediator.taobao.domain.ProductCategoryMapping;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.SpringUtil;

import org.junit.Before;
import org.junit.Test;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.domain.Product;
import com.taobao.api.request.ProductsSearchRequest;
import com.taobao.api.response.ProductsSearchResponse;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月4日 下午4:07:23
 */
public class TmallAttributeTest {

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
	
	@Before
	public void before() {
		//ApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
	}

	@Test
	public void save() {
		Long channelCategoryId = 122590003L;
		ProductCategoryMappingDao dao = SpringUtil.getBean(ProductCategoryMappingDao.class);
		
		Long productId = getProduct(channelCategoryId);
		if (null != productId) {
			ProductCategoryMapping mapping = new ProductCategoryMapping();
			mapping.setChannelCategoryId(channelCategoryId);
			mapping.setProductId(productId);
			mapping.setApplicationId(1);
			dao.insertProductCategoryMapping(mapping);
		} else {
			System.out.println("============该类目下没有商品===========");
		}
	}
	
	private Long getProduct(Long cid) {
		ProductsSearchRequest request = new ProductsSearchRequest();
		request.setFields("product_id");
		request.setCid(cid);
		request.setProps("20000:31681");
		request.setPageNo(1L);
		request.setPageSize(5L);
		ProductsSearchResponse response = getTaobaoConnector().execute(request);
		List<Product> products = response.getProducts();
		if (CollectionUtil.isNotEmpty(products)) {
			return products.get(0).getProductId();
		}
		return null;
	}
}
