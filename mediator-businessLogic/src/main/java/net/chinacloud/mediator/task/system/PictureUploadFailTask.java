/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：SaoweiTask.java
 * 描述： 扫尾task
 */
package net.chinacloud.mediator.task.system;

import net.chinacloud.mediator.exception.ApplicationException;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.product.ProductTask;
import net.chinacloud.mediator.task.service.TaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <扫尾task>
 * <将对应的task template可重跑的task取出重新运行>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月20日
 * @since 2015年1月20日
 */
@Component
@Scope(value="prototype")
public class PictureUploadFailTask extends ProductTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PictureUploadFailTask.class);
	
	private static final String UPLOADPICTURE_TYPE = "uploadpicture";
	
	static {
		TaskManager.registTask(PRODUCT_TYPE, UPLOADPICTURE_TYPE, PictureUploadFailTask.class);
	}
	
	@Autowired
	TaskService taskService;
	@Autowired
	TaskManager taskManager;

	@Override
	protected String getSubType() {
		// 查询失败图片task
		return UPLOADPICTURE_TYPE;
	}

	@Override
	public void doTask() throws ApplicationException {
		LOGGER.info("--------------扫尾task开始运行--------------");
//		List<picture> picturelist=taskService.getAllReRunTask();
//		for() {
//			picture;
//			SpringUtil.getBean("pictureUpload");
//			task.data(pic);
//		}
//		if (CollectionUtil.isNotEmpty(tasks)) {
//			for (Task task : tasks) {
//				taskManager.executeTask(task);
//			}
//		}
		LOGGER.info("--------------扫尾task结束运行--------------");
	}

}
