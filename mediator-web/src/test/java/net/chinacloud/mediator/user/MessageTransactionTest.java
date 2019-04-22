package net.chinacloud.mediator.user;

import javax.annotation.Resource;

import net.chinacloud.mediator.jms.JMSQueueProducer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-jms-transaction.xml"})
public class MessageTransactionTest {
	
	@Resource
	JMSQueueProducer producerA;
	
	@Test
	public void send() {
		//String message = "{\"item_get_response\":{\"item\":{\"approve_status\":\"instock\",\"auction_point\":5,\"cid\":1623,\"created\":\"2014-08-13 17:34:57\",\"delist_time\":\"2014-11-14 16:31:41\",\"detail_url\":\"http://item.taobao.com/item.htm?id=40589374816&spm=2014.12440355.0.0\",\"ems_fee\":\"13.00\",\"express_fee\":\"8.00\",\"freight_payer\":\"buyer\",\"has_discount\":false,\"has_invoice\":true,\"has_showcase\":false,\"has_warranty\":false,\"input_pids\":\"13021751\",\"input_str\":\"13CR2A12A\",\"is_fenxiao\":0,\"is_timing\":false,\"is_virtual\":false,\"item_imgs\":{\"item_img\":[{\"id\":0,\"position\":0,\"url\":\"https://img.alicdn.com/bao/uploaded/i4/T1OCejXe0rXXXXXXXX_!!0-item_pic.jpg\"},{\"id\":14902255219,\"position\":1,\"url\":\"https://img.alicdn.com/bao/uploaded/i1/133006562/TB23tr2aXXXXXagXpXXXXXXXXXX_!!133006562.jpg\"},{\"id\":19958836853,\"position\":2,\"url\":\"https://img.alicdn.com/bao/uploaded/i1/133006562/TB2Xi_eaVXXXXadXpXXXXXXXXXX_!!133006562.jpg\"},{\"id\":19958836854,\"position\":3,\"url\":\"https://img.alicdn.com/bao/uploaded/i1/133006562/TB2VbfcaVXXXXbJXpXXXXXXXXXX_!!133006562.jpg\"},{\"id\":19958836855,\"position\":4,\"url\":\"https://img.alicdn.com/bao/uploaded/i2/133006562/TB28.fkaVXXXXXTXXXXXXXXXXXX_!!133006562.jpg\"}]},\"list_time\":\"2014-11-11 00:00:00\",\"location\":{\"city\":\"广州\",\"state\":\"广东\"},\"modified\":\"2015-11-23 14:02:59\",\"nick\":\"歌莉娅官方旗舰店\",\"num\":0,\"num_iid\":40589374816,\"outer_id\":\"13CR2A12A\",\"pic_url\":\"https://img.alicdn.com/bao/uploaded/i4/T1OCejXe0rXXXXXXXX_!!0-item_pic.jpg\",\"post_fee\":\"8.00\",\"postage_id\":1341832960,\"price\":\"249.00\",\"product_id\":291185197,\"prop_imgs\":{\"prop_img\":[{\"id\":14734047399,\"position\":0,\"properties\":\"1627207:28338\",\"url\":\"https://img.alicdn.com/bao/uploaded/i3/133006562/TB2Y1TeaVXXXXarXpXXXXXXXXXX_!!133006562.jpg\"},{\"id\":14734047400,\"position\":0,\"properties\":\"1627207:28335\",\"url\":\"https://img.alicdn.com/bao/uploaded/i1/133006562/TB2.u_faVXXXXXXXpXXXXXXXXXX_!!133006562.jpg\"}]},\"property_alias\":\"1627207:28335:G04#浅绿;1627207:28338:U91#深蓝;20509:28315:M(160/84A);20509:28314:S(155/80A);20509:28316:L(165/88A)\",\"props\":\"13021751:334692932;20000:31681;122216347:209928863;1627207:28335;1627207:28338;20509:28314;20509:28315;20509:28316\",\"props_name\":\"13021751:334692932:货号:13CR2A12A;20000:31681:品牌:GOELIA/歌莉娅;122216347:209928863:年份季节:2013年冬季;1627207:28335:颜色分类:绿色;1627207:28338:颜色分类:蓝色;20509:28314:尺码:S;20509:28315:尺码:M;20509:28316:尺码:L\",\"sell_point\":\"歌莉娅\",\"seller_cids\":\"\",\"skus\":{\"sku\":[{\"created\":\"2014-08-13 17:34:57\",\"modified\":\"2014-11-22 22:49:53\",\"outer_id\":\"13CR2A12AU91S\",\"price\":\"249.00\",\"properties\":\"1627207:28338;20509:28314\",\"properties_name\":\"1627207:28338:颜色分类:蓝色;20509:28314:尺码:S\",\"quantity\":0,\"sku_id\":60311042996,\"with_hold_quantity\":0},{\"created\":\"2014-08-13 17:34:57\",\"modified\":\"2014-11-22 22:49:53\",\"outer_id\":\"13CR2A12AU91M\",\"price\":\"249.00\",\"properties\":\"1627207:28338;20509:28315\",\"properties_name\":\"1627207:28338:颜色分类:蓝色;20509:28315:尺码:M\",\"quantity\":0,\"sku_id\":60311042997,\"with_hold_quantity\":0},{\"created\":\"2014-08-13 17:34:57\",\"modified\":\"2014-11-22 22:49:53\",\"outer_id\":\"13CR2A12AU91L\",\"price\":\"249.00\",\"properties\":\"1627207:28338;20509:28316\",\"properties_name\":\"1627207:28338:颜色分类:蓝色;20509:28316:尺码:L\",\"quantity\":0,\"sku_id\":60311042998,\"with_hold_quantity\":0},{\"created\":\"2014-08-13 17:34:57\",\"modified\":\"2014-11-22 22:49:53\",\"outer_id\":\"13CR2A12AG04S\",\"price\":\"249.00\",\"properties\":\"1627207:28335;20509:28314\",\"properties_name\":\"1627207:28335:颜色分类:绿色;20509:28314:尺码:S\",\"quantity\":0,\"sku_id\":60311042999,\"with_hold_quantity\":0},{\"created\":\"2014-08-13 17:34:57\",\"modified\":\"2014-11-22 22:49:53\",\"outer_id\":\"13CR2A12AG04M\",\"price\":\"249.00\",\"properties\":\"1627207:28335;20509:28315\",\"properties_name\":\"1627207:28335:颜色分类:绿色;20509:28315:尺码:M\",\"quantity\":0,\"sku_id\":60311043000,\"with_hold_quantity\":0},{\"created\":\"2014-08-13 17:34:57\",\"modified\":\"2014-11-22 22:49:53\",\"outer_id\":\"13CR2A12AG04L\",\"price\":\"249.00\",\"properties\":\"1627207:28335;20509:28316\",\"properties_name\":\"1627207:28335:颜色分类:绿色;20509:28316:尺码:L\",\"quantity\":0,\"sku_id\":77237544001,\"with_hold_quantity\":0}]},\"stuff_status\":\"new\",\"sub_stock\":2,\"template_id\":\"343590674\",\"title\":\"歌莉娅GOELIA冬季新品荷叶边半裙13CR2A12A下架26-30\",\"type\":\"fixed\",\"violation\":false}}}";
		//producer.send(JsonUtil.object2JsonString(message));
		int i = 1;
		while (true) {
			producerA.send("hello AAAAAAAA! " + i);
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			i++;
		}
	}
}
