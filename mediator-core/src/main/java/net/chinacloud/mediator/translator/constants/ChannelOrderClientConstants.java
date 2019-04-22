package net.chinacloud.mediator.translator.constants;

/*
 *-----------------------------------------------------------------
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * WebSphere Commerce
 *
 * (C) Copyright IBM Corp. 2011
 *
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 *-----------------------------------------------------------------
 */

public interface ChannelOrderClientConstants {

	public static final String PARTNUMBER = "partNumber";
	public static final String QUANTITY = "quantity";
	public static final String CATENTRYID = "catentryId";
	public static final String DESCRIPTION = "description";
	public static final String BUYERCOMMENTS = "buyerMessage";
	public static final String ORDERITEMPRICE = "price";
	
	public static final String CHANNELORDERID = "channelOrderId";
	public static final String ORDERAMOUNT = "orderAmount";
	public static final String BUYERID = "buyerId";
	public static final String ORDERITEM = "orderItem";
	public static final String ORDERITEMID = "channelOrderItemId";
	public static final String CHANNELPRODUCTID = "channelProductId";
	public static final String STOREID = "storeId";
	public static final String POSTFEE = "shippingCharge";
	public static final String PRICES = "orderItemPrice";
	public static final String SHIPPINGTYPE = "shippingType";
	
	public static final String PAYTIME = "payTime";
	public static final String CHANNELID = "channelId";
	public static final String PAYMETHOD = "payType";
	public static final String PAYMENT = "payment";
	public static final String ADDRESSID = "shippingAddressId";
	
	public static final String ERROR = "error";
	public static final String ORDERID = "orderId";
	
	public static final String TOTALADJUSTMENT = "totalAdjustment";
	
	public static final String ORDERDISCOUNT = "orderDiscount";
	public static final String ITEMDISCOUNT = "itemDiscount";
	public static final String CREATETIME = "createTime";
	public static final String BUYERALIPAYNO = "buyerAlipayNo";
	public static final String RECEIVETIME = "endTime";
	
	public static final String JINGDONGSHIP = "cky2Name";
	public static final String INVOICETYPE = "invoiceType";
	public static final String INVOICETITLE = "invoiceTitle";
	public static final String INVOICECONTENT = "invoiceContent";
	public static final String MODIFIED = "modified";
	
	//预售状态
	public static final String STEPTRADESTATUS = "stepTradeStatus";
	//预售金额
	public static final String STEPPAIDFEE  = "stepPaidFee";
	//品牌特卖
	public static final String BRANDSALE = "brandSale";
	public static final String BRANDSALE_TRUE = "1";
	public static final String BRANDSALE_FALSE = "0";
	
	//卖家备注
	public static final String SELLERMEMO = "sellerMemo";
	
	public static final String OVERSOLD = "overSold";
	public static final String OVERSOLD_TRUE = "1";
	public static final String OVERSOLD_FALSE = "0";
	
	//address
	public static final String CITY = "city";
	public static final String PROVINCE = "state";
	public static final String DISTRICT = "district";
	public static final String DETAIL_ADDRESS = "address";
	public static final String TELEPHONE1 = "phone";
	public static final String MOBILEPHONE1 = "mobilePhone";
	public static final String DETAIL_ADDRESS_KEY = "detailAddress";
	public static final String SHOPPER = "shopper";
	public static final String LOCATION = "location";
	public static final String RECIPIENTS = "recipients";
	public static final String POSTAL_CODE = "zipCode";
	public static final String CONTACTNAME = "contactName";
	
	//
	public static final String TASK_ID = "taskId";
	public static final String RESULT = "result";
	public static final String CONTENT = "content";
	public static final String REPLY = "reply";
	
	//YHD add
	//商品运费分摊金额
	public static final String DELIVERY_FEE_AMOUNT = "deliveryFeeAmount";
	//促销活动立减分摊金额
	public static final String PROMOTION_AMOUNT = "promotionAmount";
	//商家抵用券分摊金额
	public static final String COUPON_AMOUNT_MERCHANT = "couponAmountMerchant";
	//1mall平台抵用券分摊金额
	public static final String COUPON_PLATFORM_DISCOUNT = "couponPlatformDiscount";
	//wcs端是否重新计算订单项分摊折扣
	public static final String CALCULATE_SHARE_DISCOUNT = "calculateShareDiscount";
}
