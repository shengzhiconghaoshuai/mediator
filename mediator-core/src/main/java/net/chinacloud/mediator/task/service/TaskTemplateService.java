/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskTemplateService.java
 * 描述： task模板管理
 */
package net.chinacloud.mediator.task.service;

import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.dao.TaskTemplateDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
/**
 * <task模板管理>
 * <task模板管理>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月29日
 * @since 2014年12月29日
 */
@Service
public class TaskTemplateService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskTemplateService.class);
	
	@Autowired
	private TaskTemplateDao templateDao;
	
	/**
	 * 根据templateId获取模板信息
	 * @param templateId
	 * @return
	 */
	@Caching(cacheable = {
			@Cacheable(value="taskTemplate", key="#templateId", unless="#result == null")
	})
	public TaskTemplate getTaskTemplateById(Integer templateId) {
		return templateDao.getTaskTemplateById(templateId);
	}
	
	/**
	 * 获取所有task模板
	 * @return
	 */
	public List<TaskTemplate> getAllTaskTemplates(Map<String,Object> map) {
		return templateDao.getAllTaskTemplates(map);
	}
	
	/**
	 * 获取task模版数量
	 * @param map
	 * @return
	 */
	public Integer countAllTaskTemplates(Map<String,Object> map) {
		return templateDao.countAllTaskTemplates(map);
	}
	
	/**
	 * 保存模板信息
	 * @param template
	 */
	public int save(TaskTemplate template) {
		return templateDao.save(template);
	}
	
	/**
	 * 更新task模板,并从缓存中移除该task template
	 * @see net.chinacloud.mediator.task.cache.TaskTemplateCache
	 * 这边其实是有隐患的,如果修改了type或者是subType,那么从缓存中清除原始的template时将清除不掉,而根据task中的type及
	 * subType从缓存中获取到的还是更新前的template
	 * @param template
	 */
	@Caching(evict={
			@CacheEvict(value="taskTemplate", key="#template.type.concat('&').concat(#template.subType)", beforeInvocation=false),
			@CacheEvict(value="taskTemplate", key="#template.id", beforeInvocation=false)
	})
	public int update(TaskTemplate template) {
		return templateDao.update(template);
	}
	
	/**
	 * 根据task template的type及subtype获取对应的模板
	 * 先从缓存中根据 type + "&" + subType 从缓存中获取,获取不到,则执行方法体,从DB中获取,然后放入缓存
	 * @see net.chinacloud.mediator.task.cache.TaskTemplateCache
	 * @Cacheable 的condition在方法执行前后都会调用?spring cache接口的实现中ValueWrapper get(Object key)
	 * 方法返回值需要判断null,不然肯定能从缓存中取到值,值可能为null
	 * spring 4.0以上版本集成了guava cache,可以参看相关源代码,大师写的代码就是不一样啊%>_<%
	 * @param type
	 * @param subType
	 * @return
	 */
	@Caching(cacheable={
			@Cacheable(value="taskTemplate", key="#type.concat('&').concat(#subType)"/*, condition="#result != null"*/, unless="#result == null")
	})
	public TaskTemplate getTaskTemplateByTypeAndSubType(String type, String subType){
		LOGGER.info("从DB中获取task template,type=" + type + ",subType=" + subType);
		TaskTemplate taskTemplate = null;
		//spring jdbctemplate查询时如果该记录不存在,不是返回null,而是抛出EmptyResultDataAccessException
		try {
			taskTemplate = templateDao.getTaskTemplateByTypeAndSubType(type, subType);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			taskTemplate = null;
		}
		return taskTemplate;
	}
	
	/**
	 * 获取所有已知的task template的type
	 * @return
	 */
	public List<String> getAllTaskTemplateType() {
		return templateDao.getAllTaskTemplateType();
	}
	
	/**
	 * 根据task template的type查询所有task template
	 * @param type
	 * @return
	 */
	public List<TaskTemplate> getTaskTemplateByType(String type) {
		return templateDao.getTaskTemplateByType(type);
	}
	
	/**
	 * 根据id删除taskTemplate
	 * @param id
	 * @return
	 */
	public int deleteTaskTemplate(int id) {
		return templateDao.delete(id);
	}

}
