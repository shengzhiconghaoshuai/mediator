/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskTemplateCache.java
 * 描述： task模板缓存
 */
package net.chinacloud.mediator.task.cache;

import net.chinacloud.mediator.task.TaskTemplate;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;
/**
 * <task模板缓存>
 * <task模板缓存>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年2月2日
 * @since 2015年2月2日
 */
public class TaskTemplateCache implements Cache {
	
	private com.google.common.cache.Cache<String, TaskTemplate> taskTemplateCache;
	
	public TaskTemplateCache () {
		taskTemplateCache = CacheBuilder.newBuilder()
							.initialCapacity(70)
							.build();
	}

	public String getName() {
		return "taskTemplate";
	}

	public Object getNativeCache() {
		return taskTemplateCache;
	}

	public ValueWrapper get(Object key) {
		Object value = taskTemplateCache.getIfPresent(key);
		return (value != null ? new SimpleValueWrapper(value) : null);
	}

	public void put(Object key, Object value) {
		taskTemplateCache.put((String)key, (TaskTemplate)value);
	}

	public void evict(Object key) {
		taskTemplateCache.invalidate(key);
	}

	public void clear() {
		//do not clear
	}

}
