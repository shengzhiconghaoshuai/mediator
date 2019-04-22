package net.chinacloud.mediator.kaola;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.kaola.util.KopUtils;

import org.junit.Test;

public class SignTest {
	@Test
	public void testSign(){
		String access_token = "b6ee443b-cba6-4327-83ae-0de0afd58c95";
		String appKey = "edb6c3b9ac4847e7584c38e2b630b14f";
		String appSecret = "8200ee92ec22fcae76e2f00bc5c79247188e0593";
		String method = "kaola.order.search";
		
		Map<String,Object> mapSign = new HashMap<String,Object>();
		mapSign.put("access_token", access_token);
		mapSign.put("app_key", appKey);
		mapSign.put("method", method);
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String timestamp = sdf.format(new Date());
		System.out.println("timestamp = "+timestamp);
		mapSign.put("timestamp", timestamp);
		mapSign.put("v", "0.2.35");
		
		String sign = KopUtils.sign(mapSign, appSecret);
		System.out.println("sign="+sign);
		
		
	}
	@Test
	public void testpage(){
		int PAGE_COUNT = 10;
		int totalResults = 100;
		int currPageIndex = 0;
		for(int i=0;i<totalResults;i++){
			int lastPageIndex = totalResults % PAGE_COUNT == 0 ? totalResults
					/ PAGE_COUNT
					: totalResults / PAGE_COUNT + 1;
			int pageIndex = currPageIndex + 1;
			System.out.println("lastPageIndex:"+lastPageIndex+",pageIndex:"+pageIndex);
		}
	}

	
	
}
