/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TmallMessageConverter.java
 * 描述： 天猫主动通知消息转换器
 */
package net.chinacloud.mediator.taobao.notify;

import java.util.HashMap;
import java.util.Map;

import net.chinacloud.mediator.domain.Order;
import net.chinacloud.mediator.domain.OrderItem;
import net.chinacloud.mediator.domain.Product;
import net.chinacloud.mediator.domain.Refund;
import net.chinacloud.mediator.taobao.domain.MyNotifyItem;
import net.chinacloud.mediator.taobao.domain.MyNotifyRefund;
import net.chinacloud.mediator.taobao.domain.MyNotifyTrade;
import net.chinacloud.mediator.task.CommonNotifyPacket;
import net.chinacloud.mediator.utils.JsonUtil;
import net.chinacloud.mediator.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.parser.json.JsonConverter;
import com.taobao.api.internal.tmc.Message;

/**
 * <天猫主动通知消息转换器>
 * < 天猫主动通知消息转换器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月12日
 * @since 2015年1月12日
 */
@Component
public class TmallMessageConverter {
    public static final Map<String, Class<? extends TaobaoObject>> messageTypes = new HashMap<String, Class<? extends TaobaoObject>>();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TmallMessageConverter.class);
    
    private JsonConverter jsonConverter = new JsonConverter();

	static {
        messageTypes.put("notify_item", MyNotifyItem.class);
        messageTypes.put("notify_trade", MyNotifyTrade.class);
        messageTypes.put("notify_refund", MyNotifyRefund.class);
    }
	
	public CommonNotifyPacket<?> convert(Message message) throws ApiException {
		String topic = message.getTopic();
		String content = message.getContent();
		
		String topicArray[] = topic.split("_");
    	if(3 != topicArray.length){
    		throw new IllegalArgumentException("the format of taobao message topic exception：" + topic);
    	}
    	String type = topicArray[1];
    	String status = topicArray[2];
    	String messageKey = "";
    	if("rdcaligenius".equals(type)){//双十一预约退款，用于hold住订单
    		messageKey = "notify_trade";
    	}else{
    		messageKey = "notify_" + type;
    	}
    	
    	LOGGER.debug("messageKey:{}", messageKey);
    	
    	Map<String, Object> map = JsonUtil.jsonString2Map(content);
    	TaobaoObject notifyMessage = null;
		try {
			notifyMessage = jsonConverter.fromJson(map, messageTypes.get(messageKey));
		} catch (ApiException e) {
			//e.printStackTrace();
			LOGGER.error("淘宝消息转换失败", e);
			throw e;
		}
    	
    	CommonNotifyPacket<?> packet = convertInterval(notifyMessage, status);
    	
		return packet;
	}
	
	private CommonNotifyPacket<?> convertInterval(TaobaoObject notifyMessage, String status) {
		if (notifyMessage instanceof MyNotifyTrade) {
			MyNotifyTrade trade = (MyNotifyTrade)notifyMessage;
			Order order = new Order();
			order.setChannelOrderId(String.valueOf(trade.getTid()));
			if (null != trade.getOid()) {
				OrderItem orderItem = new OrderItem();
				orderItem.setChannelOrderItemId(String.valueOf(trade.getOid()));
				order.addOrderItem(orderItem);
			}
			if (StringUtils.hasText(trade.getPayment())) {
				order.setPayment(Double.valueOf(trade.getPayment()));
			}
			//这里的status是从topic中解析出来的,content中没有状态信息
			order.setStatus(status);
			
			LOGGER.debug("generate order packet:{}", order);
			
			CommonNotifyPacket<Order> packet = new CommonNotifyPacket<Order>(order);
			return packet;
		} else if(notifyMessage instanceof MyNotifyItem) {
			MyNotifyItem item = (MyNotifyItem)notifyMessage;
			Product product = new Product();
			product.setChannelProductId(String.valueOf(item.getNumIid()));
			
			LOGGER.debug("generate product packet:{}", product);
			
			CommonNotifyPacket<Product> packet = new CommonNotifyPacket<Product>(product);
			packet.setType(status);
			return packet;
		} else if(notifyMessage instanceof MyNotifyRefund) {
			MyNotifyRefund notifyRefund = (MyNotifyRefund)notifyMessage;
			
			Refund refund = new Refund();
			refund.setChannelRefundId(String.valueOf(notifyRefund.getRid()));
			refund.setStatus(status);
			
			LOGGER.debug("generate refund packet:{}", refund);
			
			CommonNotifyPacket<Refund> packet = new CommonNotifyPacket<Refund>(refund);
			return packet;
		}
		return null;
	}
}
