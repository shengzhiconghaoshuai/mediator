package net.chinacloud.mediator.kaola.bean;

public class KaoLaOrderExpress {
	
	private String express_company_code;//快递公司编码
	private String express_no;//快递运单号
	
	public String getExpress_company_code() {
		return express_company_code;
	}
	public void setExpress_company_code(String express_company_code) {
		this.express_company_code = express_company_code;
	}
	public String getExpress_no() {
		return express_no;
	}
	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}
	@Override
	public String toString() {
		return "KaoLaOrderExpress [express_company_code="
				+ express_company_code + ", express_no=" + express_no + "]";
	}
	
	
	

}
