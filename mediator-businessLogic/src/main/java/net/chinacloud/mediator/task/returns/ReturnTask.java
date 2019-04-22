/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ReturnTask.java
 * 描述： 退货相关task基类
 */
package net.chinacloud.mediator.task.returns;

import net.chinacloud.mediator.domain.Return;
import net.chinacloud.mediator.domain.ReturnItem;
import net.chinacloud.mediator.exception.order.VendorPartnumberNotExistException;
import net.chinacloud.mediator.exception.product.ProductException;
import net.chinacloud.mediator.exception.returns.ReturnException;
import net.chinacloud.mediator.init.translator.TranslateException;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.service.ReturnService;
import net.chinacloud.mediator.service.cache.ServiceManager;
import net.chinacloud.mediator.task.Task;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * <退货相关task基类>
 * <退货相关task基类>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2014年12月12日
 * @since 2014年12月12日
 */
@Component
@Scope(value="prototype")
public abstract class ReturnTask extends Task {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnTask.class);
	
	@Autowired
	private ServiceManager serviceManager;
	
	@Override
	protected String getType() {
		return "RETURN";
	}
	
	/**
	 * 获取渠道对应的业务接口实现
	 * @param clazz 业务接口
	 * @param channelCode 渠道标识
	 * @return
	 */
	protected <T> T getService(Class<T> clazz, String channelCode){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("get service " + clazz.getSimpleName() + " for channel " + channelCode);
		}
		return serviceManager.getService(clazz, channelCode);
	}
	
	protected Return getReturn() throws ReturnException, TranslateException, VendorPartnumberNotExistException {
		Return returns = null;
		if (getData() instanceof Return) {
			returns = (Return)getData();
		} else if (data instanceof String) {
			String strData = (String)data;
			char first = strData.charAt(0);
			
			if ('{' == first) {
				returns = JsonUtil.jsonString2Object(strData, Return.class);
			} else {
				String dataId = null;
				if ('"' == first) {
					dataId = JsonUtil.jsonString2Object(strData, String.class);
				} else {
					dataId = getDataId();
				}
				
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("get refund " + getDataId() + "by service from channel " + getContext().getChannelCode());
				}
				ReturnService returnService = getService(ReturnService.class, getContext().getChannelCode());
				returns = returnService.getReturnByReturnId(dataId);
			}
		}
		
		if (!returns.getReturnItems().isEmpty()) {
			for (ReturnItem returnItem : returns.getReturnItems()) {
				if (!StringUtils.hasText(returnItem.getOuterSkuId())) {
					if (StringUtils.hasText(returnItem.getChannelSkuId())) {	//sku渠道编号不为null
						ProductService productService = getService(ProductService.class, getContext().getChannelCode());
						String outerSkuId = null;
						try {
							outerSkuId = productService.getOuterSkuIdByChannelSkuId(returnItem.getChannelSkuId().trim(), "");
							returnItem.setOuterSkuId(outerSkuId);
						} catch (ProductException e) {
							//e.printStackTrace();
							LOGGER.error("get return product error", e);
						}
						if(!StringUtils.hasText(outerSkuId)){
							throw new VendorPartnumberNotExistException(returns.getChannelOrderId(), returnItem.getChannelOrderItemId());
						}
					}
				}
			}
		}
		
		return returns;
	}
}
