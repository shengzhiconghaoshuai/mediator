package net.chinacloud.mediator.translator.constants;


/**
 * This class defines the constants used to interact with the ChannelRefund service.
 */
public class ChannelRefundFacadeConstants {

	
	// ==========================================================================================
	// The service module (component) name.
	// ==========================================================================================
	/**
	 * The name for the ChannelRefund service module.
	 */
	public static final String COMPONENT_NAME = "com.ibm.commerce.channelrefund";

	// ==========================================================================================
	// Predefined access profiles.
	// ==========================================================================================
	/**
	 * This constants represents an access profile that returns detail
	 * information about the noun.  Generally this includes summary 
	 * information plus the common information associated with the 
	 * specified noun.
	 */
	public static final String ACCESS_PROFILE_DETAILS_INFORMATION = "ibm_Details";
	/**
	 * This constants represents an access profile that returns summary
	 * information about the noun being returned.  This includes enough
	 * information to display a list of nouns and does not include much
	 * detail about that noun.
	 */
	public static final String ACCESS_PROFILE_SUMMARY_INFORMATION = "ibm_Summary";
	/**
	 * This constants represents an access profile that returns all
	 * information about the noun being returned.  Generally this 
	 * includes detail information plus any additional information
	 * about the noun.
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
	 * The attribute name of a unique ID in a noun Refund.
	 */
	public static final String NOUN_ATTRIBUTE_NAME_REFUND_UID = "UniqueID";
	
	/**
	 * The <code>XPath</code> selection of Refund with by unique ID.
	 */
	public static final String XPATH_REFUND_UID = "/Refund/RefundIdentifier[("+NOUN_ATTRIBUTE_NAME_REFUND_UID+"=";
	
	/**
	 * The attribute name of Name in noun Refund.
	 */
	public static final String NOUN_ATTRIBUTE_NAME_REFUND_NAME = "Name";
	
	/**
	 * The <code>XPath</code> selection of Refund by Name.
	 */
	public static final String XPATH_REFUND_NAME = "/Refund/RefundIdentifier/ExternalIdentifier[("+NOUN_ATTRIBUTE_NAME_REFUND_NAME+"=";
	
	/**
	 * The <code>XPath</code> selection of Refund with Name.
	 */
	public static final String XPATH_REFUND = "/Refund";
	
	// ==========================================================================================
	// Process actions
	// ==========================================================================================

    /**
     * Constant for the Process Create action on Refund
     */
    public static final String PROCESS_VERB_ACTION_CREATE_REFUND = "CreateRefund";
    public static final String PROCESS_VERB_ACTION_CLOSE_REFUND = "CloseRefund";
    /**
     * Constant for the Process Delete action on Refund
     */
    public static final String PROCESS_VERB_ACTION_DELETE_REFUND = "Delete";
    public static final String PROCESS_VERB_ACTION_REOPEN_REFUND = "ReopenRefund";
    public static final String PROCESS_VERB_ACTION_CANCEL_REFUND = "CancelRefund";
    
	// ==========================================================================================
	// The following are constants used for URL input parameters.	
	// ==========================================================================================
	
    public static final String STOREID = "storeId";
	
	
    public static final String B2CREFUNDID = "b2cChrId";
    public static final String STATUS = "status";
	public static final String CHANNELREFUNDID = "refundId";
	public static final String CHANNELORDERID = "tid";
	public static final String CHANNELORDERITEMID = "oid";
	public static final String BUYERNICK = "buyerNick";
	public static final String REFUNDFEE = "refundFee";
	public static final String REASON = "reason";
	public static final String COMMENTS = "desc";
	public static final String CREATED = "created";
	public static final String ERROR = "error";
	public static final String XREFUNDID = "xRefundId";
	public static final String ADJUSTMENT = "adjustment";
	public static final String CANCELTIME = "cancelTime";
	public static final String CLOSETIME = "closeTime";
	
	public static final String CHANNELRMAID = "channelRmaId";
	
	
	public static final String KEY_INVALID_PARAMETER_VALUE = "INVALID_PARAMETER_VALUE";
	public static final String KEY_MISSING_PARAMETER = "MISSING_PARAMETER";
	
	//
	public static final String TASK_ID = "taskId";
}