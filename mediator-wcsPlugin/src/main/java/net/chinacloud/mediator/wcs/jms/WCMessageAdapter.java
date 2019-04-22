/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：TaskAdapterManager.java
 * 描述： jms消息转换器
 */
package net.chinacloud.mediator.wcs.jms;

import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageConversionException;

import javax.jms.*;

/**
 * <jms消息转换器>
 * <jms消息转换器>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月8日
 * @since 2015年1月8日
 */
public class WCMessageAdapter implements MessageConverter {
    public Object fromMessage(Message message) throws JMSException,
            MessageConversionException {
        if (message instanceof ObjectMessage) {
            return ((ObjectMessage) message).getObject();
        } else if (message instanceof TextMessage) {
            return ((TextMessage) message).getText();
        }
        return message;
    }

    public Message toMessage(Object object, Session session) throws JMSException,
            MessageConversionException {
        return null;
    }
}