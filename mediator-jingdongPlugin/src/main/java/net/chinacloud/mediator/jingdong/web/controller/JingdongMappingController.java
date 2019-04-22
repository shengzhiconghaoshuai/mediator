package net.chinacloud.mediator.jingdong.web.controller;

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
import net.chinacloud.mediator.domain.CategoryMapping;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.domain.ChannelAttributeValueMapping;
import net.chinacloud.mediator.init.connector.cache.ConnectorManager;
import net.chinacloud.mediator.jingdong.JingDongConstant;
import net.chinacloud.mediator.jingdong.domain.ChannelAttributeMapping;
import net.chinacloud.mediator.jingdong.domain.JdAttValue;
import net.chinacloud.mediator.jingdong.domain.JdAttribute;
import net.chinacloud.mediator.jingdong.domain.JdCategory;
import net.chinacloud.mediator.jingdong.service.JingdongProductService;
import net.chinacloud.mediator.pojo.Page;
import net.chinacloud.mediator.pojo.PageView;
import net.chinacloud.mediator.service.ProductAttributeService;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.domain.category.Category;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeSearchRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeValueSearchRequest;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse.Attribute;
import com.jd.open.api.sdk.response.category.CategoryAttributeValueSearchResponse;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;

@Controller
@RequestMapping("/system/jingdong/mapping/")
public class JingdongMappingController {
	public static final Logger LOGGER = LoggerFactory.getLogger(JingdongMappingController.class);
	
	@Autowired
	JingdongProductService jingdongProductService;
	
	@Autowired
	ChannelService channelService;
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	ProductAttributeService productAttributeService;
	
	@Autowired
	ConnectorManager<JdRequest<?>> connectorManager;
	/**
	 * 跳转到京东渠道主目录映射配置页面
	 * @return
	 */
	@RequestMapping("/")
	public String toCategoryMapping(Model model) {
		Channel channel = channelService.getChannelByCode(JingDongConstant.CHANNEL_JINGDONG);
		List<Application> applications = applicationService.getAvailableApplicationsByChannelId(channel.getId());
		model.addAttribute("applications", applications);
		return "jingdong/jdCategoryMapping";
	}
	
	@RequestMapping("/getCategory")
	public void getCategory(HttpServletRequest request,Model model,HttpServletResponse response) throws JdException, IOException{
	String lev = request.getParameter("lev");
	String attType = request.getParameter("attType");
	if("3".equals(lev)){
		    String cid = request.getParameter("id");
		    Application application = applicationService.getApplicationById(Integer.parseInt(request.getParameter("selectValue")));
		    ContextUtil.set(Constant.APPLICATION_CODE, application.getCode());
			CategoryAttributeSearchRequest casrequest = new CategoryAttributeSearchRequest();
			casrequest.setCid(cid);
			CategoryAttributeSearchResponse casresponse = this.connectorManager.getConnector().execute(casrequest);
			List<Attribute> attributes = casresponse.getAttributes();
			List<JdAttribute> jdAttributes = new ArrayList<JdAttribute>();
			for(Attribute a : attributes){
				JdAttribute jd = new JdAttribute();
				jd.setId(a.getAid());
				jd.setFid(a.getCid());
				jd.setName(a.getName());
				jd.setAttType(a.getAttType());
				jd.setColorProp(a.isColorProp());
				jd.setGroupId(a.getGroupId());
				jd.setIndexId(a.getIndexId());
				jd.setInputType(a.getInputType());
				jd.setIsFet(a.getFet());
				jd.setIsNav(a.getNav());
				jd.setIsParent(true);
				jd.setIsReq(a.getReq());
				jd.setKeyProp(a.getKeyProp());
				jd.setOptions(a.getOptions());
				jd.setSaleProp(a.getSaleProp());
				jd.setSizeProp(a.isSizeProp());
				jd.setStatus(a.getStatus());
				jd.setIdentify("属性映射");
				jdAttributes.add(jd);
			}
			LOGGER.debug(JsonUtil.object2JsonString(jdAttributes));
			this.responseWriter(jdAttributes, response);
	}else{
		if(attType!=null){
			String avs = request.getParameter("id");
			Application application = applicationService.getApplicationById(Integer.parseInt(request.getParameter("selectValue")));
			ContextUtil.set(Constant.APPLICATION_CODE, application.getCode());
			CategoryAttributeValueSearchRequest cavsrequest = new CategoryAttributeValueSearchRequest();
			cavsrequest.setAvs(avs);
			CategoryAttributeValueSearchResponse cavsresponse = this.connectorManager.getConnector().execute(cavsrequest);
			List<AttValue> attrValues =  cavsresponse.getAttValues();
			List<JdAttValue> jdattrValues =  new ArrayList<JdAttValue>();
			for(AttValue a : attrValues){
				JdAttValue jd = new JdAttValue();
				jd.setId(a.getVid());
				jd.setFid(a.getAid());
				jd.setFeatures(a.getFeatures());
				jd.setName(a.getName());
				jd.setIdentify("属性值映射");
				jd.setIndexId(a.getIndexId());
				jd.setStatus(a.getStatus());
				jdattrValues.add(jd);
			}
			LOGGER.debug(JsonUtil.object2JsonString(jdattrValues));
			this.responseWriter(jdattrValues, response);
		}else{
			Application application = applicationService.getApplicationById(Integer.parseInt(request.getParameter("selectValue")));
			ContextUtil.set(Constant.APPLICATION_CODE, application.getCode());
			CategorySearchRequest  crequest = new CategorySearchRequest();
			CategorySearchResponse  cresponse=this.connectorManager.getConnector().execute(crequest);
			List<Category> category =  cresponse.getCategory();
			List<JdCategory> jdcategory = new ArrayList<JdCategory>();  
			for(Category c : category){
				JdCategory jd = new JdCategory();
				jd.setId(c.getId());
				jd.setName(c.getName());
				jd.setFid(c.getFid());
				jd.setIsParent(true);
				jd.setStatus(c.getStatus());
				jd.setLev(c.getLev());
				if(c.getLev()==3){
				jd.setIdentify("叶子目录");
				}
				jdcategory.add(jd);
			}
			LOGGER.debug(JsonUtil.object2JsonString(jdcategory));
			this.responseWriter(jdcategory, response);
		}
	  }
	}
	
