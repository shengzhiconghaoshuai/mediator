/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MailSender.java
 * 描述： 
 */
package net.chinacloud.mediator.mail;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.FreeMarkerUtil;
import net.chinacloud.mediator.utils.MessageUtils;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import freemarker.template.TemplateException;

/**
 * @description 邮件发送器
 * @author yejunwu123@gmail.com
 * @since 2015年7月18日 下午6:26:52
 */
public class MailSender {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);
	
	private static final String DEFAULT_ENCODING = "utf-8";
	private JavaMailSender sender;
	/**编码方式*/
	private String encoding = DEFAULT_ENCODING;
	
	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 发送邮件
	 * @param email
	 */
	public void send(Email email) {
		check(email);
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(
					message, 
					CollectionUtil.isEmpty(email.getAttachements()) ? false : true, 
					encoding);
			helper.setFrom(email.getFrom());
			helper.setTo(email.getReceivers().toArray(new String[email.getReceivers().size()]));
			helper.setSubject(email.getSubject());
			//生成邮件内容 
			String text = getMailText(email);
			helper.setText(text, email.isHtml());
			//附件
			if (CollectionUtil.isNotEmpty(email.getAttachements())) {
				for (File attachment : email.getAttachements()) {
					helper.addAttachment(attachment.getName(), attachment);
				}
			}
			sender.send(message);
		} catch (MailException e) {
			//e.printStackTrace();
			LOGGER.error("邮件发送失败", e);
		} catch (MessagingException e) {
			//e.printStackTrace();
			LOGGER.error("邮件发送失败", e);
		}
	}
	
	/**
	 * 生成邮件内容
	 * @param email
	 * @return
	 */
	private String getMailText(Email email) {
		if (email.getType() == EmailType.SIMPLE) {
			return email.getContent();
		} else if (email.getType() == EmailType.TEMPLATE) {
			// 解析模板,生成邮件内容
			try {
				return FreeMarkerUtil.mergeFileTemplate(email.getTemplateName(), email.getTemplateParam());
			} catch (IOException e) {
				//e.printStackTrace();
				LOGGER.error("模板解析失败", e);
				throw new IllegalArgumentException(MessageUtils.getMessage("exception.freemarker.template.err", email.getTemplateName()));
			} catch (TemplateException e) {
				//e.printStackTrace();
				LOGGER.error("模板解析失败", e);
				throw new IllegalArgumentException(MessageUtils.getMessage("exception.freemarker.template.err", email.getTemplateName()));
			}
		}
		return "";
	}
	
	/**
	 * 校验
	 * @param email
	 */
	private void check(Email email) {
		if (CollectionUtil.isEmpty(email.getReceivers())) {
			// 收件人列表为空
			throw new IllegalArgumentException(MessageUtils.getMessage("exception.email.receivers.list.empty"));
		}
		if (!StringUtils.hasLength(email.getFrom())) {
			// 发件人信息为空
			throw new IllegalArgumentException(MessageUtils.getMessage("exception.email.sender.empty"));
		}
		if (email.getType() == EmailType.TEMPLATE) {
			if (!StringUtils.hasLength(email.getTemplateName())) {
				// 模板名称为空
				throw new IllegalArgumentException(MessageUtils.getMessage("exception.email.template.name.empty"));
			}
		}
	}
}
