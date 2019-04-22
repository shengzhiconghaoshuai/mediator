/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：OraclePageSqlGenerator.java
 * 描述： 
 */
package net.chinacloud.mediator.dao.dialect.impl;

import java.util.Map;

import net.chinacloud.mediator.Constant;
import net.chinacloud.mediator.dao.dialect.PageSqlGenerator;
import net.chinacloud.mediator.utils.CollectionUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 生成oracle相关的分页SQL
 * @author yejunwu123@gmail.com
 * @since 2015年8月12日 下午4:28:56
 */
public class OraclePageSqlGenerator implements PageSqlGenerator {
	
	private static final String FORMAT_PREFIX = "SELECT B.* FROM (SELECT ROWNUM AS ROWNO, A.* FROM (";
	private static final String FORMAT_SUFFIX = ") A WHERE ROWNUM <= :" + Constant.PAGE_END_INDEX + ") B WHERE B.ROWNO > :" + Constant.PAGE_START_INDEX;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OraclePageSqlGenerator.class);
	
	@Override
	public String generatePageableSql(String originalSql, Map<String, Object> params) {
		if (StringUtils.hasText(originalSql)) {
			if (!CollectionUtil.isEmpty(params)) {
				if (params.containsKey(Constant.PAGE_START_INDEX) && params.containsKey(Constant.PAGE_END_INDEX)) {
					originalSql = FORMAT_PREFIX + originalSql;
					originalSql = originalSql + FORMAT_SUFFIX;
				}
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("generated oracle pageable sql:" + originalSql);
		}
		return originalSql;
	}

}
