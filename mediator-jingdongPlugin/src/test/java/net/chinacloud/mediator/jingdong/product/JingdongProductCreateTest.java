package net.chinacloud.mediator.jingdong.product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.ProductPartnumberMappingDao;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.exception.product.PropertyRequireException;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.ImageUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.FileItem;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.domain.category.Category;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.domain.ware.WarePropimg;
import com.jd.open.api.sdk.request.category.CategoryAttributeSearchRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeValueSearchRequest;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.request.order.OrderSearchRequest;
import com.jd.open.api.sdk.request.ware.SkuCustomGetRequest;
import com.jd.open.api.sdk.request.ware.WareAddRequest;
import com.jd.open.api.sdk.request.ware.WareDeleteRequest;
import com.jd.open.api.sdk.request.ware.WareDelistingGetRequest;
import com.jd.open.api.sdk.request.ware.WareGetRequest;
import com.jd.open.api.sdk.request.ware.WareInfoByInfoRequest;
import com.jd.open.api.sdk.request.ware.WarePropimgAddRequest;
import com.jd.open.api.sdk.request.ware.WarePropimgsSearchRequest;
import com.jd.open.api.sdk.request.ware.WareSkuAddRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateDelistingRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateListingRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateRequest;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse.Attribute;
import com.jd.open.api.sdk.response.category.CategoryAttributeValueSearchResponse;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;
import com.jd.open.api.sdk.response.ware.SkuCustomGetResponse;
import com.jd.open.api.sdk.response.ware.WareAddResponse;
import com.jd.open.api.sdk.response.ware.WareDeleteResponse;
import com.jd.open.api.sdk.response.ware.WareDelistingGetResponse;
import com.jd.open.api.sdk.response.ware.WareGetResponse;
import com.jd.open.api.sdk.response.ware.WareInfoByInfoSearchResponse;
import com.jd.open.api.sdk.response.ware.WarePropimgAddResponse;
import com.jd.open.api.sdk.response.ware.WarePropimgsSearchResponse;
import com.jd.open.api.sdk.response.ware.WareSkuAddResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateListingResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateResponse;

public class JingdongProductCreateTest {
		
		
		@org.junit.Before
		public void Before() {
			ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-test.xml");
		}
		
	    //创建一个京东的连接
		private static JdClient getJingdongConnector(){
	
			return new DefaultJdClient("https://api.jd.com/routerjson", "ab427a93-d6ee-440f-95bc-a4e036df5f7c",
					"1C8E24557ACCC919B1F85978224965CD", "a7747b70f9734fdb9efd72780daed1f4"); //歌莉娅   
			
			/*return new DefaultJdClient("https://api.jd.com/routerjson", "25312f79-62e5-4ba2-97a0-9e28602aecd5",
					"957FBD9E2D2B36A2DA6127FA8038AC9E", "78e7a3341b8448488844b13412445da4");*/
			/*return new DefaultJdClient("https://api.jd.com/routerjson", "8dc5c8f0-2521-4dc6-a7f7-7bb26fb18782",
					"1C8E24557ACCC919B1F85978224965CD", "a7747b70f9734fdb9efd72780daed1f4");*/
			
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
			CategorySearchResponse response = this.getJingdongConnector().execute(request);
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
			request.setCid("9719"); //类目id
			//request.setFields( "jingdong" );
			
			//--------------应用级别参数  可选 非必须-------------------------------------
	//		request.setKeyProp( "jingdong" );  是否关键属性
	//		request.setSaleProp( "jingdong" ); 是否销售属性
	//		request.setAid( "jingdong" );  属性id
			CategoryAttributeSearchResponse response = this.getJingdongConnector().execute(request);
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
			request.setAvs("89311");   
			//request.setFields( "jingdong" );
			CategoryAttributeValueSearchResponse  response = this.getJingdongConnector().execute(request);
			System.out.println(JSON.toJSON(response));
			List<AttValue> attValues = response.getAttValues();
			 for(AttValue a : attValues){
	             System.out.println(JSON.toJSON(a));
				 System.out.println("----------------------------------------------");
		        }
		}
		
