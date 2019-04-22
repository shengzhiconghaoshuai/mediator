/**
 * 文件名：JingDongConstants.java
 * 版权：Copyright 2015- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 描述： 
 */
package net.chinacloud.mediator.jingdong.constants;

/**
 * <常量定义>
 * <京东常量定义>
 * @author mwu@wuxicloud.com
 * @version 0.0.0,2015年3月27日
 * @since 2015年3月27日
 */
public class JingDongConstants {
	public static  String WAIT_SELLER_STOCK_OUT ="WAIT_SELLER_STOCK_OUT";
	
	public static  String FINISHED_L = "FINISHED_L";
	
	public static  String TRADE_CANCELED = "TRADE_CANCELED";
	
	public static  String STATUS_RETURN_CREATE = "STATUS_RETURN_CREATE";
	
	public static  String STATUS_REFUND_AFS_CREATE = "STATUS_REFUND_AFS_CREATE";
	
	
	public static  String BATCH_GET_AFSREFUND= "BATCH_GET_AFSREFUND";
	
	/**退款阶段,因为京东现阶段都是未发货退款，所以这个是个默认值onsale */
	public static final String KEY_REFUND_PHASE = "refundPhase";
	
	/**京东sdk申请退款 状态常数*/
	public static final String API_REFUND_STATUS0 = "0";

}
