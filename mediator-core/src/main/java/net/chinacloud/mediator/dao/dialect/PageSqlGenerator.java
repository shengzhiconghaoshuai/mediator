/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：PageSqlGenerator.java
 * 描述： 
 */
package net.chinacloud.mediator.dao.dialect;

import java.util.Map;

/**
 * @description 
 * @author yejunwu123@gmail.com
 * @since 2015年8月12日 下午4:22:04
 */
public interface PageSqlGenerator {
	/**
	 * 根据原始的SQL,生成分页的SQL
	 * 原始的SQL是指能执行的包含where条件但不包含分页条件的SQL
	 * 原始的SQL中不包含DB特定的函数
	 * @param originalSql
	 * @param params 查询参数
	 * @return 带分页条件的SQL
	 */
	String generatePageableSql(String originalSql, Map<String, Object> params);
}
