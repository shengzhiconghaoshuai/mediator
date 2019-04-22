/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskFailureService.java
 * 描述： 
 */
package net.chinacloud.mediator.task.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.chinacloud.mediator.task.dao.TaskFailureDao;

/**
 * @description task失败处理service
 * @author yejunwu123@gmail.com
 * @since 2015年7月8日 下午2:18:20
 */
@Service
public class TaskFailureService {
	
	@Autowired
	TaskFailureDao taskFailureDao;
	
	public void createTaskFailureMsg(Long taskId, String code, Date createtime,Integer status) {
		taskFailureDao.createTaskFailureMsg(taskId, code, createtime, status);
    }
    
    public List<Map<String,Object>> getFailureMsgsByStatus(Integer status) {
        return taskFailureDao.getFailureMsgsByStatus(status);
    }

    /**
     * 更新task状态(根据原先状态更新)
     * @param aTask
     */
    public int updateTaskFailureMsgStatus(Long taskId,Integer status,Date lastupdate) {
    	return taskFailureDao.updateTaskFailureMsgStatus(taskId, status, lastupdate);
    }
}