	   @Test
	   public void  wareaddrequest() throws Exception{
		   /**
		    * 新增一个商品
		    */
		   WareAddRequest wareAddRequest = new WareAddRequest();  //新增商品的方法名
		   
		   //------------商品的必填信息-----------------------------------
		   wareAddRequest.setCid("1354"); //	类目id
		   wareAddRequest.setTitle("测试商品衬衫5646"); //商品标题
		   wareAddRequest.setStockNum("20"); //库存
		   wareAddRequest.setLength("100"); //长
		   wareAddRequest.setWide("200"); //宽
		   wareAddRequest.setHigh("100"); //高
		   wareAddRequest.setWeight("2"); //重量
		   wareAddRequest.setMarketPrice("50"); //市场价, 精确到2位小数，单位:元
		   wareAddRequest.setJdPrice("40"); // 京东价,精确到2位小数，单位:元 
		   String notes = "<br/> <img src='http://img30.360buyimg.com/popWareDetail/jfs/t1633/298/310580460/48973/b916e1cf/55717eb2N0c7a927b.jpg' alt='' id='6336e9ade88f44f38b50659461b9ca59' /><br />"
		   		+ " <img src='http://img30.360buyimg.com/popWareDetail/jfs/t1153/204/1048688559/5788/5351b3f4/556ff46cNc741dfbb.png' "
		   		+ "alt='' id='2998e8c889414e1e8205df4bc7397c22' /><br /> "
		   		+ " <img src='http://img30.360buyimg.com/popWareDetail/jfs/t1582/190/282933894/107069/eac59351/556fef95N49dee813.jpg'"
		   		+ " alt='' id='010a3182292143b78299473e2294d67d' />这是衬衫";
		   wareAddRequest.setNotes(notes); //描述（最多支持3万个英文字符）  
		  
		   wareAddRequest.setAttributes("3205:53285|8904:222036"); //商品属性列表,多组之间用|分隔，格式:aid:vid 
		                                               //或 aid:vid|aid1:vid1 或 aid1:vid1（需要从类目服务接口获取） 
		                                               //如输入类型input_type为1或2，则attributes为必填属性；
		                                               //如输入类型input_type为3，则用字段input_str填入属性的值 
		   wareAddRequest.setWareBigSmallModel("2"); //商品件型
		   wareAddRequest.setWarePackType("2"); // 商品包装
		   wareAddRequest.setWareImage(ImageUtil.image2Bytes("D:\\jd.jpg")); //图片信息（图片尺寸为800*800，单张大小不超过 1024K）
		   //-------------商品的可选信息----------------------------------
	//	   wareAddRequest.setTradeNo("tradeNo"); //流水号
		   wareAddRequest.setOptionType("offsale"); //是否为上架或者下架  默认为下架状态
	//	   wareAddRequest.setItemNum("itemNum"); //外部商品编号，对应商家后台货号
	//	   wareAddRequest.setProducter("producter"); //生产厂商
		   wareAddRequest.setWrap("包装规格"); // 包装规格
	//	   wareAddRequest.setCostPrice("costPrice");//进货价
	//	   wareAddRequest.setPackListing("packListing"); //包装清单
	//	   wareAddRequest.setService("service"); //售后服务
		   wareAddRequest.setSkuProperties("1000000043:1504584653^1000000031:1504485380|1000000043:1509737382^000000031:1504485411"); //sku 属性,一组sku 属性之间用^分隔，多组用|分隔格式:aid:vid^aid1:vid2|aid3:vid3^aid4:vid4 （需要从类目服务接口获取）
		 //  wareAddRequest.setSkuPrices("40"); //sku 价格,多组之间用‘|’分隔，格式:p1|p2 
	      // wareAddRequest.setSkuStocks("20"); //sku 库存,多组之间用‘|’分隔， 格式:s1|s2
		   //wareAddRequest.setPropertyAlias("propertyAlias"); //自定义属性值别名： 属性ID:属性值ID:别名 ，多组之间用^分开，如aid:vid:别名^aid1:vid1:别名1 
	       //wareAddRequest.setOuterId("1241414"); //SKU外部ID，对个之间用‘|’分隔格，比如：sdf|sds（支持没有sku的情况下，可以输入外部id，并将外部id绑定在默认生成的sku上），对应商家后台‘商家skuid’ 
		   
	       wareAddRequest.setAdContent("又好又便宜，赶紧来买"); //广告词内容最大支持45个字符 
	       //wareAddRequest.setListTime("listTime");  //定时上架时间 时间格式：yyyy-MM-dd HH:mm:ss;规则是大于当前时间，10天内。 
	       
	       
	       WareAddResponse response = this.getJingdongConnector().execute(wareAddRequest);
		   System.out.println(response.getCreated());
		   System.out.println(response.getUrl());
		   System.out.println(response.getZhDesc());
		   System.out.println(response.getWareId());
	   }
	   
