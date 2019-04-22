/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ApiRuleException.java
 * 描述： 
 */
package net.chinacloud.mediator.mediator.b2c.exception;

import net.chinacloud.mediator.exception.ApplicationException;

/**
 * @description b2c api验证错误,减少服务端的校验成本
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 下午6:11:29
 */
public class ApiRuleException extends ApplicationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8220764497407387308L;
	
	/**
	 * 
	 * @param code 消息key
	 * @param args 参数
	 */
	public ApiRuleException(String code, Object...args) {
		super("b2c", "", args);
	}

}
