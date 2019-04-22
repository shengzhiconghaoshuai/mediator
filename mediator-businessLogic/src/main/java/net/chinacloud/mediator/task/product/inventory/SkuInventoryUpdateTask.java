/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：InventorySingleUpdate.java
 * 描述： 单个sku库存同步
 */
package net.chinacloud.mediator.task.product.inventory;

import net.chinacloud.mediator.domain.InventoryUpdateRemark;
import net.chinacloud.mediator.domain.Sku;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.exception.product.InventoryUpdateException;
import net.chinacloud.mediator.service.ProductAttributeService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.system.registry.RegistryData;
import net.chinacloud.mediator.system.registry.dao.RegistryDao;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.task.product.ProductTask;
import net.chinacloud.mediator.translator.ChannelProductFacadeClient;
import net.chinacloud.mediator.utils.DateUtil;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * <单个sku库存同步>
 * <单个sku库存同步,以sku作单位,每个sku生成一个task>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月15日
 * @since 2015年1月15日
 */
@Component
@Scope(value="prototype")
public class SkuInventoryUpdateTask extends ProductTask {
	
	private static final String SKU_INVENTORY_UPDATE_TYPE = "skuInventoryUpdate";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SkuInventoryUpdateTask.class);

	static {
		TaskManager.registTask(PRODUCT_TYPE, SKU_INVENTORY_UPDATE_TYPE, SkuInventoryUpdateTask.class);
	}
	
	@Autowired
	ChannelProductFacadeClient productFacadeClient;
	@Autowired
	private RegistryDao registryDao;
	@Autowired
	private ProductAttributeService productAttributeService;
	
	@Override
	protected String getSubType() {
		//单个库存同步
		return SKU_INVENTORY_UPDATE_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Sku sku = (Sku)getData();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("update inventory sku info:" + sku);
		}
		
		if (null != sku) {
			RegistryData registryData = registryDao.getRegistry("isResponseUpdateSkuInventory");//是否给oms回复库存更新日志消息,
			RegistryData registry= registryDao.getRegistry("isToInventoryUpdateInterface");
			String isResponse = registryData.getValue();//Y:给oms回复库存日志
			String isToInventoryUpdateInterface = registry.getValue();//Y:不管限制条件，直接去调库存更新接口
			boolean flag = false;//是否去调用接口
			Double number = sku.isFull()? sku.getQtyCanSell():sku.getQtyChange();//更新的库存数
			InventoryUpdateRemark inventoryUpdateRemark = null;
			try {
				inventoryUpdateRemark = productAttributeService.getInventoryUpdateRemarkByOuterId(sku.getOuterSkuId(), context.getApplicationId());
				if("Y".equals(isToInventoryUpdateInterface)){
					flag = true;
				}else{
					if(inventoryUpdateRemark == null){//不存在库存更新日志
						flag = true;
					}else if(inventoryUpdateRemark.isFull()!=sku.isFull()){//更新的库存模式不一样
						flag = true;
					}else if(inventoryUpdateRemark.getStockNumber()!=number.intValue()){//更新的库存数不一样
						flag = true;
					}else if(DateUtil.getDayDiffStandardFormat(DateUtil.parse(inventoryUpdateRemark.getMotifyTime()),new Date())!=0){//更新的库存时间不在一天
						flag = true;
					}
				}
				if(flag){
					ProductService productService = getService(ProductService.class, getContext().getChannelCode());
					productService.updateSkuInventory(sku);
					if("Y".equals(isResponse)){
						productFacadeClient.skuInventoryResponse(sku, context, this.id, true, null);
					}
				}
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				//e1.printStackTrace();
				// 发送失败信息
				String errorMessage = e1.getMessage();
				if("Y".equals(isResponse)){
					productFacadeClient.skuInventoryResponse(sku, context, this.id, false, errorMessage);
				}
				throw new InventoryUpdateException(errorMessage);
			}finally{
				if(flag){
					inventoryUpdateRemark = new InventoryUpdateRemark();
					inventoryUpdateRemark.setApplicationId(context.getApplicationId());
					inventoryUpdateRemark.setFull(sku.isFull());
					inventoryUpdateRemark.setMotifyTime(DateUtil.format(new Date()));
					inventoryUpdateRemark.setStockNumber(number.intValue());
					inventoryUpdateRemark.setOuterId(sku.getOuterSkuId());
					productAttributeService.saveOrupdateInventoryUpdateRemark(inventoryUpdateRemark);
				}else{
					LOGGER.info(sku+",没有去调用更新库存");
				}
			}
		}
	}

}