	   @Test
	   public void wareUpdateRequest() throws Exception{
		   /**
		    * 修改一个商品
		    */
		   WareUpdateRequest request = new WareUpdateRequest(); //修改商品的方法名
		   //----------------商品的必填信息---------------------------------------------------------
		   request.setWareId("1270567042"); //商品id 
		   //request.setTimestamp(ImageUtil.getTime()); //时间戳
		   //-----------------商品的非必选项------------------------------------------------------------------
		   //request.setShopCategory("shopcategory"); //自定义店内分类(保留字段)
		   request.setTitle("修改测试商品2"); //标题 
		   request.setPayFirst("true"); //是否先款后货 ,false为否,true为是
		   request.setCanVAT("false"); //发票限制：非必须输入，true为限制，false为不限制开增值税发票，FBP、LBP、SOPL、SOP类型商品均可输入； 
		   request.setImported("true"); //是否是进口商品，非必须输入，false为否，true为是，FBP类型商品可输入； 
		   //request.setHealthProduct("true"); //是否保健品：非必须输入，false为否，true为是，FBP类型商品可输入； 
	       //request.setSerialNo("serialNo"); //是否序列号管理：非必须输入，false为否，true为是，FBP类型商品可输入；
	       request.setWareBigSmallModel("7"); //商品件型：必须输入，0免费、1超大件、2超大件半件、3大件、4大件半件、5中件、6中件半件、7小件、8超小件，FBP类型商家适用； 
	       request.setWarePackType("1"); //商品包装：必须输入，1普通商品、2易碎品、3裸瓶液体、4带包装液体、5按原包装出库，FBP类型商家适用； 
	       request.setShelfLife("false"); //是否保质期管理商品, false为否，true为是 
	       //request.setUpcCode("upccode"); //upc编码
	       request.setOptionType("offsale"); //操作类型，现只支持：offsale 或onsale 下架类型请传入：offsale 上架类型请传入：onsale 如果不传操作类型，商品保持原状态 
	       //request.setItemNum("item_num"); //外部商家编号，对应商家后台"货号"
	//       request.setStockNum("stocknum"); //库存数(针对FBP无效)
	//       request.setProducter("producter"); //生产厂商
	//       request.setWrap("wrap"); //包装规格
	//       request.setLength(""); //长(单位:mm) 如不确定，默认为0。 
	//       request.setWide(""); //宽(单位:mm) 如不确定，默认为0。 
	//       request.setHigh(""); //高(单位:mm) 如不确定，默认为0。 
	//       request.setWeight(""); //重量(单位:kg) 如不确定，默认为0。 
	//       request.setCostPrice(""); //进货价'
	//       request.setMarketPrice(""); //市场价
	//       request.setJdPrice(""); //京东价
	//       request.setNotes("修改测试商品"); //描述
	//       request.setSkuProperties(""); //sku属性
	//       request.setAttributes(""); //商品属性列表
	//       request.setSkuPrices(skuPrices);
	//       request.setSkuStocks(skuStocks);
	//       request.setOuterId(outerId);
	//       request.setAdContent(adContent);
	//       request.setListTime(listTime);
		   WareUpdateResponse response = this.getJingdongConnector().execute(request);
		   System.out.println(JSON.toJSONString(response));
	   }
	   
