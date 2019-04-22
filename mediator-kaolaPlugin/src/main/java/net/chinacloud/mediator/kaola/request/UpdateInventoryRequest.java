package net.chinacloud.mediator.kaola.request;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.chinacloud.mediator.kaola.response.UpdateInventoryResponse;

public class UpdateInventoryRequest extends AbstractRequest implements KaoLaRequest{

	private String key;//Sku的key
	private int stock;//库存数（可售库存+冻结库存）
	private String sku_outer_id;//sku的outerid
	
	@Override
	public String getApiMethodName() {
		return "kaola.item.sku.stock.update";
	}

	@Override
	public Class<?> getResponseClass() {
		return UpdateInventoryResponse.class;
	}

	@Override
	public Map<String, Object> getAppJsonParams() throws IOException {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("key",key);
		map.put("stock",stock);
//		map.put("sku_outer_id",sku_outer_id);
		return map;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getSku_outer_id() {
		return sku_outer_id;
	}

	public void setSku_outer_id(String sku_outer_id) {
		this.sku_outer_id = sku_outer_id;
	}

	@Override
	public String toString() {
		return "UpdateInventoryRequest [key=" + key + ", stock=" + stock
				+ ", sku_outer_id=" + sku_outer_id + "]";
	}
	
	

}
