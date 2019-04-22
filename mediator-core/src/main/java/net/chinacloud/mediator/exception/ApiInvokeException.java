/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ApiInvokeException.java
 * 描述： api调用过程中的异常
 */
package net.chinacloud.mediator.exception;
/**
 * <api调用过程中的异常>
 * <api调用过程中的异常>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
public class ApiInvokeException extends SystemException {

	private static final long serialVersionUID = 3674293392732678921L;
	
	protected static final String MODULE = "api";
	
	public ApiInvokeException(String code, Object... args){
		super(MODULE, code, args, "API调用失败");
	}
	
	public ApiInvokeException(String errorMessage){
		this("exception.api.invoke", errorMessage);
	}
}