	   @Test
	   public void wareDeleteRequest() throws JdException {
		   /**
		    *删除一个商品
		    */
		   WareDeleteRequest request = new WareDeleteRequest();
		   
		   //-----------应用级别参数  必须输入 -------------------------
		   request.setWareId("1269966155"); // 商品的id
	       //request.setTradeNo(tradeNo); //流水号   注：京东api上写这必须输入的属性值，但是经过测试，不set该字段也可以正常删除一个商品
		   WareDeleteResponse response = this.getJingdongConnector().execute(request);
		   System.out.println(JSON.toJSON(response));
	   }
	   
	   
	   @Test
	   public void wareGetRequest() throws JdException{
		   WareGetRequest request = new WareGetRequest();
		   /**
		    * 根据商品ID查询单个商品的详细信息
		    */
		   //--------------- 必须输入-------------------------
		   request.setWareId("1270291890"); // 商品的id 
		   
		   //----------------非必须输入 -------------------------------
		   //request.setFields("fields"); // 需返回的字段列表。可选值：ware结构体中的所有字段；字段之间用,分隔 
		   WareGetResponse response = this.getJingdongConnector().execute(request);
		   System.out.println(JSON.toJSON(response.getWare()));
		   
	   }
	   
	   @Test
	   public void  warePropimgAddRequest() throws Exception{
		   /**
		    * 根据商品id以及属性id添加相应的图片
		    *
		    */
		   WarePropimgAddRequest request = new WarePropimgAddRequest();
		   request.setMainPic(true);
		   //request.setTimestamp(ImageUtil.getTime());
		   request.setWareId("1282457705");
		   //request.setAttributeValueId("1504477528");
		   //request.setImage(new FileItem(new File("D:\\jd.jpg")));//new FileItem()
		   request.setImage(new FileItem("测试图片",ImageUtil.image2Bytes("D:\\jd.jpg")));
		   WarePropimgAddResponse response = this.getJingdongConnector().execute(request);
		   System.out.println(JSON.toJSON(response));
		   
	   }
	   
	   
	   @Test
	   public void wareSkuAddRequest() throws JdException{
		   WareSkuAddRequest request = new WareSkuAddRequest();
		   /**
		    * 增加sku信息
		    */
		   //-------------应用级别参数  必须输入------------------------
		   request.setWareId("1270052122");  //商品id
		   request.setJdPrice("50"); //京东价格
		   request.setAttributes("15067:227034"); //sku属性  示例值100041:150041^1000046:15844
		   request.setStockNum("30"); //库存 	
		   //--------------应用级别参数  可选 非必须-------------------------------------
		   //request.setTradeNo(tradeNo); //流水号
		   //request.setOuterId(outerId); //外部id
		   WareSkuAddResponse response = this.getJingdongConnector().execute(request);
		   System.out.println(JSON.toJSON(response));
	   }
	   
	   @Test
	   public void warePropimgsSearchRequest() throws JdException{
		   /**
		    * 根据商品Id，检索商品图片
		    */
		   WarePropimgsSearchRequest  request = new WarePropimgsSearchRequest();
		   //-------------应用级别参数  必须输入------------------------
		   request.setWareId("1267483440"); //商品的id
		   request.setPage("1");   //分页 
		   request.setPageSize("10"); //每页多少条 
		   //request.setTimestamp(ImageUtil.getTime()); //时间戳，格式为yyyy-MM-dd HH:mm:ss，例如：2011-06-16 13:23:30
		   //--------------应用级别参数  可选 非必须-------------------------------------
		   //requ	est.setFields(fields);  //可选字段 
		   WarePropimgsSearchResponse response = this.getJingdongConnector().execute(request);
		   System.out.println(JSON.toJSON(response));
		   List<WarePropimg> warePropimg =  response.getWarePropimg();
		   for(WarePropimg w : warePropimg){
			   System.out.println(w.getImgUrl());
			   System.out.println(w.getMain());
		   }
	   }
	   
	   @Test
	   public void skuCustomGetRequest() throws JdException{
		   /**
		    * 根据商家设定的sku的外部id获取所对应的sku数据，一个sku的外部id对应一个sku数据
		    */
		   SkuCustomGetRequest request = new SkuCustomGetRequest();
		   request.setOuterId("1241414"); //外部id
		   //request.setTimestamp(ImageUtil.getTime());
		   SkuCustomGetResponse response = this.getJingdongConnector().execute(request);
		   System.out.println(JSON.toJSON(response.getSku()));
	   }
	   
