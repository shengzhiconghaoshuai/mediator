package net.chinacloud.mediator.user;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.chinacloud.mediator.utils.DateUtil;

public class MessageReceiver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	public void printMessageA(String message) {
		try {
			System.out.println("receive message from A:" + message);
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void printMessageB(String message) {
		try {
			System.out.println("receive message from B:" + message);
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void processMsgWithTransaction(String message) {
		int count = counter.incrementAndGet();
		LOGGER.info(new StringBuilder().append(count).append(" receive message at ").append(DateUtil.format(new Date())).append(" ").append(message).toString());
		if (message.equals("hello AAAAAAAA! 5")) {
			String name = Thread.currentThread().getName();
			LOGGER.info(name + "##ERROR:" + message);
			throw new RuntimeException("error");
		}
		/*if (count % 5 == 0) {
			LOGGER.info("##ERROR:" + message);
			throw new RuntimeException("error");
		}*/
	}
}
