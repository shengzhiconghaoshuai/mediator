package net.chinacloud.mediator.productMapping.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.ProductPartnumberMapping;
import net.chinacloud.mediator.pojo.Page;
import net.chinacloud.mediator.pojo.PageView;
import net.chinacloud.mediator.service.ProductPartnumberMappingService;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.system.registry.service.RegistryService;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.domain.TaskDomain;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/system/productMapping/")
public class ProductMappingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductMappingController.class);
	
	@Autowired
	ProductPartnumberMappingService  productPartnumberMappingService;
	
	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	ChannelService channelService;
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	RegistryService registryService;
		
	@RequestMapping("list")
	public String toProductMapping(Model model){
		List<Channel> channels = channelService.getAvailableChannels();
		model.addAttribute("channels", channels);
		return "system/productMapping/list";
	}
		
		
	@RequestMapping("listData")
	public void loadData(@RequestParam(required=false) Integer rows,
			@RequestParam(required=false) Integer page,
			@RequestParam(required=false) String channelProductId, 
			@RequestParam(required=false) String outer_id, 
			HttpServletRequest request,
			HttpServletResponse response){
		Map<String,Object> queryParam = new HashMap<String,Object>();
		Page<ProductPartnumberMapping> pg = new Page<ProductPartnumberMapping>(page, rows);
		queryParam.put(Constant.PAGE_START_INDEX, pg.getFirstIndex());
		queryParam.put(Constant.PAGE_END_INDEX,pg.getLastIndex());
		if (StringUtils.hasText(channelProductId)) {
			queryParam.put("CHANNEL_PRODUCT_ID", channelProductId.trim());
		}
		if (StringUtils.hasText(outer_id)) {
			queryParam.put("OUTER_ID", outer_id.trim());
		}
		List<ProductPartnumberMapping> productPartnumberMapping= productPartnumberMappingService.queryProductPartnumberMapping(queryParam);
		pg.setTotalRecords(productPartnumberMappingService.count(queryParam));
		pg.setData(productPartnumberMapping);
		response.setContentType("application/json");
		PrintWriter pw = null;
		try{
			pw = response.getWriter();
			pw.write(JsonUtil.object2JsonString(new PageView<ProductPartnumberMapping>(pg)));
		}catch(IOException e){
			LOGGER.error("",e);
		}finally{
			if(pw!=null){
				pw.close();
				pw = null;
			}
		}
	}
	
	@RequestMapping("upload")
	public void uploadData(HttpServletRequest request,HttpServletResponse response,@RequestParam("jitfile_input")MultipartFile jitfile_input){
		try {
			int applicationId = Integer.valueOf(request.getParameter("applicationId"));
			int channelId = Integer.valueOf(request.getParameter("channelId"));
			
			Registry registry = SpringUtil.getBean(Registry.class);
			String path = registry.get("productMappingPath");
			
			
			String fileName = path + jitfile_input.getOriginalFilename();
			
			jitfile_input.transferTo(new File(fileName));
			
			List<String> str = this.importCsv(new File(fileName));
			List<Product> products = new ArrayList<Product>();
			
			for (String s : str) {
				String[] sku = s.split(",");
				String channelProductId = sku[1].trim();
				String outerId = sku[0].trim();
				if (!StringUtils.hasText(outerId) || !StringUtils.hasText(channelProductId)) {
					continue;
				}
				Product p = new Product();
				p.setChannelProductId(channelProductId);
				p.setOuterProductId(outerId);
				products.add(p);
			}
			
			if (!CollectionUtil.isEmpty(products)) {
				
				Channel channel = channelService.getChannelById(channelId);
				Application application = applicationService.getApplicationById(applicationId);
				
				TaskContext context = new TaskContext();
				context.setApplicationId(applicationId);
				context.setChannelId(channelId);
				context.setChannelCode(channel.getCode());
				context.setApplicationCode(application.getCode());
				context.setStoreId(application.getStoreId());
				
				for (Product p : products) {
					try {
						CommonNotifyPacket<Product> productInfoPacket = new CommonNotifyPacket<Product>(p,"mapping");
						Task task = taskManager.generateTask(channel.getCode(), productInfoPacket);
						
						if (null != task) {
							//传递上下文
							task.setContext(context);
							taskManager.executeTask(task);
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<String> importCsv(File file) {
		List<String> dataList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				dataList.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataList;
	}
		
}