	   @Test
	   public void orderSearchRequest(){
		   OrderSearchRequest  request = new OrderSearchRequest();
		   request.setStartDate("2012-01-10 12:12:23"); //order_state=WAIT_SELLER_STOCK_OUT 等待出库，
													   //则start_date可以为"否"（开始时间和结束时间均为空，默认返回前一个月的订单）；
													   //order_state=其他值，则start_date必须为"是"
													   //（开始时间和结束时间，不得相差超过1个月。此时间仅针对订单状态及运单号修改的时间）
	
		   request.setEndDate("2012-02-20 12:13:13");//order_state=WAIT_SELLER_STOCK_OUT 等待出库，
												     //则start_date可以为"否"（开始时间和结束时间均为空，默认返回前一个月的订单）；
												     //order_state=其他值，则start_date必须为"是"（开始时间和结束时间，不得相差超过1个月。
												     //此时间仅针对订单状态及运单号修改的时间）
		   request.setOrderState("WAIT_SELLER_STOCK_OUT"); //查询时间类型，默认按修改时间查询。
		                                                   // 1为按订单创建时间查询；其它数字为按订单（订单状态、修改运单号）修改时间
		   request.setPage("1");                       //查询的页数（最大page 50条，请缩短时间间隔，以获取全部订单）
		   request.setPageSize("100");                  //每页的条数（最大page_size 100条）
		   request.setOptionalFields("vender_id,order_id,pay_type");
	   }
	   
	 
	   @Test
	   public void productCancel() throws JdException{
		   WareDeleteRequest request = new WareDeleteRequest();
		   request.setWareId("1283935485");
		   this.getJingdongConnector().execute(request);
	   }


	public void offShelf(Product product) throws ProductException, JdException {
			WareUpdateDelistingRequest request = new WareUpdateDelistingRequest();
			request.setWareId("1283669874"); //商品id
			//request.setTradeNo("tradeNo"); //新版api必须设，但是旧版的京东api可以不设，具体要不要设等待测试,我觉得可以不设
		    this.getJingdongConnector().execute(request);
		}
	   
	   public void onShelf(Product product) throws ProductException, JdException {
			WareUpdateListingRequest request = new WareUpdateListingRequest();
			request.setWareId("1283669874"); //商品id
			//request.setTradeNo("tradeNo"); //新版api必须设，但是旧版的京东api可以不设，具体要不要设等待测试
			WareUpdateListingResponse response = this.getJingdongConnector().execute(request);
			System.out.println(response.getCode());
		}
	   
	   @Test
	   public void updatePrice() throws ProductException, JdException {
			WareUpdateRequest request = new WareUpdateRequest();
			request.setWareId("1290401681"); //更新商品必设属性
			List<Sku> skus = new ArrayList<Sku>();
			Sku s1 = new Sku();
			s1.setOuterSkuId("14SC4A210B45S");
			s1.setPrice(477.00);
			skus.add(s1);
			
			if(CollectionUtil.isNotEmpty(skus)){ //如果skus不为空,更新sku的价格信息,如果为空，则只更新商品的京东价
				StringBuilder sku_prices = new StringBuilder();
				StringBuilder outer_id = new StringBuilder();
				StringBuilder sku_stock = new StringBuilder();
				StringBuilder sku_property = new StringBuilder();
				for(Sku s : skus){
					/**
					 * 调用京东商品更新接口去更新sku的价格，由于该接口要求ku_prices、sku_properties、sku_stocks和outer_id四个参数的sku组数必须保持一致
					 * 所以调用了SkuCustomRequest去获取相关的sku_stock和sku_property数据
					 */
					String outSkuId = s.getOuterSkuId();
					Double skuPrice = s.getPrice();
					if(!StringUtils.hasLength(outSkuId)){
						throw new PropertyRequireException("SKU外部ID");
					}
					if(skuPrice==null){
						throw new PropertyRequireException("SKU价格");
					}
					SkuCustomGetRequest scgRequest = new SkuCustomGetRequest(); //根据商家设定的sku的外部id获取所对应的sku数据，一个sku的外部id对应一个sku数据
					scgRequest.setOuterId(outSkuId); 
					SkuCustomGetResponse scgResponse = this.getJingdongConnector().execute(scgRequest);
					com.jd.open.api.sdk.domain.ware.Sku sku = scgResponse.getSku();
					sku_prices.append(skuPrice).append("|"); //需要更新的价格
					outer_id.append(outSkuId).append("|"); //该sku的外部id
					sku_stock.append(sku.getStockNum()).append("|"); //通过接口获取的sku库存
					sku_property.append(sku.getAttributes()).append("|"); //通过接口获取的sku销售属性组合字符串
				} 
			    request.setSkuPrices(sku_prices.substring(0,sku_prices.length()-1));
			    request.setOuterId(outer_id.substring(0,outer_id.length()-1));
			    request.setSkuStocks(sku_stock.substring(0,sku_stock.length()-1));
			    request.setSkuProperties(sku_property.substring(0,sku_property.length()-1));
			}
		    WareUpdateResponse response = this.getJingdongConnector().execute(request);
		}
	
