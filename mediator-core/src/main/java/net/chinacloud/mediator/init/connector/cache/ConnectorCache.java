/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ConnectorCache.java
 * 描述： 各渠道连接器缓存
 */
package net.chinacloud.mediator.init.connector.cache;

import net.chinacloud.mediator.init.connector.Connector;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;
/**
 * <各渠道连接器缓存>
 * <各渠道连接器缓存,key为应用的code>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月17日
 * @since 2014年12月17日
 */
public class ConnectorCache<E> implements Cache {
	
	private com.google.common.cache.Cache<String, Connector<E>> connectorCache = null;
	
	public ConnectorCache(){
		connectorCache = CacheBuilder.newBuilder()
							.initialCapacity(20)
							.build();
	}

	public String getName() {
		return "connector";
	}

	public Object getNativeCache() {
		return connectorCache;
	}

	public ValueWrapper get(Object key) {
		return new SimpleValueWrapper(connectorCache.getIfPresent(key));
	}

	@SuppressWarnings("unchecked")
	public void put(Object key, Object value) {
		connectorCache.put((String)key, (Connector<E>)value);
	}

	public void evict(Object key) {
		//do not evict
	}

	public void clear() {
		//do not clear
	}

}
