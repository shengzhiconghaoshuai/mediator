package net.chinacloud.mediator.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * add 2015-06-26 spring加载完bean定义后,判断是否是prototype类型的bean(concrete的task),预先使用Class.forName()
 * 加载,使得concrete类型的task的static代码块能执行,完成task的注册,暂时没有想到更好的解决方式
 * @author ywu
 *
 */
@Component
public class TaskInitPostProcessor implements BeanFactoryPostProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskInitPostProcessor.class);

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
		for (String beanDefinitionName : beanDefinitionNames) {
			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
			if (beanDefinition.isPrototype()) {
				// TODO 暂时没有更好的办法,只能先将所有prototype类型的bean先加载一下(concrete的task应该都是prototype类型)
				String beanClassName = beanDefinition.getBeanClassName();
				try {
					Class.forName(beanClassName);
				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
					LOGGER.error("task " + beanClassName + " 预加载失败", e);
				}
			}
		}
	}

}
