package edu.ucsd.system;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SystemApplicationContext {
	private static ApplicationContext appContext;
	
	static {
		appContext = new ClassPathXmlApplicationContext("nlp-context.xml");	
	}
	
	public static ApplicationContext getApplicationContext() {
		return appContext;
	}
}