	@RequestMapping("/loadCategoryMapping")
	public void loadCategory(HttpServletRequest request,HttpServletResponse response,int page, int rows) throws IOException{
		Page<CategoryMapping> pg = new Page<CategoryMapping>(page, rows);
		int applicationId = Integer.parseInt(request.getParameter("applicationId"));
		String channelCategoryName = request.getParameter("channelCategoryName");
		String categoryName = request.getParameter("categoryName");
		Map<String,Object> param =new HashMap<String, Object>();
		param.put("APPLICATION_ID", applicationId);
		param.put(Constant.PAGE_START_INDEX, pg.getFirstIndex());
		param.put(Constant.PAGE_END_INDEX,pg.getLastIndex());
		if(channelCategoryName!=null){
			param.put("CHANNEL_CAT_NAME", channelCategoryName.trim());
		}
		if(categoryName!=null){
			param.put("CAT_NAME", categoryName.trim());
		}
		List<CategoryMapping> _categoryMappings = this.productAttributeService.loadCaregroyMapping(param);
		pg.setTotalRecords(this.productAttributeService.countCaregroyMapping(param));
		pg.setData(_categoryMappings);
		this.responseWriter(new PageView<CategoryMapping>(pg), response);
	}
	
	@RequestMapping("/loadjdChannelAttrMapping")
	public void loadjdChannelAttrvalMapping(HttpServletRequest request,HttpServletResponse response,int page, int rows) throws IOException{
		Page<ChannelAttributeMapping> pg = new Page<ChannelAttributeMapping>(page,rows);
		int applicationId = Integer.parseInt(request.getParameter("_applicationId"));
		String channelAttributeName = request.getParameter("channelAttributeName");
		Map<String,Object> param =new HashMap<String, Object>();
		param.put("APPLICATION_ID", applicationId);
		param.put(Constant.PAGE_START_INDEX, pg.getFirstIndex());
		param.put(Constant.PAGE_END_INDEX,pg.getLastIndex());
		if(channelAttributeName!=null){
			param.put("CHANNEL_ATTR_NAME", channelAttributeName);
		}
		List<ChannelAttributeMapping> _channelAttributeMapping = this.jingdongProductService.loadjdChannelAttrMapping(param);
		pg.setData(_channelAttributeMapping);
		pg.setTotalRecords(this.jingdongProductService.countChannelAttributeMapping(param));
        this.responseWriter(new PageView<ChannelAttributeMapping>(pg), response);
	}
	
