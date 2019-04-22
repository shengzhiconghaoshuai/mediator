package net.chinacloud.mediator.task.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.service.ExchangeService;
import net.chinacloud.mediator.service.ProductService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelExchangeFacadeClient;
@Component
@Scope(value="prototype")
public class ExchangeInfoTask extends ExchangeTask{

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeInfoTask.class);
	protected static final String EXCHANGE_INFO_TYPE = "Info";
	
	static {
		TaskManager.registTask(EXCHANGE_TYPE, EXCHANGE_INFO_TYPE, ExchangeInfoTask.class);
	}
	
	@Autowired
	ChannelExchangeFacadeClient exchangeFacadeClient;
	
	@Override
	protected String getSubType() {
		return EXCHANGE_INFO_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Exchange exchange = getExchange();
		ExchangeService exchangeService = null;
		/**平台上直接获取的ExchangeSku和BoughtSku是平台上的skuId,OMS不认识，需要重新获取*/
		if(exchange!=null){
			ProductService productService = getService(ProductService.class, getContext().getChannelCode());
			String exchangeskuId = exchange.getExchangeSku();
			String exchangeouterId = productService.getOuterIdbySkuIdandNick(exchangeskuId);
			exchange.setExchangeSku(exchangeouterId);
			/**平台上获取的换货实体类没有卖家备注或卖家备注，需要用新的接口获取换货单的卖家备注或者卖家备注*/
			exchangeService = getService(ExchangeService.class, getContext().getChannelCode());
			Exchange exchangeMessage =exchangeService.exchangeMessageByExchangeId(exchange.getDisputeId());
			if(exchangeMessage != null){
				exchange.setSellerMessage(exchangeMessage.getSellerMessage());
				exchange.setBuyerMessage(exchangeMessage.getBuyerMessage());
			}else{
				exchange.setSellerMessage(null);
				exchange.setBuyerMessage(null);
			}
		}
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" info:" + exchange);
		}
		exchangeFacadeClient.submitChannelExchange(exchange, getContext(), this.id);
	}

}
