/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：DaoTest.java
 * 描述： 
 */
package net.chinacloud.mediator;

import java.util.Date;

import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.task.TaskContext;
import net.chinacloud.mediator.task.TaskTemplate;
import net.chinacloud.mediator.task.dao.TaskDao;
import net.chinacloud.mediator.utils.SpringUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月11日 下午2:39:33
 */
public class DaoTest {
	
	@Before
	public void Before() {
		//ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-test.xml");
	}

	@Test
	public void testCreateTask() {
		TaskDao taskDao = SpringUtil.getBean(TaskDao.class);
		Task task = new TestTask();
		task.setData("fdhtrjytjytkuk");
		task.setDataId("243546577");
		task.setStartTime(new Date());
		task.setStatus(1);
		TaskTemplate template = new TaskTemplate();
		template.setId(1);
		task.setTemplate(template);
		TaskContext context = new TaskContext();
		context.setApplicationId(1);
		context.setChannelId(1);
		task.setContext(context);
		taskDao.createTask(task);
		
		System.out.println("task id:" + task.getId());
	}

}
