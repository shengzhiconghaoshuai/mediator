/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：FreeMarkerUtil.java
 * 描述： 
 */
package net.chinacloud.mediator.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月20日 下午1:50:08
 */
public class FreeMarkerUtil {
	private static FreeMarkerConfigurer freemarkerConfig;
	
	public static void setFreemarkerConfig(FreeMarkerConfigurer freemarkerConfig) {
		FreeMarkerUtil.freemarkerConfig = freemarkerConfig;
	}

	/**
	 * 获取模板填充后的内容
	 * @param templateName 模板名称
	 * @param param 参数
	 * @return 使用参数填充模板后的内容
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String mergeFileTemplate(String templateName, Map<String, Object> param) throws IOException, TemplateException {
	    Template template = freemarkerConfig.getConfiguration().getTemplate(templateName);
	    return FreeMarkerTemplateUtils.processTemplateIntoString(template, param);
	}
}