		   public void exportSql(File file,List<JDProduct> jd){
			   FileOutputStream out=null;
		       OutputStreamWriter osw=null;
		       BufferedWriter bw=null;
		       try {
		           out = new FileOutputStream(file);
		           osw = new OutputStreamWriter(out);
		           bw =new BufferedWriter(osw);
		           //int i = 1; 
		          // for(JDProduct jdp : jd ) {
		        	//bw.append(jdp.getItemNum()).append(",").append(jdp.getStatus()).append(",").append(jdp.getTitle()).append("\r");
//		           	bw.append(" INSERT INTO PRODUCT_PARTNUMBER_MAPPING(CHANNEL_PRODUCT_ID,OUTER_ID,TYPE,APPLICATION_ID) VALUES(  ");
//		           	bw.append("'"+jdp.getChannelProductId()+"'"+", ").append("'"+jdp.getItemNum()+"'"+",").append("0, ").append("10 );").append("\r");
//		           	i++;
		  		   // }
		           
		           for(JDProduct jdp : jd ) {
		        	   bw.append(jdp.getItemNum()).append(",").append(jdp.getChannelProductId()).append("\r");
//		           	bw.append(" UPDATE T_PRODUCT_STORE SET PUBLISHED=1,STATUS= ");
//		           	bw.append(jdp.getStatus());
//		           	bw.append(" WHERE PRODUCT_ID=(SELECT PRODUCT_ID FROM T_PRODUCT WHERE PARTNUMBER= ");
//		           	bw.append("'"+jdp.getItemNum()+"'");
//		           	bw.append(" ) AND STORE_ID= 1006 ;").append("\r");
		  		   }
		           
		       } catch (Exception e) {
		       	e.printStackTrace();
		       }finally{
		           if(bw!=null){
		               try {
		                   bw.close();
		                   bw=null;
		               } catch (IOException e) {
		                   e.printStackTrace();
		               } 
		           }
		           if(osw!=null){
		               try {
		                   osw.close();
		                   osw=null;
		               } catch (IOException e) {
		                   e.printStackTrace();
		               } 
		           }
		           if(out!=null){
		               try {
		                   out.close();
		                   out=null;
		               } catch (IOException e) {
		                   e.printStackTrace();
		               } 
		           }
		       }
		  }
		  
