/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：B2CDataTranslator.java
 * 描述： 
 */
package net.chinacloud.mediator.mediator.b2c.init;

import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.init.translator.DataTranslator;
import net.chinacloud.mediator.init.translator.TranslateException;

/**
 * @description b2c渠道数据转换器
 * @author yejunwu123@gmail.com
 * @since 2015年7月9日 下午6:17:12
 */
public class B2CDataTranslator implements DataTranslator {

	@Override
	public Order transformOrder(Object source) throws TranslateException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Return transformReturn(Object source) throws TranslateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Refund transformRefund(Object source) throws TranslateException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Exchange translateExchange(Object source) throws TranslateException {
		// TODO Auto-generated method stub
		return null;
	}

}
