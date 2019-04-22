package net.chinacloud.mediator.vip.vop.domain;


import java.io.Serializable;
import java.util.List;

public class ImportDeliverDetailMsg implements Serializable {

	private static final long serialVersionUID = 6975350436349691026L;
	/*
	 * po_no true string PO订单号 storage_no true string 入库单号 delivery_id false
	 * string 创建送货单返回的ID delivery_list true string 出仓产品列表（所含参数为以下5个字段）
	 */

	public String po_no;
	public String storage_no;
	public String delivery_id;
	public List<DeliverySkuDetailBean> delivery_list;

	public String getPo_no() {
		return po_no;
	}

	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}

	public String getStorage_no() {
		return storage_no;
	}

	public void setStorage_no(String storage_no) {
		this.storage_no = storage_no;
	}

	public String getDelivery_id() {
		return delivery_id;
	}

	public void setDelivery_id(String delivery_id) {
		this.delivery_id = delivery_id;
	}

	public List<DeliverySkuDetailBean> getDelivery_list() {
		return delivery_list;
	}

	public void setDelivery_list(List<DeliverySkuDetailBean> delivery_list) {
		this.delivery_list = delivery_list;
	}

}
