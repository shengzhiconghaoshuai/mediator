/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CacheService.java
 * 描述： 
 */
package net.chinacloud.mediator.cache;

import org.springframework.cache.annotation.Cacheable;

import net.chinacloud.mediator.task.TaskTemplate;

public class CacheService {
	@Cacheable(value="taskTemplate", key="#type.concat('&').concat(#subType)")
	public TaskTemplate getTaskTemplateByTypeAndSubType(String type, String subType){
		System.out.println("从DB中获取task template,type=" + type + ",subType=" + subType);
		TaskTemplate taskTemplate = null;
		//spring jdbctemplate查询时如果该记录不存在,不是返回null,而是抛出EmptyResultDataAccessException
		try {
			taskTemplate = new TaskTemplate();
			taskTemplate.setType("ORDER");
			taskTemplate.setSubType("create");
		} catch (Exception e) {
			taskTemplate = null;
		}
		return taskTemplate;
	}
}
