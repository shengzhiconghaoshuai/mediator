/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoProductService.java
 * 描述： 封装taobao_num_sku相关数据库操作
 */
package net.chinacloud.mediator.taobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinacloud.mediator.dao.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * <封装taobao_num_sku相关数据库操作>
 * <封装taobao_num_sku相关数据库操作>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
//@Repository
public class NumOuterDao extends DAO {
	
	protected Logger logger = LoggerFactory.getLogger(NumOuterDao.class);
    
    /**
     * 根据sku外部商家编码查询淘宝numiid(商品)
     * @param outerId sku条码
     * @param appId 应用id
     * @return
     */
    public List<Long> findNumiidByOuterId(String outerId,int appId) {
    	String sql = "select num_iid from taobao_num_sku where outer_id = :outer_id and appid = :appid";
    	Map<String,Object> params = new HashMap<String, Object>();
    	params.put("outer_id", outerId);
    	params.put("appid", appId);
    	return super.queryForList(sql, new RowMapper<Long>(){
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("num_iid");
			}
    	}, params);
    }

    /**
     * 批量创建
     * @param list
     * @param appId
     */
    public void createBatch(List<Object[]> list,int appId) {
    	final String sql = "insert into taobao_num_sku (appid,num_iid,outer_id) values (:appid,:num_iid,:outer_id)";
    	if (list == null || list.isEmpty()) {
    		logger.warn("createBatch() is null");
    		return;
    	}
    	List<Map<String, Object>> params = new ArrayList<Map<String,Object>>();
    	for (Object[] entry : list) {
    		Map<String,Object> temp = new HashMap<String,Object>(3);
    		temp.put("appid", appId);
    		temp.put("num_iid", entry[0]);
    		temp.put("outer_id", entry[1]);
    		params.add(temp);
    	}
        super.batchUpdate(sql, params);
    }

	/**
	 * taobao_num_sku清理
	 * @param appId
	 * @return
	 */
	public int clean(int appId) {
		String sql = "delete from taobao_num_sku where appid = ?";
		return super.update(sql, appId);
	}
}
