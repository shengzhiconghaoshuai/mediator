/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ExceptionTest.java
 * 描述： 
 */
package net.chinacloud.mediator.exception;

import net.chinacloud.mediator.exception.order.VendorPartnumberNotExistException;
import net.chinacloud.mediator.task.exception.DuplicateTaskException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExceptionTest {

	@SuppressWarnings("unused")
	private ApplicationContext context = null;
	
	@Before
	public void before(){
		context = new ClassPathXmlApplicationContext("spring-test.xml");
	}
	
	@Test
	public void testVendorPartnumberNotExistException() {
		try {
			test();
		} catch (VendorPartnumberNotExistException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.toString());
		}
	}
	
	@Test
	public void testDuplicateTaskException() {
		try {
			test1();
		} catch (DuplicateTaskException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.toString());
		}
	}
	
	public void test() throws VendorPartnumberNotExistException{
		throw new VendorPartnumberNotExistException("1234", "5678");
	}
	
	public void test1() throws DuplicateTaskException{
		throw new DuplicateTaskException("TAOBAO", "NW", "order", "create");
	}
}
