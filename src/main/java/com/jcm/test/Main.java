package com.jcm.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	
	public static void main(String[] args) {
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		Object obj = applicationContext.getBean("testService") ;
		System.out.println(obj);
	}

}
