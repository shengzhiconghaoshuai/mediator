/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MediatorFunctions.java
 * 描述： 自定义函数
 */
package net.chinacloud.mediator.web.taglib;
/**
 * <自定义函数>
 * <有些在页面实现起来比较复杂的功能,通过自定义函数实现>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月30日
 * @since 2015年1月30日
 */
public class MediatorFunctions {
	/**
	 * 在调度编辑页面判断当前应用是否勾选
	 * @param applicationId 当前迭代的应用id
	 * @param applicationIds 配置在调度表中的应用id列表,格式:1,3,14
	 * @return
	 */
	public static boolean checked(Integer applicationId, String applicationIds) {
		if (null == applicationIds || "".equals(applicationIds)) {
			return false;
		}
		applicationIds = applicationIds.trim();
		String[] applicationIdArr = applicationIds.split(",");
		String apps = applicationId.toString();
		for (String ap : applicationIdArr) {
			if (apps.equals(ap)) {
				return true;
			}
		}
		return false;
	}
}
