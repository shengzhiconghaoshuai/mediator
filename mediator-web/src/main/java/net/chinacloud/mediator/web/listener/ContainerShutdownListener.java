/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ContainerShutdownListener.java
 * 描述： 
 */
package net.chinacloud.mediator.web.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.chinacloud.mediator.mail.MailSendUtil;
import net.chinacloud.mediator.task.service.TaskService;
import net.chinacloud.mediator.utils.SpringUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年9月11日 上午10:49:18
 */
public class ContainerShutdownListener implements ServletContextListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerShutdownListener.class);

	private static Integer TIME = 10;
	
	private static Boolean SHUTDOWNSWITCH = false;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String time = sce.getServletContext().getInitParameter("time");
		String shutdownSwitch =sce.getServletContext().getInitParameter("shutdownSwitch");
		if(StringUtils.hasText(time)){
			TIME = Integer.parseInt(time);
		}
		if(StringUtils.hasText(shutdownSwitch)){
			SHUTDOWNSWITCH = Boolean.parseBoolean(shutdownSwitch);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.info("----------shutdown container--------------");
		// ... First close any background tasks which may be using the DB ...
	    // ... Then close any DB connection pools ...

	    // Now deregister JDBC drivers in this context's ClassLoader:
	    // Get the webapp's ClassLoader
	    ClassLoader cl = Thread.currentThread().getContextClassLoader();
	    // Loop through all drivers
	    Enumeration<Driver> drivers = DriverManager.getDrivers();
	    while (drivers.hasMoreElements()) {
	        Driver driver = drivers.nextElement();
	        if (driver.getClass().getClassLoader() == cl) {
	            // This driver was registered by the webapp's ClassLoader, so deregister it:
	            try {
	            	LOGGER.info("Deregistering JDBC driver {}", driver);
	                DriverManager.deregisterDriver(driver);
	            } catch (SQLException ex) {
	            	LOGGER.error("Error deregistering JDBC driver {}", driver, ex);
	            }
	        } else {
	            // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
	        	LOGGER.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader", driver);
	        }
	    }
	    
	    int count = 0;
        while(SHUTDOWNSWITCH){
        	TaskService taskService =  SpringUtil.getBean(TaskService.class);
   	        long times = taskService.findRunningTask();
   	        LOGGER.info("状态为1的TASK数量为:"+times);
   	        if(times>0){
   	        	try {
   	        		count++;
   	        		if(count>=TIME){
   	        			MailSendUtil.sendEmail("应用关闭异常", "关闭等待已达到上限，系统还有运行task");
   	        			break;
   	        		}
   	     	    	//让jvm等待一会再推出
   	     			Thread.sleep(5000);
   	     		} catch (InterruptedException e) {
   	     			LOGGER.error("", e);
   	     		}
   	        }else{
   	        	break;
   	        }
        }
        LOGGER.info("----------shutdown container finished--------------");
	}

}
