/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：PropertyRequireException.java
 * 描述： 
 */
package net.chinacloud.mediator.exception.product;

/**
 * @description 必填属性缺失异常
 * @author yejunwu123@gmail.com
 * @since 2015年6月29日 下午4:10:50
 */
public class PropertyRequireException extends ProductException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8194213834684896722L;
	
	public PropertyRequireException(String property) {
		super("exception.product.property.require", property);
	}
}
