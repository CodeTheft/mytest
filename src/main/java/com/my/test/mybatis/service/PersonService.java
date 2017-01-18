package com.my.test.mybatis.service;

import java.util.List;

import com.my.test.mybatis.model.Person;

public interface PersonService {

	public List<Person> getPersons();
	
	public Person getPerson(int id);
	
}
