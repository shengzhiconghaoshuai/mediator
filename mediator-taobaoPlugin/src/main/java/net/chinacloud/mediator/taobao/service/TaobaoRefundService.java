/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoRefundService.java
 * 描述： 淘宝渠道退款接口
 */
package net.chinacloud.mediator.taobao.service;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.refund.RefundException;
import net.chinacloud.mediator.service.RefundService;
/**
 * <淘宝渠道退款接口>
 * <淘宝渠道退款接口>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月20日
 * @since 2015年1月20日
 */
public interface TaobaoRefundService extends RefundService {
	
	public void AgWareHouseUpdate(Refund refund)throws RefundException;
	
	public void AgSendGoodsCancel(Refund refund)throws RefundException;
	
}
