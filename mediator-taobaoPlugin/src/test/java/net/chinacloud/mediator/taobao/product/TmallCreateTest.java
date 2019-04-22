package net.chinacloud.mediator.taobao.product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.chinacloud.mediator.domain.ApplicationParam;
import net.chinacloud.mediator.domain.ChannelPictureMapping;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.init.connector.Connector;
import net.chinacloud.mediator.taobao.dao.ChannelPictureMappingDao;
import net.chinacloud.mediator.taobao.init.TaobaoConnector;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.junit.Test;

import com.taobao.api.FileItem;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemImg;
import com.taobao.api.domain.Sku;
import com.taobao.api.request.ItemAddRequest;
import com.taobao.api.request.ItemImgUploadRequest;
import com.taobao.api.request.ItemJointImgRequest;
import com.taobao.api.request.ItemSkuAddRequest;
import com.taobao.api.request.ItemSkusGetRequest;
import com.taobao.api.request.ItemsCustomGetRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemAddResponse;
import com.taobao.api.response.ItemJointImgResponse;
import com.taobao.api.response.ItemSkuAddResponse;
import com.taobao.api.response.ItemSkusGetResponse;
import com.taobao.api.response.ItemsCustomGetResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
public class TmallCreateTest {
//	@Before
//	public void Before() {
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-test.xml");
//		System.out.print(ctx);
//	}
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
	public void tmallProduct(){
		List<ProductPartnumberMapping> onsalelist=this.insertOnsaleProduct(1L, new ArrayList<ProductPartnumberMapping>());
		List<ProductPartnumberMapping> onstocklist=this.insertOnstockProduct(1L, new ArrayList<ProductPartnumberMapping>());
		List<String> outids=new ArrayList<String>();
		int page=onsalelist.size()%40==0?onsalelist.size()/40:onsalelist.size()/40+1;
		int aa=onsalelist.size()%40;
		int bb=0;
		for(int j=0;j<page;j++){
			String id="";
			if(j!=page-1&&onsalelist.size()%40!=0){
				bb+=40;
			}else{
				bb+=aa;
			}
			for(int i=bb-40;i<bb;i++){
				int cc=bb%40==0?40:bb%40;
				id+=cc-1==i?onsalelist.get(i).getChannelProductId():onsalelist.get(i).getChannelProductId()+",";
				}
			String ids[]=id.split(",");
			System.out.print(ids.length);
			ItemSkusGetRequest req = new ItemSkusGetRequest();
			req.setFields("outer_id");
			req.setNumIids(id);
			ItemSkusGetResponse response =getTaobaoConnector().execute(req);
			if(response.getSkus()!=null){
				for(int k=0;k<response.getSkus().size();k++){
					outids.add(response.getSkus().get(k).getOuterId());
				}
			}
		}
		this.exportSku(outids, "D:/天猫在售Sku.txt");
		List<String> outids1=new ArrayList<String>();
		int page1=onstocklist.size()%40==0?onstocklist.size()/40:onstocklist.size()/40+1;
		int aa1=onstocklist.size()%40;
		int bb1=0;
		for(int j=0;j<page1;j++){
			String id="";
			if(j!=page1-1&&onstocklist.size()%40!=0){
				bb1+=40;
			}else{
				bb1+=aa1;
			}
			for(int i=bb1-40;i<bb1;i++){
				int cc=bb1%40==0?40:bb1%40;
				id+=cc-1==i?onstocklist.get(i).getChannelProductId():onstocklist.get(i).getChannelProductId()+",";
				}
			String ids[]=id.split(",");
			System.out.print(ids.length);
			ItemSkusGetRequest req = new ItemSkusGetRequest();
			req.setFields("outer_id");
			req.setNumIids(id);
			ItemSkusGetResponse response =getTaobaoConnector().execute(req);
			if(response.getSkus()!=null){
				for(int k=0;k<response.getSkus().size();k++){
					outids1.add(response.getSkus().get(k).getOuterId());
				}
			}
		}
		this.exportSku(outids1, "D:/天猫仓库Sku.txt");
		this.exportOMSSql(onsalelist, "1", "D:/天猫上架初始化.sql");
		onsalelist.addAll(onstocklist);
		this.exportSql(onsalelist,"D:/天猫商品初始化.sql");
		this.exportOMSSql(onstocklist, "0","D:/天猫下架初始化.sql");
	}
	//@Test
	public  List<ProductPartnumberMapping> insertOnsaleProduct(Long pageNo,List<ProductPartnumberMapping> productList){
//		String outid="";
//		ChannelPictureMappingDao channelPictureMappingDao=SpringUtil.getBean(ChannelPictureMappingDao.class);
		ItemsOnsaleGetRequest req=new ItemsOnsaleGetRequest();
			req.setPageNo(pageNo);
			req.setPageSize(200L);
			req.setFields("title,num_iid,outer_id");
			ItemsOnsaleGetResponse response = getTaobaoConnector().execute(req);
				for(int i=0;i<response.getItems().size();i++){
					boolean bool = true;
					ProductPartnumberMapping product=new ProductPartnumberMapping();
					product.setChannelProductId(response.getItems().get(i).getNumIid().toString());
					product.setOuterId(response.getItems().get(i).getOuterId());
					Iterator<ProductPartnumberMapping> iterator = productList.iterator();
					if(StringUtils.hasText(response.getItems().get(i).getOuterId())) {
					 while(iterator.hasNext()) {
			    		  String _itemNum = iterator.next().getOuterId();
			    		  if(response.getItems().get(i).getOuterId().equals(_itemNum)) {
			    			  System.out.println("repeat:---------outid:"+_itemNum+"    numiid:  "+response.getItems().get(i).getNumIid()+"    title:  "+response.getItems().get(i).getTitle());
			    			  iterator.remove();
			    			  bool = false;
				    	      break;
				    	   }
			    	   }
					 if(bool) {
		    			  productList.add(product);
			    	   }
					}else{
						System.out.println("nullOutid:---------numiid:  "+response.getItems().get(i).getNumIid()+"    title:  "+response.getItems().get(i).getTitle());
					}
//					if(StringUtils.hasText(product.getOuterId())){
//						List<ProductPartnumberMapping> list=channelPictureMappingDao.getList(product.getOuterId());
//						if(list.size()<1){
//							channelPictureMappingDao.saveProductMapping(product);
//						}else{
//							outid+="outId:"+product.getOuterId()+"title:"+response.getItems().get(i).getTitle()+"numiid:"+product.getChannelProductId()+";";
//						}
//					}else{
//						outid+="numiid:"+product.getChannelProductId()+"title:"+response.getItems().get(i).getTitle()+";";
//					}
				}
				int total=Integer.parseInt(response.getTotalResults().toString());
				int totalPage = total % 200 == 0 ? total/200 : total/200+1 ;
			    Long pager = pageNo+1;
			       if(pager<=totalPage){
			    	   this.insertOnsaleProduct(pager,productList);
			       }
			    System.out.println(response.getTotalResults()+"onsaletotal");   
				return productList;
//				System.out.print("repeatOutId:"+"----"+outid);
//				System.out.print("nullOutId:"+"----"+outid);
	}
	//@Test
	public  List<ProductPartnumberMapping> insertOnstockProduct(Long pageNo,List<ProductPartnumberMapping> productList){
//		String outid="";
//		ChannelPictureMappingDao channelPictureMappingDao=SpringUtil.getBean(ChannelPictureMappingDao.class);
		ItemsInventoryGetRequest req=new ItemsInventoryGetRequest();
		req.setPageNo(pageNo);
		req.setPageSize(200L);
		req.setFields("title,num_iid,outer_id");
		ItemsInventoryGetResponse response = getTaobaoConnector().execute(req);
		for(int i=0;i<response.getItems().size();i++){
			boolean bool = true;
			ProductPartnumberMapping product=new ProductPartnumberMapping();
			product.setChannelProductId(response.getItems().get(i).getNumIid().toString());
			product.setOuterId(response.getItems().get(i).getOuterId());
			Iterator<ProductPartnumberMapping> iterator = productList.iterator();
			if(StringUtils.hasText(response.getItems().get(i).getOuterId())) {
				 while(iterator.hasNext()) {
		    		  String _itemNum = iterator.next().getOuterId();
		    		  if(response.getItems().get(i).getOuterId().equals(_itemNum)) {
		    			  System.out.println("repeat:---------outid:"+_itemNum+"    numiid:  "+response.getItems().get(i).getNumIid()+"    title:  "+response.getItems().get(i).getTitle());
		    			  iterator.remove();
		    			  bool = false;
			    	      break;
			    	   }
		    	   }
				 if(bool) {
	    			  productList.add(product);
		    	   }
				}else{
					System.out.println("nullOutid:---------numiid:  "+response.getItems().get(i).getNumIid()+"    title:  "+response.getItems().get(i).getTitle());
				}
//			if(StringUtils.hasText(product.getOuterId())){
//				List<ProductPartnumberMapping> list=channelPictureMappingDao.getList(product.getOuterId());
//				if(list.size()<1){
//					channelPictureMappingDao.saveProductMapping(product);
//				}else{
//					outid+="outId:"+product.getOuterId()+"title:"+response.getItems().get(i).getTitle()+"numiid:"+product.getChannelProductId()+";";
//					
//				}
//			}else{
//				outid+="numiid:"+product.getChannelProductId()+"title:"+response.getItems().get(i).getTitle()+";";
//			}
		}
		int total=Integer.parseInt(response.getTotalResults().toString());
		int totalPage = total % 200 == 0 ? total/200 : total/200+1 ;
	    Long pager = pageNo+1;
	       if(pager<=totalPage){
	    	   this.insertOnstockProduct(pager,productList);
	       }
	    System.out.println(response.getTotalResults()+"onstocktotal");
		return productList;
//		System.out.print("repeatOutId:"+"----"+outid);
//		System.out.print("nullOutId:"+"----"+outid);
	}
	//@Test
	public void insertPicture(){
		ChannelPictureMappingDao channelPictureMappingDao=SpringUtil.getBean(ChannelPictureMappingDao.class);
		List<ProductPartnumberMapping> pList=channelPictureMappingDao.getProductPartnumberMapping();
		for(ProductPartnumberMapping pMapping:pList){
			ItemsCustomGetRequest req=new ItemsCustomGetRequest();
			req.setOuterId(pMapping.getOuterId());
			req.setFields("item_img.id,item_img.position");
			ItemsCustomGetResponse response=getTaobaoConnector().execute(req);
			for(int i=0;i<response.getItems().get(0).getItemImgs().size();i++){
				ChannelPictureMapping cMapping=new ChannelPictureMapping();
				cMapping.setChannelProductId(pMapping.getChannelProductId());
				cMapping.setImageId(response.getItems().get(0).getItemImgs().get(i).getId().toString());
				cMapping.setSort(Integer.parseInt(response.getItems().get(0).getItemImgs().get(i).getPosition().toString()));
				if(!"0".equals(cMapping.getImageId())){
					channelPictureMappingDao.savePicture(cMapping);
				}
			}
		}
		
	}
	//@Test
	public void testCreateItemWithoutSku() {
		createItemWithoutSku();
	}
	
