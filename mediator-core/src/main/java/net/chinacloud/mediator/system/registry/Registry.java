/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Registry.java
 * 描述： 
 */
package net.chinacloud.mediator.system.registry;

/**
 * @description 注册表
 * @author yejunwu123@gmail.com
 * @since 2015年7月27日 下午6:57:51
 */
public interface Registry {
	/**
	 * 初始化
	 * @throws Exception
	 */
	public void initialize() throws Exception;
	/**
	 * 刷新所有
	 * @throws Exception
	 */
	public void refresh() throws Exception;
	/**
	 * 刷新指定内容
	 * @param key
	 * @throws Exception
	 */
	public void refresh(String key) throws Exception;
	/**
	 * 从注册表中获取数据
	 * @param key 数据的key
	 * @return
	 */
	public String get(String key);
}
