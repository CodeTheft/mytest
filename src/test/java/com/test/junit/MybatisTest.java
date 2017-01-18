package com.test.junit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.my.test.mybatis.model.Person;
import com.my.test.mybatis.service.PersonService;
import com.my.test.mybatis.service.PersonService2;

public class MybatisTest {

	private static ApplicationContext context;
	
	@Autowired
	private PersonService2 personService2;
	
	static {
		context = new ClassPathXmlApplicationContext("application-context.xml");
	}
	
	public static void main(String[] args) {
		new MybatisTest().testPersonService();
		new MybatisTest().testPersonService2();
	}
	
	public void testPersonService( ) {
		PersonService personService = (PersonService) context.getBean("personService");
		List<Person> list = personService.getPersons();
		System.out.println(new Gson().toJson(list));
		Person person = personService.getPerson(100);
		System.out.println(new Gson().toJson(person));
	}
	
	public void testPersonService2() {
//		PersonService2 personService2 = (PersonService2) context.getBean("personService2");
		List<Person> list = personService2.getAllPerson();
		System.out.println(new Gson().toJson(list));
	}
	
}
