/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskAdapterCache.java
 * 描述： task适配器缓存
 */
package net.chinacloud.mediator.task.cache;

import net.chinacloud.mediator.task.TaskAdapter;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;
/**
 * <task适配器缓存>
 * <task适配器缓存>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月17日
 * @since 2014年12月17日
 */
public class TaskAdapterCache implements Cache {
	
	private com.google.common.cache.Cache<String, TaskAdapter> taskAdapterCache;

	public TaskAdapterCache(){
		taskAdapterCache = CacheBuilder.newBuilder()
							.initialCapacity(10)
							.build();
	}
	
	public String getName() {
		return "taskAdapter";
	}

	public Object getNativeCache() {
		return taskAdapterCache;
	}

	public ValueWrapper get(Object key) {
		return new SimpleValueWrapper(taskAdapterCache.getIfPresent(key));
	}

	public void put(Object key, Object value) {
		taskAdapterCache.put((String)key, (TaskAdapter)value);
	}

	public void evict(Object key) {
		//do not evict
	}

	public void clear() {
		//do not clear
	}
}
