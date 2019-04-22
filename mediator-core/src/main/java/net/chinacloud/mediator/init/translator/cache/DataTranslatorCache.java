/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DataTranslatorCache.java
 * 描述： 
 */
package net.chinacloud.mediator.init.translator.cache;

import net.chinacloud.mediator.init.translator.DataTranslator;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;

public class DataTranslatorCache implements Cache {

	private com.google.common.cache.Cache<String, DataTranslator> dataTranslatorCache = null;
	
	public DataTranslatorCache(){
		dataTranslatorCache = CacheBuilder.newBuilder()
							.initialCapacity(10)
							.build();
	}
	
	@Override
	public String getName() {
		return "dataTranslator";
	}

	@Override
	public Object getNativeCache() {
		return dataTranslatorCache;
	}

	@Override
	public ValueWrapper get(Object key) {
		return new SimpleValueWrapper(dataTranslatorCache.getIfPresent(key));
	}

	@Override
	public void put(Object key, Object value) {
		dataTranslatorCache.put((String)key, (DataTranslator)value);
	}

	@Override
	public void evict(Object key) {
		//do not evict
	}

	@Override
	public void clear() {
		//do not clear
	}
	
}
