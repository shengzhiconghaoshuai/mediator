package net.chinacloud.mediator.taobao.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.CategoryMapping;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.domain.ChannelAttributeValueMapping;
import net.chinacloud.mediator.pojo.Page;
import net.chinacloud.mediator.pojo.PageView;
import net.chinacloud.mediator.service.ProductAttributeService;
import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.taobao.domain.ItemPropWrapper;
import net.chinacloud.mediator.taobao.domain.TaobaoChannelAttributeMapping;
import net.chinacloud.mediator.taobao.service.TaobaoProductService;
import net.chinacloud.mediator.utils.ContextUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.PropValue;
@Controller
@RequestMapping("/system/taobao/mapping/")
public class TaobaoMappingController extends AbstractController {
	
	@Autowired
	private ServiceManager serviceManager;
	@Autowired
	ChannelService channelService;
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	@Qualifier("taobaoProductServiceImpl")
	TaobaoProductService productService;
	@Autowired
	ProductAttributeService productAttributeService;
	
	/**
	 * 跳转到淘宝渠道主目录映射配置页面
	 * @return
	 */
	@RequestMapping("/")
	public String toCategoryMapping(Model model) {
		Channel channel = channelService.getChannelByCode(TaobaoConstant.CHANNEL_TAOBAO);
		if (null != channel) {
			List<Application> applications = applicationService.getAvailableApplicationsByChannelId(channel.getId());
			model.addAttribute("applications", applications);
		}
		return "taobao/categoryMapping";
	}
	
	/**
	 * 根据父目录id获取淘宝子目录信息
	 * @return
	 */
	@RequestMapping("/category/get")
	public void getTaobaoCategory(@RequestParam(required = false, defaultValue = "0", value = "cid") long parentCategoryId, Integer applicationId, HttpServletResponse response) {
		Application application = applicationService.getApplicationById(applicationId);
		ContextUtil.set(Constant.APPLICATION_CODE, application.getCode());;
		List<ItemCat> itemCats = productService.getChannelCategory(parentCategoryId);
		
		super.ajaxResponse(itemCats, response);
	}
	
