/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：RefundService.java
 * 描述： 退款接口
 */
package net.chinacloud.mediator.service;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.exception.refund.RefundException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.task.CommonNotifyPacket;

/**
 * <退款接口>
 * <退款相关接口定义>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public interface RefundService {
	/**
	 * 根据退款编号获取退款详情
	 * @param refundId 渠道退款编号
	 * @return 系统内部退款详情
	 * @throws RefundException
	 */
	public Refund getRefundByRefundId(String refundId)throws RefundException, TranslateException;
	/**
	 * 根据状态获取一段时间内的退款
	 * @param status 退款状态
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return CommonNotifyPacket包装过的退款结构
	 * @throws RefundException
	 */
	public List<CommonNotifyPacket<Refund>> getRefundListByStatus(String status,Date startTime, Date endTime,String name)
			throws RefundException;
}
