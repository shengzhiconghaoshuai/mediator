package net.chinacloud.mediator.jingdong.product;

import java.util.List;

import net.chinacloud.mediator.domain.Category;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.request.category.CategoryAttributeSearchRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeValueSearchRequest;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse.Attribute;
import com.jd.open.api.sdk.response.category.CategoryAttributeValueSearchResponse;

public class JingdongimgCreateTest {

	 //创建一个京东的连接
		public static JdClient getJingdongConnector(){
			JdClient client=new DefaultJdClient("http://gw.api.360buy.com/routerjson","da60830b-b97b-4571-8393-8fb87faa3c88",
					"1C8E24557ACCC919B1F85978224965CD","a7747b70f9734fdb9efd72780daed1f4");
			return client;
		}
		
		public void getAttribute(String categoryId) throws JdException{
			Category c = new Category();
			c.setChannelCategoryId(categoryId);
			/**
			 * 获取类目属性信息
			 */
			CategoryAttributeSearchRequest request=new CategoryAttributeSearchRequest();
			request.setCid(categoryId); //类目id
			CategoryAttributeSearchResponse response = getJingdongConnector().execute(request);
	        List<Attribute>  attr =  response.getAttributes();
	        for(Attribute a : attr){
	        	net.chinacloud.mediator.domain.Attribute  attr1 = new net.chinacloud.mediator.domain.Attribute();
	        	attr1.setAttributeId(String.valueOf(a.getAid()));
	        	attr1.setAttributeName(String.valueOf(a.getName()));
	        	attr1.setColorProperty(a.isColorProp());
	        	attr1.setKeyProperty(a.getKeyProp());
	        	attr1.setRequired(Boolean.parseBoolean(a.getReq()));
	        	attr1.setSalesProperty(a.getSaleProp());
	        	if(1==a.getInputType()){
	        		attr1.setType(net.chinacloud.mediator.domain.Attribute.ATTRIBUTE_TYPE_SELECT);
	        	}else if(2==a.getInputType()){
	        		attr1.setType(net.chinacloud.mediator.domain.Attribute.ATTRIBUTE_TYPE_SELECT);
	        	}else if(3==a.getInputType()){
	        		attr1.setType(net.chinacloud.mediator.domain.Attribute.ATTRIBUTE_TYPE_INPUT);
	        	}
	        	if(Boolean.parseBoolean(a.getReq()) && !a.getSaleProp()){
	        		CategoryAttributeValueSearchRequest cavsrequest = new CategoryAttributeValueSearchRequest();
		        	cavsrequest.setAvs(String.valueOf(a.getAid()));   
		    		CategoryAttributeValueSearchResponse  cavsresponse = getJingdongConnector().execute(cavsrequest);
		    		List<AttValue> attValues = cavsresponse.getAttValues();
		    		for(AttValue a1 : attValues){
		    			net.chinacloud.mediator.domain.AttributeValue attValue = new net.chinacloud.mediator.domain.AttributeValue();
		    			attValue.setId(String.valueOf(a1.getVid()));
		    			attValue.setName(a1.getName());
		    			attr1.addAttributeValue(attValue);
		    	      }
		    		 c.addAttribute(attr1);
	        	}
	        }
	        System.out.println(JSON.toJSONString(c.getAttributes()));
			
		}
		
		@Test
		public void test() throws JdException{
			getAttribute("9719");
		}
		
}
