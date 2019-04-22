/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Email.java
 * 描述： 
 */
package net.chinacloud.mediator.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.StringUtils;

/**
 * @description 邮件
 * @author yejunwu123@gmail.com
 * @since 2015年7月18日 下午6:27:35
 */
public class Email {
	/**收件人列表*/
	private List<String> receivers = new ArrayList<String>(1);
	/**发件人*/
	private String from;
	/**邮件主题*/
	private String subject;
	/**邮件内容*/
	private String content;
	/**附件列表*/
	private List<File> attachements;
	/**是否为html邮件,如果设置为true,则会添加mime类型text/html,否则mime类型为text/plain*/
	private boolean html = true;
	/**邮件类型*/
	private EmailType type = EmailType.SIMPLE;
	/**模板名称*/
	private String templateName;
	/**模板参数*/
	private Map<String, Object> templateParam;
	
	public Email(String from, String receivers, String subject) {
		this.from = from;
		addReceivers(receivers);
		this.subject = subject;
	}
	
	public Email(String from, List<String> receivers, String subject, String content,
			boolean html, EmailType type) {
		this.from = from;
		addReceivers(StringUtils.join(receivers, ";"));
		this.subject = subject;
		this.content = content;
		this.html = html;
		this.type = type;
	}
	
	public List<String> getReceivers() {
		return receivers;
	}
	public void addReceiver(String receiver) {
		addReceivers(receiver);
	}
	public void addReceivers(String receivers) {
		if (StringUtils.hasLength(receivers)) {
			if (receivers.contains(";")) {
				String[] receiverArray = StringUtils.split(receivers, ";");
				for (String receiver : receiverArray) {
					if (StringUtils.hasLength(receiver)) {
						this.receivers.add(receiver.trim());
					}
				}
			} else {
				this.receivers.add(receivers.trim());
			}
		}
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<File> getAttachements() {
		return attachements;
	}
	public void addAttachement(File attachement) {
		if (CollectionUtil.isEmpty(attachements)) {
			attachements = new ArrayList<File>();
		}
		this.attachements.add(attachement);
	}
	public boolean isHtml() {
		return html;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}
	public EmailType getType() {
		return type;
	}
	public void setType(EmailType type) {
		this.type = type;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public Map<String, Object> getTemplateParam() {
		return templateParam;
	}
	public void setTemplateParam(Map<String, Object> templateParam) {
		this.templateParam = templateParam;
	}
	
}
