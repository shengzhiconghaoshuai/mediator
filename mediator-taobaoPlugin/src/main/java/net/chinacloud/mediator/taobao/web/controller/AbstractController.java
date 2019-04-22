/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：AbstractColtroller.java
 * 描述： 
 */
package net.chinacloud.mediator.taobao.web.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chinacloud.mediator.pojo.AjaxResult;
import net.chinacloud.mediator.utils.JsonUtil;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月15日 下午2:11:33
 */
public class AbstractController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
	/**
	 * 重定向
	 * @param logicView
	 * @return
	 */
	public String redirect(String logicView) {
		return "redirect:" + logicView;
	}
	
	/**
	 * ajax操作返回结果信息
	 * @param status 状态,0:成功,1:失败
	 * @param msg 提示消息
	 * @return
	 */
	public AjaxResult ajaxResult(int status, String msg) {
		if (null != msg && !msg.isEmpty()) {
			return new AjaxResult(status, msg);
		}
		return new AjaxResult(status);
	}
	
	/**
	 * ajax json返回
	 * @param object
	 * @param response
	 */
	public void ajaxResponse(Object object, HttpServletResponse response) {
		response.setContentType("application/json");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			String json = JsonUtil.object2JsonString(object);
			pw.write(json);
		} catch (IOException e) {
			//e.printStackTrace();
			LOGGER.error("", e);
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
}
