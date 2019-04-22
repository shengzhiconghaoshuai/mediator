/**
 * 版权：Copyright 2014- LakeCloud Tech. Co. Ltd. All Rights Reserved.
 * 文件名：ConnectTmallInit.java
 * 描述： 天猫长连接初始化
 */
package net.chinacloud.mediator.taobao.notify;

import java.util.ArrayList;
import java.util.List;

import net.chinacloud.mediator.domain.Application;
import net.chinacloud.mediator.domain.Channel;
import net.chinacloud.mediator.system.application.service.ApplicationService;
import net.chinacloud.mediator.system.channel.service.ChannelService;
import net.chinacloud.mediator.taobao.TaobaoConstant;
import net.chinacloud.mediator.utils.CollectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.internal.toplink.LinkException;

/**
 * <天猫长连接初始化>
 * <接收天猫的主动通知消息,转发>
 * @author yejunwu123@gmail.com
 * @version 0.0.0,2015年1月12日
 * @since 2015年1月12日
 */
//@Component
public class ConnectTmallInit implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectTmallInit.class);
	//private static final Logger STATICLOGGER = LoggerFactory.getLogger("taskStatics");
	
	@Autowired
	ApplicationService applicationService;
	@Autowired
	TmallMessageProcessor messageProcessor;
	@Autowired
	ChannelService channelService;
	
	//存储TmcClient的引用,在容器关闭时关闭TmcClient,释放连接
	List<TmcClient> tmcClients = new ArrayList<TmcClient>();
	
	public void init() { 
		LOGGER.info("==========ConnectTmallInit start============");
		connect(TaobaoConstant.CHANNEL_TMALL);
		connect(TaobaoConstant.CHANNEL_TAOBAO);
		LOGGER.info("==========ConnectTmallInit complete============");
	}

	private void connect(String channelCode) {
		Channel channel = channelService.getChannelByCode(channelCode);
		if (null != channel) {
			List<Application> applications = applicationService.getApplicationsByChannelId(channel.getId());
			
			if(CollectionUtil.isNotEmpty(applications)) {
				for(final Application application : applications) {
					if(application.getStatus() == 1) {
						
						LOGGER.info("淘宝渠道 {} 应用长连接启动...", application.getName());
						
						TmcClient client = new TmcClient(
								application.getParam().getAppKey(), 
								application.getParam().getAppSecret(), 
								application.getName());
						client.setMessageHandler(new MessageHandler() {
							@Override
							public void onMessage(Message message, MessageStatus status) throws Exception {
								//long startTime = new Date().getTime();
								try {
									//默认不抛出异常则认为消息处理成功
									LOGGER.info("应用[{}],topic:{},content:{}", new Object[]{application.getCode(), message.getTopic(), message.getContent()});

									//不麻烦了,不再通过httpclient转发消息了,直接处理
									messageProcessor.process(
											application.getChannelId(), 
											application.getId(), 
											application.getCode(), 
											application.getStoreId(),
											message);
								} catch (Exception e) {
									//e.printStackTrace();
									LOGGER.error("淘宝消息处理失败", e);
									//消息处理失败回滚，服务端需要重发
									status.fail();
								}
								//STATICLOGGER.debug("taobao message process time " + (new Date().getTime() - startTime));
							}
						});
						try {
							client.connect();
							tmcClients.add(client);
						} catch (LinkException e) {
							//e.printStackTrace();
							LOGGER.error("淘宝渠道 " + application.getName() + " 应用长连接启动失败!!!", e);
						}
					}
				}
			}
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//有两个spring容器,没有这个判断,则每个容器初始化完都会执行一边,导致初始化两次
		if (event.getApplicationContext().getParent() == null) {
			init();
		}
	}
	
	public void disConnect() {
		LOGGER.info("==close tmall connector==");
		if (CollectionUtil.isNotEmpty(tmcClients)) {
			for (TmcClient tmcClient : tmcClients) {
				if (null != tmcClient) {
					tmcClient.close();
				}
			}
		}
	}
}
