package com.my.test.mybatis.service.impl;

import java.util.List;

import com.my.test.mybatis.dao.PersonDao;
import com.my.test.mybatis.model.Person;
import com.my.test.mybatis.service.PersonService;

public class PersonServiceImpl implements PersonService {

	private PersonDao personDao;

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public List<Person> getPersons() {
		return personDao.getAllPersons();
	}

	public Person getPerson(int id) {
		return personDao.getPersonById(id);
	}
	
}
