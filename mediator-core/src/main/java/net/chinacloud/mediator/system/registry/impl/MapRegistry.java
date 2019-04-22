/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MapRegistry.java
 * 描述： 
 */
package net.chinacloud.mediator.system.registry.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.system.registry.RegistryData;
import net.chinacloud.mediator.system.registry.dao.RegistryDao;
import net.chinacloud.mediator.utils.StringUtils;

/**
 * @description map实现的注册表
 * @author yejunwu123@gmail.com
 * @since 2015年7月27日 下午7:05:03
 */
public class MapRegistry implements Registry {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MapRegistry.class);
	
	private static Map<String, String> DATA = null;
	
	/*private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();*/
	@Autowired
	private RegistryDao registryDao;

	@Override
	public void initialize() throws Exception {
		DATA = new HashMap<String, String>();
	}

	@Override
	public void refresh() throws Exception {
		/*writeLock.lock();
		try {
			DATA.clear();
		} finally {
			writeLock.unlock();
		}*/
		synchronized (MapRegistry.class) {
			DATA.clear();
		}
	}

	@Override
	public String get(String key) {
		if (DATA.containsKey(key)) {
			return DATA.get(key);
		}
		
		synchronized (MapRegistry.class) {
			if (!DATA.containsKey(key)) {
				// get from DB
				try {
					RegistryData registryData = registryDao.getRegistry(key);
					if (StringUtils.hasText(registryData.getValue())) {
						DATA.put(key, registryData.getValue().trim());
					}
				} catch (Exception e) {
					// e.printStackTrace();
					LOGGER.warn("get empty registry value for key:" + key);
				}
			}
		}
		return DATA.get(key);
		/*readLock.lock();
		try {
			if (DATA.containsKey(key)) {
				return (T)DATA.get(key);
			}
			readLock.unlock();
			writeLock.lock();
			try {
				// get from DB
				try {
					RegistryData registryData = registryDao.getRegistry(key);
					if (StringUtils.hasText(registryData.getValue())) {
						DATA.put(key, registryData.getValue().trim());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				writeLock.unlock();
			}
			readLock.lock();
			if (DATA.containsKey(key)) {
				return (T)DATA.get(key);
			}
			return null;
		} finally {
			readLock.unlock();
		}*/
	}

	@Override
	public void refresh(String key) throws Exception {
		synchronized (MapRegistry.class) {
			DATA.remove(key);
		}
	}

}
