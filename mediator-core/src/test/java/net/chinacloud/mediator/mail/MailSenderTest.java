/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MailSenderTest.java
 * 描述： 
 */
package net.chinacloud.mediator.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月21日 上午9:24:15
 */
public class MailSenderTest {
	
	ApplicationContext ctx = null;
	
	@Before
	public void before() {
		ctx = new ClassPathXmlApplicationContext("spring-mail-test.xml");
	}

	@Test
	public void testPlainEmail() {
		MailSender sender = ctx.getBean(MailSender.class);
		Email email = new Email("ywu@wuxicloud.com", "yejunwu126@126.com", "test");
		email.setContent("hello,world!");
		sender.send(email);
	}

	@Test
	public void testTemplateEmail() {
		MailSender sender = ctx.getBean(MailSender.class);
		Email email = new Email("ywu@wuxicloud.com", "yejunwu126@126.com", "test");
		email.setType(EmailType.TEMPLATE);
		email.setTemplateName("errors.ftl");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subject", "test");
		params.put("msg", "我是一只加班狗,啦啦啦啦啦！！");
		
		email.setTemplateParam(params);
		
		sender.send(email);
	}
	
	@Test
	public void testAttachment() throws IOException {
		MailSender sender = ctx.getBean(MailSender.class);
		Email email = new Email("ywu@wuxicloud.com", "yejunwu126@126.com", "test");
		email.setType(EmailType.TEMPLATE);
		email.setTemplateName("errors.ftl");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subject", "test");
		params.put("msg", "我是有附件的邮件,不信你看");
		
		email.setTemplateParam(params);
		
		ClassPathResource resource = new ClassPathResource("pic/img-2ec0b31f2b3b26c50a288e4d972661f4.jpg");
		
		email.addAttachement(resource.getFile());
		
		sender.send(email);
	}
}
