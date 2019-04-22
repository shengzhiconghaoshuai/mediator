package net.chinacloud.mediator.jingdong.product;

import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.domain.category.Category;
import com.jd.open.api.sdk.request.category.CategoryAttributeSearchRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeValueSearchRequest;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse.Attribute;
import com.jd.open.api.sdk.response.category.CategoryAttributeValueSearchResponse;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;

public class JingdongProductTest {

		 //创建一个京东的连接
		public static JdClient getJingdongConnector(){
			JdClient client=new DefaultJdClient("http://gw.api.360buy.com/routerjson","da60830b-b97b-4571-8393-8fb87faa3c88",
					"1C8E24557ACCC919B1F85978224965CD","a7747b70f9734fdb9efd72780daed1f4");
			return client;
		}
		
		@Test
		public void wareCatsGet() throws JdException{
			/**
			 * 获取商家类目信息   Category实体类中包含 id  fid  name index_id status lev isParent 字段
			 * id 类目id
			 * fid 父类目id
			 * name 类目名称
			 * index_id 排序(越小越靠前)
			 * status  类目状态 (DELETED,UNVALID,VALID)
			 * lev 等级（类目分为1 2 3 级）
			 * isParent  该类目是否为父类目(即：该类目是否还有子类目)
			 */
			CategorySearchRequest request = new CategorySearchRequest(); //获取目录的方法名
			//-----------应用级别参数  必须输入 -------------------------
			//request.setFields("name"); //需返回的字段列表。未设置参数时，默认返回全部值  示例值 id,fid,status,lev,name,index_id 
			CategorySearchResponse response = getJingdongConnector().execute(request);
			List<Category> category =  response.getCategory();
			for(Category c : category){
				System.out.println(JSON.toJSON(c));
	        	System.out.println("----------------------------------------------");
			}
		}
		
		
		@Test
		public void categoryAttributeSearchRequest() throws JdException{
			/**
			 * 获取类目属性信息
			 */
			CategoryAttributeSearchRequest request=new CategoryAttributeSearchRequest();
			//-------------应用级别参数  必须输入----------------------------------------
			request.setCid("1355"); //类目id
			//request.setFields( "jingdong" );
			
			//--------------应用级别参数  可选 非必须-------------------------------------
//			request.setKeyProp( "jingdong" );  是否关键属性
//			request.setSaleProp( "jingdong" ); 是否销售属性
//			request.setAid( "jingdong" );  属性id
			CategoryAttributeSearchResponse response = getJingdongConnector().execute(request);
	        List<Attribute>  attr =  response.getAttributes();
	        for(Attribute a : attr){
	        	System.out.println(JSON.toJSON(a));
	        	System.out.println("----------------------------------------------");
	        }
		}
		
		@Test
		public void categoryAttributeValueSearchRequest() throws JdException{
			/**
			 * 获取类目下的属性值信息
			 */
			CategoryAttributeValueSearchRequest request = new CategoryAttributeValueSearchRequest();
			request.setAvs("1000000031");   
			//request.setFields( "jingdong" );
			CategoryAttributeValueSearchResponse  response = getJingdongConnector().execute(request);
			System.out.println(JSON.toJSON(response));
			List<AttValue> attValues = response.getAttValues();
			 for(AttValue a : attValues){
	             System.out.println(JSON.toJSON(a));
				 System.out.println("----------------------------------------------");
		        }
		}
		
	/*	public static void main(String[] args) throws JdException {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(sdf.format(new Date()));
			String categoryId ="9719";
			net.chinacloud.mediator.domain.Category c = new net.chinacloud.mediator.domain.Category();
			c.setCategoryId(categoryId);
			c.setChannelCategoryId(categoryId);
			*//**
			 * 获取类目属性信息
			 *//*
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
	        	CategoryAttributeValueSearchRequest cavsrequest = new CategoryAttributeValueSearchRequest();
	        	cavsrequest.setAvs(String.valueOf(a.getAid())); 
	        	if(a.getSaleProp()){
	        		CategoryAttributeValueSearchResponse  cavsresponse = getJingdongConnector().execute(cavsrequest);
		    		List<AttValue> attValues = cavsresponse.getAttValues();
		    		for(AttValue a1 : attValues){
		    			StringBuilder sb = new StringBuilder();
		    			sb.append("insert into T_PRODUCT_MAPPING (NAME, CATALOG_ID, STORE_TYPE, VALUE) values ( ");
		    			sb.append("'").append(a1.getName()).append("'");
		    			sb.append(", 310, 3, ");
		    			sb.append("'").append(a1.getAid()).append(":").append(a1.getVid()).append("'").append(" );");
		    			System.out.println(sb.toString());
		    	      }	
	        	}
	    		
	        }
		   System.out.println(JSON.toJSONString(c.getAttributes()));
		   System.out.println(sdf.format(new Date()));		
		}
		
		*/
		
		
		
}
