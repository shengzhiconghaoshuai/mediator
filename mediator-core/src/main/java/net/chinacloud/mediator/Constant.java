/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：Constant.java
 * 描述： 系统使用的常量
 */
package net.chinacloud.mediator;
/**
 * <系统使用的常量>
 * <系统使用的常量>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月31日
 * @since 2014年12月31日
 */
public final class Constant {
	private Constant(){}
	
	/**应用代号*/
	public static final String APPLICATION_CODE = "APPLICATION_CODE";
	/**渠道代号*/
	public static final String CHANNEL_CODE = "CHANNEL_CODE";
	/**渠道id*/
	public static final String CHANNEL_ID = "CHANNEL_ID";
	/**应用id*/
	public static final String APPLICATION_ID = "APPLICATION_ID";
	
	/**调度任务组组名前缀*/
	public static final String SCHEDULE_GROUP_PREFIX = "MEDIATOR_";
	
	/**调度参数*/
	public static final String SCHEDULE_PARAM_STATUS = "STATUS";
	/**task重跑时多久之前的task还能被扫尾*/
	public static final String SCHEDULE_PARAM_TASK_RERUN_DELAY = "TASK_RERUN_DELAY";
	
	/**调度group名称分隔符*/
	public static final String SCHEDULE_NAME_SPLIT = "$";
	
	/**缓存名称*/
	public static final String CACHE_CACHE_NAME = "CACHE_NAME";
	
	/**缓存key*/
	public static final String CACHE_CACHE_KEY = "CACHE_KEY";
	
	/**分页开始索引*/
	public static final String PAGE_START_INDEX = "START_INDEX";
	/**分页结束索引*/
	public static final String PAGE_END_INDEX = "END_INDEX";
}
