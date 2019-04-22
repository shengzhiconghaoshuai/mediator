/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：InventoryUpdateException.java
 * 描述： 
 */
package net.chinacloud.mediator.exception.product;

/**
 * @description 商品库存更新失败异常
 * @author yejunwu123@gmail.com
 * @since 2015年7月8日 下午6:10:51
 */
public class InventoryUpdateException extends ProductException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8615795443952940944L;

	/**
	 * @param code
	 * @param args
	 */
	public InventoryUpdateException(String errorMessage) {
		super("exception.product.inventory.update.fail", errorMessage);
	}

}
