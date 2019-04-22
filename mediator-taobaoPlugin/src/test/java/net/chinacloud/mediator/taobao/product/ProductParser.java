/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductParser.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.product;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoParser;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.internal.parser.json.ObjectJsonParser;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年10月10日 上午11:16:46
 */
public class ProductParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductParser.class);
/*
	@Test
	public void testParseProduct() throws FileNotFoundException, IOException, ApiException {
		Resource resource = new FileSystemResource("D:\\tb_product.txt");
		BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
		String line = null;
		TaobaoParser<ItemGetResponse> parser = new ObjectJsonParser<ItemGetResponse>(ItemGetResponse.class);
		while(null != (line = br.readLine())) {
			ItemGetResponse response = parser.parse(line);
			Item item = response.getItem();
			String productOuterId = item.getOuterId();
			LOGGER.info(productOuterId);
		}
		br.close();
	}
	
	@Test
	public void testParseSku() throws FileNotFoundException, IOException, ApiException {
		Resource resource = new FileSystemResource("D:\\tm_product.txt");
		BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
		String line = null;
		TaobaoParser<ItemGetResponse> parser = new ObjectJsonParser<ItemGetResponse>(ItemGetResponse.class);
		while(null != (line = br.readLine())) {
			ItemGetResponse response = parser.parse(line);
			Item item = response.getItem();
			List<Sku> skus = item.getSkus();
			if (CollectionUtil.isNotEmpty(skus)) {
				for(Sku sku : item.getSkus()) {
					if (StringUtils.hasText(sku.getOuterId())) {
						LOGGER.info(sku.getOuterId() + "," + item.getApproveStatus());
					} else {
						System.out.println("sku outer id is null:" + item.getOuterId() + ",title:" + item.getTitle());
						break;
					}
				}
			}
		}
		br.close();
	}
	
	*//**
	 * mediator初始化数据
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ApiException
	 *//*
	@Test
	public void testParseProductMediator() throws FileNotFoundException, IOException, ApiException {
		Set<String> filter = new HashSet<String>();
		
		Resource resource = new FileSystemResource("D:\\tb_product.txt");
		BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
		
		File outer = new File("E:\\Goelia\\product\\categoryMapping\\product\\tb");
		if (!outer.exists()) {
			outer.mkdirs();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outer, "tb_product.sql")));
		String line = null;
		TaobaoParser<ItemGetResponse> parser = new ObjectJsonParser<ItemGetResponse>(ItemGetResponse.class);
		while(null != (line = br.readLine())) {
			ItemGetResponse response = parser.parse(line);
			Item item = response.getItem();
			String productOuterId = item.getOuterId();
			if (StringUtils.hasText(productOuterId)) {
				if (filter.contains(productOuterId)) {
					System.out.println("outer id dup:" + productOuterId);
				} else {
					filter.add(productOuterId);
					StringBuilder str = new StringBuilder("INSERT INTO PRODUCT_PARTNUMBER_MAPPING(CHANNEL_PRODUCT_ID, OUTER_ID, TYPE, APPLICATION_ID) VALUES('").append(item.getNumIid().toString()).append("' ,'").append(productOuterId).append("' , 0, 3);").append("\r");
					write(bw, str.toString());
				}
			} else {
				System.out.println("outer id is null:" + item.getTitle());
			}
		}
		br.close();
		bw.close();
	}
	
	*//**
	 * oms上下架初始化数据
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ApiException
	 *//*
	@Test
	public void testParseProductOms() throws FileNotFoundException, IOException, ApiException {
		Set<String> filter = new HashSet<String>();
		
		Resource resource = new FileSystemResource("D:\\tb_product.txt");
		BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
		
		File outer = new File("E:\\Goelia\\product\\categoryMapping\\product\\tb");
		if (!outer.exists()) {
			outer.mkdirs();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outer, "tb_oms.sql")));
		String line = null;
		TaobaoParser<ItemGetResponse> parser = new ObjectJsonParser<ItemGetResponse>(ItemGetResponse.class);
		while(null != (line = br.readLine())) {
			ItemGetResponse response = parser.parse(line);
			Item item = response.getItem();
			String productOuterId = item.getOuterId();
			if (StringUtils.hasText(productOuterId)) {
				if (filter.contains(productOuterId)) {
					System.out.println("outer id dup:" + productOuterId);
				} else {
					filter.add(productOuterId);
					StringBuilder str = new StringBuilder("UPDATE T_PRODUCT_STORE SET PUBLISHED = 1, STATUS = ").append(item.getApproveStatus().equals("onsale") ? 1 : 0).append(" WHERE PRODUCT_ID = (SELECT PRODUCT_ID FROM T_PRODUCT WHERE PARTNUMBER = '").append(item.getOuterId()).append("') AND STORE_ID= 1003;").append("\r");
					write(bw, str.toString());
				}
			} else {
				System.out.println("outer id is null:" + item.getTitle());
			}
		}
		br.close();
		bw.close();
	}
	
	private void write(BufferedWriter bw, String str) throws IOException {
		bw.write(str);
	}
	*/
	@Test
	public void test() {
		LOGGER.info("get jdp order data with currentPage {}, sellerNick {}, offset {}, rows {}", 1, "zhangsan", 1, 200);
	}
}
