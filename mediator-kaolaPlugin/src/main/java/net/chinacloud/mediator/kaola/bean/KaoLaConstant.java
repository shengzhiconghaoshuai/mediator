package net.chinacloud.mediator.kaola.bean;

/**
 * <小红书常量>
 */
public class KaoLaConstant {
	public static final String CHANNEL_KAOLA = "KAOLA";
	
	/**考拉订单状态1(已付款)、2(已发货)、3(已签收)、5(取消待确认)、6(已取消)
	 * */
	public static final int ORDER_PAID = 1;//已付款
	public static final int ORDER_SIGNED = 3;//已签收
	public static final int ORDER_PRE_CANCEL = 5;//取消待确认
	public static final int ORDER_CANCELED = 6;//已取消  未发货退款
	
	public static final String ORDER_TO_REFUND = "order_to_refund";
	public static final String ORDER_TO_REFUND_DESC = "订单取消待确认";
	
	public static final String ORDER_TO_CANCELED = "order_to_canceled";
	public static final String ORDER_TO_CANCELED_DESC = "订单已取消";
	
	public static final String ORDER_TO_REVOKE = "order_to_revoke";
	public static final String ORDER_TO_REVOKE_DESC = "订单恢复为已付款待发货";
	
	
	
	/**订单搜索日期类型1(支付时间)、2(发货时间)、3(签收时间)、1001（待发货时间）、1005(取消待处理时间)*/ 
	public static final int ORDER_PAY_TIME = 1;
	public static final int ORDER_SIGNED_TIME = 3;
	public static final int ORDER_DELIVERY_TIME = 1001;
	public static final int ORDER_PRECANCEL_TIME = 1005;
	
	
	/**1.待处理2.待退款3.待用户修改4.待考拉仲裁5.已完成
	 * */
	public static final int REFUND_PRE = 1;//待处理       申请中
	public static final int REFUND_PENDING = 2;//待退款   同意退款
	public static final int REFUND_USER_MODIFIED = 3;//待用户修改     拒绝退款,等待用户修改
	public static final int REFUND_PRE_KOALA = 4;//待考拉仲裁        同意退货,等待用户发货
	public static final int REFUND_SUCCESS = 5;//已完成      用户已退货，等待验货
	
	
	
}
