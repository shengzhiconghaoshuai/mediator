/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SystemInitListener.java
 * 描述： 系统初始化
 */
package net.chinacloud.mediator.init;

import java.text.ParseException;
import java.util.List;

import net.chinacloud.mediator.system.registry.Registry;
import net.chinacloud.mediator.system.schedule.domain.CronConfig;
import net.chinacloud.mediator.system.schedule.service.SchedulerService;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
/**
 * <系统初始化>
 * <系统初始化>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月5日
 * @since 2015年1月5日
 */
@Component
public class SystemInitListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemInitListener.class);
	
	@Autowired
	SchedulerService schedulerService;
	@Autowired
	Registry registry;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//有两个spring容器,没有这个判断,则每个容器初始化完都会执行一边,导致初始化两次
		if(event.getApplicationContext().getParent() == null){
			//init schedule
			try {
				initSchedule();
			} catch (Exception e) {
				//e.printStackTrace();
				LOGGER.error("调度初始化异常", e);
				throw new RuntimeException("调度初始化失败");
			}
			try{
				initRegistry();
			}catch (Exception e) {
				//e.printStackTrace();
				LOGGER.error("注册表初始化异常", e);
				throw new RuntimeException("注册表初始化失败");
			}
		}
	}

	/**
	 * 初始化调度
	 * @throws SchedulerException
	 * @throws ParseException 
	 */
	private void initSchedule() throws SchedulerException, ParseException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("==========开始初始化调度==========");
		}
		
		//SchedulerService schedulerService = SpringUtil.getBean(SchedulerService.class);
		schedulerService.unscheduleAllCluster();
		schedulerService.unscheduleAllSingle();
		List<CronConfig> configs = schedulerService.getAllCronConfigs();
		for(CronConfig config : configs){
			LOGGER.debug("当前调度配置{}", config);
			if(config.getStatus() == 1){
				schedulerService.schedule(config);
			}
		}
	}
	
	private void initRegistry() throws Exception{
		registry.initialize();
		LOGGER.debug("注册表初始化");
	}
	
}
