/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CacheTest.java
 * 描述： 
 */
package net.chinacloud.mediator.cache;

import net.chinacloud.mediator.task.TaskTemplate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CacheTest {
	
	ApplicationContext context;
	
	@Before
	public void before () {
		context = new ClassPathXmlApplicationContext("spring-cacheTest.xml");
	}

	@Test
	public void testGetTaskTemplateByTypeAndSubType() {
		CacheService service = context.getBean("cacheService", CacheService.class);
		TaskTemplate template = service.getTaskTemplateByTypeAndSubType("ORDER", "create");
		System.out.println("template1:" + template);
		template = service.getTaskTemplateByTypeAndSubType("ORDER", "create");
		System.out.println("template2:" + template);
	}

}
