/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ObjectUtil.java
 * 描述： 
 */
package net.chinacloud.mediator.utils;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 下午1:07:16
 */
public abstract class ObjectUtil {
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(final Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
