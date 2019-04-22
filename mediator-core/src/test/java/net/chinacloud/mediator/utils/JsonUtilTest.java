package net.chinacloud.mediator.utils;

import java.util.Date;

import net.chinacloud.mediator.domain.ObjectWrapper;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Person;
import net.chinacloud.mediator.pojo.MessageActionCode;
import net.chinacloud.mediator.pojo.MessageObject;

import org.junit.Test;

public class JsonUtilTest {
	
	//private Group group = null;
	
	/*@Before
	public void before(){
		group = new Group();
		group.setId(0L);
		group.setName("admin");

		User1 guestUser = new User1();
		guestUser.setId(2L);
		guestUser.setName("guest");
		guestUser.setBirthday(new Date());
		Map<String,Integer> map1 = new HashMap<String, Integer>();
		map1.put("height", 167);
		map1.put("weight", 60);
		guestUser.setParams(map1);

		User1 rootUser = new User1();
		rootUser.setId(3L);
		rootUser.setName("root");
		rootUser.setBirthday(DateUtil.parse("2014-12-06 00:00:00"));
		Map<String,Integer> map2 = new HashMap<String, Integer>();
		map2.put("height", 174);
		map2.put("weight", 67);
		rootUser.setParams(map2);

		group.getUsers().add(guestUser);
		group.getUsers().add(rootUser);
	}*/

	/*@Test
	public void testObject2JsonString() {
		String jsonStr = JsonUtil.object2JsonString(group);
		System.out.println(jsonStr);
	}*/
	
	/*@Test
	public void testJsonString2Object() {
		String jsonStr = "{\"id\":0,\"name\":\"admin\",\"users\":[{\"birthday\":1417756591280,\"id\":2,\"name\":\"guest\",\"params\":{\"height\":167,\"weight\":60}},{\"birthday\":1417795200000,\"id\":3,\"name\":\"root\",\"params\":{\"height\":174,\"weight\":67}}]}";
		Group tempGroup = JsonUtil.jsonString2Object(jsonStr, Group.class);
		System.out.println(tempGroup);
	}*/
	
	/*@Test
	public void testJsonString2Map(){
		String jsonStr = "{\"id\":0,\"name\":\"admin\",\"users\":[{\"birthday\":1417756591280,\"id\":2,\"name\":\"guest\",\"params\":{\"height\":167,\"weight\":60}},{\"birthday\":1417795200000,\"id\":3,\"name\":\"root\",\"params\":{\"height\":174,\"weight\":67}}]}";
		Map<String,Object> tempGroup = JsonUtil.jsonString2Map(jsonStr);
		System.out.println(tempGroup.get("name"));
		
		@SuppressWarnings("unchecked")
		List<User1> users = (List<User1>)tempGroup.get("users");
		System.out.println(users.get(0));
	}*/

	@Test
	public void testJson() {
		/*String jsonStr = "{" + " 'actionCode': 'Order/Create',"
		        + "'content': {" + "  'additionalParams': {"
		        + "      'brandSale': 0,"
		        + "      'buyerAlipayNo': '2013092211001001930088334816'" + "  },"
		        + "  'calculateShareDiscount': 'Y',"
		        + "  'channelOrderId': '427728230651518'," + "  'codPrice': 0,"
		        + "  'createTime': '2013-09-22 02:17:51',"
		        + "  'payTime': '2013-09-22 02:17:51',"
		        + "  'endTime': '2013-09-22 17:07:07'," + "  'invoice':{'type':'1','title':'1','content':'1'"
		        + "     }," + "  'modified': '2013-09-25 21:51:04',"
		        + "  'orderAmount': 1690," + "  'orderDiscount': -1810,"
		        + "  'orderItems': [{" + "      'additionalParams': {'totalFee': 490" 
		        + "      },"
		        + "      'channelOrderItemId': '427728230651518',"
		        + "      'channelSkuId': '25002459550',"
		        + "      'itemDiscount': 905,"
		        + "      'outerProductId': '301032393L',"
		        + "      'outerSkuId': '640365771896',"
		        + "      'overSold': false," + "     'price': 1690,"
		        + "      'quantity': 1," + "      'shippingType': 'express',"
		        + "      'status': 'TRADE_CLOSED_BY_TAOBAO'" + "  }],"
		        + "  'payType': 'alipay'," + "  'payment': 785,"
		        + "  'receiverAddress': {" + "      'address': '阜上新村36号和善私房菜',"
		        + "      'city': '黄山市'," + "      'contactName': '邵周蓉',"
		        + "      'country': ''," + "      'district': '屯溪区',"
		        + "      'email': ''," + "      'mobile': '18505590310',"
		        + "      'phone': ''," + "      'state': '安徽省',"
		        + "      'zipCode': '245000'" + "  }," + "  'shippingCharge': 0,"
		        + "  'shippingType': 'express'," + "  'shopper': {"
		        + "      'channelUserId': 'its1024'" + "  },"
		        + "  'status': 'CLOSED_BY_CHANNEL'," + " 'totalAdjustment': -905,"
		        + "  'tradeFrom': 'WAP'," + "  'type': 'fixed'" + " },"
		        + " 'context': {" + "    'storeId': 10152" + " }" + "}";
		
		MessageObject<Order> mo = JsonUtil.jsonString2Object(jsonStr, MessageObject.class);
		System.out.println(mo.getContent());*/
	}
	