	@RequestMapping("/loadChannelAttrvalMapping")
	public void loadChannelAttrvalMapping(HttpServletRequest request,HttpServletResponse response,int page, int rows) throws IOException{
		Page<ChannelAttributeValueMapping> pg = new Page<ChannelAttributeValueMapping>(page,rows);
		int applicationId = Integer.parseInt(request.getParameter("_applicationId"));
		String channelAttributeValue = request.getParameter("channelAttributeValue");
		String attributeValue = request.getParameter("attributeValue");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("APPLICATION_ID", applicationId);
		params.put(Constant.PAGE_START_INDEX, pg.getFirstIndex());
		params.put(Constant.PAGE_END_INDEX,pg.getLastIndex());
		if(channelAttributeValue!=null){
			params.put("CHANNEL_ATTRVAL",channelAttributeValue.trim());
		}
		if(attributeValue!=null){
			params.put("ATTRVAL", attributeValue.trim());
		}
		List<ChannelAttributeValueMapping>  _channelAttributeValueMapping = this.productAttributeService.loadChannelAttrvalMapping(params);
		Long totalCount = this.productAttributeService.countChannelAttrvalMapping(params);
		pg.setData(_channelAttributeValueMapping);
		pg.setTotalRecords(totalCount);
		this.responseWriter(new PageView<ChannelAttributeValueMapping>(pg), response);
	}
	
