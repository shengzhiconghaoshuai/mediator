package net.chinacloud.mediator.kaola.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.kaola.response.GetSkuByStatusReponse;
import net.chinacloud.mediator.utils.StringUtils;

public class GetSkuByStatusRequest extends AbstractRequest implements
		KaoLaRequest {
	/*若不填写，默认全部*/
	private String start_modified;
	/*若不填写，默认全部*/
	private String end_modified;
	/*商品的状态1, 待提交审核、2, 审核中、3, 审核未通过、4, 待上架(审核已通过)、5, 在售、6, 下架、8, 强制下架、9.待修改、若不填写，默认全部*/
	private int item_edit_status;
	/*默认为1*/
	private int page_no;
	/*默认为20,最大为100*/
	private int page_size;

	@Override
	public String getApiMethodName() {
		return "kaola.item.searchBasicByCondition";
	}

	@Override
	public Class<?> getResponseClass() {
		return GetSkuByStatusReponse.class;
	}

	@Override
	public Map<String, Object> getAppJsonParams() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.hasLength(getStart_modified())){
			map.put("start_modified",start_modified );
		}
		if(StringUtils.hasLength(getEnd_modified())){
			map.put("end_modified",end_modified );
		}
		map.put("item_edit_status",item_edit_status );
		map.put("page_no",page_no );
		map.put("page_size",page_size );
		return map;
	}

	@Override
	public String getRequestMethod() {
		return "GET";
	}

	public String getStart_modified() {
		return start_modified;
	}

	public void setStart_modified(String start_modified) {
		this.start_modified = start_modified;
	}

	public String getEnd_modified() {
		return end_modified;
	}

	public void setEnd_modified(String end_modified) {
		this.end_modified = end_modified;
	}

	public int getItem_edit_status() {
		return item_edit_status;
	}

	public void setItem_edit_status(int item_edit_status) {
		this.item_edit_status = item_edit_status;
	}

	public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		this.page_no = page_no;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	
	

}
