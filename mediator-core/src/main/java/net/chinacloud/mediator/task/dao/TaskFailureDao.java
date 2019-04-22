/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskFailureDao.java
 * 描述： 
 */
package net.chinacloud.mediator.task.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description task失败处理DAO
 * @author yejunwu123@gmail.com
 * @since 2015年7月8日 下午2:11:44
 */
//@Repository
public class TaskFailureDao extends DAO {
	protected static Logger LOGGER = LoggerFactory.getLogger(TaskFailureDao.class);
	
    public void createTaskFailureMsg(Long taskId, String code, Date createtime,Integer status) {
    	String sql = "INSERT INTO TASK_FAILURE_MSG (TASK_ID, CODE, CREATETIME, STATUS) VALUES (?,?,?,?)";
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("createTaskFailureMsg sql : " + sql);
    	}
    	super.update(sql, taskId, code, createtime, status);
    }
    
    public List<Map<String,Object>> getFailureMsgsByStatus(Integer status) {
        String sql = "SELECT TASK_ID, CODE FROM TASK_FAILURE_MSG WHERE STATUS = ?";
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("getFailureMsgsByStatus() sql :" + sql);
        }
        List<Map<String,Object>> results = super.queryForList(sql, status);
        return results;
    }

    /**
     * 更新task状态(根据原先状态更新)
     * @param aTask
     */
    public int updateTaskFailureMsgStatus(Long taskId,Integer status,Date lastupdate) {
        String sql = "UPDATE TASK_FAILURE_MSG SET STATUS = ?, LASTUPDATE = ? WHERE TASK_ID = ?";
        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("updateTaskFailureMsgStatus() sql :" + sql);;
        }
        return super.update(sql, status, lastupdate, taskId);
    }
}
