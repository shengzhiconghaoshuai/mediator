package net.chinacloud.mediator.vip.vop;

import net.chinacloud.mediator.vip.vop.exception.VopJitException;
import net.chinacloud.mediator.vip.vop.init.DefaultVopClient;


public class VopTest {
	
	//test
	static String appKey = "fbd1a01c" ;
	static String appSecret = "0E881BC2F3FC6D56B81BD86E1DD10075";
	static String appURL = "http://vipapis.com/";
	//static String appURL = "http://sandbox.vipapis.com/";
	static int vendor_id = 550;
	
	//pro
/*	static String appKey = "db8af35c" ;
	static String appSecret = "846A166DDA5CFFD57B7F68A6F6CD2011";
	static String appURL = "http://vipapis.com/";
	static int vendor_id = 891;*/
	
	static DefaultVopClient aVipShopJITConnector = new DefaultVopClient(appKey,appSecret,appURL,"1");
	
	public static void getPOList() throws VopJitException{
		 /*try {
			 GetPoListResponse resp = aVipShopJITConnector.getConnector().getPoList(null, null, null, "2000011854", null, vendor_id+"", null, null, null, null, 1, 50);
			 List<vipapis.delivery.PurchaseOrder> purchase_order_list = resp.getPurchase_order_list();
		} catch (OspException e) {
			e.printStackTrace();
		}*/
	}

	public static void main(String[] args) throws VopJitException {
		getPOList();
	}

}
