package net.chinacloud.mediator.task.exchange;

import net.chinacloud.mediator.domain.Exchange;
import net.chinacloud.mediator.exception.message.MessageSendException;
import net.chinacloud.mediator.service.ExchangeService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.translator.ChannelExchangeFacadeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class ExchangeReturngoodsRefuseTask extends ExchangeTask {
	protected static final String EXCHANGE_RETURNGOODS_REFUSE_TYPE = "ReturngoodsRefuse";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeReturngoodsRefuseTask.class);
	
	static {
		TaskManager.registTask(EXCHANGE_TYPE, EXCHANGE_RETURNGOODS_REFUSE_TYPE, ExchangeReturngoodsRefuseTask.class);
	}
	@Autowired
	ChannelExchangeFacadeClient exchangeFacadeClient;

	@Override
	protected String getSubType() {
		return EXCHANGE_RETURNGOODS_REFUSE_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Exchange exchange = getExchange();
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("ReturngoodsRefuse exchange info:" + exchange);
		}
		
		if (null != exchange) {
			try {
				ExchangeService exchangeService = getService(ExchangeService.class, getContext().getChannelCode());
				exchangeService.refuseExchangeReturngoods(exchange);
				exchangeFacadeClient.refuseExchangeReturngoodsResponse(exchange, context, this.id, true, null);
			} catch (MessageSendException e) {
				throw e;
			} catch (Exception e1) {
				// 发送失败信息
				String errorMsg = e1.getMessage();
				exchangeFacadeClient.refuseExchangeReturngoodsResponse(exchange, context, this.id, false, errorMsg);
				throw e1;
			} 
		}
	}

}
