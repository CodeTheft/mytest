package com.my.test.mongo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.my.test.mongo.dao.UserRepository;

public class MainTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/application-context.xml");
		UserRepository userRepository = context.getBean(UserRepository.class);
		System.out.println(new Gson().toJson(userRepository.getAll()));
		System.out.println(new Gson().toJson(userRepository.getByName("test")));
		context.close();
	}
	
}