		  public Map<String,Object> waresearch(int currentPage,int id,List<JDProduct> list,List<String> skulist,Map<String,Object> map) throws JdException, IOException{
			   WareInfoByInfoRequest request=new WareInfoByInfoRequest();
			   request.setPage(String.valueOf(currentPage));
			   request.setPageSize("100");
			   request.setFields("ware_id,ware_status,item_num,title");
			   WareInfoByInfoSearchResponse response = this.getJingdongConnector().execute(request);
			   List<Ware> wares = response.getWareInfos();
			   System.out.println("商品总数:"+response.getTotal());
			   int total = response.getTotal();
			      
			   String itemNum = null;
			   String status = null;
			   for(Ware w : wares){
			   	  boolean bool = true;
			   	  itemNum = w.getItemNum();
			   	 if(!StringUtils.hasText(itemNum)) {
		   		   skulist.add("WareId："+w.getWareId() +"商品标题:" + w.getTitle());
		   		   continue;
		   	     }
		   	   status = w.getWareStatus(); 
		   	   
		   	   JDProduct jdp = new JDProduct();
		   	   jdp.setChannelProductId(String.valueOf(w.getWareId()));
		   	   jdp.setItemNum(itemNum);
		   	   jdp.setStatus(status.equals("ON_SALE") ? "1" : "0");
		   	   //jdp.setStatus(status);
		   	   jdp.setTitle(w.getTitle());
		   	   Iterator<JDProduct> iterator = list.iterator();
		   	   
		   	   while(iterator.hasNext()) {
		   		  String _itemNum = iterator.next().getItemNum();
		   		  if(itemNum.equals(_itemNum)) {
		   			  skulist.add("WareId："+w.getWareId() +"  商品标题:" + w.getTitle() +"  外部商品Id:"+itemNum);
		   			  iterator.remove();
			    	      bool = false;
			    	      break;
			    	   }
		   	   }
		   	   
		   	   if(bool) {
		   		   list.add(jdp);
		   	   }
		      }
		      
		      int totalPage = total % 100 == 0 ? total/100 : total/100+1 ;
		      int pager = currentPage+1;
		      if(pager<=totalPage){
		   	   this.waresearch(pager,id,list,skulist,map);
		      }
		      map.put("product", list);
		      map.put("repeatproduct", skulist);
		      return map;
		  }
		  
		  @Test
		  public void excute() throws JdException, IOException{
			   Map<String,Object>  jd =  waresearch(1,1,new ArrayList<JDProduct>(),new ArrayList<String>(),new HashMap<String,Object>());
			   CSVUtils.exportCsv(new File("D:/JD商家编码没填的、重复的.csv"), (List<String>) jd.get("repeatproduct")); // 外部编码重复，或者没填的商品
			   this.exportSql(new File("D:/JD商品更新.csv"),(List<JDProduct>) jd.get("product"));  //JD平台正常的商品sql
	/*		   List<JDProduct> jdp = (List<JDProduct>) jd.get("product");

			   List<String> str = new ArrayList<String>();
			   List<String> skustr = new ArrayList<String>();
			   
			   StringBuilder sb = new StringBuilder();
			   
			   for(int i=1; i<=jdp.size(); i++) {
				   sb.append(jdp.get(i-1).getChannelProductId()).append(",");
				   if (i%10==0) {
					   str.add(sb.deleteCharAt(sb.lastIndexOf(",")).toString());
					   sb.setLength(0);
				   }
			   }
			  
			   for(String s : str) {
				   try{
					   WareListRequest request = new WareListRequest();
					   request.setFields("ware_id,item_num,skus,ware_status");
					   request.setWareIds(s);
					   WareListResponse wgr = getJingdongConnector().execute(request); 
					   List<Ware> ware = wgr.getWareList();
					   for(Ware w : ware) {
						  String status = w.getWareStatus(); 
						  status = status.equals("ON_SALE") ? "上架" : "下架" ;
						  for(com.jd.open.api.sdk.domain.ware.Sku sk : w.getSkus()) {
							  if(!StringUtils.hasText(sk.getOuterId())) {
								  System.out.println(w.getWareId()+","+w.getItemNum()+","+w.getWareStatus());
								  break;
							  }
							  skustr.add(sk.getOuterId()+","+status);
						  }
					   }
				   } catch(Exception e){
					    System.out.println(s);
				   }
				  
			   }
			   CSVUtils.exportCsv(new File("D:/JD商品Sku.csv"), skustr);*/
		  }
	
