package com.my.test.mybatis.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.*;
import com.my.test.mybatis.dao.PersonDao2;
import com.my.test.mybatis.model.Person;

@Repository
public class PersonDao2Impl implements PersonDao2 {

	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<Person> getAllPerson() {
		return sqlSessionTemplate.selectList("PersonDao.getAllPersons");
	}

}
