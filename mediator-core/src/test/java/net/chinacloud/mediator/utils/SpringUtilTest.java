/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SpringUtilTest.java
 * 描述： 
 */
package net.chinacloud.mediator.utils;

import net.chinacloud.mediator.domain.User1;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtilTest {
	ApplicationContext context = null;
	@Before
	public void before(){
		context = new ClassPathXmlApplicationContext("spring-test1.xml");
	}

	@Test
	public void testGetBean() {
		User1 user = SpringUtil.getBean(User1.class);
		System.out.println(user);
	}

}