		/*  public static void main(String[] args) throws JdException {
			  List<String> list = CSVUtils.importCsv(new File("D:/aa.csv"));
			  List<String> datalist = new ArrayList<String>();
			  StringBuilder sb = new StringBuilder();
              for(String str : list) {
            	  WareGetRequest request = new WareGetRequest();
    			  request.setFields("item_num");
    			  request.setWareId(str);
    			  WareGetResponse response = getJingdongConnector().execute(request);
    			  String outId = response.getWare().getItemNum();
    			  sb.append(" insert into PRODUCT_PARTNUMBER_MAPPING(CHANNEL_PRODUCT_ID,OUTER_ID,TYPE,APPLICATION_ID) values ( ");
    			  sb.append("'"+str+"'").append(", ").append("'"+outId+"'").append(", ").append("0").append(", ").append("10").append("");
    			  
              }
		}
		  
*/		@Test
		public void excutePrice() throws JdException {
			List<String> list  = CSVUtils.importCsv(new File("D:/export.csv"));
			ProductPartnumberMappingDao ppdao = SpringUtil.getBean(ProductPartnumberMappingDao.class);
		    for(String s : list) {
		    	ProductPartnumberMapping p = ppdao.getProductPartnumberMappingByOuterId(s, 10, 0);
		    	System.out.println(s+"     "+p.getOuterId());
		    }
		}
		
		@Test
		public void excutewareId() throws JdException {
			List<String> list = new ArrayList<String>();
			list = excuteQuery(1,list);
			CSVUtils.exportCsv(new File("D:/下架.csv"), list);
		}
		
		public List<String>  excuteQuery(int currentPage,List<String> list) throws JdException {
			WareDelistingGetRequest request = new WareDelistingGetRequest();
			request.setPage(String.valueOf(currentPage));
			request.setPageSize("100");
			request.setFields("ware_id,item_num");
			WareDelistingGetResponse  response = this.getJingdongConnector().execute(request);
			List<Ware> w =  response.getWareInfos(); 
		    for(Ware wa : w) {
		    	list.add(wa.getWareId()+","+wa.getItemNum());
		    }
			int total = response.getTotal();
			int totalPage = total % 100 == 0 ? total/100 : total/100+1 ;
	        int pager = currentPage+1;
	        if(pager<=totalPage){
	   	      this.excuteQuery(pager,list);
	        }
			return list;
		}
		
	/*	public static void main(String[] args) {
          List<String>	list = CSVUtils.importCsv(new File("D:/cc.csv"));
          List<String>  exstr = new ArrayList<String>();
          
          Map<String,String> map = new HashMap<String, String>();
          int i = 0;
          for(String s : list) {
        	  String[] str = s.split(",");
        	  map.put(str[0], str[1]);
        	  System.out.println(i++);
          }
          List<String> list1 = CSVUtils.importCsv(new File("D:/dd.csv"));
          for(String ss : list1) {
        	 exstr.add(ss+","+map.get(ss))  ;
          }
          CSVUtils.exportCsv(new File("D:/export.csv"), exstr);
		}*/
		
//		public static void main(String[] args) throws JdException {
//			OrderSearchRequest request = new OrderSearchRequest();
//			request.setOrderState("WAIT_SELLER_STOCK_OUT");
//			request.setStartDate("2015-11-19 00:00:00");
//			request.setEndDate("2015-11-19 13:00:00");
//			request.setPage("1");
//			request.setPageSize("100");
//			request.setOptionalFields("vender_id,order_id,pay_type,order_total_price,order_seller_price,freight_price,seller_discount,order_payment,delivery_type,order_state,order_state_remark,invoice_info,order_remark,order_start_time,order_end_time,consignee_info,item_info_list,coupon_detail_list,return_order,pin,payment_confirm_time,logistics_id,modified");
//			OrderSearchResponse response =getJingdongConnector().execute(request);
//			OrderResult orderResult = response.getOrderInfoResult();
//			List<OrderSearchInfo> list =  orderResult.getOrderInfoList();
//			
//		}
		 
//	    public static void main(String[] args) {
//	    	WareInfoByInfoRequest request = new WareInfoByInfoRequest();
//	    	request.setPage("1");
//	    	request.setPageSize("10");
//	    	request.setFields("item_num,jd_price");
//	    	request.set
//		}	
	
		
		
		  
}
