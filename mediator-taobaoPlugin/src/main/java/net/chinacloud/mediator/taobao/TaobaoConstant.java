/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaobaoConstant.java
 * 描述： 淘宝渠道用到的常量
 */
package net.chinacloud.mediator.taobao;
/**
 * <淘宝渠道用到的常量>
 * <淘宝渠道用到的常量>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年2月27日
 * @since 2015年2月27日
 */
public final class TaobaoConstant {
	/**淘宝渠道CODE*/
	public static final String CHANNEL_TAOBAO = "TAOBAO";
	/**天猫渠道CODE*/
	public static final String CHANNEL_TMALL = "TMALL";
	/**订单同步分页大小*/
	public static final int ORDEY_SYNC_PAGE_SIZE = 300;
	/**订单同步处理大小*/
	public static final int ORDEY_SYNC_PROCESS_SIZE = 1000;
	/**订单同步处理的交易状态*/
	public static final String ORDER_SYNC_TRADE_STATUS = "WAIT_SELLER_SEND_GOODS";
	/**子订单总金额*/
	public static final String KEY_ORDER_ITEM_TOTALFEE = "totalFee";
	/**子订单优惠分摊*/
	public static final String KEY_ORDER_ITEM_PART_MJZ_DISCOUNT = "partMjzDiscount";
	/**品牌特卖*/
	public static final String KEY_ORDER_BRANDSALE = "brandSale";
	/**支付宝账号*/
	public static final String KEY_ORDER_BUYER_ALIPAY_NO = "buyerAlipayNo";
	/**支付宝交易流水号*/
	public static final String KEY_ORDER_ALIPAY_NO = "alipayNo";
	/**卖家备注旗帜*/
	public static final String KEY_ORDER_SELLER_FLAG = "sellerFlag";
	/**征集预售状态*/
	public static final String KEY_ORDER_ZHENGJI = "zhengji_status";
	/**退款阶段*/
	public static final String KEY_REFUND_PHASE = "refundPhase";
	/**jdp调度截止时间是否启用增量时间,如果启用,则调度在开始时间基础上增加KEY_REGISTRY_JDP_SCHEDULETIME_INCREMENT
	 * 指定的时间,否则截止时间为当前时间,类型:boolean*/
	public static final String KEY_REGISTRY_JDP_SCHEDULETIME_MODIFY = "jdpScheduleEndTimeModify";
	/**jdp调度时间增量,单位:s*/
	public static final String KEY_REGISTRY_JDP_SCHEDULETIME_INCREMENT = "jdpScheduleEndTimeIncrement";
	/**特殊字符,替换*/
	public static final String KEY_SPECIAL_STRING_LIST = "SPECIAL_STR_LIST";
	/**抓取淘宝平台类目信息时只抓取指定目录下的信息(淘宝平台类目太多)*/
	public static final String KEY_TAOBAO_INCLUDE_CAT = "taobao_include_cat";
	
	/**============exchange 返回字段 目前支持2018131  ==============*/
	/**换货详情*/
	public static final String FIELDS_EXCHANGE_GET = "dispute_id,bizorder_id,num,buyer_nick,status,created,modified,reason,title,buyer_logistic_no,seller_logistic_no,bought_sku,exchange_sku,buyer_address,address,buyer_phone,buyer_logistic_name,seller_logistic_name,alipay_no,buyer_name,seller_nick,time_out,good_status";
	/**卖家确认收货*/
	public static final String FIELDS_EXCHANGE_RETURNGOODS_AGREE = "dispute_id,bizorder_id,modified,status";
	/**卖家创建换货留言 和 查询列表*/
	public static final String FIELDS_EXCHANGE_MESSAGE_ADD = "id,refund_id,owner_id,owner_nick,owner_role,content,created,message_type";
	/**卖家同意或拒绝换货申请*/
	public static final String FIELDS_EXCHANGE_AGREE_REFUSE = "dispute_id,bizorder_id,status,modified";
	/**卖家发货*/
	public static final String FIELDS_EXCHANGE_CONSIGNGOODS = "dispute_id,bizorder_id,status,modified";
	/**获取拒绝换货原因列表*/
	public static final String FIELDS_EXCHANGE_REFUSEREASON_GET = "reason_id,reason_text";
	/**============end==============*/
}