	@Test
	public void test() {
		Order order = new Order();
		order.setChannelOrderId("123456");
		order.setCreateTime(new Date());
		order.setOrderAmount(20D);
		order.setShippingCompany("SF");
		
		String json = JsonUtil.object2JsonString(order);
		System.out.println(json);
	}
	
	@Test
	public void test1() {
		String str = "{\"additionalParams\":{},\"calculateShareDiscount\":\"Y\",\"channelOrderId\":\"123456\",\"codPrice\":0,\"createTime\":\"2015-01-21 11:05:24\",\"orderAmount\":20,\"orderDiscount\":0,\"orderItems\":[],\"payType\":\"alipay\",\"payment\":0,\"shippingCharge\":0,\"shippingCompany\":\"SF\",\"shippingType\":\"express\",\"totalAdjustment\":0,\"type\":\"fixed\"}";
		Object obj = JsonUtil.jsonString2Object(str, Order.class);
		if (obj instanceof Order) {
			Order order = (Order)obj;
			System.out.println(order);
		}
	}
	
	@Test
	public void test2() {
		/*String str = "{\"appId\":0,\"channelId\":0,\"count\":2,\"end\":false,\"list\":[{\"appId\":0,\"channelId\":0,\"isFull\":true,\"partNumber\":\"885110361513\",\"qtyAll\":1000.0,\"qtyCanSell\":899.0,\"qtyChange\":899.0,\"storeId\":0,\"userData\":{}},{\"appId\":0,\"channelId\":0,\"isFull\":true,\"partNumber\":\"885110361520\",\"qtyAll\":19985.0,\"qtyCanSell\":19936.0,\"qtyChange\":19936.0,\"storeId\":0,\"userData\":{}}],\"storeId\":10151,\"subcount\":2,\"userData\":{}}";
		InventoryList inventoryList = JsonUtil.jsonString2Object(str, InventoryList.class);
		for (SKUInventory skuInventory : inventoryList.getList()) {
			System.out.println(skuInventory.isFull());
		}*/
	}
	
	@Test
	public void test3() {
		ObjectWrapper wrapper = new ObjectWrapper();
		Person person = new Person();
		person.setChannelOrderId("123");
		person.setPrice(1.3D);
		
		wrapper.setType("person");
		wrapper.setContent(person);
		
		System.out.println(JsonUtil.object2JsonString(wrapper));
	}
	
	@Test
	public void test4() {
		String json = "{\"content\":{\"channelOrderId\":\"123\",\"price\":1.3},\"type\":\"person\"}";
		ObjectWrapper wrapper = JsonUtil.jsonString2Object(json, ObjectWrapper.class);
		
		System.out.println(wrapper);
		
		/*Person person = (Person)wrapper.getContent();
		System.out.println(person.getChannelOrderId());*/
	}
	
	@Test
	public void test5() {
		String str = "abc";
		System.out.println(JsonUtil.object2JsonString(str));
	}
	
	@Test
	public void test6() {
		Order order = new Order();
		order.setInvoice(null);
		System.out.println(JsonUtil.object2JsonString(order));
	}
	
	@Test
	public void test7() {
		Order order = new Order();
		order.setChannelOrderId("1100337996494970");
		order.setShippingId("102148632220");
		order.setShippingCompany("SF");
		order.setSplit(false);
		
		/*OrderItem orderItem = new OrderItem();
		orderItem.setChannelOrderItemId("1116023916154970");
		
		order.addOrderItem(orderItem);*/
		
		MessageObject<Order> mo = new MessageObject<Order>(
				MessageActionCode.ACTION_CODE_ORDER_DELIVER_ORDER, 
				1002);
		mo.setContent(order);
		System.out.println(JsonUtil.object2JsonString(mo));
	}
	
	@Test
	public void test8() {
		System.out.println(JsonUtil.object2JsonString(Double.valueOf("0.04")));;
	}
}
