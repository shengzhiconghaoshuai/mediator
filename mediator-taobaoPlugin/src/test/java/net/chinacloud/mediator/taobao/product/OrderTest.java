/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OrderTest.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.product;

import net.chinacloud.mediator.taobao.domain.MyNotifyRefund;
import net.chinacloud.mediator.taobao.domain.MyNotifyTrade;
import net.chinacloud.mediator.utils.JsonUtil;

import org.junit.Test;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月18日 下午6:36:24
 */
public class OrderTest {

	@Test
	public void test() {
		MyNotifyTrade trade = new MyNotifyTrade();
		trade.setTid(1157225503578675L);
		System.out.println(JsonUtil.object2JsonString(trade));
	}

	@Test
	public void test1() {
		MyNotifyRefund refund = new MyNotifyRefund();
		refund.setRid(51514779637586L);
		System.out.println(JsonUtil.object2JsonString(refund));
	}
}