	//@Test
	public void testCreateSku() {
		createSku(520004176773L);
	}
	
	//@Test
	public void testUploadItemImage() throws URISyntaxException {
		uploadItemImage(520004176773L);
	}
	
	//@Test
	public void testJoinItemImage() {
		joinItemImage(520004176773L);
	}
	
	/**
	 * 上传商品图片
	 * 这个接口只能通过二进制文件上传图片
	 * @throws URISyntaxException 
	 */
	public void uploadItemImage(Long numIid) throws URISyntaxException {
		ItemImgUploadRequest imgUploadRequest = new ItemImgUploadRequest();
		//-------------------必填参数--------------------------
		imgUploadRequest.setNumIid(numIid);		//商品数字ID，该参数必须 
		
		//-------------------可选参数--------------------------
		imgUploadRequest.setIsMajor(true);		//是否将该图片设为主图,可选值:true,false;默认值:false(非主图) 
		FileItem fileItem = new FileItem(new File(URI.create("http://img2.duitang.com/uploads/item/201206/13/20120613133915_sAf5z.thumb.600_0.jpeg")));
		imgUploadRequest.setImage(fileItem);
	}
	
	public void joinItemImage(Long numIid) {
		ItemJointImgRequest itemJointImgRequest = new ItemJointImgRequest();
		itemJointImgRequest.setNumIid(numIid);
		itemJointImgRequest.setPicPath("i1/728274110/TB23tCadXXXXXbcXpXXXXXXXXXX-728274110.jpeg");
		itemJointImgRequest.setIsMajor(false);
		
		ItemJointImgResponse response = getTaobaoConnector().execute(itemJointImgRequest);
		System.out.println("errorCode:" + response.getErrorCode() + ",subCode:" + response.getSubCode());
		System.out.println("msg:" + response.getMsg() + ",subMsg:" + response.getSubMsg());
		System.out.println(response.getBody());
		
		ItemImg img = response.getItemImg();
		System.out.println(img.getUrl());
	}
	
