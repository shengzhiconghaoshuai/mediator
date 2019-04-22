/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：GrantTest.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.grant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.taobao.api.internal.util.WebUtils;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月15日 上午10:34:48
 */
public class GrantTest {
	
	//private static final String authorizeUrl = "https://oauth.tbsandbox.com/authorize";
	private static final String authorizeUrl = "https://oauth.taobao.com/authorize";
	//private static final String tokenUrl = "https://oauth.tbsandbox.com/token";
	private static final String tokenUrl = "https://oauth.taobao.com/token";
	//U9zjtNasMlLwe7GGY5HAyyTz3150
	@Test
	public void testGrant() {
		WebUtils.setIgnoreSSLCheck(true);
		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", "1012339797");
		params.put("response_type", "code");
		params.put("redirect_uri", "http://www.wuxicloud.com");
		
		try {
			String result = WebUtils.doPost(authorizeUrl, params, 10000, 10000);
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAuth() {
		WebUtils.setIgnoreSSLCheck(true);
		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", "12316299");
		params.put("client_secret", "5fc8324f0b76fac2b284e80eab8fc331");
		params.put("grant_type", "authorization_code");
		params.put("code", "DW0vxFGW3RcIG7Y0CYvLIRQ1198917");
		params.put("redirect_uri", "http://www.google.com.hk");
		
		try {
			String result = WebUtils.doPost(tokenUrl, params, 10000, 10000);
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
