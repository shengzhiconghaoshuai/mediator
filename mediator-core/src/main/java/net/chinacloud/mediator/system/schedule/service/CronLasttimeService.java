/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：CronLasttimeService.java
 * 描述： 
 */
package net.chinacloud.mediator.system.schedule.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.system.schedule.dao.CronLasttimeDao;
import net.chinacloud.mediator.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * <调度时间service>
 * <调度时间service>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年2月26日
 * @since 2015年2月26日
 */
@Service
public class CronLasttimeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CronLasttimeService.class);
	
	private static int PRE_INTERVLA = -1000 * 60 * 60 * 24 * 6;	//往前推6天
	
	@Autowired
	CronLasttimeDao cronLasttimeDao;
	/**
	 * 获取上次调度运行的时间
	 * @param applicationId 应用giddy
	 * @param templateId 模板id
	 * @return
	 */
	public Date getLastime(int applicationId, int templateId) {
		Long lasttime = null;
		try {
			lasttime = cronLasttimeDao.getLastime(applicationId, templateId);
		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error(e.getMessage());
			lasttime = null;
		}
		
		//如果是第一次,则开始时间为当前时间往前推六天
		if (lasttime == null) {
			lasttime = DateUtil.modify(new Date(), PRE_INTERVLA).getTime();
		}
		
		return new Date(lasttime);
	}
	
	/**
	 * 保存或更新上次调度运行的时间
	 * @param applicationId 应用id
	 * @param templateId 模板id
	 * @param lasttime 上次运行时间
	 * @return
	 */
	public void insertOrUpdateLasttime(int applicationId, int templateId, long lasttime) {
		int result = cronLasttimeDao.updateLasttime(applicationId, templateId, lasttime);
		if (0 == result) {
			cronLasttimeDao.insertLasttime(applicationId, templateId, lasttime);
		}
	}
	
	public List<Map<String, Object>> list() {
		return cronLasttimeDao.list();
	}
}
