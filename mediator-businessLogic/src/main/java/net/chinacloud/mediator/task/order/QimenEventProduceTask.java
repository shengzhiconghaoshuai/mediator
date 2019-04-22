package net.chinacloud.mediator.task.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.chinacloud.mediator.domain.QimenEventProduce;
import net.chinacloud.mediator.service.OrderService;
import net.chinacloud.mediator.task.TaskManager;
import net.chinacloud.mediator.utils.JsonUtil;
@Component
@Scope(value="prototype")
public class QimenEventProduceTask extends OrderTask {

	
	protected static final String ORDER_QIMENEVENTPRODUCT_TYPE = "qimenEventProduce";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QimenEventProduceTask.class);
	
	static {
		TaskManager.registTask(ORDER_TYPE, ORDER_QIMENEVENTPRODUCT_TYPE, QimenEventProduceTask.class);
	}

	@Override
	protected String getSubType() {
		// TODO Auto-generated method stub
		return ORDER_QIMENEVENTPRODUCT_TYPE;
	}

	@Override
	public void doTask() throws Exception {
		Object data = getData();
		QimenEventProduce qimenEventProduce = null;
		if(data instanceof QimenEventProduce){
			qimenEventProduce = (QimenEventProduce)getData();
		} else if (data instanceof String) {
			String strData = (String)data;
			qimenEventProduce = JsonUtil.jsonString2Object(strData, QimenEventProduce.class);
		}
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(" deliver order event to qimen info:" + qimenEventProduce);
		}
		
		if (null != qimenEventProduce) {
			OrderService orderService = getService(OrderService.class, getContext().getChannelCode());
			orderService.deliverOrderEventToQimen(qimenEventProduce);
		}
	}

}
