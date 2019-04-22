/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ReturnService.java
 * 描述： 退货接口
 */
package net.chinacloud.mediator.service;

import java.util.Date;
import java.util.List;

import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.exception.returns.ReturnException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.task.CommonNotifyPacket;

/**
 * <退货接口>
 * <退货相关接口定义>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月11日
 * @since 2014年12月11日
 */
public interface ReturnService {
	/**
	 * 根据退货单号获取退货详情
	 * @param returnId 第三方渠道退货编号
	 * @return 系统内部退货结构
	 * @throws ReturnException
	 */
	public Return getReturnByReturnId(String returnId) throws ReturnException, TranslateException;
	/***
	 * 根据状态获取一段时间内的退货
	 * @param status 退货状态
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return CommonNotifyPacket包装过的退货结构
	 * @throws ReturnException
	 */
	public List<CommonNotifyPacket<Return>> getReturnListByStatus(String status,Date startTime, Date endTime)
			throws ReturnException;
}
