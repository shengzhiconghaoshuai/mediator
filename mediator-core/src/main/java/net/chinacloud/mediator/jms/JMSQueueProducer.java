package net.chinacloud.mediator.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * jms p2p消息生产者
 * @author ywu
 *
 */
public class JMSQueueProducer {
	
	private static Logger LOGGER = LoggerFactory.getLogger(JMSQueueProducer.class);
	
	JmsTemplate template = null;

	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}
	
	public void send(final String message){
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("send message:" + message);
		}
		
		template.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage tm = session.createTextMessage();
				tm.setText(message);
				return tm;
			}
		});
	}
}
