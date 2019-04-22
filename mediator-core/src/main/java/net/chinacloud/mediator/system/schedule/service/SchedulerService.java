/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ScheduleService.java
 * 描述： 调度业务类
 */
package net.chinacloud.mediator.system.schedule.service;

import java.text.ParseException;
import java.util.List;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.system.schedule.dao.CronConfigDao;
import net.chinacloud.mediator.system.schedule.domain.CronConfig;
import net.chinacloud.mediator.system.schedule.domain.CronParam;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.JobDetailBean;
import org.springframework.stereotype.Service;

/**
 * <调度管理>
 * <调度管理>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月18日
 * @since 2014年12月18日
 */
@Service
public class SchedulerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);
	
	@Autowired
	private CronConfigDao cronConfigDao;
	
	@Autowired
	@Qualifier(value="clusterScheduler")
	StdScheduler clusterScheduler;
	@Autowired
	@Qualifier(value="singleScheduler")
	StdScheduler singleScheduler;
	/**
	 * 根据cron配置创建一个任务
	 * @param cronConfig 任务配置
	 * @throws SchedulerException 
	 * @throws ParseException 
	 */
	public void createCron(CronConfig cronConfig) throws ParseException, SchedulerException {
		//非cron的调度不需要持久化,因为调度一次就结束了,cron的调度即使服务重启后还要继续调度
		if (cronConfig.getCron() == 1) {
			cronConfigDao.createCron(cronConfig);
			cronConfigDao.createCronParam(cronConfig);
		}
		
		//自动添加到Scheduler中
		if (cronConfig.getStatus() == 1) {
			schedule(cronConfig);
		}
	}
	
	/**
	 * 根据cron id查找cron的源信息
	 * @param id
	 * @return
	 */
	public CronConfig findCron(Integer id){
		return cronConfigDao.findCron(id);
	}
	
	/**
	 * 获取所有调度配置
	 * @return
	 */
	public List<CronConfig> getAllCronConfigs() {
		return cronConfigDao.getAllCronConfigs();
	}
	
	/**
	 * 获取调度的参数
	 * @param config
	 */
	public List<CronParam> getCronConfigParams(CronConfig config) {
		return cronConfigDao.getCronConfigParams(config);
	}
	
	/**
	 * 根据调度配置调度一个调度
	 * @param config
	 * @throws ParseException 
	 * @throws SchedulerException 
	 */
	public void schedule(CronConfig cronConfig) throws ParseException, SchedulerException {
		if(null == cronConfig.getParams() || cronConfig.getParams().isEmpty()){
			cronConfig.setParams(getCronConfigParams(cronConfig));
		}
		
		Class<?> clazz = null;
		try {
			clazz = Class.forName(cronConfig.getClassName());
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			LOGGER.error("调度初始化失败,类  " + cronConfig.getClassName() + "  加载失败", e);
		}
		
		String name = clazz.getName() + Constant.SCHEDULE_NAME_SPLIT + cronConfig.getId();
		
		JobDetail jobDetail = new JobDetailBean();
		jobDetail.setJobClass(clazz);
		jobDetail.setName(name);
		jobDetail.setGroup(Constant.SCHEDULE_GROUP_PREFIX);
		
		//设置调度参数
		for(CronParam param : cronConfig.getParams()){
			jobDetail.getJobDataMap().put(param.getParamName(), param.getParamValue());
		}
		
		if(cronConfig.getGlobal() == 1){
			clusterScheduler.addJob(jobDetail, true);
			if(cronConfig.getCron() == 1){
				CronTrigger cronTrigger = new CronTriggerBean();
				cronTrigger.setName(name);
				cronTrigger.setCronExpression(cronConfig.getExpression());
				cronTrigger.setJobName(jobDetail.getName());
				cronTrigger.setGroup(Constant.SCHEDULE_GROUP_PREFIX);
				cronTrigger.setJobGroup(Constant.SCHEDULE_GROUP_PREFIX);
				
				clusterScheduler.scheduleJob(cronTrigger);
			}else{
				//非cron的调度
				SimpleTrigger simpleTrigger = new SimpleTrigger();
				simpleTrigger.setName(name);
				simpleTrigger.setJobName(jobDetail.getName());
				simpleTrigger.setGroup(Constant.SCHEDULE_GROUP_PREFIX);
				simpleTrigger.setJobGroup(Constant.SCHEDULE_GROUP_PREFIX);
				
				//TODO 结束时间需要完善
				simpleTrigger.setStartTime(cronConfig.getStartTime());
				simpleTrigger.setRepeatCount(cronConfig.getRepeatCount());
				if (cronConfig.getRepeatInterval() > 0) {
					simpleTrigger.setRepeatInterval(cronConfig.getRepeatInterval() * 1000);
				}
				//simpleTrigger.setEndTime(cronConfig.getEndTime());
				
				clusterScheduler.scheduleJob(simpleTrigger);
			}
		} else {
			singleScheduler.addJob(jobDetail, true);
			if (cronConfig.getCron() == 1) {
				CronTrigger cronTrigger = new CronTriggerBean();
				cronTrigger.setName(name);
				cronTrigger.setCronExpression(cronConfig.getExpression());
				cronTrigger.setJobName(jobDetail.getName());
				cronTrigger.setGroup(Constant.SCHEDULE_GROUP_PREFIX);
				cronTrigger.setJobGroup(Constant.SCHEDULE_GROUP_PREFIX);
				
				singleScheduler.scheduleJob(cronTrigger);
			} else {
				//非cron的调度
				SimpleTrigger simpleTrigger = new SimpleTrigger();
				simpleTrigger.setName(name);
				simpleTrigger.setJobName(jobDetail.getName());
				simpleTrigger.setGroup(Constant.SCHEDULE_GROUP_PREFIX);
				simpleTrigger.setJobGroup(Constant.SCHEDULE_GROUP_PREFIX);
				
				//TODO 结束时间需要完善
				simpleTrigger.setStartTime(cronConfig.getStartTime());
				simpleTrigger.setRepeatCount(cronConfig.getRepeatCount());
				if (cronConfig.getRepeatInterval() > 0) {
					simpleTrigger.setRepeatInterval(cronConfig.getRepeatInterval() * 1000);
				}
				//simpleTrigger.setEndTime(cronConfig.getStartTime());
				
				singleScheduler.scheduleJob(simpleTrigger);
			}
		}
	}
	
	/**
	 * 移除所有集群调度
	 * @throws SchedulerException
	 */
	public void unscheduleAllCluster() throws SchedulerException {
		unscheduleAll(clusterScheduler);
	}
	
	/**
	 * 移除所有单机调度
	 * @throws SchedulerException
	 */
	public void unscheduleAllSingle() throws SchedulerException {
		unscheduleAll(singleScheduler);
	}
	
	private void unscheduleAll(StdScheduler scheduler) throws SchedulerException {
		String[] groupNames = scheduler.getTriggerGroupNames();
		if(groupNames != null && groupNames.length > 0){
			for(String groupName : groupNames){
				String[] triggerNames = scheduler.getTriggerNames(groupName);
				if(triggerNames != null && triggerNames.length > 0){
					for(String triggerName : triggerNames){
						scheduler.unscheduleJob(triggerName, groupName);
					}
				}
			}
		}
	}
	
	/**
	 * 从调度器中卸载一个job
	 * @param cronConfig
	 * @throws SchedulerException
	 */
	private void unschedule(CronConfig cronConfig) throws SchedulerException {
		String className = cronConfig.getClassName();
		String triggerName = className + Constant.SCHEDULE_NAME_SPLIT + cronConfig.getId();
		int global = cronConfig.getGlobal();
		if (global == 1) {
			clusterScheduler.unscheduleJob(triggerName, Constant.SCHEDULE_GROUP_PREFIX);
		} else {
			singleScheduler.unscheduleJob(triggerName, Constant.SCHEDULE_GROUP_PREFIX);
		}
	}
	
	/**
	 * 删除调度(物理删除)
	 * @param cronId
	 * @throws SchedulerException
	 */
	public void delete(int cronId) throws SchedulerException {
		CronConfig cronConfig = findCron(cronId);
		cronConfigDao.delete(cronId);
		//判断cron的类型及状态,从scheduler中移除
		unschedule(cronConfig);
	}
	
	/**
	 * 删除调度参数
	 * @param cronId
	 */
	public void deleteCronParam(int cronId) {
		cronConfigDao.deleteCronParam(cronId);
	}
	
	/**
	 * 修改调度配置
	 * @param cronConfig
	 * @throws SchedulerException 
	 * @throws ParseException 
	 */
	public void update(CronConfig cronConfig) throws ParseException, SchedulerException {
		//1、将调度原来的参数从DB中删除,将新的参数持久化到DB;
		//2、将最新调度配置信息更新到DB中;
		//3、判断调度状态,如果状态为1,则需要更新scheduler中正在运行的调度,如果为0,则不处理;
		//如果调度原来状态为1,现在修改状态为0,那么调度仍存在于schedule中,会继续执行,直到下次重启服务;
		//如果想要立即停止还在运行的调度,可以将调度删除,而不是仅修改调度的状态
		
		int status = cronConfig.getStatus();
		CronConfig oldConfig = null;
		
		//if (status == 1) {
			oldConfig = cronConfigDao.findCron(cronConfig.getId());
		//}
		
		//1、--
		cronConfigDao.deleteCronParam(cronConfig.getId());
		cronConfigDao.createCronParam(cronConfig);
		
		//2、--
		cronConfigDao.updateCronConfig(cronConfig);
		
		//3、--
		if (status == 1) {
			unschedule(oldConfig);
			
			schedule(cronConfig);
		} else {
			unschedule(oldConfig);
		}
	}
}
