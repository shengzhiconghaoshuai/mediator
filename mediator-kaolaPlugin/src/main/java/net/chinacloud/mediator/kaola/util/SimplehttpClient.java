package net.chinacloud.mediator.kaola.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import net.chinacloud.mediator.utils.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class SimplehttpClient {

	private static final CloseableHttpClient httpClient;
	public static final String CHARSET = "UTF-8";
 
	static {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}
 
	public static String doGet(String url, Map<String, String> params, Map sysParams) throws ParseException, UnsupportedEncodingException, IOException{
		return doGet(url, params, sysParams, CHARSET);
	}
	public static String doRequest(String url, Map<String, String> params, Map sysParams, String method) throws ParseException, IOException{
		return doRequest(url, params, sysParams, CHARSET, method);
	}
   
    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     */
	public static String doGet(String url,Map<String,String> params, Map sysParams,String charset) throws ParseException, UnsupportedEncodingException, IOException{
		if(StringUtils.isEmpty(url)){
			return null;
		}
      
        if(params != null && !params.isEmpty()){
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for(Map.Entry<String,String> entry : params.entrySet()){
                String value = entry.getValue();
                if(value != null){
                    pairs.add(new BasicNameValuePair(entry.getKey(),value));
                }
            }
            url += "&" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        
        HttpGet httpGet = new HttpGet(url);
        
      /*  Iterator it = sysParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry obj = (Entry) it.next();
			if(StringUtils.hasText(obj.getValue().toString())){
				httpGet.setHeader(obj.getKey().toString(), obj.getValue().toString());
			}
		}*/
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpGet.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null){
            result = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        response.close();
        return result;
    }
     
    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     * @throws IOException 
     * @throws ParseException 
     */
    public static String doRequest(String url,Map<String,String> params, Map sysParams, String charset, String method) throws ParseException, IOException{
    	if(StringUtils.isEmpty(url)){
			return null;
		}
		HttpEntityEnclosingRequestBase httpRequest = null;
		if(method.equals("POST")){
			httpRequest = new HttpPost(url);
		}else if(method.equals("PATCH")){
			httpRequest = new HttpPatch(url);
		}else {
			httpRequest = new HttpPut(url);
			
		}
		if(params != null && !params.isEmpty()){
			httpRequest.setEntity(new StringEntity(JSON.toJSONString(params)));
		}
		/*Iterator it = sysParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry obj = (Entry) it.next();
			if(StringUtils.hasText(obj.getValue().toString())){
				httpRequest.setHeader(obj.getKey().toString(), obj.getValue().toString());
			}
		}*/
		CloseableHttpResponse response = httpClient.execute(httpRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			httpRequest.abort();
		    throw new RuntimeException("HttpClient,error status code :" + statusCode);
		}
		HttpEntity entity = response.getEntity();
		String result = null;
		if (entity != null){
		    result = EntityUtils.toString(entity, "utf-8");
		}
		EntityUtils.consume(entity);
		response.close();
		return result;
        
    }
    
   
}
