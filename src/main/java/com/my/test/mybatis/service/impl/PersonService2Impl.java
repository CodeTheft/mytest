package com.my.test.mybatis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.test.mybatis.dao.PersonDao2;
import com.my.test.mybatis.model.Person;
import com.my.test.mybatis.service.PersonService2;

@Service
public class PersonService2Impl implements PersonService2 {

	@Autowired
	private PersonDao2 personDao2;
	
	public List<Person> getAllPerson() {
		List<Person> list = personDao2.getAllPerson();
		return list;
	}

}
