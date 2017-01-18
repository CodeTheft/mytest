package com.my.test.mybatis.dao;

import java.util.List;

import com.my.test.mybatis.model.Person;

public interface PersonDao {

	public List<Person> getAllPersons();

	public Person getPersonById(int id);
	
}
