/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ProductException.java
 * 描述： 商品相关异常父类
 */
package net.chinacloud.mediator.exception.product;

import net.chinacloud.mediator.exception.ApplicationException;
/**
 * <商品相关异常>
 * <商品相关异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public class ProductException extends ApplicationException {

	private static final long serialVersionUID = 2415138137082469703L;

	protected static final String MODULE = "product";
	
	public ProductException(String code, Object... args){
		super(MODULE, code, args);
	}
}
