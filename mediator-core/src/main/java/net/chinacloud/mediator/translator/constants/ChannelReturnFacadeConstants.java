package net.chinacloud.mediator.translator.constants;

/**
 * This class defines the constants used to interact with the ChannelReturn
 * service.
 */
public class ChannelReturnFacadeConstants {

	// ==========================================================================================
	// The service module (component) name.
	// ==========================================================================================
	/**
	 * The name for the ChannelReturn service module.
	 */
	public static final String COMPONENT_NAME = "com.ibm.commerce.channelreturn";

	// ==========================================================================================
	// Predefined access profiles.
	// ==========================================================================================
	/**
	 * This constants represents an access profile that returns detail
	 * information about the noun. Generally this includes summary information
	 * plus the common information associated with the specified noun.
	 */
	public static final String ACCESS_PROFILE_DETAILS_INFORMATION = "ibm_Details";
	/**
	 * This constants represents an access profile that returns summary
	 * information about the noun being returned. This includes enough
	 * information to display a list of nouns and does not include much detail
	 * about that noun.
	 */
	public static final String ACCESS_PROFILE_SUMMARY_INFORMATION = "ibm_Summary";
	/**
	 * This constants represents an access profile that returns all information
	 * about the noun being returned. Generally this includes detail information
	 * plus any additional information about the noun.
	 */
	public static final String ACCESS_PROFILE_ALL_INFORMATION = "ibm_All";
	/**
	 * This constants if the default access profile when one is not specified.
	 * The default access profile is the summary access profile.
	 */
	public static final String ACCESS_PROFILE_DEFAULT = ACCESS_PROFILE_SUMMARY_INFORMATION;

	/**
	 * Access profile for updating a noun
	 */
	public static final String ACCESS_PROFILE_UPDATE = "ibm_Update";

	/**
	 * Access profile for resolving a noun's ID
	 */
	public static final String ACCESS_PROFILE_ID_RESOLVE = "ibm_IdResolve";

	// ==========================================================================================
	// Predefined XPath constants used to build XPath expressions.
	// ==========================================================================================

	/**
	 * The constant for closing <code>XPath</code> with a bracket.
	 */
	public static final String CLOSE_XPATH = "]";

	/**
	 * The constant for closing <code>XPath</code> with two brackets.
	 */
	public static final String DOUBLE_CLOSE_XPATH = ")]";

	/**
	 * The attribute name of a unique ID in a noun Return.
	 */
	public static final String NOUN_ATTRIBUTE_NAME_RETURN_UID = "UniqueID";

	/**
	 * The <code>XPath</code> selection of Return with by unique ID.
	 */
	public static final String XPATH_RETURN_UID = "/Return/ReturnIdentifier[(" + NOUN_ATTRIBUTE_NAME_RETURN_UID + "=";

	/**
	 * The attribute name of Name in noun Return.
	 */
	public static final String NOUN_ATTRIBUTE_NAME_RETURN_NAME = "Name";

	/**
	 * The <code>XPath</code> selection of Return by Name.
	 */
	public static final String XPATH_RETURN_NAME = "/Return/ReturnIdentifier/ExternalIdentifier[("
			+ NOUN_ATTRIBUTE_NAME_RETURN_NAME + "=";

	/**
	 * The <code>XPath</code> selection of Return with Name.
	 */
	public static final String XPATH_RETURN = "/Return";

	// ==========================================================================================
	// Process actions
	// ==========================================================================================

	/**
	 * Constant for the Process Create action on Return
	 */
	public static final String PROCESS_VERB_ACTION_CREATE_RETURN = "CreateReturn";
	
	public static final String PROCESS_VERB_ACTION_CREATE_AFS_RETURN = "CreateAFSReturn";
	/**
	 * Constant for the Process Delete action on Return
	 */
	public static final String PROCESS_VERB_ACTION_DELETE_RETURN = "Delete";

	// ==========================================================================================
	// The following are constants used for URL input parameters.
	// ==========================================================================================

	// Define URL parameter names here
	public static final String STOREID = "storeId";
	public static final String COMMENTS = "comments";
	public static final String REFUNDAMOUNT = "refundAmount";
	public static final String STATUS = "status";
	public static final String CREATE4XCHG = "create4Xchg";
	public static final String RMAID = "rmaId";
	public static final String ORDERID = "orderId";
	public static final String RETURNITEMID = "returnItemId";
	public static final String ORDERITEMID = "orderItemId";
	public static final String QUANTITY = "quantity";
	public static final String REQUIRERECEIVE = "requireReceive";
	public static final String ITEMREFUNDAMOUNT = "itemRefundAmount";
	public static final String REASONCODE = "reasonCode";
	public static final String DESCRIPTION = "description";
	public static final String REASONTYPE = "reasonType";

	public static final String KEY_INVALID_PARAMETER_VALUE = "INVALID_PARAMETER_VALUE";
	public static final String KEY_MISSING_PARAMETER = "MISSING_PARAMETER";
	public static final String CURRENCY = "CNY";
	public static final String ERROR = "error";
	public static final String ERROR_MSG = "errorMsg";
	
	public static final String CHANNEL_ORDER_ID = "channelOrderId";
	public static final String AFS_SERVICE_ID = "afsServiceId  ";
	public static final String AFS_APPLY_ID = "afsApplyId ";
	public static final String AFS_CATEGORY_ID = "afsCategoryId";
	public static final String WARE_ID = "wareId";
	public static final String WARE_NAME = "wareName";
	public static final String CUSTOMER_NAME = "customerName";
	public static final String CUSTOMER_MOBILE_PHONE = "customerMobilePhone";
	public static final String APPROVE_NAME = "approveName";
	public static final String AFS_APPLY_TIME = "afsApplyTime";
	public static final String APPROVE_DATE = "approvedDate";
	public static final String CHANNEL_RMA_ID = "channelRmaId";
	public static final String SKU_PARTNUMBER = "skuPartnumber";
	
	public static final String TASK_ID = "taskId";
}