	/**
	 * 3100011828247
	 * 创建sku
	 * 创建sku时,sku所属商品所挂叶子目录中的 is_sale_prop 属性为true的属性影响sku,但不是必填的
	 * @param numIid
	 */
	public void createSku(Long numIid) {
		ItemSkuAddRequest itemSkuAddRequest = new ItemSkuAddRequest();
		//-------------------------sku必填属性--------------------------------------
		itemSkuAddRequest.setNumIid(numIid);		//Sku所属商品数字id，可通过 taobao.item.get 获取。必选
		itemSkuAddRequest.setProperties("1627207:3232483;20509:649458002;");		//Sku属性串。格式:pid:vid;pid:vid,如:1627207:3232483;1630696:3284570,表示:机身颜色:军绿色;手机套餐:一电一充。
		itemSkuAddRequest.setQuantity(1L);			//Sku的库存数量。sku的总数量应该小于等于商品总数量(Item的NUM)。取值范围:大于零的整数 
		itemSkuAddRequest.setPrice("7.50");			//Sku的销售价格。商品的价格要在商品所有的sku的价格之间。精确到2位小数;单位:元。如:200.07，表示:200元7分 
		
		//-------------------------sku可选属性--------------------------------------
		itemSkuAddRequest.setOuterId("11223344");		//Sku的商家外部id
		//itemSkuAddRequest.setItemPrice("");		//sku所属商品的价格。当用户新增sku，使商品价格不属于sku价格之间的时候，用于修改商品的价格，使sku能够添加成功 
		
		ItemSkuAddResponse response = getTaobaoConnector().execute(itemSkuAddRequest);
		
		System.out.println("errorCode:" + response.getErrorCode() + ",subCode:" + response.getSubCode());
		System.out.println("msg:" + response.getMsg() + ",subMsg:" + response.getSubMsg());
		System.out.println(response.getBody());
		
		Sku sku = response.getSku();
		System.out.println(sku.getSkuId());
	}

