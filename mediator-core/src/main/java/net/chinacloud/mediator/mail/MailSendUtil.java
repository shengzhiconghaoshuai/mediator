/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MailSendUtil.java
 * 描述： 
 */
package net.chinacloud.mediator.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

/**
 * @description 邮件发送工具类
 * @author yejunwu123@gmail.com
 * @since 2015年8月15日 下午1:40:56
 */
public final class MailSendUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MailSendUtil.class);
	
	private MailSendUtil() {
		
	}
	
	public static void sendEmail(String subject, String content) {
		Registry registry = SpringUtil.getBean(Registry.class);
		String sendEmail = registry.get("sendMail");
		if (StringUtils.hasText(sendEmail)) {
			if (Boolean.valueOf(sendEmail)) {
				try {
					MailSender sender = SpringUtil.getBean(MailSender.class);
					Email email = new Email(
							registry.get("mailFrom").toString(), 
							registry.get("mailToList").toString(), 
							subject);
					email.setContent(content);
					sender.send(email);
				} catch (Exception e1) {
					//e1.printStackTrace();
					LOGGER.error("mail send error, subject = " + subject + ", content = " + content, e1);
				}
			}
		}
	}
}
