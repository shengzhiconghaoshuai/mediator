/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RegistryController.java
 * 描述： 
 */
package net.chinacloud.mediator.system.registry.web.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.pojo.Page;
import net.chinacloud.mediator.pojo.PageView;
import net.chinacloud.mediator.system.registry.RegistryData;
import net.chinacloud.mediator.system.registry.service.RegistryService;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月1日 下午2:44:45
 */
@Controller
@RequestMapping("/system/task/registry")
public class RegistryController {

	public static final Logger LOGGER = LoggerFactory.getLogger(RegistryController.class);
	
	
	@Autowired
	RegistryService registryServiceImpl;
	
	@RequestMapping("/")
	public String registry(){
		return "system/registry/registry";
	}
	
	@RequestMapping("/loadRegistry")
	public void loadRegitry(HttpServletRequest request,HttpServletResponse response,int rows,int page) throws IOException{
		Page<RegistryData> pages = new Page<RegistryData>(page, rows);
		Map<String,Object> params = new HashMap<String, Object>();
		params.put(Constant.PAGE_START_INDEX, pages.getFirstIndex());
		params.put(Constant.PAGE_END_INDEX, pages.getLastIndex());
		List<RegistryData> registryDatas = registryServiceImpl.getAllRegistryData(params);
		pages.setData(registryDatas);
		pages.setTotalRecords(registryServiceImpl.countAllRegistryData(params));
		this.responseWriter(new PageView<RegistryData>(pages), response);
	}
	
	@RequestMapping("/updateRegistry")
	public void updateRegistry(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String key = request.getParameter("key");
		String rvalue = request.getParameter("value");
		String rdescription = request.getParameter("description");
		RegistryData rd = new RegistryData(key,rvalue,rdescription);
		int result = registryServiceImpl.update(rd);
		this.responseWriter(result, response);
	}
	
	@RequestMapping("/removeRegistry")
	public void removeRegistry(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String key = request.getParameter("key");
		int result = registryServiceImpl.remove(key);
		this.responseWriter(result, response);
	}
	
	@RequestMapping("/addRegistry")
	public void addRegistry(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String description = request.getParameter("description");
		RegistryData registry = new RegistryData(key,value,description);
		int result = registryServiceImpl.add(registry);
		this.responseWriter(result, response);
	}
	
	private void responseWriter(Object object,HttpServletResponse response){
		response.setContentType("application/json");
		PrintWriter pw = null;
		try{
			pw = response.getWriter();
			pw.write(JsonUtil.object2JsonString(object));
		}catch(IOException e){
			LOGGER.error("",e);
		}finally{
			if(pw!=null){
				pw.close();
				pw = null;
			}
		}
		
	}
	
}