	/**
	 * 520004176773
	 * 创建商品,不包括sku
	 * 仅创建商品时,商品所挂叶子目录中要求的 must 属性为true的属性必须设置,其他属性可以不设置
	 * 如果要通过图片绝对地址上传图片,貌似只能在创建商品的时候就指定图片路径
	 */
	public Item createItemWithoutSku() {
		ItemAddRequest itemAddRequest = new ItemAddRequest();
		
		//---------------商品必填信息-----------------------------------------------------------
		itemAddRequest.setNum(10L);		//商品数量。必须 ,取值范围:0-900000000的整数。且需要等于Sku所有数量的和。 拍卖商品中增加拍只能为1，荷兰拍要在[2,500)范围内
		itemAddRequest.setPrice("7.50");	//商品价格。必须,取值范围:0-100000000;精确到2位小数;单位:元。如:200.07，表示:200元7分。需要在正确的价格区间内。 拍卖商品对应的起拍价
		itemAddRequest.setType("fixed");	//发布类型,必须
		itemAddRequest.setStuffStatus("new");	//新旧程度。必须,可选值：new(新)，second(二手)。B商家不能发布二手商品。 如果是二手商品，特定类目下属性里面必填新旧成色属性
		itemAddRequest.setTitle("好看的连衣裙");		//宝贝标题。不能超过30字符，受违禁词控制。天猫图书管控类目最大允许120字符； 
		itemAddRequest.setDesc("走过路过不要错过");	//宝贝描述。字数要大于5个字符，小于25000个字符，受违禁词控制
		itemAddRequest.setLocationState("江苏");	//在地省份。如浙江
		itemAddRequest.setLocationCity("无锡");	//所在地城市。如杭州 
		itemAddRequest.setCid(50010850L);		//叶子类目id
		
		//---------------商品可选信息---------------------------------------------------------------
		itemAddRequest.setApproveStatus("onsale");	//商品上传后的状态。可选值:onsale(出售中),instock(仓库中);默认值:onsale 
		/**
		 * 商品属性列表。格式:pid:vid;pid:vid。属性的pid调用taobao.itemprops.get取得，
		 * 属性值的vid用taobao.itempropvalues.get取得vid。 如果该类目下面没有属性，可以不用填写。
		 * 如果有属性，必选属性必填，其他非必选属性可以选择不填写.属性不能超过35对。所有属性加起来包括分割符不能超过549字节，单个属性没有限制。
		 *  如果有属性是可输入的话，则用字段input_str填入属性的值
		 */
		itemAddRequest.setProps("122216347:379886796;");
		/**
		 * 运费承担方式。可选值:seller（卖家承担）,buyer(买家承担);默认值:seller。卖家承担不用设置邮费和postage_id.
		 * 买家承担的时候，必填邮费和postage_id 如果用户设置了运费模板会优先使用运费模板，
		 * 否则要同步设置邮费（post_fee,express_fee,ems_fee） 
		 */
		itemAddRequest.setFreightPayer("seller");
		//itemAddRequest.setValidThru(validThru);		//有效期。可选值:7,14;单位:天;默认值:14 
		itemAddRequest.setHasInvoice(false);		//是否有发票。可选值:true,false (商城卖家此字段必须为true);默认值:false(无发票) 
		itemAddRequest.setHasWarranty(false);		//是否有保修。可选值:true,false;默认值:false(不保修) 
		itemAddRequest.setHasShowcase(false);		//橱窗推荐。可选值:true,false;默认值:false(不推荐) 
		//itemAddRequest.setSellerCids("");			//商品所属的店铺类目列表。按逗号分隔。结构:",cid1,cid2,...,"，如果店铺类目存在二级类目，必须传入子类目cids。
		
		//itemAddRequest.setHasDiscount(false);		//支持会员打折。可选值:true,false;默认值:false(不打折) 
		//itemAddRequest.setPostFee("");				//平邮费用。取值范围:0.01-999.00;精确到2位小数;单位:元。如:5.07，表示:5元7分. 注:post_fee,express_fee,ems_fee需要一起填写
		//itemAddRequest.setExpressFee("");			//快递费用。取值范围:0.01-999.00;精确到2位小数;单位:元。如:15.07，表示:15元7分 
		//itemAddRequest.setEmsFee("");				//ems费用。取值范围:0.01-999.00;精确到2位小数;单位:元。如:25.07，表示:25元7分 
		//itemAddRequest.setListTime(new Date());		//定时上架时间。(时间格式：yyyy-MM-dd HH:mm:ss) 
		//itemAddRequest.setImage(image);			//商品主图片。类型:JPG,GIF;最大长度:500K,支持的文件类型：gif,jpg,jpeg,png 
		//itemAddRequest.setPostageId(1L);			//宝贝所属的运费模板ID。取值范围：整数且必须是该卖家的运费模板的ID（可通过taobao.delivery.template.get获得当前会话用户的所有邮费模板）
		//itemAddRequest.setPropertyAlias("");		//属性值别名。如pid:vid:别名;pid1:vid1:别名1 ，其中：pid是属性id vid是属性值id。总长度不超过512字节 
		//itemAddRequest.setInputPids("");			//用户自行输入的类目属性ID串。结构："pid1,pid2,pid3"，如："20000"（表示品牌） 注：通常一个类目下用户可输入的关键属性不超过1个
		
		itemAddRequest.setOuterId("123456");				//商品外部编码，该字段的最大长度是64个字节
		//itemAddRequest.setProductId(1L);			//商品所属的产品ID(B商家发布商品需要用) 
		/**
		 * 商品主图需要关联的图片空间的相对url。这个url所对应的图片必须要属于当前用户。
		 * pic_path和image只需要传入一个,如果两个都传，默认选择pic_path 
		 */
		itemAddRequest.setPicPath("http://img01.taobaocdn.com/imgextra/i1/728274110/TB23tCadXXXXXbcXpXXXXXXXXXX-728274110.jpeg");
		/**
		 * 用户自行输入的子属性名和属性值，结构:"父属性值;一级子属性名;一级子属性值;二级子属性名;自定义输入值,....",
		 * 如：“耐克;耐克系列;科比系列;科比系列;2K5,Nike乔丹鞋;乔丹系列;乔丹鞋系列;乔丹鞋系列;json5”，
		 * 多个自定义属性用','分割，input_str需要与input_pids一一对应，注：通常一个类目下用户可输入的关键属性不超过1个。
		 * 所有属性别名加起来不能超过3999字节 
		 */
		//itemAddRequest.setInputStr("");
		itemAddRequest.setIsTaobao(true);			//是否在淘宝上显示（如果传FALSE，则在淘宝主站无法显示该商品） 
		itemAddRequest.setIsEx(false);				//是否在外店显示 
		itemAddRequest.setSellPromise(false);		//是否承诺退换货服务!虚拟商品无须设置此项! 
		/**
		 * 商品是否为新品。只有在当前类目开通新品,并且当前用户拥有该类目下发布新品权限时才能设置is_xinpin为true，
		 * 否则设置true后会返回错误码:isv.invalid-permission:add-xinpin。同时只有一口价全新的宝贝才能设置为新品，
		 * 否则会返回错误码：isv.invalid-parameter:xinpin。不设置该参数值或设置为false效果一致。 
		 */
		itemAddRequest.setIsXinpin(false);
		itemAddRequest.setSubStock(2L);				//商品是否支持拍下减库存:1支持;2取消支持(付款减库存);0(默认)不更改 集市卖家默认拍下减库存; 商城卖家默认付款减库存 
		
		//---------------------------sku信息--------------------------------------------------------------
		/**
		 * 更新的sku的属性串，调用taobao.itemprops.get获取。格式:pid1:vid;pid2:vid,多个sku属性之间用逗号分隔。
		 * 该字段内的属性需要在props字段同时包含。如果新增商品包含了sku，则此字段一定要传入,字段长度要控制在512个字节以内。
		 */
		//itemAddRequest.setSkuProperties("");
		//itemAddRequest.setSkuQuantities("");		//Sku的数量串，结构如：num1,num2,num3 如：2,3 
		//itemAddRequest.setSkuPrices("");			//Sku的价格串，结构如：10.00,5.00,… 精确到2位小数;单位:元。如:200.07，表示:200元7分
		/**
		 * Sku的外部id串，结构如：1234,1342,… sku_properties, sku_quantities, sku_prices, sku_outer_ids
		 * 在输入数据时要一一对应，如果没有sku_outer_ids也要写上这个参数，入参是","(这个是两个sku的示列，逗号数应该是sku个数减1)；
		 * 该参数最大长度是512个字节 
		 */
		//itemAddRequest.setSkuOuterIds("");
		
		ItemAddResponse response = getTaobaoConnector().execute(itemAddRequest);
		System.out.println("errorCode:" + response.getErrorCode() + ",subCode:" + response.getSubCode());
		System.out.println("msg:" + response.getMsg() + ",subMsg:" + response.getSubMsg());
		System.out.println(response.getBody());
		
		Item item = response.getItem();
		System.out.println(item.getNumIid());
		
		return item;
	}
	 public void exportSql(List<ProductPartnumberMapping> tb,String filepath){
		    System.out.println(tb.size());
		    FileOutputStream out=null;
	        OutputStreamWriter osw=null;
	        BufferedWriter bw=null;
	        try {
	            out = new FileOutputStream(new File(filepath));
	            osw = new OutputStreamWriter(out);
	            bw =new BufferedWriter(osw);
	            for(ProductPartnumberMapping tbproduct : tb ) {
	            	bw.append(" INSERT INTO PRODUCT_PARTNUMBER_MAPPING(CHANNEL_PRODUCT_ID,OUTER_ID,TYPE,APPLICATION_ID) VALUES(  ");
	            	bw.append("'").append(tbproduct.getChannelProductId()).append("','").append(tbproduct.getOuterId()).append("',").append("0, ").append("1 );").append("\r");
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
	 public void exportOMSSql(List<ProductPartnumberMapping> tb,String status,String filepath){
		    FileOutputStream out=null;
	        OutputStreamWriter osw=null;
	        BufferedWriter bw=null;
	        try {
	            out = new FileOutputStream(new File(filepath));
	            osw = new OutputStreamWriter(out);
	            bw =new BufferedWriter(osw);
	            for(ProductPartnumberMapping tbproduct : tb ) {
	            	bw.append(" UPDATE T_PRODUCT_STORE SET PUBLISHED=1,STATUS=").append(status).append(" WHERE PRODUCT_ID=(SELECT PRODUCT_ID FROM T_PRODUCT WHERE PARTNUMBER= '").append(tbproduct.getOuterId()).append("' ) AND STORE_ID= 1007 ").append("\r");
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
	 
	 public void exportSku(List<String> tb,String filepath){
		    FileOutputStream out=null;
	        OutputStreamWriter osw=null;
	        BufferedWriter bw=null;
	        try {
	            out = new FileOutputStream(new File(filepath));
	            osw = new OutputStreamWriter(out);
	            bw =new BufferedWriter(osw);
	            for(String outid : tb ) {
	            	bw.append(outid).append("\r");
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
}
