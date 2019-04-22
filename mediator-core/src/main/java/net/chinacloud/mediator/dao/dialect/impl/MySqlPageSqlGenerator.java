/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：MySqlPageSqlGenerator.java
 * 描述： 
 */
package net.chinacloud.mediator.dao.dialect.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.dialect.PageSqlGenerator;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.StringUtils;

/**
 * @description 生成mysql相关的分页SQL
 * @author yejunwu123@gmail.com
 * @since 2015年8月12日 下午4:27:51
 */
public class MySqlPageSqlGenerator implements PageSqlGenerator {
	
	private static final String LIMIT = " LIMIT :" + Constant.PAGE_START_INDEX + ", :" + Constant.PAGE_END_INDEX;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MySqlPageSqlGenerator.class);

	@Override
	public String generatePageableSql(String originalSql, Map<String, Object> params) {
		if (StringUtils.hasText(originalSql)) {
			if (!CollectionUtil.isEmpty(params)) {
				if (params.containsKey(Constant.PAGE_START_INDEX) && params.containsKey(Constant.PAGE_END_INDEX)) {
					// mysql分页参数是基于偏移量和行数的,但是参数统一使用开始索引和结束索引,因此mysql的行数需要计算
					Integer startIndex = (Integer)params.get(Constant.PAGE_START_INDEX);
					Integer endIndex = (Integer)params.get(Constant.PAGE_END_INDEX);
					Integer rows = endIndex - startIndex + 1;
					
					params.put(Constant.PAGE_END_INDEX, rows);
					
					originalSql += LIMIT;
				}
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("generated mysql pageable sql:" + originalSql);
		}
		return originalSql;
	}

}
