package com.ctfo.spring.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring加载
 * @author James
 */
public enum InitSpring {
	//枚举方式单例模式
	INSTANCE;
	private static final ApplicationContext context = new ClassPathXmlApplicationContext("springConfig/spring-*.xml");
//	private static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"springConfig/spring-dataSource.xml", "springConfig/spring-service.xml"});
	public ApplicationContext getContext() {
		return context;
	}
	
	/** 获取Bean对象 **/
	public Object getBean(String beanName){
		return context.getBean(beanName);
	}
}