	/**
	 * 保存类目映射
	 * @param channelCid
	 * @param channelCname
	 * @param systemCid
	 * @param systemCname
	 * @param applicationId
	 */
	@RequestMapping("/category/save")
	public void saveCategoryMapping(
			@RequestParam(value="channelCid", required=true) long channelCid, 
			@RequestParam(value="channelCname", required=true) String channelCname, 
			@RequestParam(value="systemCid", required=true) long systemCid, 
			@RequestParam(value="systemCname", required=true) String systemCname,
			@RequestParam(value="applicationId", required=true) int applicationId,
			HttpServletResponse response) {
		CategoryMapping categroyMapping = new CategoryMapping();
		categroyMapping.setChannelCategoryId(channelCid);
		categroyMapping.setChannelCategoryName(channelCname);
		categroyMapping.setCategoryId(systemCid);
		categroyMapping.setCategoryName(systemCname);
		categroyMapping.setApplicationId(applicationId);
		int result = productAttributeService.saveCategroyMapping(categroyMapping);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "类目映射失败"), response);
		}
	}
	
	/**
	 * 查询已映射的类目
	 * @param response
	 * @param page
	 * @param rows
	 * @throws IOException
	 */
	@RequestMapping("/category/loadCategoryMapping")
	public void loadCategory(
			@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "applicationId") int applicationId,
			@RequestParam(value = "channelCategoryId", required = false) Long channelCategoryId,
			@RequestParam(value = "channelCategoryName", required = false) String channelCategoryName,
			@RequestParam(value = "categoryName", required = false) String categoryName,
			HttpServletResponse response) throws IOException{
		Page<CategoryMapping> pager = new Page<CategoryMapping>(page, rows);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("APPLICATION_ID", applicationId);
		params.put(Constant.PAGE_START_INDEX, pager.getFirstIndex());
		params.put(Constant.PAGE_END_INDEX, pager.getLastIndex());
		if (null != channelCategoryId) {
			params.put("CHANNEL_CAT_ID", channelCategoryId);
		}
		if (StringUtils.hasLength(channelCategoryName)) {
			params.put("CHANNEL_CAT_NAME", channelCategoryName);
		}
		if (StringUtils.hasLength(categoryName)) {
			params.put("CAT_NAME", categoryName);
		}
		
		List<CategoryMapping> categoryMappings = productAttributeService.loadCaregroyMapping(params);
		Long totalCount = productAttributeService.countCaregroyMapping(params);
		pager.setData(categoryMappings);
		pager.setTotalRecords(totalCount);
		
		super.ajaxResponse(new PageView<CategoryMapping>(pager), response);
	}
	
	/**
	 * 修改类目映射
	 * @param categoryMappingId
	 * @param channelCid
	 * @param channelCname
	 * @param systemCid
	 * @param systemCname
	 * @param applicationId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/category/update")
	public void updateCategroyMapping(
			@RequestParam(value="id", required=true) int categoryMappingId,
			@RequestParam(value="channelCid", required=true) long channelCid, 
			@RequestParam(value="channelCname", required=true) String channelCname, 
			@RequestParam(value="systemCid", required=true) long systemCid, 
			@RequestParam(value="systemCname", required=true) String systemCname,
			@RequestParam(value="applicationId", required=true) int applicationId,
			HttpServletResponse response) throws IOException{
		CategoryMapping categoryMapping = new CategoryMapping();
		categoryMapping.setApplicationId(applicationId);
		categoryMapping.setCategoryId(systemCid);
		categoryMapping.setCategoryName(systemCname);
		categoryMapping.setChannelCategoryId(channelCid);
		categoryMapping.setChannelCategoryName(channelCname);
		categoryMapping.setId(categoryMappingId);
		int result = productAttributeService.updateCategroyMapping(categoryMapping);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "类目映射修改失败"), response);
		}
	}
	
	@RequestMapping("/attr/update")
	public void updateAttrMapping(
			@RequestParam(value="id", required=true) int attrMappingId,
			@RequestParam(value="newSystemAttrId", required=true) String systemAttrId, 
			@RequestParam(value="newDefaultValue", required=true) String defaultValue, 
			@RequestParam(value="applicationId", required=true) int applicationId,
			HttpServletResponse response) throws IOException{
		TaobaoChannelAttributeMapping attributeMapping = new TaobaoChannelAttributeMapping();
		attributeMapping.setId(attrMappingId);
		attributeMapping.setAttributeCode(systemAttrId);
		attributeMapping.setDefaultValue(defaultValue);
		int result = productService.updateChannelAttributeMapping(attributeMapping);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "类目映射修改失败"), response);
		}
	}
	
	/**
	 * 删除类目映射
	 * @param categoryMappingId
	 * @param channelCid
	 * @param channelCname
	 * @param systemCid
	 * @param systemCname
	 * @param applicationId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/category/delete")
	public void deleteCategroyMapping(
			@RequestParam(value="id", required=true) int categoryMappingId,
			HttpServletResponse response) throws IOException{
		int result = productAttributeService.deleteCategroyMapping(categoryMappingId);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "类目映射删除失败"), response);
		}
	}
	
	@RequestMapping("/attr/delete")
	public void deleteAttrMapping(
			@RequestParam(value="id", required=true) int attrMappingId,
			HttpServletResponse response) throws IOException{
		int result = productService.deleteChannelAttributeMapping(attrMappingId);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "类目映射删除失败"), response);
		}
	}
	
	/**
	 * 获取淘宝属性
	 * @param categoryId
	 */
	@RequestMapping("/attr/get")
	public void getTaobaoAttr(
			@RequestParam(value = "channelCid") long categoryId,
			@RequestParam(value = "applicationId") int applicationId,
			HttpServletResponse response) {
		Application application = applicationService.getApplicationById(applicationId);
		ContextUtil.set(Constant.APPLICATION_CODE, application.getCode());;
		List<ItemPropWrapper> itemPropsWrapper = productService.getChannelAttribute(categoryId);
		
		super.ajaxResponse(itemPropsWrapper, response);
	}
	
	/**
	 * 添加属性映射
	 * @param categoryId
	 * @param channelAttrId
	 * @param channelAttrName
	 * @param attrCode
	 * @param defaultValue
	 * @param keyProp
	 * @param salesProp
	 * @param colorProp
	 * @param enumProp
	 * @param itemProp
	 * @param must
	 * @param multi
	 * @param status
	 * @param inputProp
	 * @param attrType
	 * @param applicationId
	 * @param response
	 */
	@RequestMapping("/attr/save")
	public void saveTaobaoAttrMapping(
			@RequestParam(value = "channelCid") long categoryId,
			@RequestParam(value = "tbAttrId") String channelAttrId,
			@RequestParam(value = "tbAttrName") String channelAttrName,
			@RequestParam(value = "systemAttrId") String attrCode,
			@RequestParam(value = "defaultValue") String defaultValue,
			@RequestParam(value = "tbKeyProp") int keyProp,
			@RequestParam(value = "tbSalesProp") int salesProp,
			@RequestParam(value = "tbColorProp") int colorProp,
			@RequestParam(value = "tbEnumProp") int enumProp,
			@RequestParam(value = "tbItemProp") int itemProp,
			@RequestParam(value = "tbReqProp") int must,
			@RequestParam(value = "tbMulti") int multi,
			@RequestParam(value = "tbStatus") int status,
			@RequestParam(value = "tbInputProp") int inputProp,
			@RequestParam(value = "tbAttrType") int attrType,
			@RequestParam(value = "applicationId") int applicationId,
			HttpServletResponse response) {
		TaobaoChannelAttributeMapping attrMapping = new TaobaoChannelAttributeMapping();
		attrMapping.setChannelCategoryId(categoryId);
		attrMapping.setChannelAttributeId(channelAttrId);
		attrMapping.setChannelAttributeName(channelAttrName);
		attrMapping.setAttributeCode(attrCode);
		attrMapping.setDefaultValue(defaultValue);
		attrMapping.setKeyProperty(keyProp);
		attrMapping.setSaleProperty(salesProp);
		attrMapping.setColorProperty(colorProp);
		attrMapping.setEnumprop(enumProp);
		attrMapping.setItemprop(itemProp);
		attrMapping.setMust(must);
		attrMapping.setMulti(multi);
		attrMapping.setStatus(status);
		attrMapping.setInputprop(inputProp);
		attrMapping.setAttributeType(attrType);
		attrMapping.setApplicationId(applicationId);
		int result = productService.saveChannelAttributeMapping(attrMapping);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "属性映射添加失败"), response);
		}
	}
	
	/**
	 * 保存属性值映射
	 * @param categoryId
	 * @param channelAttrId
	 * @param channelAttrValId
	 * @param channelAttrVal
	 * @param attrId
	 * @param attrval
	 * @param applicationId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/attrval/save")
	public void saveChannelAttrvalMapping(
			@RequestParam(value = "channelCid") long categoryId,
			@RequestParam(value = "tbAttrId") String channelAttrId,
			@RequestParam(value = "channelAttrValId") String channelAttrValId,
			@RequestParam(value = "channelAttrVal") String channelAttrVal,
			@RequestParam(value = "systemAttrId") String attrId,
			@RequestParam(value = "systemAttrVal") String attrval,
			@RequestParam(value = "systemAttrValId", defaultValue = "") String attrvalId,
			@RequestParam(value = "applicationId") int applicationId,
			HttpServletResponse response) throws IOException{
		ChannelAttributeValueMapping cavmapping = new ChannelAttributeValueMapping();
		cavmapping.setApplicationId(applicationId);
		
		cavmapping.setChannelCategoryId(categoryId);
		cavmapping.setChannelAttributeId(channelAttrId);
		cavmapping.setChannelAttributeValueId(channelAttrValId);
		cavmapping.setChannelAttributeValue(channelAttrVal);
		
		cavmapping.setAttributeCode(attrId);
		cavmapping.setAttributeValue(attrval);
		cavmapping.setAttributeValueCode(attrvalId);
		int result = this.productAttributeService.saveChannelAttrvalMapping(cavmapping);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "属性映射添加失败"), response);
		}
	}
	
	@RequestMapping("/attrval/update")
	public void updateChannelAttrvalMapping(
			@RequestParam(value="id", required=true) int attrValMappingId,
			@RequestParam(value="newSystemAttrId", required=true) String attrCode,
			@RequestParam(value="newSystemAttrVal", required=true) String attrval,
			@RequestParam(value="newSystemAttrValId", required=true, defaultValue = "") String attrvalCode,
			HttpServletResponse response) throws IOException{
		ChannelAttributeValueMapping cavmapping = new ChannelAttributeValueMapping();
		cavmapping.setId(attrValMappingId);
		cavmapping.setAttributeCode(attrCode);
		cavmapping.setAttributeValue(attrval);
		cavmapping.setAttributeValueCode(attrvalCode);
		int result = productAttributeService.updateChannelAttrvalMapping(cavmapping);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "属性值修改失败"), response);
		}
	}
	
	/**
	 * 删除属性值映射
	 * @param attrValMappingId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/attrval/delete")
	public void deleteChannelAttrvalMapping(
			@RequestParam(value="id", required=true) int attrValMappingId,
			HttpServletResponse response) throws IOException{
		int result = productAttributeService.deleteChannelAttrvalMapping(attrValMappingId);
		if (result > 0) {
			super.ajaxResponse(super.ajaxResult(0, null), response);
		} else {
			super.ajaxResponse(super.ajaxResult(0, "属性值删除失败"), response);
		}
	}
	
	/**
	 * 获取淘宝属性值
	 * @param categoryId
	 */
	@RequestMapping("/attrval/get")
	public void getTaobaoAttrVal(
			@RequestParam(value = "cid") long categoryId,
			@RequestParam(value = "pid") long propertyId,
			@RequestParam(value = "applicationId") int applicationId,
			HttpServletResponse response) {
		Application application = applicationService.getApplicationById(applicationId);
		ContextUtil.set(Constant.APPLICATION_CODE, application.getCode());;
		List<PropValue> itemProps = productService.getChannelAttributeValue(categoryId, propertyId);
		super.ajaxResponse(itemProps, response);
	}
	
	/**
	 * 加载属性映射
	 * @param page
	 * @param rows
	 * @param applicationId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/attr/loadAttrMapping")
	public void loadChannelAttrMapping(
			@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "applicationId") int applicationId,
			@RequestParam(value = "channelAttributeName", required = false) String channelAttributeName,
			HttpServletResponse response) throws IOException{
		
		Page<TaobaoChannelAttributeMapping> pager = new Page<TaobaoChannelAttributeMapping>(page, rows);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("APPLICATION_ID", applicationId);
		params.put(Constant.PAGE_START_INDEX, pager.getFirstIndex());
		params.put(Constant.PAGE_END_INDEX, pager.getLastIndex());
		
		if (StringUtils.hasLength(channelAttributeName)) {
			params.put("CHANNEL_ATTR_NAME", channelAttributeName);
		}
		
		List<TaobaoChannelAttributeMapping> channelAttributeMapping = productService.getTaobaoChannelAttributeMappings(params);
		long totalRecords = productService.getTaobaoChannelAttributeMappingsCount(params);
		pager.setData(channelAttributeMapping);
		pager.setTotalRecords(totalRecords);
		
		super.ajaxResponse(new PageView<TaobaoChannelAttributeMapping>(pager), response);
	}
	
	/**
	 * 加载属性值映射
	 * @param page
	 * @param rows
	 * @param applicationId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/attrval/loadAttrValMapping")
	public void loadChannelAttrvalMapping(
			@RequestParam(value = "page", defaultValue = "1") int page, 
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "applicationId") int applicationId,
			@RequestParam(value = "channelAttributeValue", required = false) String channelAttributeValue,
			@RequestParam(value = "attributeValue", required = false) String attributeValue,
			HttpServletResponse response) throws IOException{
		Page<ChannelAttributeValueMapping> pager = new Page<ChannelAttributeValueMapping>(page, rows);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("APPLICATION_ID", applicationId);
		params.put(Constant.PAGE_START_INDEX, pager.getFirstIndex());
		params.put(Constant.PAGE_END_INDEX, pager.getLastIndex());
		
		if (StringUtils.hasLength(channelAttributeValue)) {
			params.put("CHANNEL_ATTRVAL", channelAttributeValue);
		}
		if (StringUtils.hasLength(attributeValue)) {
			params.put("ATTRVAL", attributeValue);
		}
		
		List<ChannelAttributeValueMapping> channelAttributeValueMapping = this.productAttributeService.loadChannelAttrvalMapping(params);
		long totalCount = this.productAttributeService.countChannelAttrvalMapping(params);
		pager.setData(channelAttributeValueMapping);
		pager.setTotalRecords(totalCount);
		
		super.ajaxResponse(new PageView<ChannelAttributeValueMapping>(pager), response);
	}
}
