/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RegistryService.java
 * 描述： 
 */
package net.chinacloud.mediator.system.registry.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.system.registry.RegistryData;
import net.chinacloud.mediator.system.registry.dao.RegistryDao;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月1日 下午2:43:54
 */
@Service
public class RegistryService {
	
	@Autowired
	RegistryDao registryDao;
	
	@Autowired
	Registry registry;
	
	public List<RegistryData> getAllRegistryData(Map<String, Object> params) {
		return registryDao.getRegistries(params);
	}
	
	public Long countAllRegistryData(Map<String, Object> params) {
		return registryDao.countRegistries(params);
	}
	
	public void refresh() throws Exception {
		registry.refresh();
	}
	
	public void refresh(String key) throws Exception {
		registry.refresh(key);
	}
	
	public Integer add(RegistryData registryData) {
		return registryDao.addRegistry(registryData);
	}
	
	public Integer update(RegistryData registryData) throws Exception {
		int result = registryDao.updateRegistry(registryData);
		registry.refresh(registryData.getKey());
		return result;
	}
	
	public Integer remove(String key) {
		return registryDao.removeRegistry(key);
	}
}