	@RequestMapping("/saveCategroyMapping")
	public void saveCategroyMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int applicationId = Integer.parseInt(request.getParameter("applicationId"));
		long channelId = Long.parseLong(request.getParameter("channelId"));
		long catId = Long.parseLong(request.getParameter("catId"));
		String channelName = request.getParameter("channelName");
		String catName = request.getParameter("catName");
		CategoryMapping categoryMapping = new CategoryMapping();
		categoryMapping.setApplicationId(applicationId);
		categoryMapping.setCategoryId(catId);
		categoryMapping.setCategoryName(catName);
		categoryMapping.setChannelCategoryId(channelId);
		categoryMapping.setChannelCategoryName(channelName);
        int result = this.productAttributeService.saveCategroyMapping(categoryMapping);
        this.responseWriter(result, response);        
	}
	
	@RequestMapping("/saveChannelAttrvalMapping")
	public void saveChannelAttrvalMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int applicationId = Integer.parseInt(request.getParameter("applicationId"));
		ChannelAttributeValueMapping cavmapping = new ChannelAttributeValueMapping();
		cavmapping.setApplicationId(applicationId);
		cavmapping.setAttributeCode(request.getParameter("attrcode"));
		cavmapping.setAttributeValue(request.getParameter("attrval"));
		cavmapping.setAttributeValueCode(request.getParameter("attrvalcode"));
		cavmapping.setChannelAttributeId(request.getParameter("channelAttrId"));
		cavmapping.setChannelAttributeValue(request.getParameter("channelAttrval"));
		cavmapping.setChannelAttributeValueId(request.getParameter("channelAttrvalId"));
		cavmapping.setChannelCategoryId(Long.parseLong(request.getParameter("channelId")));
		int result = this.productAttributeService.saveChannelAttrvalMapping(cavmapping);
		this.responseWriter(result, response);
	}
	
	@RequestMapping("/savejdChannelAttrMapping")
	public void savejdChannelAttrvalMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int applicationId = Integer.parseInt(request.getParameter("applicationId"));
		ChannelAttributeMapping caMapping = new ChannelAttributeMapping();
		caMapping.setApplicationId(applicationId);
		caMapping.setChannelAttributeId(request.getParameter("channelAttrvalId"));
		caMapping.setChannelAttributeName(request.getParameter("channelAttrval"));
		caMapping.setChannelCategoryId(Long.parseLong(request.getParameter("channelId")));
		caMapping.setAttributeCode(request.getParameter("jdattrcode"));
		caMapping.setDefaultValue(request.getParameter("defaultValue"));
		caMapping.setAttributeType(Integer.parseInt(request.getParameter("attributeType")));
		caMapping.setInputType(Integer.parseInt(request.getParameter("inputType")));
		caMapping.setKeyProperty(this.bool2Int(request.getParameter("keyProperty")));
		caMapping.setSaleProperty(this.bool2Int(request.getParameter("saleProperty")));
		caMapping.setSizeProperty(this.bool2Int(request.getParameter("sizeProperty")));
		caMapping.setColorProperty(this.bool2Int(request.getParameter("colorProperty")));
        caMapping.setRequired(this.bool2Int(request.getParameter("required")));
        caMapping.setStatus("VALID".equals(request.getParameter("status").trim().toString()) ? 1 : 0);
        int result = this.jingdongProductService.saveChannelAttributeMapping(caMapping);
        this.responseWriter(result, response);
	}
	
	@RequestMapping("/removeCategoryMapping")
	public void removeCategoryMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int mappingId = Integer.parseInt(request.getParameter("id"));
		int result = productAttributeService.deleteCategroyMapping(mappingId);
		this.responseWriter(result, response);
	}
	
	@RequestMapping("/removejdChannelAttrMapping")
	public void removejdChannelAttrMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int result =jingdongProductService.deletejdChannelAttrMapping(id);
		this.responseWriter(result, response);
	}
	
	@RequestMapping("/removeChannelAttrvalMapping")
	public void removeChannelAttrvalMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int result = productAttributeService.deleteChannelAttrvalMapping(id);
		this.responseWriter(result, response);
	}
	
	/**
	 * 更新CategoryMapping
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/updateCategroyMapping")
	public void updateCategroyMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int applicationId = Integer.parseInt(request.getParameter("applicationId"));
		long channelId = Long.parseLong(request.getParameter("channelId"));
		long catId = Long.parseLong(request.getParameter("catId"));
		String channelName = request.getParameter("channelName");
		String catName = request.getParameter("catName");
		CategoryMapping categoryMapping = new CategoryMapping();
		categoryMapping.setApplicationId(applicationId);
		categoryMapping.setCategoryId(catId);
		categoryMapping.setCategoryName(catName);
		categoryMapping.setChannelCategoryId(channelId);
		categoryMapping.setChannelCategoryName(channelName);
		categoryMapping.setId(id);
		int result = productAttributeService.updateCategroyMapping(categoryMapping);
		this.responseWriter(result, response);
	}
	
	@RequestMapping("/updateChannelAttrvalMapping")
	public void updateChannelAttrvalMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int applicationId = Integer.parseInt(request.getParameter("applicationId"));
		ChannelAttributeValueMapping cavmapping = new ChannelAttributeValueMapping();
		cavmapping.setId(id);
		cavmapping.setApplicationId(applicationId);
		cavmapping.setAttributeCode(request.getParameter("attrcode"));
		cavmapping.setAttributeValue(request.getParameter("attrval"));
		cavmapping.setAttributeValueCode(request.getParameter("attrvalcode"));
		cavmapping.setChannelAttributeId(request.getParameter("channelAttrId"));
		cavmapping.setChannelAttributeValue(request.getParameter("channelAttrval"));
		cavmapping.setChannelAttributeValueId(request.getParameter("channelAttrvalId"));
		cavmapping.setChannelCategoryId(Long.parseLong(request.getParameter("channelId")));
		int result = productAttributeService.updateChannelAttrvalMapping(cavmapping);
		this.responseWriter(result, response);
	}
	
	@RequestMapping("/updatejdChannelAttrMapping")
	public void updatejdChannelAttrvalMapping(HttpServletRequest request,HttpServletResponse response) throws IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		int applicationId = Integer.parseInt(request.getParameter("applicationId"));
		ChannelAttributeMapping caMapping = new ChannelAttributeMapping();
		caMapping.setId(id);
		caMapping.setApplicationId(applicationId);
		caMapping.setChannelAttributeId(request.getParameter("channelAttrvalId"));
		caMapping.setChannelAttributeName(request.getParameter("channelAttrval"));
		caMapping.setChannelCategoryId(Long.parseLong(request.getParameter("channelId")));
		caMapping.setAttributeCode(request.getParameter("jdattrcode"));
		caMapping.setDefaultValue(request.getParameter("defaultValue"));
		caMapping.setAttributeType(Integer.parseInt(request.getParameter("attributeType")));
		caMapping.setInputType(this.bool2Int(request.getParameter("inputType")));
		caMapping.setKeyProperty(this.bool2Int(request.getParameter("keyProperty")));
		caMapping.setSaleProperty(this.bool2Int(request.getParameter("saleProperty")));
		caMapping.setSizeProperty(this.bool2Int(request.getParameter("sizeProperty")));
		caMapping.setColorProperty(this.bool2Int(request.getParameter("colorProperty")));
        caMapping.setRequired(this.bool2Int(request.getParameter("required")));
        caMapping.setStatus(request.getParameter("status").trim().toString()=="VALID" ? 1 : 0);
        int result = jingdongProductService.updatejdChannelAttrMapping(caMapping);
        this.responseWriter(result, response);
	}
	
	private int bool2Int(String str){
		boolean bool = Boolean.parseBoolean(str.trim());
		return bool ==true ? 1 : 0 ;
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
