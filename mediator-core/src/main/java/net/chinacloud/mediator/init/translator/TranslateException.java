/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TranslateException.java
 * 描述： 
 */
package net.chinacloud.mediator.init.translator;

import net.chinacloud.mediator.exception.ApplicationException;

public class TranslateException extends ApplicationException {

	private static final long serialVersionUID = -1140253437615376482L;

	public TranslateException(String module, String code, Object... args){
		super(module, "exception.data.transform", args);
	}
}
